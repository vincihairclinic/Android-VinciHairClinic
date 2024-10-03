package com.example.vinciandroidv2.ui.main.book

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.network.respons.BookInformation
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_book_info_tab_information.*
import kotlinx.android.synthetic.main.item_row_text_view_image_sample_one.view.*

class TabInformationFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_book_info_tab_information
    override val TAG = "TabInformationFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        informationRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = Adapter(getBookInfo().bookInformationList ?: ArrayList())
        }
    }

    private fun getBookInfo() = (parentFragment as BookInfoFragment).bookInfo

    override fun setLocalization() {
        super.setLocalization()
    }

    inner class Adapter(private val list: ArrayList<BookInformation>) :
        RecyclerView.Adapter<Adapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_row_text_view_image_sample_one, parent, false)
        )

        override fun onBindViewHolder(holder: Adapter.Holder, position: Int) =
            holder.bind(list[position])

        override fun getItemCount() = list.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(bookInformation: BookInformation) {
                Glide.with(itemView).load(bookInformation.urlImage).into(itemView.cardImage)
                val textHtml = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(bookInformation.name ?: "", Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(bookInformation.name ?: "")
                }
                itemView.cardTextField?.text = textHtml
                itemView.setOnClickListener {
                    replace(
                        R.id.content, Screens.Book.getExpandedInfoArticleFragment(
                            bookInformation.urlImage,
                            bookInformation.name,
                            bookInformation.content
                        )
                    )
                }
            }
        }
    }
}