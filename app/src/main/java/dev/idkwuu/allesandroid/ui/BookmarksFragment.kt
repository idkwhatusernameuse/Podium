package dev.idkwuu.allesandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.api.Repo

class BookmarksFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookmarks, container, false)

        Timeline(
            title = getString(R.string.title_bookmarks),
            view = view.findViewById(R.id.timeline),
            withReload = true,
            loader = { context, param -> Repo().getBookmarks(context, param) },
            viewLifecycleOwner = viewLifecycleOwner
        )
        return view
    }
}