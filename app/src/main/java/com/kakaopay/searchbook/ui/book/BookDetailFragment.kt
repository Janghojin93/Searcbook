package com.kakaopay.searchbook.ui.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.kakaopay.searchbook.R
import com.kakaopay.searchbook.databinding.FragmentBookDetailBinding
import com.kakaopay.searchbook.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BookDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentBookDetailBinding

    override val layoutId = R.layout.fragment_book_detail

    private lateinit var mRootView: View

    private val bookViewModel: BookViewModel by activityViewModels()

    private val TAG = "BookDetailFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = binding.root

        Log.d(TAG, "item::${bookViewModel.detailBook?.title}")

        initUi()

        return mRootView
    }

    companion object {
        @JvmStatic
        fun newInstance() = BookDetailFragment()
    }


    private fun initUi() {
        binding.apply {
            bookDetailModel = bookViewModel.detailBook
        }

        binding.imagebuttonBookdetailLikebutton.onThrottleClick {
            Log.d(TAG, "좋아요 클릭")
            changeLikeState()
            bookViewModel.likeBook()
        }


        binding.imagebuttonBookdetailBackbutton.onThrottleClick {
            (activity as BookActivity).onBackPressed()
        }

    }

    private fun changeLikeState() {
        if (bookViewModel.detailBook!!.islike) {
            binding.imagebuttonBookdetailLikebutton.setImageResource(R.drawable.ic_baseline_favorite_24_false)
            Toast.makeText(activity, R.string.detailbook_like_false, Toast.LENGTH_SHORT).show()
        } else {
            binding.imagebuttonBookdetailLikebutton.setImageResource(R.drawable.ic_baseline_favorite_24_true)
            Toast.makeText(activity, R.string.detailbook_like_true, Toast.LENGTH_SHORT).show()
        }
    }


}