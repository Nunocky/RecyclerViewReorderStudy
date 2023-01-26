package com.example.recyclerviewreorderstudy

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


/**
 * RecyclerViewを表示する画面
 */
class MainFragment : Fragment(R.layout.fragment_main) {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)

        adapter = ListItemAdapter()
        recyclerView.adapter = adapter

        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter.setData(items.sortedWith(compareBy { it.sortOrder }).toMutableList())
        }

        // テキスト変更のコールバック
        adapter.callback = object : ListItemAdapter.Callback {
            override fun onTextChanged(position: Int, text: String) {
                Log.d(TAG, "$position 番目のアイテム  テキスト変更 → $text")

                // データベースを更新する
                viewModel.updateText(position, text)
//                adapter.notifyItemChanged(position) // エラーになる
            }
        }

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
                    return true
                }

                override fun onMoved(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    fromPos: Int,
                    target: RecyclerView.ViewHolder,
                    toPos: Int,
                    x: Int,
                    y: Int
                ) {
                    super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
                    viewModel.onItemMoved(fromPos, toPos)
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }
            }
        )
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    companion object {
        private const val TAG = "MainFragment"
    }
}