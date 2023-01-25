package com.example.recyclerviewreorderstudy

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView


class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var recyclerView: RecyclerView

    private val adapter = ListItemAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.adapter = adapter

        adapter.submitList(
            listOf(
                ListItem(id = 1, title = "Item 1", sortOrder = 1),
                ListItem(id = 2, title = "Item 2", sortOrder = 2),
                ListItem(id = 3, title = "Item 3", sortOrder = 3),
                ListItem(id = 4, title = "Item 4", sortOrder = 4),
                ListItem(id = 5, title = "Item 5", sortOrder = 5),
                ListItem(id = 6, title = "Item 6", sortOrder = 6),
                ListItem(id = 7, title = "Item 7", sortOrder = 7),
                ListItem(id = 8, title = "Item 8", sortOrder = 8),
                ListItem(id = 9, title = "Item 9", sortOrder = 9),
            )
        )
    }
}