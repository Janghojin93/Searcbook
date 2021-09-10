package com.kakaopay.searchbook.ui.book

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.kakaopay.searchbook.R
import com.kakaopay.searchbook.databinding.ActivityBookBinding
import com.kakaopay.searchbook.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookActivity : BaseActivity(), BookNavigator {

    private lateinit var binding: ActivityBookBinding
    private val bookViewModel: BookViewModel by viewModels()

    //뒤로 가기 연속 클릭 대기 시간
    var backWait: Long = 0

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

    override fun onBackPressed() {

        //뒤로 갈 수 있는 프래그먼트가 남아있는지 없는지 체크한다.
        if (supportFragmentManager.backStackEntryCount == 0) {
            if (System.currentTimeMillis() - backWait >= 2000) {
                backWait = System.currentTimeMillis()
                Snackbar.make(
                    binding.constraintlayoutBookParentView,
                    "뒤로가기 버튼을 한번 더 누르면 종료됩니다.",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                finish() //액티비티 종료
            }
        } else {
            super.onBackPressed()
        }


    }
}