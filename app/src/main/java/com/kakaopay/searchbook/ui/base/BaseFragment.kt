package com.kakaopay.searchbook.ui.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import androidx.fragment.app.Fragment
import com.kakaopay.searchbook.utils.OnThrottleClickListener

abstract class BaseFragment : Fragment() {

    abstract val layoutId: Int

    fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }

    fun getNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork : NetworkInfo? = cm.activeNetworkInfo
        val isConnected : Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }

}