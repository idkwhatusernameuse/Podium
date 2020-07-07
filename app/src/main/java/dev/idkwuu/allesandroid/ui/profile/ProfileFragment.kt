package dev.idkwuu.allesandroid.ui.profile

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.button.MaterialButton
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.api.AllesEndpointsInterface
import dev.idkwuu.allesandroid.api.RetrofitClientInstance
import dev.idkwuu.allesandroid.models.AllesVote
import dev.idkwuu.allesandroid.ui.feed.FeedAdapter
import dev.idkwuu.allesandroid.util.SharedPreferences
import dev.idkwuu.allesandroid.util.dont_care_lol
import jp.wasabeef.blurry.Blurry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalStateException

class ProfileFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }
    private lateinit var adapter: FeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val shimmer = view.findViewById<ShimmerFrameLayout>(R.id.shimmer)
        shimmer.startShimmer()

        adapter = FeedAdapter(view.context)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false

        val user: String = try {
            requireArguments().getString("user").toString()
        } catch (e: IllegalStateException) {
            SharedPreferences.current_user.toString()
        }

        observeData(user)

        // Setup pull to refresh
        val pullToRefresh = view.findViewById<SwipeRefreshLayout>(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            shimmer.startShimmer()
            shimmer.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            view.findViewById<View>(R.id.profile).visibility = View.GONE
            observeData(user)
            pullToRefresh.isRefreshing = true
        }
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun observeData(user: String) {
        viewModel.fetchUser(user).observe(viewLifecycleOwner, Observer {
            val shimmer = requireView().findViewById<ShimmerFrameLayout>(R.id.shimmer)
            shimmer.stopShimmer()
            shimmer.visibility = View.GONE
            // Set profile info
            val profileView = requireView().findViewById<View>(R.id.profile)
            profileView.visibility = View.VISIBLE
                // Profile picture
            Glide.with(requireView().context)
                .asBitmap()
                .load("https://avatar.alles.cx/u/${it.username!!}?size=100")
                .into(object: SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap,transition: Transition<in Bitmap>?) {
                        profileView.findViewById<ImageView>(R.id.profile_image).setImageBitmap(resource)
                        Blurry.with(requireContext()).from(resource).into(profileView.findViewById(R.id.imageView))
                    }
                })

                // Text
            profileView.findViewById<TextView>(R.id.user_title).text = it.nickname
            profileView.findViewById<TextView>(R.id.user_handle).text = "@${it.username}"
            profileView.findViewById<TextView>(R.id.user_followers).text = "${it.followers.toString()} ${getString(R.string.followers)}"
            //profileView.findViewById<TextView>(R.id.user_following).text = "${it.following.toString()} ${getString(R.string.following)}"
            if (it.followingUser!!) {
                profileView.findViewById<TextView>(R.id.follows_you).visibility = View.VISIBLE
            }
            profileView.findViewById<TextView>(R.id.user_description).text = it.about
            if (user != SharedPreferences.current_user) {
                val followButton = profileView.findViewById<MaterialButton>(R.id.follow_button)
                followButton.visibility = View.VISIBLE
                if (it.following!!) {
                    setFollow(followButton, it.following!!, it.username!!)
                }
                var following = it.following!!
                followButton.setOnClickListener {_ ->
                    following = !following
                    setFollow(followButton, !following, it.username!!)
                }
            }
            // Set posts list
            requireView().findViewById<RecyclerView>(R.id.recyclerView).visibility = View.VISIBLE
            requireView().findViewById<SwipeRefreshLayout>(R.id.pullToRefresh).isRefreshing = false
            adapter.setListData(it.posts!!.toMutableList())
            adapter.notifyDataSetChanged()
        })
    }

    private fun setFollow(followButton: MaterialButton, follow: Boolean, username: String) {
        val retrofit = RetrofitClientInstance().getRetrofitInstance()
            .create(AllesEndpointsInterface::class.java)
        if (follow) {
            followButton.background = requireContext().getDrawable(R.drawable.solid_blue_rounded)
            followButton.setTextColor(requireContext().getColor(R.color.background))
            followButton.text = getString(R.string.unfollow)
            retrofit.follow(SharedPreferences.login_token!!, username).enqueue(dont_care_lol)

        } else {
            followButton.background = requireContext().getDrawable(R.drawable.rounded_rectangle_small)
            followButton.setTextColor(requireContext().getColor(R.color.profile_text_opacity))
            followButton.text = getString(R.string.follow)
            retrofit.unfollow(SharedPreferences.login_token!!, username).enqueue(dont_care_lol)
        }

    }
}