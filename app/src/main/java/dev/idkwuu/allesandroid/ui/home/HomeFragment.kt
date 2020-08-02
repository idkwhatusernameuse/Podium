package dev.idkwuu.allesandroid.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.models.AllesPost
import dev.idkwuu.allesandroid.ui.FloatingActionButtonLayout
import dev.idkwuu.allesandroid.ui.Timeline
import dev.idkwuu.allesandroid.ui.post.PostActivity

class HomeFragment : Fragment() {

    private lateinit var timeline: Timeline
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        viewModel.fetchTimeline(requireContext())

        timeline = Timeline(
            title = getString(R.string.title_home),
            view = view.findViewById(R.id.timeline),
            withReload = true,
            data = viewModel.timeline as LiveData<MutableList<AllesPost>>,
            viewLifecycleOwner = viewLifecycleOwner,
            reloadFunction = { viewModel.fetchTimeline(requireContext()) }
        )

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

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 69) {
            viewModel.fetchTimeline(requireContext())
        }
    }
}