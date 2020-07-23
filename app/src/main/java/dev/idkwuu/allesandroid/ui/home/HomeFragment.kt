package dev.idkwuu.allesandroid.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.todou.nestrefresh.RefreshHeaderView
import com.todou.nestrefresh.base.OnRefreshListener
import dev.idkwuu.allesandroid.ui.PostActivity
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.ui.feed.FeedAdapter

class HomeFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val shimmer = view.findViewById<ShimmerFrameLayout>(R.id.shimmer)
        shimmer.startShimmer()

        val adapter = FeedAdapter(view.context)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        observeData(adapter)

        // Hide FAB on scroll
        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.nestedScrollView)
        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener  { _, _, scrollY: Int, _, oldScrollY: Int ->
            if (scrollY > oldScrollY) {
                fab.hide()
            } else {
                fab.show()
            }
        })
        // Setup pull to refresh
        val pullToRefresh = view.findViewById<RefreshHeaderView>(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh() {
                observeData(adapter, false)
            }
        })

        // Post FAB!
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            startActivity(Intent(context, PostActivity::class.java))
        }
        return view
    }

    private fun observeData(adapter: FeedAdapter, hideShimmer: Boolean = true) {
        viewModel.fetchPosts().observe(viewLifecycleOwner, Observer {
            if (hideShimmer) {
                val shimmer = requireView().findViewById<ShimmerFrameLayout>(R.id.shimmer)
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                requireView().findViewById<RecyclerView>(R.id.recyclerView).visibility = View.VISIBLE
            }
            requireView().findViewById<RefreshHeaderView>(R.id.pullToRefresh).stopRefresh()
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }
}