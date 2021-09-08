package com.kakaopay.searchbook.data.model.book

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "book_table")
data class Book(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,

    //도서 저자 리스트
    @SerializedName("authors") var authors: List<String>,

    //도서 소개
    @SerializedName("contents") var contents: String,

    //도서 출판날짜, ISO 8601 형식 [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
    @SerializedName("datetime") var datetime: String,

    //ISBN10(10자리) 또는 ISBN13(13자리) 형식의 국제 표준 도서번호(International Standard Book Number) ISBN10 또는 ISBN13 중 하나 이상 포함 두 값이 모두 제공될 경우 공백(' ')으로 구분
    @SerializedName("isbn") var isbn: String,

    //도서 정가
    @SerializedName("price") var price: Int,

    //도서 출판사
    @SerializedName("publisher") var publisher: String,

    //도서 판매가
    @SerializedName("sale_price") var sale_price: Int,

    //도서 판매 상태 정보 (정상, 품절, 절판 등) 상황에 따라 변동 가능성이 있으므로 문자열 처리 지양, 단순 노출 요소로 활용 권장
    @SerializedName("status") var status: String,

    //도서 표지 미리보기 URL
    @SerializedName("thumbnail") var thumbnail: String,

    //도서 제목
    @SerializedName("title") var title: String,

    //도서 번역자 리스트
    @SerializedName("translators") var translators: List<String>,

    //도서 상세 URL
    @SerializedName("url") var url: String,

    //좋아요여부
    @SerializedName("islike") var islike: Boolean = false
)
