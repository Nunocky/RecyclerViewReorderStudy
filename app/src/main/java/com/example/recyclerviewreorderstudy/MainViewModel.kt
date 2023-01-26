package com.example.recyclerviewreorderstudy

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * MainFragmentの ViewModel
 * RecyclerViewに表示するデータ (items)の操作を行う
 */
class MainViewModel : ViewModel() {

    // RecyclerViewに表示するデータ
    val items = MutableLiveData<MutableList<ListItem>>()

    init {
        // データベースに登録されていることを想定して sortOrderの並びをばらばらにしている
        // sortOrderは 1始まり
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
        )
    }

    fun onItemMoved(fromPos: Int, toPos: Int) {
        Log.d(TAG, "onItemMoved : $fromPos -> $toPos")
        // fromPos/toPosは 0始まり
        // 以下に相当する操作をデータベースに対しても行うこと
        // 隣り合ったアイテムでしか moveが発生していないけど、2つ以上の移動もありうるのだろうか
        items.value?.sortedWith(compareBy { it.sortOrder })?.also {
            it[fromPos].sortOrder = toPos + 1
            it[toPos].sortOrder = fromPos + 1
        }

        // 確認用
        items.value?.forEach { item ->
            Log.d(TAG, "  ${item.title}, sortOrder=${item.sortOrder}")
        }
    }

    fun updateText(position: Int, text: String) {
        // 以下に相当する操作をデータベースに対しても行うこと
        items.value?.sortedWith(compareBy { it.sortOrder })?.also {
            it[position].title = text
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}