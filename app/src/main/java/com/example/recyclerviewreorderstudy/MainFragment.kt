package com.example.recyclerviewreorderstudy

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListItemAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)

        val items = arrayListOf(
            ListItem(id = 9, title = "Item 9", sortOrder = 1),
            ListItem(id = 8, title = "Item 8", sortOrder = 2),
            ListItem(id = 7, title = "Item 7", sortOrder = 3),
            ListItem(id = 6, title = "Item 6", sortOrder = 4),
            ListItem(id = 5, title = "Item 5", sortOrder = 5),
            ListItem(id = 4, title = "Item 4", sortOrder = 6),
            ListItem(id = 3, title = "Item 3", sortOrder = 7),
            ListItem(id = 2, title = "Item 2", sortOrder = 8),
            ListItem(id = 1, title = "Item 1", sortOrder = 9),
        )

        adapter = ListItemAdapter(items)
        recyclerView.adapter = adapter

        // RecyclerViewのアイテムをドラッグ操作で並べ替える
        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val fromPos = viewHolder.adapterPosition //  bindingAdapterPosition
                    val toPos = target.adapterPosition  //bindingAdapterPosition
                    adapter.notifyItemMoved(fromPos, toPos)

                    // itemsの並びが変更になったらデータベースを更新する

                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }
            }
        )
        itemTouchHelper.attachToRecyclerView(recyclerView)

        adapter.callback = object : ListItemAdapter.Callback {
            override fun onTextChanged(position: Int, text: String) {
                Log.d("MainFragment", "$position 番目のアイテム  テキスト変更 → $text")

                // データベースを更新する
            }
        }
    }
}