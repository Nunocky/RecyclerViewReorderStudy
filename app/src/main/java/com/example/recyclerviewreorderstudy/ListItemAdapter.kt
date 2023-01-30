package com.example.recyclerviewreorderstudy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView用のリストアダプタ
 */
class ListItemAdapter :
    RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {

    interface Callback {
        /**
         * @param position itemsの表示位置
         * @param text 更新後のテキスト
         */
        fun onTextChanged(position: Int, text: String)
    }

    private val dataSet = mutableListOf<ListItem>()

    var callback: Callback? = null

    fun setData(newItems: List<ListItem>) {
        dataSet.clear()
        dataSet.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val listItem = dataSet[position]

        holder.etTitle.setText(listItem.title)

        holder.etTitle.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ソフトウェアキーボードを閉じる
                val inputMethodManager =
                    holder.etTitle.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                inputMethodManager.hideSoftInputFromWindow(
                    v.windowToken,
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
                )

                // テキストの変更を通知
                callback?.onTextChanged(
                    position,
                    holder.etTitle.text.toString()
                )

                return@setOnEditorActionListener true
            }
            false
        }

        // フォーカスを外れたらテキストの変更を通知
        holder.etTitle.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                callback?.onTextChanged(position, holder.etTitle.text.toString())
            }
        }
    }

    override fun getItemCount(): Int = dataSet.size

    class ListItemViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val etTitle: EditText = view.findViewById(R.id.etTitle)
    }
}