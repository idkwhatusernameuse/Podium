package dev.idkwuu.allesandroid.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.button.MaterialButton
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.databinding.FragmentProfileBinding
import dev.idkwuu.allesandroid.databinding.LayoutProfileInfoBinding
import dev.idkwuu.allesandroid.ui.SettingsActivity
import dev.idkwuu.allesandroid.util.SharedPreferences
import jp.wasabeef.blurry.internal.Blur
import jp.wasabeef.blurry.internal.BlurFactor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var user: String = ""

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    private var _v: FragmentProfileBinding? = null
    private val v get() = _v!!

    fun newInstance(user: String, withBackButton: Boolean): ProfileFragment? {
        val bundle = Bundle()
        bundle.putString("user", user)
        bundle.putBoolean("withBackButton", withBackButton)
        val fragment = ProfileFragment()
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _v = FragmentProfileBinding.inflate(inflater, container, false)
        setProfile()
        /*user = try {
            requireArguments().getString("user")
        } catch (e: IllegalStateException) {
            SharedPreferences.current_user
        }.toString()*/

        //viewModel.fetchUserInfo(requireContext(), user)
       // viewModel.fetchUserPosts(requireContext(), user)

        /*Timeline(
            view = view.findViewById(R.id.timeline),
            withReload = false,
            data = viewModel.posts as LiveData<MutableList<AllesPost>>,
            viewLifecycleOwner = viewLifecycleOwner
        )*/

        /*if (user == SharedPreferences.current_user) {
            // Set up FAB
            val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButtonLayout)
            FloatingActionButtonLayout().set(
                activity = requireActivity(),
                context = view.context,
                fab = view.findViewById(R.id.floatingActionButtonLayout),
                nestedScrollView = view.findViewById(R.id.nestedScrollView)
            )

            // Post FAB!
            fab.setOnClickListener {
                startActivityForResult(Intent(context, PostActivity::class.java), 69)
            }
        }*/

        //observeData(user)

        // Back button
        val withBackButton = try {
            requireArguments().getBoolean("withBackButton")
        } catch (e: IllegalStateException) {
            false
        }
        if (withBackButton) {
            v.profile.back.setOnClickListener { requireActivity().finish() }
            v.profile.back.visibility = View.VISIBLE
        }
        // Settings button
        if (user == SharedPreferences.current_user) {
            v.profile.settings.visibility = View.VISIBLE
            v.profile.settings.setOnClickListener {
                startActivity(Intent(requireContext(), SettingsActivity::class.java))
            }
        }
        /*

        val pullToRefresh = view.findViewById<RefreshHeaderView>(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh() {
                viewModel.fetchUserInfo(requireContext(), user)
                viewModel.fetchUserPosts(requireContext(), user)
            }
        })*/
        return v.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _v = null
    }

    private fun setProfile() {
        val pv = v.profile
        pv.userTitle.text = "idkwhatusernameuse"
        pv.userTag.text = "#0001"
        pv.followsYou.visibility = View.VISIBLE
        pv.userFollowers.text = "69 followers"
        pv.userPosts.text = "420 posts"
        pv.chipPlus.visibility = View.VISIBLE
        pv.chipStaff.visibility = View.VISIBLE
        var follows = false
        pv.followButton.setOnClickListener {
            follows = !follows
            setFollow(pv.followButton, follows, "")
        }
        if (!enableBlurryProfile) {
            pv.profilePictureCard.visibility = View.VISIBLE
            pv.loading.visibility = View.GONE
        }
        Glide.with(requireContext())
            .asBitmap()
            .load("https://pbs.twimg.com/profile_images/1300450987916832768/Xj0mONzD_400x400.jpg")
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    pv.profilePicture.setImageBitmap(resource)
                    if (enableBlurryProfile) createBlurredProfile(pv, resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) { }
            })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (enableBlurryProfile) {
            v.profile.profileInfo.visibility = View.VISIBLE
            v.profile.profilePictureCard.visibility = View.INVISIBLE
            v.profile.loading.visibility = View.VISIBLE
            setProfile()
        }
    }

    private val enableBlurryProfile = SharedPreferences.blurredProfile

    private fun createBlurredProfile(pv: LayoutProfileInfoBinding, profilePicture: Bitmap) {
        GlobalScope.launch {
            val bitmap = Bitmap.createBitmap(pv.root.width, pv.root.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            // Blur the profile picture
            val blurredProfilePicture = Blur.of(
                requireContext(),
                profilePicture,
                BlurFactor().apply {
                    this.width = profilePicture.width
                    this.height = profilePicture.height
                    this.radius = 75
                }
            )
            val scaledBlurryPicture = Bitmap.createScaledBitmap(
                blurredProfilePicture,
                bitmap.width,
                bitmap.width * (bitmap.width / bitmap.height),
                false
            )
            // Align blurred image to be in the center
            val alignment: Float
            val doYaxis: Boolean
            if (bitmap.width > bitmap.height) {
                // Y
                alignment = (scaledBlurryPicture.height - bitmap.height) / 2f
                doYaxis = true
            } else {
                // X
                alignment = (scaledBlurryPicture.width - bitmap.width) / 2f
                doYaxis = false
            }
            // Draw everything to the bitmap
                // Blurred profile picture
            canvas.drawBitmap(
                scaledBlurryPicture,
                if (!doYaxis) -alignment else 0f,
                if (doYaxis) -alignment else 0f,
                null
            )
            // Draw the layout in the canvas
                // Temporal bitmap
            val layoutBitmap = Bitmap.createBitmap(
                pv.root.width,
                pv.root.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas2 = Canvas(layoutBitmap)
            pv.info.draw(canvas2)
                // Draw it
            val maskPaint = Paint()
            maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            canvas.drawBitmap(layoutBitmap, 0f, 0f, maskPaint)
            layoutBitmap.recycle()
            scaledBlurryPicture.recycle()
            requireActivity().runOnUiThread {
                pv.bgPfp.setImageBitmap(blurredProfilePicture)
                pv.bgInfo.setImageBitmap(bitmap)
                pv.profileInfo.visibility = View.INVISIBLE
                pv.profilePictureCard.visibility = View.VISIBLE
                pv.loading.visibility = View.GONE
            }
        }
    }

    private fun setFollow(followButton: MaterialButton, follow: Boolean, id: String) {
        //val retrofit = RetrofitClientInstance().getRetrofitInstance()
        //    .create(AllesEndpointsInterface::class.java)
        if (follow) {
            followButton.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.solid_blue_rounded
            )
            followButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.white
                )
            )
            followButton.text = getString(R.string.unfollow)
            //retrofit.follow(username).enqueue(dont_care_lol)
        } else {
            followButton.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.rounded_rectangle_small
            )
            followButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            followButton.text = getString(R.string.follow)
            //retrofit.unfollow(username).enqueue(dont_care_lol)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeData(user: String) {
        /*viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            requireView().findViewById<RefreshHeaderView>(R.id.pullToRefresh).stopRefresh()
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
        })*/
    }

    /*

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 69) {
            viewModel.fetchUserPosts(requireContext(), user)
        }
    }*/
}