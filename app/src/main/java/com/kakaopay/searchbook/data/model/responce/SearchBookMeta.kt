package com.kakaopay.searchbook.data.model.responce

import com.google.gson.annotations.SerializedName

data class SearchBookMeta(

    //현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
    @SerializedName("is_end") var is_end: Boolean,

    //total_count 중 노출 가능 문서 수
    @SerializedName("pageable_count") var pageable_count: Int,

    //검색된 문서 수
    @SerializedName("total_count") var total_count: Int,
)
