package com.example.dopaminalert

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.twidyuni.dopaminalert.AppInfo
import com.twidyuni.dopaminalert.R

class AppAdapter(val appList: List<AppInfo>) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appName: TextView = itemView.findViewById(R.id.appName)
        val maxUsageTime: EditText = itemView.findViewById(R.id.maxUsageTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val appInfo = appList[position]
        holder.appName.text = appInfo.appName

        val sharedPreferences = holder.itemView.context.getSharedPreferences(
            "AppUsagePreferences",
            Context.MODE_PRIVATE
        )
        val savedTime = sharedPreferences.getInt(appInfo.packageName, 0)
        holder.maxUsageTime.setText(if (savedTime > 0) savedTime.toString() else "")

        holder.maxUsageTime.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                appInfo.maxUsageTime = s.toString().toIntOrNull()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun getItemCount() = appList.size
}
