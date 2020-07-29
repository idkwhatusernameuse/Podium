package dev.idkwuu.allesandroid.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.widget.NestedScrollView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.idkwuu.allesandroid.ui.post.PostActivity

class FloatingActionButtonLayout {
    fun set(activity: Activity, context: Context, fab: FloatingActionButton, nestedScrollView: NestedScrollView) {
        fab.visibility = View.VISIBLE
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener  { _, _, scrollY: Int, _, oldScrollY: Int ->
            if (scrollY > oldScrollY) {
                fab.hide()
            } else {
                fab.show()
            }
        })

        // Post FAB!
        fab.setOnClickListener {
            startActivityForResult(activity, Intent(context, PostActivity::class.java), 69, null)
        }
    }
}