package com.example.calling.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calling.R
import com.example.calling.model.CallLogs
import java.util.*

class CallLogsAdapter(private val callLogsArrayList: ArrayList<CallLogs>) :
    RecyclerView.Adapter<CallLogsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.call_log_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLog: CallLogs = callLogsArrayList[position]
        holder.mobile.text = currentLog.mobile
        holder.date.text = currentLog.date
        holder.time.text = currentLog.time
        holder.action.text = currentLog.action

    }

    override fun getItemCount(): Int {
        return callLogsArrayList.size
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var mobile: TextView = itemView.findViewById(R.id.textViewMobile)
        var date: TextView = itemView.findViewById(R.id.textViewDate)
        var time: TextView = itemView.findViewById(R.id.textViewTime)
        var action: TextView = itemView.findViewById(R.id.textViewAction)
    }

}