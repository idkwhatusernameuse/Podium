package dev.idkwuu.allesandroid.ui.bookmarks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.models.AllesPost
import dev.idkwuu.allesandroid.ui.Timeline

class BookmarksFragment : Fragment() {

    private val viewModel: BookmarksViewModel by lazy {
        ViewModelProvider(this).get(BookmarksViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookmarks, container, false)

        viewModel.fetchBookmarks(requireContext())

        Timeline(
            title = getString(R.string.title_bookmarks),
            view = view.findViewById(R.id.timeline),
            withReload = true,
            data = viewModel.bookmarks as LiveData<MutableList<AllesPost>>,
            viewLifecycleOwner = viewLifecycleOwner,
            reloadFunction = { viewModel.fetchBookmarks(requireContext()) }
        )
        return view
    }
}