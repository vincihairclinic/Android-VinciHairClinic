package com.example.vinciandroidv2.ui.global

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.DI.APP_SCOPE
import com.example.vinciandroidv2.helper.AlertListener
import com.example.vinciandroidv2.helper.showBaseAlert
import com.example.vinciandroidv2.lifecycle.LifecycleObserver
import com.example.vinciandroidv2.model.LocalizationEvent
import com.example.vinciandroidv2.model.NetworkStateEvent
import com.example.vinciandroidv2.model.UpdateFragmentScopeEvent
import com.example.vinciandroidv2.network.respons.Base
import com.example.vinciandroidv2.ui.global.mvp.BaseView
import com.example.vinciandroidv2.ui.global.widgets.ActionButtonFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import toothpick.Scope
import toothpick.Toothpick
import toothpick.ktp.KTP

abstract class BaseFragment : Fragment(), BaseView, LifecycleObserver {
    abstract val layoutRes: Int
    abstract val TAG: String
    protected open val parentScopeName: String by lazy {
        (parentFragment as? BaseFragment)?.fragmentScopeName ?: APP_SCOPE
    }
    private lateinit var fragmentScopeName: String
    protected lateinit var scope: Scope
        private set
    private var instanceStateSaved: Boolean = false

    private val delegate = object : LayoutChangeListener.Delegate {
        private val uiHandler = Handler(Looper.getMainLooper())

        override fun layoutDidChange(oldHeight: Int, newHeight: Int, tempBottom: Int) {
            uiHandler.post {
                if (tempBottom <= resources?.getDimension(R.dimen.navigation_height).toInt() ?: 82
                ) {
                    return@post
                }
                keyboardStateChanged(newHeight > oldHeight)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentScopeName = savedInstanceState?.getString(STATE_SCOPE_NAME) ?: objectScopeName()
        initFragmentScope()
        lifecycle.addObserver(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addOnLayoutChangeListener(LayoutChangeListener().also {
            it.delegate = delegate
        })
        view.findViewById<ViewGroup>(R.id.rootView)?.setOnClickListener {
            hideKeyboard()
            updateFocusOnCards()
        }
    }

     private fun initFragmentScope() {
        if (Toothpick.isScopeOpen(fragmentScopeName)) {
            scope = Toothpick.openScope(fragmentScopeName)
        } else {
            scope = Toothpick.openScopes(parentScopeName, fragmentScopeName)
            installModules(scope)
        }
        Toothpick.inject(this, scope)
    }

    fun addTopMarginForFragment() {
        context?.let {
            (view?.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin =
                getStatusBarHeight(it)
        }
    }

    fun Activity.setStatusBarNormal() {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    fun Activity.setFullScreen() {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
        outState.putString(STATE_SCOPE_NAME, fragmentScopeName)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needCloseScope()) {
            KTP.closeScope(scope.name)
        }
    }

    private fun needCloseScope(): Boolean =
        when {
            activity?.isChangingConfigurations == true -> false
            activity?.isFinishing == true -> true
            else -> isRealRemoving()
        }

    private fun isRealRemoving(): Boolean =
        (isRemoving) || ((parentFragment as? BaseFragment)?.isRealRemoving() ?: false)

    protected open fun installModules(scope: Scope) {}

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    open fun onMessageEvent(event: LogoutEvent?) {
//        RealmHelper.removeSensitiveDataFromBase()
//
//        startActivity(Intent(activity, SplashActivity::class.java))
//        activity?.finish()
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: NetworkStateEvent) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: LocalizationEvent) {
        setLocalization()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: UpdateFragmentScopeEvent) {
        KTP.closeScope(scope.name)
        initFragmentScope()
    }

    protected fun replace(
        @IdRes hostId: Int = R.id.content,
        fragment: BaseFragment,
        isAddToBackStack: Boolean = true
    ) {
        val trans = activity?.supportFragmentManager?.beginTransaction()
            ?.replace(hostId, fragment, fragment.TAG)
        trans?.setReorderingAllowed(true)
        if (isAddToBackStack) {
            trans?.addToBackStack(fragment.TAG)
        }
        trans?.commit()
    }

    protected fun add(@IdRes hostId: Int = R.id.content, fragment: BaseFragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(hostId, fragment, fragment.TAG)
            ?.addToBackStack(fragment.TAG)
            ?.setReorderingAllowed(true)
            ?.commit()
    }

    protected fun finishFragment() = activity?.onBackPressed()

    protected fun findButton(@IdRes id: Int): ActionButtonFragment? =
        childFragmentManager.findFragmentById(id) as? ActionButtonFragment

    //
//    protected fun findToolbar(@IdRes id: Int = R.id.toolbarView): ToolbarFragment? =
//        childFragmentManager.findFragmentById(id) as? ToolbarFragment

    //
    protected fun findWidgetFragment(@IdRes fragmentTag: Int): Fragment? =
        childFragmentManager.findFragmentById(fragmentTag)

    //
    protected fun findFragment(fragmentTag: String): Fragment? =
        activity?.supportFragmentManager?.findFragmentByTag(fragmentTag)

    internal fun EditText.showKeyboard() {
        try {
            this.requestFocus()
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    internal fun hideKeyboard() {
        try {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    open fun updateFocusOnCards(tag: String = "", hideKeyboard: Boolean = true) {

    }

    override fun connectListener() {
        setLocalization()
    }

    override fun disconnectListener() {
        hideKeyboard()
    }

    open fun setLocalization() {}

    open fun keyboardStateChanged(isShown: Boolean) {}

    override fun apiError(error: String, apiException: Base?, function: (() -> Unit)?) {
        val listener = object : AlertListener {
            override fun actionPositive() {
                function?.invoke()
            }

            override fun actionNegative() {
            }
        }
        when (error) {
            API_INTERNET_ERROR -> showBaseAlert(
                context,
                title = apiException?.title,
                subtitle = getString(R.string.check_internet_connection),
                positiveText = getString(R.string.re_try),
                alertListener = listener
            )
            API_SERVER_ERROR -> showBaseAlert(
                context,
                title = apiException?.title,
                subtitle = apiException?.message,
                positiveText = getString(R.string.ok_label),
                alertListener = listener
            )
        }
    }
}

