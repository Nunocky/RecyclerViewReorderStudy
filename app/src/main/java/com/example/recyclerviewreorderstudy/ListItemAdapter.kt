package com.example.recyclerviewreorderstudy

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
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

    var callback : Callback? = null

    fun setData(newItems : List<ListItem>) {
        dataSet.clear()
        dataSet.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        val etTitle = view.findViewById<EditText>(R.id.etTitle)

        etTitle.setOnKeyListener { v, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                val inputMethodManager =
                    parent.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    v.windowToken,
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
                )

                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        return ListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val listItem = dataSet[position]

        holder.etTitle.setText(listItem.title)

        holder.onTextChanged = {}
        holder.etTitle.setText(dataSet[position].title)
        holder.onTextChanged = {
            // dataSet[position].title = it
            callback?.onTextChanged(position, it)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    class ListItemViewHolder(view: View, var onTextChanged: ((String) -> Unit) = {}) :
        RecyclerView.ViewHolder(view) {
        val etTitle: EditText = view.findViewById(R.id.etTitle)

        init {
            etTitle.doOnTextChanged { text, _, _, _ ->
                onTextChanged(text.toString())
            }
        }
    }
}