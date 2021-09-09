package com.kakaopay.searchbook.utils

import androidx.recyclerview.widget.DiffUtil
import com.kakaopay.searchbook.data.model.book.Book

class DiffCallback : DiffUtil.ItemCallback<Book>() {

    //두 객체를 비교해서 동일한 객체를 교체
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }

}