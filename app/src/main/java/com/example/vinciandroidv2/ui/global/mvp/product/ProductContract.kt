package com.example.vinciandroidv2.ui.global.mvp.product

import com.example.vinciandroidv2.network.respons.BaseProduct
import com.example.vinciandroidv2.ui.global.mvp.BaseView

interface ProductContract {
    interface View : BaseView {
        fun productByIdLoaded(baseProduct: BaseProduct) {}
        fun productListByCategoryLoaded(categoryId: Int?, baseProduct: BaseProduct) {}
        fun productCategoryListLoaded(baseProduct: BaseProduct) {}
    }

    interface Presenter {
        fun getProductById(productId: Int)
        fun getProductList(page: Int)
        fun getProductListByCategory(categoryId: Int, page: Int)
        fun getProductCategoryList()
    }
}