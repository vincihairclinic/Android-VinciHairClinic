package com.example.vinciandroidv2.ui.main.book

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.model.BookLocator
import com.example.vinciandroidv2.network.respons.BookInfo
import com.example.vinciandroidv2.ui.global.*
import com.example.vinciandroidv2.ui.main.FullScreenVideoActivity
import com.example.vinciandroidv2.ui.main.VIDEO_URL_EXTRA
import com.folioreader.Config
import com.folioreader.FolioReader
import com.folioreader.model.locators.ReadLocator
import com.folioreader.util.AppUtil
import com.folioreader.util.ReadLocatorListener
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_book_info.*
import kotlinx.android.synthetic.main.view_card_button_sample_two.view.*
import kotlin.math.abs

class BookInfoFragment(val bookInfo: BookInfo) : BaseFragment(), ReadLocatorListener,
    FolioReader.OnClosedListener {

    override val layoutRes = R.layout.fragment_book_info
    override val TAG = "BookInfoFragment"

    private var folioReader: FolioReader? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbarContainer?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            println("${verticalOffset}_____${appBarLayout.totalScrollRange}")
            toolbar?.isVisible = (abs(verticalOffset) - appBarLayout.totalScrollRange == 0)
        })

        backButton?.setOnClickListener { activity?.onBackPressed() }
        toolbarBackButton?.setOnClickListener { activity?.onBackPressed() }

        Glide.with(this).load(bookInfo.urlImage).into(bookMainImage)
        toolbarTitleTextField?.text = bookInfo.name

        includeReadBookletCard?.apply {
            startDrawable?.setImageResource(R.drawable.ic_book_white_20)
            startDrawable?.layoutParams?.width = 20.dp
            startDrawable?.layoutParams?.height = 20.dp
            setOnClickListener {
                val book = RealmHelper.getBookById(bookInfo.id)
                val languageKey = RealmHelper.getUserData()?.languageKey
                if (null != book && null != languageKey) {
                    when (languageKey) {
                        "en" -> book.bookPath1
                        "pt" -> book.bookPath2
                        else -> book.bookPath1
                    }?.let { path ->
                        openBook(bookPath = path, readLocator = getLastReadLocator(path))
                    }
                }
            }
        }
        includePlayVideoCard?.apply {
            startDrawable?.setImageResource(R.drawable.ic_play_white_12)
            startDrawable?.layoutParams?.width = 20.dp
            startDrawable?.layoutParams?.height = 20.dp
            setOnClickListener {
                startActivity(
                    Intent(activity, FullScreenVideoActivity::class.java)
                        .putExtra(VIDEO_URL_EXTRA, bookInfo.urlVideo)
                )
            }
        }

        ViewPagerWithTitleStateAdapter(
            this,
            arrayOf("", "", ""),
            arrayOf(TabOverviewFragment(), TabFAQFragment(), TabInformationFragment())
        ).initViewPagerWithTabs(viewPager, tabLayout, null)
        viewPager.isUserInputEnabled = false

        initFolioBook()
    }

    override fun setLocalization() {
        super.setLocalization()
        includeReadBookletCard?.buttonCardTextField?.text =
            RealmHelper.getLocalizedText("home.bookinfo.button.readbooklet.text")
        includePlayVideoCard?.buttonCardTextField?.text =
            RealmHelper.getLocalizedText("home.bookinfo.button.playvideo.text")

        tabLayout?.getTabAt(0)?.text =
            RealmHelper.getLocalizedText("home.bookinfo.tab.one.title.text")
        tabLayout?.getTabAt(1)?.text =
            RealmHelper.getLocalizedText("home.bookinfo.tab.two.title.text")
        tabLayout?.getTabAt(2)?.text =
            RealmHelper.getLocalizedText("home.bookinfo.tab.three.title.text")
    }

    private fun initFolioBook() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!Environment.isExternalStorageManager()) {
//                val intent = Intent().apply {
//                    action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
//                    data = Uri.fromParts("package", activity?.packageName, null)
//                }
//                startActivityForResult(intent, 2221)
//            }
//        }
        if (permissionListApproved(context)) {
            initFolioBookAfterPermissionGrand()
        } else {
            resultPermissionLauncher.launch(permissionListToCheck)
        }
    }

    private fun initFolioBookAfterPermissionGrand() {
        folioReader = FolioReader.get()
            .setReadLocatorListener(this)
            .setOnClosedListener(this)
    }

    private fun openBook(bookPath: String, href: String? = null, readLocator: ReadLocator? = null) {
        var config = AppUtil.getSavedConfig(context)
        if (config == null) config = Config()

        config.setThemeColorInt(Color.parseColor("#C2C6CC"))
        config.isItemTitle = false
        config.isItemMenu = true
        config.isItemConfig = true
        config.isItemSearch = true
        config.isItemBookmark = true

        if (null != href) folioReader?.setChapterLink(href)
        else if (null != readLocator) folioReader?.setReadLocator(readLocator)

        folioReader?.setConfig(config, true)?.openBook("file:///android_asset/$bookPath.epub")
    }

    override fun onFolioReaderClosed() {}

    override fun saveReadLocator(readLocator: ReadLocator?) {
        RealmHelper.realm?.executeTransaction {
            it.insertOrUpdate(BookLocator().apply {
                bookId = readLocator?.bookId ?: ""
                locatorJson = readLocator?.toJson()
            })
        }
    }

    private fun getLastReadLocator(bookId: String): ReadLocator? =
        RealmHelper.realm?.where(BookLocator::class.java)
            ?.equalTo("bookId", bookId)
            ?.findFirst()
            ?.let { ReadLocator.fromJson(it.locatorJson ?: "") }


    /** Permission listener **/

    private val resultPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { list ->
        list?.forEach { if (!it.value) return@registerForActivityResult }
        initFolioBookAfterPermissionGrand()
    }


    /** Permissions **/

    private fun permissionListApproved(context: Context?): Boolean {
        permissionListToCheck.forEach { permission ->
            if (PackageManager.PERMISSION_GRANTED !=
                context?.let { checkSelfPermission(it, permission) }
            ) return false
        }
        return true
    }

    private val permissionListToCheck = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}