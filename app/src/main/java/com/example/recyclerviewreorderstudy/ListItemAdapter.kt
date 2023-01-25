package com.example.recyclerviewreorderstudy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

//private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListItem>() {
//    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
//        return oldItem == newItem
//    }
//
//    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
//        return oldItem.id == newItem.id
//    }
//}

class ListItemAdapter(private val dataSet: ArrayList<ListItem>) : RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val listItem = dataSet[position]
        holder.textValue = listItem.title
    }

    override fun getItemCount(): Int = dataSet.size

    class ListItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        var textValue: String
            get() = view.findViewById<EditText>(R.id.etTitle).text.toString()
            set(value) {
                view.findViewById<EditText>(R.id.etTitle).setText(value)
            }
    }
}