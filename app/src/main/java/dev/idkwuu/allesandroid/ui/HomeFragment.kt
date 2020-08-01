package dev.idkwuu.allesandroid.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.api.Repo

class HomeFragment : Fragment() {

    private lateinit var timeline: Timeline

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        timeline = Timeline(
            title = getString(R.string.title_home),
            view = view.findViewById(R.id.timeline),
            fabLayout = view.findViewById(R.id.floatingActionButtonLayout),
            activity = requireActivity(),
            withReload = true,
            loader = { context, param -> Repo().getPosts(context, param) },
            viewLifecycleOwner = viewLifecycleOwner
        )

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 69) run {
            timeline.fullReload()
        }
    }
}