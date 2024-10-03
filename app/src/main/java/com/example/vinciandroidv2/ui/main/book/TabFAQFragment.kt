package com.example.vinciandroidv2.ui.main.book

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_book_info_tab_faq.*
import kotlinx.android.synthetic.main.item_row_faq_question_expandable_answer_sample_one.view.*

class TabFAQFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_book_info_tab_faq
    override val TAG = "TabFAQFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getBookInfo().faqList?.forEach { faq ->
            LayoutInflater.from(context).inflate(
                R.layout.item_row_faq_question_expandable_answer_sample_one,
                faqContainer,
                false
            )?.apply {
                questionTextField?.text = faq.question
                answerTextField?.text = faq.answer
                setOnClickListener {
                    isSelected = !isSelected
                    answerTextField?.isVisible = isSelected
                    dividerBottom?.setBackgroundColor(
                        Color.parseColor(if (isSelected) "#7D5F2B" else "#F1F0EA")
                    )
                }
            }?.also { faqContainer?.addView(it) }
        }
    }

    private fun getBookInfo() = (parentFragment as BookInfoFragment).bookInfo

    override fun setLocalization() {
        super.setLocalization()
        val textHtml = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(RealmHelper.getLocalizedText("home.bookinfo.tab.two.subtitle.text") ?: "", Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(RealmHelper.getLocalizedText("home.bookinfo.tab.two.subtitle.text") ?: "")
        }
        titleTextField?.text = StringBuilder()
            .append(getBookInfo().name)
            .append(" ")
            .append(textHtml)
    }
}