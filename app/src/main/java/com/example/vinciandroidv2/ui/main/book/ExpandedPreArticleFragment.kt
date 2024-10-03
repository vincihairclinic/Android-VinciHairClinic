package com.example.vinciandroidv2.ui.main.book

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.network.respons.*
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.dp
import com.google.android.material.divider.MaterialDivider
import com.makeramen.roundedimageview.RoundedImageView

class ExpandedPreArticleFragment(
    private val image: String?,
    private val title: String?,
    private val content: String?,
    private val instructionList: ArrayList<Instruction>?,
    private val additionInfoList: ArrayList<PreAdditionInfo>?,
) : BaseFragment() {
    override val layoutRes = R.layout.fragment_expanded_book_information_article
    override val TAG = "ExpandedPreArticleFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.findViewById<CardView>(R.id.closeButton)
            ?.setOnClickListener { activity?.onBackPressed() }

        view?.findViewById<RoundedImageView>(R.id.imageContainer)?.apply {
            Glide.with(this).load(image).into(this)
        }
        view?.findViewById<TextView>(R.id.titleTextField)?.apply {
            text = title ?: run { isVisible = false; "" }
        }
        val textHtml = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(content ?: "", Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(content ?: "")
        }
        view?.findViewById<TextView>(R.id.mainTextField)?.apply {
            text = textHtml ?: run { isVisible = false; "" }
        }
        view?.findViewById<LinearLayout>(R.id.instructionContainer)?.apply {
            if (null != instructionList && 0 != instructionList.size) {
                instructionList.forEach { instruction ->
                    addView(createInformationItemView(instruction, this))
                }
            } else isVisible = false
        }
        view?.findViewById<LinearLayout>(R.id.additionContainer)?.apply {
            if (null != additionInfoList && 0 != additionInfoList.size) {
                additionInfoList.forEachIndexed { additionIndex, additionInfo ->
                    addView(createAdditionInfoView(additionIndex, additionInfo, this))
                }
            } else isVisible = false
        }
    }

    private fun createInformationItemView(instruction: Instruction, root: ViewGroup) =
        LayoutInflater.from(context).inflate(
            R.layout.item_row_faq_question_expandable_answer_sample_one, root, false
        )?.apply {
            findViewById<TextView>(R.id.questionTextField)?.apply { text = instruction.title }
            findViewById<TextView>(R.id.answerTextField)?.apply { text = instruction.content }
            setOnClickListener {
                isSelected = !isSelected
                findViewById<TextView>(R.id.answerTextField)?.isVisible = isSelected
                findViewById<MaterialDivider>(R.id.dividerBottom)?.setBackgroundColor(
                    Color.parseColor(if (isSelected) "#7D5F2B" else "#F1F0EA")
                )
            }
        }

    private fun createAdditionInfoView(
        additionIndex: Int,
        addition: PreAdditionInfo,
        root: ViewGroup
    ) = LayoutInflater.from(context).inflate(
        R.layout.view_addition_expanded_book_information_article, root, false
    )?.apply {
        findViewById<TextView>(R.id.titleTextField)?.apply {
            text = addition.title ?: run { isVisible = false; "" }
            if (1 >= additionIndex) {
                updateLayoutParams<ConstraintLayout.LayoutParams> {
                    setMargins(32.dp, 32.dp, 32.dp, 0)
                }
            }
        }
        findViewById<RecyclerView>(R.id.imageRecyclerView)?.apply {
            if (null != addition.urlImageList && 0 != addition.urlImageList?.size) {
                addition.urlImageList?.let { list ->
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    setHasFixedSize(true)
                    adapter = ImageAdditionAdapter(list)
                }
            } else isVisible = false
        }
        findViewById<TextView>(R.id.mainTextField)?.apply {
            text = addition.content ?: run { isVisible = false; "" }
        }
        findViewById<LinearLayout>(R.id.itemContainer)?.apply {
            if (null != addition.additionItemList && 0 != addition.additionItemList?.size) {
                addition.additionItemList?.forEach { item ->
                    addView(createAdditionItemView(item, this))
                }
            } else isVisible = false
        }
    }

    private fun createAdditionItemView(item: AdditionItem, root: ViewGroup) =
        LayoutInflater.from(context).inflate(
            R.layout.item_row_card_view_middle_two_text_view_sample_five, root, false
        )?.apply {
            findViewById<TextView>(R.id.titleTextField)?.apply {
                text = item.title ?: run {
                    this.isVisible = false
                    findViewById<TextView>(R.id.textField)?.setTextColor(Color.parseColor("#BF495057"))
                    ""
                }
            }
            findViewById<TextView>(R.id.textField)?.apply {
                text = item.content ?: run { isVisible = false; "" }
            }
        }

    inner class ImageAdditionAdapter(private val urlImageList: ArrayList<String>) :
        RecyclerView.Adapter<ImageAdditionAdapter.ImageHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageHolder(
            LayoutInflater.from(context).inflate(R.layout.view_drawable_sample_one, parent, false)
                .apply {
                    findViewById<RoundedImageView>(R.id.imageContainer)?.apply {
                        updateLayoutParams<ConstraintLayout.LayoutParams> { width = 163.dp }
                    }
                }
        )

        override fun onBindViewHolder(holder: ImageHolder, position: Int) =
            holder.bind(urlImageList[position])

        override fun getItemCount() = urlImageList.size

        inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(urlImage: String) {
                itemView.findViewById<RoundedImageView>(R.id.imageContainer)?.apply {
                    Glide.with(itemView).load(urlImage).into(this)
                }
            }
        }
    }
}