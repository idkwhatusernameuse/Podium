package dev.idkwuu.allesandroid.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        Repo().getMentions(requireContext()).observe(viewLifecycleOwner, Observer {
            val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerView)
            val shimmer = requireView().findViewById<ShimmerFrameLayout>(R.id.shimmer)
            if (hideShimmer) {
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
            requireView().findViewById<RefreshHeaderView>(R.id.pullToRefresh).stopRefresh()
            recyclerView.adapter = null
            recyclerView.adapter = adapter

            val errorLayout = requireView().findViewById<View>(R.id.error_loading)
            if (it == null) {
                // Show R.layout.layout_error_loading if there was an error
                recyclerView.visibility = View.GONE
                errorLayout.visibility = View.VISIBLE
                errorLayout.findViewById<Button>(R.id.retry).setOnClickListener {
                    recyclerView.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    shimmer.startShimmer()
                    shimmer.visibility = View.VISIBLE
                    observeData(PostListAdapter(requireContext()), hideShimmer = true)
                }
            } else {
                errorLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.setListData(it)
                adapter.notifyDataSetChanged()
            }
        })
    }
}