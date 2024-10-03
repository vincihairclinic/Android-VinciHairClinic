package com.example.vinciandroidv2.ui.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.HomeModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.openWhatsApp
import com.example.vinciandroidv2.network.respons.Article
import com.example.vinciandroidv2.network.respons.BaseHome
import com.example.vinciandroidv2.network.respons.BookInfo
import com.example.vinciandroidv2.network.respons.PodcastEpisode
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.auth.questionnaire.QuestionnaireFlow
import com.example.vinciandroidv2.ui.auth.questionnaire.questionnaireFlow
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.dp
import com.example.vinciandroidv2.ui.global.mvp.home.HomeContract
import com.example.vinciandroidv2.ui.main.MainActivity
import com.example.vinciandroidv2.ui.main.NavTabAction
import com.example.vinciandroidv2.ui.main.profile.ProfileBottomSheet
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.createSkeleton
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_card_drawable_one_text_view_56_sample_one.view.*
import kotlinx.android.synthetic.main.item_recommendation.view.*
import kotlinx.android.synthetic.main.item_row_card_view_big_sample_one.view.*
import kotlinx.android.synthetic.main.item_row_card_view_small_one_text_view_sample_four.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

enum class TabAction(val tabIcon: Int, var tabName: String) {
//    BOOK_A_PROCEDURE(R.drawable.ic_calendar_brown_24, ""),
    BOOK_A_CONSULTATION(R.drawable.ic_calendar_brown_24, ""),
    REQUEST_SIMULATION(R.drawable.ic_request_simulation, ""),
    WHATSAPP(R.drawable.ic_whatsapp_brown_24, ""),
    FIND_CLINIC_NEARLY(R.drawable.ic_location_brown_2_24, ""),
}

class HomeFragment : BaseFragment(), HomeContract.View {
    override val layoutRes = R.layout.fragment_home
    override val TAG = "HomeFragment"
    var recommendedPodcasts: ArrayList<PodcastEpisode> = ArrayList()
    var recommendedArticles: ArrayList<Article> = ArrayList()

    private val homePresenter: HomeContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(HomeModule(this))
    }

    private lateinit var skeleton: Skeleton

    private val recommendedAdapter: RecommendedAdapter by  lazy { RecommendedAdapter() }
    private val bookAdapter: BookAdapter by lazy { BookAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        skeleton = rootView.createSkeleton()

        profileMenuButton?.setOnClickListener {
            Screens.Profile.getProfileBottomSheet()
                .show(childFragmentManager, ProfileBottomSheet().TAG)
        }

        updateHomeData()

        seeMore?.setOnClickListener {
            replace(android.R.id.content, Screens.Feed.getAllRecommendedFragment().apply {
                this.recommendedArticles = this@HomeFragment.recommendedArticles
                this.recommendedPodcasts = this@HomeFragment.recommendedPodcasts
            })
        }

        itemContainerOne?.removeAllViews()
        TabAction.values().forEach { tab ->
            LayoutInflater.from(context)
                .inflate(
                    R.layout.item_card_drawable_one_text_view_56_sample_one,
                    itemContainerOne,
                    false
                )?.apply {
                    (cardContainer?.layoutParams as? FrameLayout.LayoutParams)?.apply {
                        setMargins(6.dp, 10.dp, 6.dp, 10.dp)
                    }
                    id = tab.ordinal
                    cardImage?.setImageResource(tab.tabIcon)
                    setOnClickListener {
                        when (tab.ordinal) {
                            0 -> {
                                questionnaireFlow = QuestionnaireFlow.HOME
                                replace(
                                    R.id.content,
                                    Screens.Auth.getGenderAndAgeFragment()
                                )
                            }
                            1 -> {
                                replace(R.id.content, Screens.Feed.getIntroducingFragment())

                            }
                            2 -> {
                                showWhatsappPhoneList()
                            }
                            3 -> (activity as? MainActivity)?.setNavViewAction(NavTabAction.FIND_CLINIC_NEARLY)
                        }
                    }
                }?.also {
                    itemContainerOne?.addView(it)
                }
        }

        homeRecyclerTwo?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(false)
            adapter = this@HomeFragment.bookAdapter
        }
        recomendedRecycler?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(false)
            adapter = this@HomeFragment.recommendedAdapter
        }
    }

    internal fun updateHomeData() {
        skeleton.showSkeleton()
        homePresenter.getHome()
    }

    override fun homeLoaded(baseHome: BaseHome) {
        skeleton.showOriginal()
        personalizeExperienceView?.isVisible = !(RealmHelper.getUserData()?.dateOfBirth != null &&
                RealmHelper.getUserData()?.phoneNumber != null &&
                RealmHelper.getUserData()?.hairLossType != null &&
                RealmHelper.getUserData()?.clinic != null &&
                RealmHelper.getUserData()?.urlHairTopImage != null &&
                RealmHelper.getUserData()?.urlHairBackImage != null &&
                RealmHelper.getUserData()?.urlHairSideImage != null &&
                RealmHelper.getUserData()?.urlHairFrontImage != null)

        personalizeExperienceView?.setOnClickListener {
            questionnaireFlow = QuestionnaireFlow.PROFILE
            replace(R.id.content, Screens.Auth.getGenderAndAgeFragment())
        }

        recommendedPodcasts = baseHome.recommendedPodcasts
        recommendedArticles = baseHome.recommendedArticles
        recommendedAdapter.notifyDataSetChanged()
//        baseHome.article?.let { a ->
//            recommendedTitleField?.isVisible = true
//            recommendedArticleCard?.apply {
//                isVisible = true
//                playCard?.isVisible = false
//                Glide.with(this@HomeFragment).load(a.urlImage).into(recommendedImageContainer)
//                recommendedCardTitleField?.text = a.name
//                recommendedCardSubTitleField?.text = a.content
//                setOnClickListener {
//                    replace(R.id.content, Screens.Feed.getExpandedFeedArticleFragment(a.id))
//                }
//            }
//        } ?: run {
//            recommendedTitleField?.isVisible = false
//            recommendedArticleCard?.isVisible = false
//        }

        bookAdapter.updateList(baseHome.bookList ?: ArrayList())

        resultsContainer?.removeAllViews()
        baseHome.procedureResultList?.forEach { result ->
            LayoutInflater.from(context)
                .inflate(R.layout.item_row_card_view_big_sample_one, resultsContainer, false)
                ?.apply {
                    Glide.with(this@HomeFragment).load(result.urlImage).into(imageContainer)
                    categoryTextField?.text = result.procedure?.name
                    titleTextField?.text = result.title
                    subTitleTextField?.text = result.subtitle
//                    dateTextField?.text = result.createdAt?.changeFormat("dd MMMM, yyyy")
                    setOnClickListener {
                        replace(
                            R.id.content,
                            Screens.Home.getExpandedResultFragment(result)
                        )
                    }
                }?.also {
                    resultsContainer?.addView(it)
                }
        }
    }

    private fun showWhatsappPhoneList() {
        Screens.Home.getWhatsappCountrySelectionBottomSheet { newId ->
            activity?.openWhatsApp(
                when (newId) {
                    7 -> "442071450112"
                    9 -> "442071450112"
                    10 -> "2348174222227"
                    11 -> "233555568046"
                    12 -> "34653111092"
                    13 -> "9613112341"
                    else -> {
                        Toast.makeText(context, "no such number", Toast.LENGTH_SHORT).show()
                        return@getWhatsappCountrySelectionBottomSheet
                    }
                }
            )
        }.show(childFragmentManager, "CountrySelectionBottomSheet")

//        showSimpleListDialog(
//            context,
//            arrayOf("UK", "Ireland", "Nigeria", "Ghana", "Spain", "Lebanon")
//        ) { _, which ->
//            activity?.openWhatsApp(
//                when (which) {
//                    0 -> "442071450112"
//                    1 -> "442071450112"
//                    2 -> "2348174222227"
//                    3 -> "233555568046"
//                    4 -> "34653111092"
//                    5 -> "9613112341"
//                    else -> return@showSimpleListDialog
//                }
//            )
//        }
    }

    override fun setLocalization() {
        super.setLocalization()
        homeTitleField?.text =
            RealmHelper.getLocalizedText("home.main.title.text")
        recommendedTitleField?.text =
            RealmHelper.getLocalizedText("home.main.recommended.title.text")
        clientInformationTitleField?.text =
            RealmHelper.getLocalizedText("home.main.client.info.title.text")
        clinicResultsTitleField?.text =
            RealmHelper.getLocalizedText("home.main.results.title.text")

        recommendedTitleField?.text = RealmHelper.getLocalizedText("home.main.recommended.title.text")
        seeMore?.text = RealmHelper.getLocalizedText("home.main.see_more.text")

        itemContainerOne?.children?.apply {
            firstOrNull { it.id == 0 }?.findViewById<TextView>(R.id.cardTextField)?.text =
                RealmHelper.getLocalizedText("home.main.actionlist.item.text.two")
            firstOrNull { it.id == 1 }?.findViewById<TextView>(R.id.cardTextField)?.text =
              "Request Simulation"  //RealmHelper.getLocalizedText("home.main.actionlist.item.text.two")
            firstOrNull { it.id == 2 }?.findViewById<TextView>(R.id.cardTextField)?.text =
                RealmHelper.getLocalizedText("home.main.actionlist.item.text.three")
            firstOrNull { it.id == 3 }?.findViewById<TextView>(R.id.cardTextField)?.text =
                RealmHelper.getLocalizedText("home.main.actionlist.item.text.four")
        }

        updateHomeData()
    }

    inner class BookAdapter : RecyclerView.Adapter<BookAdapter.Holder>() {
        val list = ArrayList<BookInfo>()

        @SuppressLint("NotifyDataSetChanged")
        internal fun updateList(list: ArrayList<BookInfo>) {
            this.list.clear()
            this.list.addAll(list)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context).inflate(
                R.layout.item_row_card_view_small_one_text_view_sample_four,
                parent,
                false
            )
        )

        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(list[position])

        override fun getItemCount() = list.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(bookInfo: BookInfo) {
                Glide.with(itemView).load(bookInfo.urlImage).into(itemView.image)
                itemView.textField?.text = bookInfo.name
                itemView.setOnClickListener {
                    replace(R.id.content, Screens.Book.getBookInfoFragment(bookInfo))
                }
            }
        }
    }

    inner class RecommendedAdapter : RecyclerView.Adapter<RecommendedAdapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context).inflate(
                R.layout.item_recommendation,
                parent,
                false
            )
        )

        override fun onBindViewHolder(holder: Holder, position: Int)  {
            if (position > recommendedPodcasts.size-1) {
                holder.bind(recommendedArticles[position-recommendedPodcasts.size])
            } else {
                holder.bind(recommendedPodcasts[position])
            }
        }

        override fun getItemCount() = recommendedPodcasts.size + recommendedArticles.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(podcast: PodcastEpisode) {
                Glide.with(itemView).load(podcast.urlImage).into(itemView.recommendedImageContainer)
                itemView.recommendedCardTitleField?.text = "New Podcast Episode"
                itemView.recommendedCardSubTitleField?.text = podcast.name

                itemView.setOnClickListener {
                    add(R.id.content, Screens.Feed.getEpisodeDetailsFragment(podcast ?: PodcastEpisode()).apply {
                        podcastEpisodeList?.addAll(recommendedPodcasts)
                    })
                }
            }

            fun bind(article: Article) {
                Glide.with(itemView).load(article.urlImage).into(itemView.recommendedImageContainer)
                itemView.recommendedCardTitleField?.text = article.articleCategory?.name
                itemView.recommendedCardSubTitleField?.text = article.name
                itemView.setOnClickListener {
                    replace(
                        R.id.content,
                        Screens.Feed.getExpandedFeedArticleFragment(article.id)
                    )
                }
            }
        }
    }
}