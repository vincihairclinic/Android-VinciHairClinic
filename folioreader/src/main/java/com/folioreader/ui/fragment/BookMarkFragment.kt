package com.folioreader.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.folioreader.Constants
import com.folioreader.R
import com.folioreader.model.Bookmark
import com.folioreader.model.locators.ReadLocator
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_highlight_list.*
import kotlinx.android.synthetic.main.progress_dialog.*
import kotlinx.android.synthetic.main.row_bookmark.view.*
import org.readium.r2.shared.Publication

class BookMarkFragment : Fragment() {
    companion object {
        const val BOOK_ID = "book_id"
        fun newInstance(bookId: String, publication: Publication) = BookMarkFragment().apply {
            arguments = Bundle(1).apply {
                putString(BOOK_ID, bookId)
                putSerializable(Constants.PUBLICATION, publication)
            }
        }
    }

    private val adapter: BookmarkAdapter = BookmarkAdapter()
    private var publication: Publication? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return LayoutInflater.from(context)
            .inflate(R.layout.fragment_highlight_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        publication = arguments?.getSerializable(Constants.PUBLICATION) as Publication?
        if (null == publication) {
            return
        }
        with(rv_highlights) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            this@BookMarkFragment.adapter.bookmarks = ArrayList(
                Realm.getDefaultInstance().where(Bookmark::class.java).equalTo(
                    "bookId", arguments?.getString(BOOK_ID, "")
                ).findAll()
            )
            adapter = this@BookMarkFragment.adapter
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
    }

    private fun updateAdapter() {
        this@BookMarkFragment.adapter.bookmarks = ArrayList(
            Realm.getDefaultInstance().where(Bookmark::class.java).equalTo(
                "bookId", arguments?.getString(BOOK_ID, "")
            ).findAll()
        )
        this@BookMarkFragment.adapter.notifyDataSetChanged()
    }

    inner class BookmarkAdapter : RecyclerView.Adapter<BookmarkAdapter.Holder>() {
        var bookmarks: ArrayList<Bookmark> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(LayoutInflater.from(context).inflate(R.layout.row_bookmark, null, false))

        override fun getItemCount(): Int = bookmarks.count()

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(bookmarks[position])
        }

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(bookmark: Bookmark) {
                val locator = ReadLocator.fromJson(bookmark.locatorJson ?: "")

                var currentSectionTitle = publication
                    ?.tableOfContents
                    ?.firstOrNull { p -> p.href?.substringAfter("#") == locator?.currentChapterId }
                    ?.title

                if (null == currentSectionTitle) {
                    currentSectionTitle = publication
                        ?.tableOfContents
                        ?.firstOrNull { p -> p.href?.substringBefore("#") == locator?.href }
                        ?.title

                    if (null == currentSectionTitle) {
                        currentSectionTitle = publication
                            ?.tableOfContents
                            ?.firstOrNull()
                            ?.title
                            ?: ""
                    }
                }

                itemView.bookmarkName?.text = currentSectionTitle
                itemView.bookmarkDate?.text = bookmark.createdAt ?: ""

                itemView.container.setOnClickListener {
                    locator?.let { locator ->
                        val intent = Intent()
                        intent.putExtra(Constants.SELECTED_CHAPTER_POSITION, locator.href)
                        intent.putExtra(Constants.BOOK_TITLE, locator.title)
                        intent.putExtra(Constants.LOCATOR_JSON, bookmark.locatorJson ?: "")
                        intent.putExtra(Constants.TYPE, Constants.BOOKMARK_SELECTED)
                        activity?.setResult(Activity.RESULT_OK, intent)
                        activity?.finish()
                    }
                }
                itemView.delete.setOnClickListener(View.OnClickListener {
                    Realm.getDefaultInstance()?.let {
                        it.executeTransaction {
                            bookmark.deleteFromRealm()
                            updateAdapter()
                        }
                    }
                })
            }
        }
    }
}