package com.kakaopay.searchbook.ui.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = binding.root
        return mRootView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BookDetailFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


}