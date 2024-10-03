package com.example.vinciandroidv2.ui.main.store

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.network.respons.BaseProduct
import com.example.vinciandroidv2.network.respons.Product
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.dp
import jdroidcoder.ua.paginationrecyclerview.OnPageChangeListener
import kotlinx.android.synthetic.main.fragment_product_tab.*
import kotlinx.android.synthetic.main.item_row_card_view_small_one_text_view_sample_one.view.*

class ProductTabFragment(private val categoryId: Int) : BaseStoreFragment(), OnPageChangeListener {
    override val layoutRes = R.layout.fragment_product_tab
    override val TAG = "ProductTabFragment"

    private val adapter: Adapter by lazy { Adapter() }
    private var page = 0
    private var list = ArrayList<Product>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerViewContainer?.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = this@ProductTabFragment.adapter
            setOnPageChangeListener(this@ProductTabFragment)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    internal fun updateTabData() {
        onPageChange(0)
    }

    override fun onPageChange(page: Int) {
        this.page = page + 1

        if (-1 == categoryId) productPresenter.getProductList(this.page)
        else productPresenter.getProductListByCategory(categoryId, this.page)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun productListByCategoryLoaded(categoryId: Int?, baseProduct: BaseProduct) {
        if (1 == page) list.clear()

        list.addAll(baseProduct.productList ?: ArrayList())
        adapter.notifyDataSetChanged()
    }

    inner class Adapter : RecyclerView.Adapter<Adapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context).inflate(
                R.layout.item_row_card_view_small_one_text_view_sample_one,
                parent,
                false
            ).apply {
                this?.image?.cornerRadius = 5.dp.toFloat()
                this?.image?.setBackgroundColor(resources.getColor(R.color.background_light_grey_F8F8F5))
                this?.image?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    topMargin = 16.dp
                }
                this?.textField?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    bottomMargin = 16.dp
                }
            }
        )

        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(list[position])

        override fun getItemCount() = list.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(product: Product) {
                Glide.with(itemView).load(product.urlImageList?.firstOrNull()).into(itemView.image)
                itemView.textField?.text = product.name
                itemView.setOnClickListener {
                    replace(
                        R.id.content,
                        Screens.Store.getExpandedProductFragment(product.id)
                    )
                }
            }
        }
    }
}