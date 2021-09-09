package com.kakaopay.searchbook.ui.book

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kakaopay.searchbook.data.model.book.Book
import com.kakaopay.searchbook.databinding.ListItemSearchBookBinding
import com.kakaopay.searchbook.utils.DiffCallback
import java.util.*

class SearchBookAdapter : ListAdapter<Book, RecyclerView.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewholder =
            SearchBookViewHolder(
                ListItemSearchBookBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
        return viewholder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchBookViewHolder) {
            val book = getItem(position) as Book
            holder.binding.apply {
                bookModel = book
                executePendingBindings()
            }
        }
    }


    inner class SearchBookViewHolder(val binding: ListItemSearchBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imagebuttonBookLikebutton.setOnClickListener {
            }
        }


        fun bind(data: Book) {
            with(binding) {
                textviewBookTitle.text = data.title
                textviewBookSubject.text = data.contents
                textviewBookPrice.text = "${data.price}"
                textviewBookReleaseDate.text = "${data.datetime}"
            }
        }

        fun setAlpha(alpha: Float) {
            with(binding) {

            }
        }
    }

}