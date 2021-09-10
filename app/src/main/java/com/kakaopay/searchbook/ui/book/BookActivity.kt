package com.kakaopay.searchbook.ui.book

import android.os.Bundle
import androidx.activity.viewModels
import com.kakaopay.searchbook.R
import com.kakaopay.searchbook.databinding.ActivityBookBinding
import com.kakaopay.searchbook.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookActivity : BaseActivity(), BookNavigator {

    private lateinit var binding: ActivityBookBinding
    private val bookViewModel: BookViewModel by viewModels()

    override fun initViewBinding() {
        binding = ActivityBookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.view_bookactivity_fragment_container, SearchBookFragment.newInstance())
                .commit()
        }

    }

    override fun openBookDetailFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            .replace(R.id.view_bookactivity_fragment_container, BookDetailFragment.newInstance())
            .addToBackStack(null).commit()
    }
}