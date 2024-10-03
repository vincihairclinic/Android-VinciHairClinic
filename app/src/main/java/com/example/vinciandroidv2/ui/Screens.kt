package com.example.vinciandroidv2.ui

import com.example.vinciandroidv2.network.respons.*
import com.example.vinciandroidv2.ui.auth.*
import com.example.vinciandroidv2.ui.auth.onboarding.OnBoardingFragment
import com.example.vinciandroidv2.ui.auth.onboarding.OnBoardingStepFragment
import com.example.vinciandroidv2.ui.localization.CountrySelectionFragment
import com.example.vinciandroidv2.ui.localization.LanguageSelectionFragment
import com.example.vinciandroidv2.ui.auth.info.EmailVerificationFragment
import com.example.vinciandroidv2.ui.auth.info.GetBackFragment
import com.example.vinciandroidv2.ui.auth.info.GetBackOrCreatePasswordFragment
import com.example.vinciandroidv2.ui.auth.info.WelcomeFragment
import com.example.vinciandroidv2.ui.auth.questionnaire.*
import com.example.vinciandroidv2.ui.auth.questionnaire2.CountryCodeSelectionBottomSheet
import com.example.vinciandroidv2.ui.auth.questionnaire2.CreatePasswordFragment
import com.example.vinciandroidv2.ui.auth.questionnaire2.HairPhotosFragment
import com.example.vinciandroidv2.ui.auth.questionnaire2.YourNumberFragment
import com.example.vinciandroidv2.ui.global.FullScreenImageFragment
import com.example.vinciandroidv2.ui.global.ImageListFragment
import com.example.vinciandroidv2.ui.main.book.BookInfoFragment
import com.example.vinciandroidv2.ui.main.book.ExpandedInfoArticleFragment
import com.example.vinciandroidv2.ui.main.book.ExpandedPostArticleFragment
import com.example.vinciandroidv2.ui.main.book.ExpandedPreArticleFragment
import com.example.vinciandroidv2.ui.main.clinics.*
import com.example.vinciandroidv2.ui.main.feed.*
import com.example.vinciandroidv2.ui.main.feed.podcast.EpisodeDetailsFragment
import com.example.vinciandroidv2.ui.main.feed.podcast.PodcastDetailsFragment
import com.example.vinciandroidv2.ui.main.home.ExpandedResultFragment
import com.example.vinciandroidv2.ui.main.home.HomeFragment
import com.example.vinciandroidv2.ui.main.home.HomeHost
import com.example.vinciandroidv2.ui.main.home.WhatsappCountrySelectionBottomSheet
import com.example.vinciandroidv2.ui.main.profile.*
import com.example.vinciandroidv2.ui.main.store.*

object Screens {
    object Localization {
        fun getCountrySelectionFragment() = CountrySelectionFragment()
        fun getLanguageSelectionFragment() = LanguageSelectionFragment()
    }

    object Auth {
        fun getOnBoardingFragment() = OnBoardingFragment()
        fun getOnBoardingStepFragment(position: Int) = OnBoardingStepFragment(position)
        fun getSignInFragment() = SignInFragment()
        fun getForgotPasswordFragment() = ForgotPasswordFragment()
        fun getSignUpFragment() = GetStartedFragment()

        fun getEmailVerificationFragment() = EmailVerificationFragment()
        fun getGenderAndAgeFragment() = GenderAndAgeFragment()
        fun getYourBackgroundFragment() = YourBackgroundFragment()
        fun getProcedureOfInterestFragment() = ProcedureOfInterestFragment()
        fun getHairTypeFragment() = HairTypeFragment()
        fun getHairLossTypeFragment() = HairLossTypeFragment()
        fun getPreferredClinicFragment() = PreferredClinicFragment()

        fun getWelcomeFragment() = WelcomeFragment()
        fun getYourNumberFragment() = YourNumberFragment()
        fun getHairPhotosFragment() = HairPhotosFragment()
        fun getGetBackFragment() = GetBackFragment()
        fun getGetBackOrCreatePasswordFragment() = GetBackOrCreatePasswordFragment()
        fun getCreatePasswordFragment() = CreatePasswordFragment()


        fun getCountryCodeSelectionBottomSheet(
            callback: (newCountryId: Int) -> Unit
        ) = CountryCodeSelectionBottomSheet(callback)

        fun getGetStartedInfoFragment() = GetStartedInfoFragment()
    }

    object Hosts {
        fun getHomeHost() = HomeHost()
        fun getClinicsHost() = ClinicsHost()
        fun getStoreHost() = StoreHost()
        fun getFeedHost() = FeedHost()
    }

    object Home {
        fun getHomeFragment() = HomeFragment()
        fun getFullScreenImageFragment(urlImage: ArrayList<String> = ArrayList()) = FullScreenImageFragment(urlImage)
        fun getExpandedResultFragment(procedureResult: ProcedureResult) =
            ExpandedResultFragment(procedureResult)

        fun getWhatsappCountrySelectionBottomSheet(
            callback: (newCountryId: Int) -> Unit
        ) = WhatsappCountrySelectionBottomSheet(callback)
    }

    object Book {
        fun getBookInfoFragment(bookInfo: BookInfo) = BookInfoFragment(bookInfo)
        fun getExpandedInfoArticleFragment(
            image: String?,
            title: String?,
            content: String?
        ) = ExpandedInfoArticleFragment(image, title, content)

        fun getExpandedPreArticleFragment(
            image: String?,
            title: String?,
            content: String?,
            instructionList: ArrayList<Instruction>?,
            additionInfoList: ArrayList<PreAdditionInfo>?
        ) = ExpandedPreArticleFragment(image, title, content, instructionList, additionInfoList)

        fun getExpandedPostArticleFragment(
            image: String?,
            title: String?,
            content: String?,
            instructionList: ArrayList<Instruction>?,
            additionInfoList: ArrayList<PostAdditionInfo>?
        ) = ExpandedPostArticleFragment(image, title, content, instructionList, additionInfoList)
    }

    object Clinics {
        fun getClinicsFragment() = ClinicsFragment()
        fun getSearchClinicsFragment() = SearchClinicsFragment()
        fun getClinicMapFragment(clinicId: Int? = null) = ClinicMapFragment().apply {
            this.selectedClinicId = clinicId
        }

        fun getExpandedClinicFragment(clinicId: Int) = ExpandedClinicFragment(clinicId)
        fun getContactBottomSheet(clinicId: Int) = ContactBottomSheet().apply {
            this.clinicId = clinicId
        }
    }

    object Store {
        fun getStoreFragment() = StoreFragment()
        fun getExpandedProductFragment(productId: Int) = ExpandedProductFragment(productId)
        fun getStoreSelectionBottomSheet(
            list: ArrayList<ShopNowUrl>,
            callback: (selectedCountryUrlLink: String) -> Unit
        ) = StoreSelectionBottomSheet(list, callback)
    }

    object Feed {
        fun getFeedFragment() = FeedFragment()
        fun getEpisodeDetailsFragment( podcastEpisode: PodcastEpisode = PodcastEpisode()) = EpisodeDetailsFragment( podcastEpisode)
        fun getExpandedFeedArticleFragment(articleId: Int) = ExpandedFeedArticleFragment(articleId)
        fun getPodcastDetailsFragment(articleId: Int) = PodcastDetailsFragment(articleId)
        fun getYouTubeFragment(articleId: Int) = YouTubeFragment()
        fun getMspUploadPhotoFragment(idSimulatorType: Int = 0) = MspUploadPhotoFragment(idSimulatorType)
        fun getFillYourDetailsFragment(simulation:Simulation = Simulation()) = FillYourDetailsFragment(simulation)
        fun getSimulationRequestFragment(simulation:Simulation = Simulation()) = SimulationRequestFragment(simulation)
        fun getIntroducingFragment() = IntroducingFragment()
        fun getSelectTypeOfSimulationFragment() = SelectTypeOfSimulationFragment()
        fun getAllRecommendedFragment() = AllRecommendedFragment()
    }


    object Profile {
        fun getProfileBottomSheet() = ProfileBottomSheet()
        fun getEditPersonalInfoFragment() = EditPersonalInfoFragment()
        fun getChangePasswordFragment() = ChangePasswordFragment()
        fun getNotificationsFragment() = NotificationsFragment()
        fun getRegionalSettingsFragment() = RegionalSettingsFragment()
        fun getIntroducingFragment() = IntroducingFragment()
        fun getCountrySelectionBottomSheet(
            selectedCountryId: Int,
            callback: (newCountryId: Int) -> Unit
        ) = CountrySelectionBottomSheet(selectedCountryId, callback)

        fun getLanguageSelectionBottomSheet(
            selectedLanguageKey: String,
            callback: (newLanguageKey: String) -> Unit
        ) = LanguageSelectionBottomSheet(selectedLanguageKey, callback)
    }
    fun getImageListScreen() = ImageListFragment()

}