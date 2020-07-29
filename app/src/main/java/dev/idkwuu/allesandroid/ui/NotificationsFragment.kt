package dev.idkwuu.allesandroid.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.todou.nestrefresh.RefreshHeaderView
import com.todou.nestrefresh.base.OnRefreshListener
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.ui.post.PostListAdapter

class NotificationsFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val shimmer = view.findViewById<ShimmerFrameLayout>(R.id.shimmer)
        shimmer.startShimmer()

        val adapter =
            PostListAdapter(view.context)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        observeData(adapter)

        // Setup pull to refresh
        val pullToRefresh = view.findViewById<RefreshHeaderView>(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh() {
                observeData(adapter, false)
            }
        })

        return view
    }

    private fun observeData(adapter: PostListAdapter, hideShimmer: Boolean = true) {
        Repo().getMentions().observe(viewLifecycleOwner, Observer {
            val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerView)
            if (hideShimmer) {
                val shimmer = requireView().findViewById<ShimmerFrameLayout>(R.id.shimmer)
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
            requireView().findViewById<RefreshHeaderView>(R.id.pullToRefresh).stopRefresh()
            recyclerView.adapter = null
            recyclerView.adapter = adapter
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }
}