package com.example.recyclerviewreorderstudy

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

// TODO テキスト編集完了時のコールバック (「x番目のテキストが xxxになった」)  → 画面の終了時にまとめて処理すればいい?
//      ソフトウェアキーボードを閉じるとき EditTextへのフォーカスを外す

class ListItemAdapter(private val dataSet: ArrayList<ListItem>) :
    RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {

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
    }

    override fun getItemCount(): Int = dataSet.size

    class ListItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val etTitle: EditText = view.findViewById(R.id.etTitle)
    }
}