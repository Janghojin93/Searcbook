package com.kakaopay.searchbook.data.model.responce

import com.google.gson.annotations.SerializedName
import com.kakaopay.searchbook.data.model.book.Book

data class SearchBookResponce(

    @SerializedName("documents")
    val documents: List<Book>,

    @SerializedName("meta")
    val meta: SearchBookMeta
)
