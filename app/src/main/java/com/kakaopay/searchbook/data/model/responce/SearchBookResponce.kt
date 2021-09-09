package com.kakaopay.searchbook.data.model.responce

import com.google.gson.annotations.SerializedName
import com.kakaopay.searchbook.data.model.book.Book

data class SearchBookResponce(

    @SerializedName("documents")
    var documents: MutableList<Book>,

    @SerializedName("meta")
    var meta: SearchBookMeta
)
