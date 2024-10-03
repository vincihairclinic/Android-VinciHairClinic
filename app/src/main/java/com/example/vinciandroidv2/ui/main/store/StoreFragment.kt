package com.example.vinciandroidv2.ui.main.store

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.BaseProduct
import com.example.vinciandroidv2.network.respons.Country
import com.example.vinciandroidv2.network.respons.ShopNowUrl
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.ViewPagerStateAdapter
import com.example.vinciandroidv2.ui.global.initViewPagerWithPageIndicator
import com.example.vinciandroidv2.ui.main.profile.ProfileBottomSheet
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_store.*
import kotlinx.android.synthetic.main.fragment_store_step.*
import kotlinx.android.synthetic.main.view_card_button_sample_two.view.*
import kotlin.math.abs

class StoreFragment : BaseStoreFragment() {
    override val layoutRes = R.layout.fragment_store
    override val TAG = "StoreFragment"

    private var tabViewPager: ViewPagerWithTitleStateAdapter? = null
    private var onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            (tabViewPager?.getFragment(position) as? ProductTabFragment)?.updateTabData()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbarContainer?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            println("${verticalOffset}_____${appBarLayout.totalScrollRange}")
            toolbar?.isVisible = (abs(verticalOffset) - appBarLayout.totalScrollRange == 0)
        })

        profileMenuButton?.setOnClickListener {
            Screens.Profile.getProfileBottomSheet()
                .show(childFragmentManager, ProfileBottomSheet().TAG)
        }

        /** appBarLayout viewpager **/

        ViewPagerStateAdapter(
            this,
            arrayListOf(
                StoreStepFragment(1),
                StoreStepFragment(2),
                StoreStepFragment(3)
            )
        ).initViewPagerWithPageIndicator(storeViewPager, pageIndicator, null)

        includedShopNowCard?.apply {
            startDrawable?.setImageResource(R.drawable.ic_shop_white_18)
            setOnClickListener {
//                RealmHelper.getCountryById(RealmHelper.getUserData()?.countryId ?: -1)
//                    ?.shopUrl
//                    ?.let { link ->
//                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
//                    }

                val list = ArrayList<ShopNowUrl>().apply {
                    RealmHelper.realm?.where(Country::class.java)
                        ?.findAll()
                        ?.forEach { item ->
                            add(ShopNowUrl().apply {
                                countryId = item.id
                                shopNowUrl = item.shopUrl
                            })
                        }
                }
                Screens.Store.getStoreSelectionBottomSheet(list) { shopUrl ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(shopUrl)))
                }.show(childFragmentManager, "StoreSelectionBottomSheet")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    internal fun updateStoreData() {
        productPresenter.getProductCategoryList()
    }

    override fun productCategoryListLoaded(baseProduct: BaseProduct) {
        if (null == tabViewPager) {
            tabViewPager = ViewPagerWithTitleStateAdapter(this)
            tabViewPagerContainer?.adapter = tabViewPager
            tabViewPagerContainer?.offscreenPageLimit =
                baseProduct.productCategoryList?.size ?: ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            tabViewPagerContainer?.registerOnPageChangeCallback(onPageChangeCallback)
        }

        tabViewPager?.apply {
            addOrUpdateFragment(
                -1,
                RealmHelper.getLocalizedText("store.main.tabs.item.one.text"),
                ProductTabFragment(-1)
            )
            baseProduct.productCategoryList?.forEach {
                addOrUpdateFragment(it.id.toLong(), it.name ?: "", ProductTabFragment(it.id))
            }
            if (null != tabLayout && null != tabViewPagerContainer) {
                TabLayoutMediator(tabLayout, tabViewPagerContainer) { tab, position ->
                    tab.text = tabViewPager?.getTitle(position)
                }.attach()
            }
        }
    }

    override fun setLocalization() {
        super.setLocalization()
        toolbarTitleTextField?.text = RealmHelper.getLocalizedText("store.main.toolbar.title.text")
        includedShopNowCard?.buttonCardTextField?.text =
            RealmHelper.getLocalizedText("store.main.button.shopnow.text")

        updateStoreData()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabViewPagerContainer?.unregisterOnPageChangeCallback(onPageChangeCallback)
    }
}

class StoreStepFragment(private val step: Int) : BaseFragment() {
    override val layoutRes = R.layout.fragment_store_step
    override val TAG = "StoreStepFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Glide.with(this).load(
            when (step) {
                1 -> R.drawable.store_step_1
                2 -> R.drawable.store_step_2
                3 -> R.drawable.store_step_1
                else -> return
            }
        ).into(stepImage)
    }

    override fun setLocalization() {
        super.setLocalization()
        titleTextFieldOne?.text = when (step) {
            1 -> RealmHelper.getLocalizedText("store.main.viewpager.one.title.text.one")
            2 -> RealmHelper.getLocalizedText("store.main.viewpager.two.title.text.one")
            3 -> RealmHelper.getLocalizedText("store.main.viewpager.three.title.text.one")
            else -> return
        }
        titleTextFieldTwo?.text = when (step) {
            1 -> RealmHelper.getLocalizedText("store.main.viewpager.one.title.text.two")
            2 -> RealmHelper.getLocalizedText("store.main.viewpager.two.title.text.two")
            3 -> RealmHelper.getLocalizedText("store.main.viewpager.three.title.text.two")
            else -> return
        }
    }
}

class ViewPagerWithTitleStateAdapter(hostFragment: Fragment) : FragmentStateAdapter(hostFragment) {
    private val itemId = ArrayList<Long>()
    private val titleList = ArrayList<String>()
    private val fragmentList = ArrayList<Fragment>()

    fun addOrUpdateFragment(id: Long, title: String, fragment: Fragment) {
        if (itemId.contains(id)) {
            titleList[itemId.indexOf(id)] = title
        } else {
            itemId.add(id)
            titleList.add(title)
            fragmentList.add(fragment)
        }
    }

    fun getFragment(position: Int) = fragmentList[position]

    fun getTitle(position: Int) = titleList[position]

    override fun getItemCount() = fragmentList.count()
    override fun getItemId(position: Int) = itemId[position]
    override fun createFragment(position: Int) = fragmentList[position]
}