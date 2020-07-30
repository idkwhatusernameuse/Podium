package dev.idkwuu.allesandroid.ui.licenses

import android.annotation.SuppressLint
import android.content.Context
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.models.License
import dev.idkwuu.allesandroid.util.LicenseExtractor

class LicensesAdapter(
    private val context: Context,
    private val licenses: List<License>
) : RecyclerView.Adapter<LicensesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bindView(license: License) {
            itemView.findViewById<TextView>(R.id.title).text = license.title
            itemView.findViewById<TextView>(R.id.license_type).text = license.type
            val body = itemView.findViewById<TextView>(R.id.license)

            // Get license text
            val c = Class.forName("dev.idkwuu.allesandroid.R")
            val f = c.declaredClasses[17]
            val d = f.getDeclaredField(license.file).getInt(null)
            body.text = LicenseExtractor().getResource(itemView.context, d)

            var hidden = true
            itemView.setOnClickListener {
                TransitionManager.beginDelayedTransition(itemView as ViewGroup)
                val params: ViewGroup.LayoutParams = body.layoutParams
                params.height = if (hidden) {
                    ViewGroup.LayoutParams.WRAP_CONTENT
                } else {
                    0
                }
                body.layoutParams = params
                hidden = !hidden
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LicensesAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_license, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = licenses.size

    override fun onBindViewHolder(holder: LicensesAdapter.ViewHolder, position: Int) {
        holder.bindView(licenses[position])
    }

}