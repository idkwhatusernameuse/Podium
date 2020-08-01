package dev.idkwuu.allesandroid.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.todou.nestrefresh.RefreshHeaderView
import com.todou.nestrefresh.base.OnRefreshListener
import de.hdodenhof.circleimageview.CircleImageView
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.api.AllesEndpointsInterface
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.api.RetrofitClientInstance
import dev.idkwuu.allesandroid.ui.post.PostActivity
import dev.idkwuu.allesandroid.ui.post.PostListAdapter
import dev.idkwuu.allesandroid.util.SharedPreferences
import dev.idkwuu.allesandroid.util.dont_care_lol
import jp.wasabeef.blurry.internal.Blur
import jp.wasabeef.blurry.internal.BlurFactor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.IllegalStateException

class ProfileFragment : Fragment() {

    fun newInstance(user: String, withBackButton: Boolean): ProfileFragment? {
        val bundle = Bundle()
        bundle.putString("user", user)
        bundle.putBoolean("withBackButton", withBackButton)
        val fragment = ProfileFragment()
        fragment.arguments = bundle
        return fragment
    }

    private lateinit var adapter: PostListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val user: String = try {
            requireArguments().getString("user")
        } catch (e: IllegalStateException) {
            SharedPreferences.current_user
        }.toString()

        Timeline(
            view = view.findViewById(R.id.timeline),
            fabLayout = if (user == SharedPreferences.current_user) {
                view.findViewById<FloatingActionButton>(R.id.floatingActionButtonLayout)
            } else null,
            activity = requireActivity(),
            withReload = true,
            loader = { context, param -> Repo().getUserPosts(context, user) },
            viewLifecycleOwner = viewLifecycleOwner
        )

        observeData(user)

        // Back button
        val withBackButton = try {
            requireArguments().getBoolean("withBackButton")
        } catch (e: IllegalStateException) {
            false
        }
        if (withBackButton) {
            val back = view.findViewById<ImageButton>(R.id.back)
            back.setOnClickListener { requireActivity().finish() }
            back.visibility = View.VISIBLE
        }
        // Settings button
        if (user == SharedPreferences.current_user) {
            val settings = view.findViewById<ImageButton>(R.id.settings)
            settings.visibility = View.VISIBLE
            settings.setOnClickListener {
                startActivity(Intent(context, SettingsActivity::class.java))
            }
        }


        return view
    }

    @SuppressLint("SetTextI18n")
    private fun observeData(user: String) {
        Repo().getUser(requireContext(), user).observe(viewLifecycleOwner, Observer {
            val nestedScrollView = requireView().findViewById<NestedScrollView>(R.id.nestedScrollView)
            val errorLayout = requireView().findViewById<View>(R.id.error_loading)
            if (it != null) {
                val profileView = requireView().findViewById<View>(R.id.profile)
                profileView.visibility = View.VISIBLE
                // Text
                profileView.findViewById<TextView>(R.id.user_title).text = if (it.plus) {
                    "${it.name}\u207A"
                } else {
                    it.name
                }
                profileView.findViewById<TextView>(R.id.user_handle).text = "@${it.username}"
                profileView.findViewById<TextView>(R.id.user_followers).text = "${it.followers} ${getString(R.string.followers)}"
                if (it.followingUser) {
                    profileView.findViewById<TextView>(R.id.follows_you).visibility = View.VISIBLE
                }
                profileView.findViewById<TextView>(R.id.user_description).text = it.about
                if (user != SharedPreferences.current_user) {
                    val followButton = profileView.findViewById<MaterialButton>(R.id.follow_button)
                    followButton.visibility = View.VISIBLE
                    if (it.following) {
                        setFollow(followButton, it.following, it.username)
                    }
                    var following = it.following
                    followButton.setOnClickListener {_ ->
                        following = !following
                        setFollow(followButton, !following, it.username)
                    }
                }

                // Profile picture
                Repo().getEtagProfilePicture(it.username).observeForever { etag ->
                    if (etag?.second != null) {
                        Glide.with(requireView().context)
                            .asBitmap()
                            .load("https://avatar.alles.cx/u/${it.username}")
                            .signature(ObjectKey(etag.second!!))
                            .into(object: SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap,transition: Transition<in Bitmap>?) {
                                    GlobalScope.launch {
                                        setBlurredBanner(resource)
                                    }
                                }
                            })
                    }
                }

                // Is online?
                Repo().getIsOnline(it.id).observeForever { isOnline ->
                    profileView.findViewById<CircleImageView>(R.id.profile_image).borderWidth = if (isOnline) { 2 } else { 0 }
                }

                errorLayout.visibility = View.GONE
                nestedScrollView.visibility = View.VISIBLE
            } else {
                errorLayout.visibility = View.VISIBLE
                errorLayout.findViewById<Button>(R.id.retry).setOnClickListener {
                    nestedScrollView.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    observeData(user)
                }
            }
        })
    }

    private fun setFollow(followButton: MaterialButton, follow: Boolean, username: String) {
        val retrofit = RetrofitClientInstance().getRetrofitInstance()
            .create(AllesEndpointsInterface::class.java)
        if (follow) {
            followButton.background = requireContext().getDrawable(R.drawable.solid_blue_rounded)
            followButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            followButton.text = getString(R.string.unfollow)
            retrofit.follow(username).enqueue(dont_care_lol)
        } else {
            followButton.background = requireContext().getDrawable(R.drawable.rounded_rectangle_small)
            followButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            followButton.text = getString(R.string.follow)
            retrofit.unfollow(username).enqueue(dont_care_lol)
        }
    }

    private fun setBlurredBanner(profile_picture: Bitmap) {
        // Create blurred version of profile picture
        val blurFactor = BlurFactor()
        blurFactor.width = profile_picture.width
        blurFactor.height = profile_picture.height
        val blurryPicture = Blur.of(requireContext(), profile_picture, blurFactor)
        requireActivity().runOnUiThread {
            requireView().findViewById<ImageView>(R.id.profile_image).setImageBitmap(profile_picture)
            requireView().findViewById<ImageView>(R.id.background).setImageBitmap(blurryPicture)
        }
    }
}