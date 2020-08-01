@file:Suppress("RemoveExplicitTypeArguments")

package dev.idkwuu.allesandroid.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.todou.nestrefresh.RefreshHeaderView
import com.todou.nestrefresh.base.OnRefreshListener
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.models.AllesPost
import dev.idkwuu.allesandroid.ui.post.PostListAdapter

class Timeline(
    title: String? = null,
    private val view: View,
    fabLayout: FloatingActionButton? = null,
    activity: Activity? = null,
    withReload: Boolean,
    private val loader: (context: Context, param: String) -> LiveData<MutableList<AllesPost>?>,
    private val viewLifecycleOwner: LifecycleOwner,
    private val user: String? = null
) {

    private val titleView: TextView by lazy { view.findViewById<TextView>(R.id.title) }
    private val recyclerView: RecyclerView by lazy { view.findViewById<RecyclerView>(R.id.recyclerView) }
    private val shimmer: ShimmerFrameLayout by lazy { view.findViewById<ShimmerFrameLayout>(R.id.shimmer) }
    private val adapter: PostListAdapter by lazy { PostListAdapter(view.context) }
    private val pullToRefresh: RefreshHeaderView by lazy { view.findViewById<RefreshHeaderView>(R.id.pullToRefresh) }
    private val errorLayout: View by lazy { view.findViewById<View>(R.id.error_loading) }

    private fun parameter(reload: Boolean?) = user ?: reload.toString()

    init {
        if (title != null) {
            titleView.text = title
        } else {
            titleView.visibility = View.GONE
        }
        shimmer.startShimmer()
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        observeData(param = parameter(false))

        // Set up FAB
        if (activity != null && fabLayout != null) {
            FloatingActionButtonLayout().set(
                activity = activity,
                context = view.context,
                fab = fabLayout,
                nestedScrollView = view.findViewById(R.id.nestedScrollView)
            )
        }

        if (withReload) {
            pullToRefresh.setOnRefreshListener(object : OnRefreshListener {
                override fun onRefresh() {
                    observeData(hideShimmer = false, param = parameter(true))
                }
            })
        } else {
            pullToRefresh.visibility = View.GONE
        }
    }

    private fun observeData(hideShimmer: Boolean = true, param: String) {
        loader(view.context, param).observe(viewLifecycleOwner, Observer {
            if (hideShimmer) {
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
            pullToRefresh.stopRefresh()
            recyclerView.adapter = null
            recyclerView.adapter = adapter

            if (it == null) {
                // Show R.layout.layout_error_loading if there was an error
                recyclerView.visibility = View.GONE
                errorLayout.visibility = View.VISIBLE
                errorLayout.findViewById<Button>(R.id.retry).setOnClickListener {
                    recyclerView.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    shimmer.startShimmer()
                    shimmer.visibility = View.VISIBLE
                    fullReload()
                }
            } else {
                errorLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.setListData(it)
                adapter.notifyDataSetChanged()
            }
        })
    }

    fun fullReload() = observeData(hideShimmer = true, param = parameter(true))
}