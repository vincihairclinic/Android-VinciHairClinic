package com.example.vinciandroidv2.ui.main.book

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.dp
import kotlinx.android.synthetic.main.fragment_expanded_book_information_article.*

class ExpandedInfoArticleFragment(
    private val image: String?,
    private val title: String?,
    private val content: String?
) : BaseFragment() {
    override val layoutRes = R.layout.fragment_expanded_book_information_article
    override val TAG = "ExpandedInfoArticleFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        closeButton?.setOnClickListener { activity?.onBackPressed() }
        Glide.with(this).load(image).into(imageContainer)
        titleTextField?.text = title
        val textHtml = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(content ?: "", Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(content ?: "")
        }
        mainTextField?.text = textHtml
        mainTextField?.setPadding(0, 0, 0, 32.dp)
        instructionContainer?.isVisible = false
        additionContainer?.isVisible = false
    }
}