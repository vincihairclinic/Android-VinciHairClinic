package com.example.vinciandroidv2.ui.global

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.DI.APP_SCOPE
import com.example.vinciandroidv2.helper.AlertListener
import com.example.vinciandroidv2.helper.showBaseAlert
import com.example.vinciandroidv2.lifecycle.LifecycleObserver
import com.example.vinciandroidv2.network.respons.Base
import com.example.vinciandroidv2.ui.global.mvp.BaseView
import com.example.vinciandroidv2.ui.global.widgets.ActionButtonFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import toothpick.Scope
import toothpick.Toothpick

abstract class BaseBottomSheetFragment : BottomSheetDialogFragment(), BaseView, LifecycleObserver {
    abstract val layoutRes: Int
    abstract val TAG: String

    protected lateinit var scope: Scope
        private set
    protected open val parentScopeName: String = APP_SCOPE
    override fun onCreate(savedInstanceState: Bundle?) {
        scope = createScope()
        installModules(scope)
        Toothpick.inject(this, scope)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    @SuppressLint("RestrictedApi", "VisibleForTests")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (dialog as BottomSheetDialog).behavior.apply {
            /** makes rounded corners when expanded too **/
            disableShapeAnimations()
            isHideable = false
            isDraggable = false
            isCancelable = false
        }
        dialog?.setOnShowListener { dialogInterface -> dialogOnShowListener(dialogInterface) }
    }

    open fun dialogOnShowListener(dialogInterface: DialogInterface?) {
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    protected fun replace(@IdRes hostId: Int, fragment: BaseFragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(hostId, fragment, fragment.TAG)
            ?.addToBackStack(fragment.TAG)
            ?.commit()
    }

    protected fun add(@IdRes hostId: Int, fragment: BaseFragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(hostId, fragment, fragment.TAG)
            ?.addToBackStack(fragment.TAG)
            ?.commit()
    }

    protected open fun createScope(): Scope {
        return Toothpick.openScopes(parentScopeName, objectScopeName())
    }

    protected open fun installModules(scope: Scope) {}


    protected fun finishFragment() = activity?.onBackPressed()

    protected fun findButton(@IdRes id: Int): ActionButtonFragment? =
        childFragmentManager.findFragmentById(id) as? ActionButtonFragment

    //
    protected fun findWidgetFragment(@IdRes fragmentTag: Int): Fragment? =
        childFragmentManager.findFragmentById(fragmentTag)

    //
    protected fun findFragment(fragmentTag: String): Fragment? =
        activity?.supportFragmentManager?.findFragmentByTag(fragmentTag)

    protected fun hideKeyboard() {
        try {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnKeyListener { _: DialogInterface, keyCode: Int, keyEvent: KeyEvent ->
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {

                    dismiss()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        hideKeyboard()
        super.onDestroy()
    }

    override fun connectListener() {
        setLocalization()
    }

    override fun disconnectListener() {
        hideKeyboard()
    }

    open fun setLocalization() {}

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