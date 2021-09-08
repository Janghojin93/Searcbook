package com.kakaopay.searchbook.ui.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kakaopay.searchbook.utils.OnThrottleClickListener

abstract class BaseActivity : AppCompatActivity() {

    protected abstract fun initViewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()

    }

    fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }

}