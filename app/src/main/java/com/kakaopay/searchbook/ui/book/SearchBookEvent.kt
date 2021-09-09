package com.kakaopay.searchbook.ui.book

sealed class SearchBookEvent {

    object NewSearchEvent : SearchBookEvent()
    object NextPageEvent : SearchBookEvent()

    // 프로세스가 죽었을때 다시 검색하는 이벤트
    object RestoreStateEvent : SearchBookEvent()

}