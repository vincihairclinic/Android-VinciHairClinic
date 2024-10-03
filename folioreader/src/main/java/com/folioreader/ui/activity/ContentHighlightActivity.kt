package com.folioreader.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.folioreader.Config
import com.folioreader.Constants
import com.folioreader.FolioReader
import com.folioreader.R
import com.folioreader.ui.fragment.BookMarkFragment
import com.folioreader.ui.fragment.HighlightFragment
import com.folioreader.ui.fragment.TableOfContentFragment
import com.folioreader.util.AppUtil
import kotlinx.android.synthetic.main.activity_content_highlight.*
import org.readium.r2.shared.Publication

class ContentHighlightActivity : AppCompatActivity() {
    companion object

    var isNightMode: Boolean = false
    var config: Config? = null
    var publication: Publication? = null
    private var adapter: ViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_highlight)
        if (supportActionBar != null) supportActionBar?.hide();
        publication = intent.getSerializableExtra(Constants.PUBLICATION) as Publication?
        config = AppUtil.getSavedConfig(this)
        isNightMode = config?.isNightMode ?: false
        btn_close?.setOnClickListener { finish() }
        initTabs();
    }

    private fun initTabs() {
//        config?.themeColor?.let { UiUtil.setColorIntToDrawable(it, btn_close.drawable) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val color: Int = if (isNightMode) {
                ContextCompat.getColor(this, R.color.black);
            } else {
                val typedArray =
                    theme.obtainStyledAttributes(intArrayOf(android.R.attr.navigationBarColor))
                typedArray.getColor(0, ContextCompat.getColor(this, R.color.white));
            }
            window.navigationBarColor = color;
        }

        val bookId = intent.getStringExtra(FolioReader.EXTRA_BOOK_ID) ?: ""
        val bookTitle = intent.getStringExtra(Constants.BOOK_TITLE) ?: ""
        val chapterSelected = intent.getStringExtra(Constants.CHAPTER_SELECTED) ?: ""

        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter?.addFragment(TableOfContentFragment.newInstance(
            publication,
            chapterSelected,
            bookTitle), "Contents")
        publication?.let {
            adapter?.addFragment(BookMarkFragment.newInstance(bookId, it), "Bookmarks")
        }
        adapter?.addFragment(HighlightFragment.newInstance(bookId, bookTitle), "Highlights")
        viewPager?.adapter = adapter
        viewPager?.offscreenPageLimit = 3
        tabLayout?.setupWithViewPager(viewPager)
    }
}

class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun getFragments(): ArrayList<Fragment> = mFragmentList

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }
}