package dev.idkwuu.allesandroid.ui.notifications

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.models.AllesPost
import dev.idkwuu.allesandroid.ui.Timeline

class NotificationsFragment : Fragment() {

    private val viewModel: NotificationsViewModel by lazy {
        ViewModelProvider(this).get(NotificationsViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        viewModel.fetchTimeline(requireContext())

        Timeline(
            title = getString(R.string.title_notifications),
            view = view.findViewById(R.id.timeline),
            withReload = true,
            data = viewModel.notifications as LiveData<MutableList<AllesPost>>,
            viewLifecycleOwner = viewLifecycleOwner,
            reloadFunction = { viewModel.fetchTimeline(requireContext()) }
        )

        return view
    }
}