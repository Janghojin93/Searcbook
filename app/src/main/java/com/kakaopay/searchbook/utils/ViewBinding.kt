package com.kakaopay.searchbook.utils

import android.net.Uri
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kakaopay.searchbook.R
import java.text.SimpleDateFormat

object ViewBinding {

    @JvmStatic
    @BindingAdapter("bookthumbnail")
    fun loadBookImage(view: ImageView, imageUrl: String) {
        Glide.with(view.context)
            .load(Uri.parse(imageUrl))
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.img_book_loading)
                    .error(R.drawable.img_book_default)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("bookprice")
    fun loadBookPrice(view: TextView, price: Int) {
        view.setText("${price}원")
    }

    @JvmStatic
    @BindingAdapter("bookcontents")
    fun loadBookContents(view: TextView, contents: String) {
        if (contents.isNotEmpty()) {
            view.setText(contents)
        } else {
            view.setText(".....")
        }
    }


    @JvmStatic
    @BindingAdapter("bookreleasedate")
    fun loadBookReleaseDate(view: TextView, date: String) {
        //날짜의 포맷을 변경한다. ( yyyy-MM-dd'T'HH:mm:ss.sssZ  --->  yyyy년 MM월 dd일)
        if (date.isNotEmpty()) {
            val oldformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ")
            val date = oldformat.parse(date)
            val newformat = SimpleDateFormat("yyyy년 MM월 dd일")
            view.setText(newformat.format(date))
        } else {
            view.setText("알수없음")
        }

    }


    @JvmStatic
    @BindingAdapter("islikebook")
    fun isLikeBook(view: ImageButton, state: Boolean) {
        if (state) {
            view.setImageResource(R.drawable.ic_baseline_favorite_24_true)
        } else {
            view.setImageResource(R.drawable.ic_baseline_favorite_24_false)
        }
    }


}