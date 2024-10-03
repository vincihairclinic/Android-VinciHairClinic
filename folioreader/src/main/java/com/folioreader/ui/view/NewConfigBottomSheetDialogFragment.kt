package com.folioreader.ui.view

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.folioreader.Config
import com.folioreader.R
import com.folioreader.model.event.ReloadDataEvent
import com.folioreader.ui.activity.FolioActivity
import com.folioreader.ui.activity.FolioActivityCallback
import com.folioreader.util.AppUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.view_config_new.*
import org.greenrobot.eventbus.EventBus

class NewConfigBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val FADE_DAY_NIGHT_MODE = 500

        @JvmField
        val LOG_TAG: String = NewConfigBottomSheetDialogFragment::class.java.simpleName
    }

    private lateinit var config: Config
    private var isNightMode = false
    private lateinit var activityCallback: FolioActivityCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_config_new, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity is FolioActivity)
            activityCallback = activity as FolioActivity

        view.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
        }

        config = AppUtil.getSavedConfig(activity)!!

        brightnessSeekBar?.max = 255
        brightnessSeekBar?.keyProgressIncrement = 1
        val brightness = Settings.System.getInt(
            context?.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS
        )
        brightnessSeekBar?.progress = brightness
        brightnessSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val temp: Float = progress / 255.toFloat()
                val lp: WindowManager.LayoutParams? = activity?.window?.attributes
                lp?.screenBrightness = temp
                activity?.window?.attributes = lp
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        nightMode?.isChecked = config.isNightMode
        nightMode?.setOnCheckedChangeListener { _, isChecked ->
            isNightMode = !isChecked
            toggleBlackTheme()
            setToolBarColor()
        }

        verticalScroll?.isChecked = activityCallback.direction == Config.Direction.VERTICAL
        verticalScroll?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                config = AppUtil.getSavedConfig(context)!!
                config.direction = Config.Direction.VERTICAL
                AppUtil.saveConfig(context, config)
                activityCallback.onDirectionChange(Config.Direction.VERTICAL)
            } else {
                config = AppUtil.getSavedConfig(context)!!
                config.direction = Config.Direction.HORIZONTAL
                AppUtil.saveConfig(context, config)
                activityCallback.onDirectionChange(Config.Direction.HORIZONTAL)
            }
        }
        smallTexts?.setOnClickListener {
            config.fontSize = 1
            AppUtil.saveConfig(activity, config)
            EventBus.getDefault().post(ReloadDataEvent())
        }
        bigTexts?.setOnClickListener {
            config.fontSize = 4
            AppUtil.saveConfig(activity, config)
            EventBus.getDefault().post(ReloadDataEvent())
        }

        checkCurrentColor()
        color1?.setOnClickListener {
            config = AppUtil.getSavedConfig(context)!!
            config.backgroundColor = "FFFFFF"
            AppUtil.saveConfig(activity, config)
            checkCurrentColor()
            EventBus.getDefault().post(ReloadDataEvent())
        }
        color2?.setOnClickListener {
            config = AppUtil.getSavedConfig(context)!!
            config.backgroundColor = "DACCB7"
            AppUtil.saveConfig(activity, config)
            checkCurrentColor()
            EventBus.getDefault().post(ReloadDataEvent())
        }
        color3?.setOnClickListener {
            config = AppUtil.getSavedConfig(context)!!
            config.backgroundColor = "95C5E0"
            AppUtil.saveConfig(activity, config)
            checkCurrentColor()
            EventBus.getDefault().post(ReloadDataEvent())
        }
        color4?.setOnClickListener {
            config = AppUtil.getSavedConfig(context)!!
            config.backgroundColor = "6985A3"
            AppUtil.saveConfig(activity, config)
            checkCurrentColor()
            EventBus.getDefault().post(ReloadDataEvent())
        }
        color5?.setOnClickListener {
            config = AppUtil.getSavedConfig(context)!!
            config.backgroundColor = "898D90"
            AppUtil.saveConfig(activity, config)
            checkCurrentColor()
            EventBus.getDefault().post(ReloadDataEvent())
        }
        color6?.setOnClickListener {
            config = AppUtil.getSavedConfig(context)!!
            config.backgroundColor = "4A5B6D"
            AppUtil.saveConfig(activity, config)
            checkCurrentColor()
            EventBus.getDefault().post(ReloadDataEvent())
        }

        changeFonts?.text = when(config.font){
            1 -> "Andada"
            2 -> "Lato"
            3 -> "Lora"
            4 -> "Raleway"
            else -> "Andada"
        }
        changeFonts?.setOnClickListener {
            context?.let { context ->
                val builderSingle = AlertDialog.Builder(context)
                val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                    context,
                    android.R.layout.select_dialog_singlechoice
                )
                arrayAdapter.add("Andada")
                arrayAdapter.add("Lato")
                arrayAdapter.add("Lora")
                arrayAdapter.add("Raleway")

                builderSingle.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                builderSingle.setAdapter(arrayAdapter) { dialog, which ->
                    config.font = which + 1
                    AppUtil.saveConfig(activity, config)
                    EventBus.getDefault().post(ReloadDataEvent())
                    changeFonts?.text = when(config.font){
                        1 -> "Andada"
                        2 -> "Lato"
                        3 -> "Lora"
                        4 -> "Raleway"
                        else -> "Andada"
                    }
                }
                builderSingle.show()
            }
        }
    }

    private fun checkCurrentColor(){
        color1?.borderWidth = 0f
        color2?.borderWidth = 0f
        color3?.borderWidth = 0f
        color4?.borderWidth = 0f
        color4?.borderWidth = 0f
        color6?.borderWidth = 0f
        when (config.backgroundColor) {
            "FFFFFF" -> color1?.borderWidth = 1f
            "DACCB7" -> color2?.borderWidth = 1f
            "95C5E0" -> color3?.borderWidth = 1f
            "6985A3" -> color4?.borderWidth = 1f
            "898D90" -> color5?.borderWidth = 1f
            "4A5B6D" -> color6?.borderWidth = 1f
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.viewTreeObserver?.addOnGlobalLayoutListener(null)
    }

    private fun toggleBlackTheme() {

        val day = ContextCompat.getColor(context!!, R.color.white)
        val night = ContextCompat.getColor(context!!, R.color.night)

        val colorAnimation = ValueAnimator.ofObject(
            ArgbEvaluator(),
            if (isNightMode) night else day, if (isNightMode) day else night
        )
        colorAnimation.duration = FADE_DAY_NIGHT_MODE.toLong()

        colorAnimation.addUpdateListener { animator ->
            val value = animator.animatedValue as Int
            container.setBackgroundColor(value)
        }

        colorAnimation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}

            override fun onAnimationEnd(animator: Animator) {
                isNightMode = !isNightMode
                config.isNightMode = isNightMode
                AppUtil.saveConfig(activity, config)
                EventBus.getDefault().post(ReloadDataEvent())
            }

            override fun onAnimationCancel(animator: Animator) {}

            override fun onAnimationRepeat(animator: Animator) {}
        })

        colorAnimation.duration = FADE_DAY_NIGHT_MODE.toLong()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val attrs = intArrayOf(android.R.attr.navigationBarColor)
            val typedArray = activity?.theme?.obtainStyledAttributes(attrs)
            val defaultNavigationBarColor = typedArray?.getColor(
                0,
                ContextCompat.getColor(context!!, R.color.white)
            )
            val black = ContextCompat.getColor(context!!, R.color.black)

            val navigationColorAnim = ValueAnimator.ofObject(
                ArgbEvaluator(),
                if (isNightMode) black else defaultNavigationBarColor,
                if (isNightMode) defaultNavigationBarColor else black
            )

            navigationColorAnim.addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                activity?.window?.navigationBarColor = value
            }

            navigationColorAnim.duration = FADE_DAY_NIGHT_MODE.toLong()
            navigationColorAnim.start()
        }

        colorAnimation.start()
    }

    private fun setToolBarColor() {
        if (isNightMode) {
            activityCallback.setDayMode()
        } else {
            activityCallback.setNightMode()
        }
    }
}
