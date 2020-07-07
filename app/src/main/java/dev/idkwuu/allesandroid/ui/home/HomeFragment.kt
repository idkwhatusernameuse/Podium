package dev.idkwuu.allesandroid.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.ui.feed.FeedAdapter

class HomeFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    private lateinit var adapter: FeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val shimmer = view.findViewById<ShimmerFrameLayout>(R.id.shimmer)
        shimmer.startShimmer()

        adapter = FeedAdapter(view.context)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        observeData()

        // Hide FAB on scroll
        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.nestedScrollView)
        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)!!
        nestedScrollView.setOnScrollChangeListener { _, _, scrollY: Int, _, oldScrollY: Int ->
            if (scrollY > oldScrollY) {
                fab.hide()
            } else {
                fab.show()
            }
        }
        // Setup pull to refresh
        val pullToRefresh = view.findViewById<SwipeRefreshLayout>(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            shimmer.startShimmer()
            shimmer.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            observeData()
            pullToRefresh.isRefreshing = true
        }
        return view
    }

    private fun observeData() {
        viewModel.fetchPosts().observe(viewLifecycleOwner, Observer {
            val shimmer = requireView().findViewById<ShimmerFrameLayout>(R.id.shimmer)
            shimmer.stopShimmer()
            shimmer.visibility = View.GONE
            requireView().findViewById<RecyclerView>(R.id.recyclerView).visibility = View.VISIBLE
            requireView().findViewById<SwipeRefreshLayout>(R.id.pullToRefresh).isRefreshing = false
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }
}