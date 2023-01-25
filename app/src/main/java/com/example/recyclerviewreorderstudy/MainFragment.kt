package com.example.recyclerviewreorderstudy

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class MainViewModel : ViewModel() {
    val items = MutableLiveData<MutableList<ListItem>>()

    init {
        items.value = mutableListOf(
            ListItem(id = 3, title = "Item 3", sortOrder = 7),
            ListItem(id = 2, title = "Item 2", sortOrder = 8),
            ListItem(id = 1, title = "Item 1", sortOrder = 9),
            ListItem(id = 6, title = "Item 6", sortOrder = 4),
            ListItem(id = 5, title = "Item 5", sortOrder = 5),
            ListItem(id = 4, title = "Item 4", sortOrder = 6),
            ListItem(id = 9, title = "Item 9", sortOrder = 1),
            ListItem(id = 8, title = "Item 8", sortOrder = 2),
            ListItem(id = 7, title = "Item 7", sortOrder = 3),
        ) // .sortedWith(compareBy { it.sortOrder }).toMutableList()
    }

    fun onItemMoved(fromPos: Int, toPos: Int) {
        Log.d("MainFragment", "onItemMoved : $fromPos -> $toPos")
        // sortOrderは 1始まり、 fromPos/toPosは 0始まりとしている
        // 以下に相当する操作をデータベースに対して行う。
        items.value?.sortedWith(compareBy { it.sortOrder })?.also {
            it[fromPos].sortOrder = toPos + 1
            it[toPos].sortOrder = fromPos + 1
        }

        // 確認用
        items.value?.forEachIndexed() { n, item ->
            Log.d("MainFragment", "  ${item.title}, sortOrder=${item.sortOrder}")
        }
    }

    fun updateText(position: Int, text: String) {
        items.value?.get(position)?.title = text
    }
}

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
                Log.d("MainFragment", "$position 番目のアイテム  テキスト変更 → $text")

                // データベースを更新する
                viewModel.updateText(position, text)
                adapter.notifyItemChanged(position)
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
                    Log.d("MainFragment", "itemsの並びが変更された: $fromPos → $toPos")
                    viewModel.onItemMoved(fromPos, toPos)
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }
            }
        )
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}