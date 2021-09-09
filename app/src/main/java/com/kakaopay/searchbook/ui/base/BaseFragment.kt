package com.kakaopay.searchbook.ui.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kakaopay.searchbook.utils.OnThrottleClickListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseFragment : Fragment() {

    abstract val layoutId: Int

    fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }

    fun getNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }

    fun EditText.afterTextChangedCustom(delay: Long, search: (String) -> Unit) {
        var job: Job? = null
        this.addTextChangedListener {
            it ?: return@addTextChangedListener
            job?.cancel()
            job = lifecycleScope.launch {
                delay(delay)
                search(it.toString())
            }
        }
    }

}