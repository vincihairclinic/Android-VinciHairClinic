package com.folioreader.ui.view

import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.folioreader.R
import com.folioreader.model.HighLight
import com.folioreader.model.HighlightImpl
import com.folioreader.model.sqlite.HighLightTable
import com.folioreader.ui.fragment.FolioPageFragment
import com.folioreader.util.HighlightUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.view_note.*

class NoteBottomSheetDialogFragment : BottomSheetDialogFragment(), TextWatcher {

    companion object {
        @JvmField
        val LOG_TAG: String = NoteBottomSheetDialogFragment::class.java.simpleName
    }

    private lateinit var parentFragment: FolioPageFragment
    private var highlightImpl: HighlightImpl? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.view_note, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view?.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
        }
        with(close) {
            setOnClickListener {
                if (true == highlightImpl?.note?.isNullOrEmpty()) {
                    (parentFragment as? FolioPageFragment)?.removeNote(highlightImpl?.rangy ?: "")
                }
                dismiss()
            }
        }
        with(save) {
            setOnClickListener {
                val note = noteText?.text?.toString()
                highlightImpl?.note = note
                if (HighLightTable.updateHighlight(highlightImpl)) {
                    HighlightUtil.sendHighlightBroadcastEvent(
                        this@NoteBottomSheetDialogFragment.activity?.applicationContext,
                        highlightImpl,
                        HighLight.HighLightAction.MODIFY
                    )
                }
                dismiss()
            }
        }
        highlightText?.text = highlightImpl?.content ?: ""
        noteText?.setText(highlightImpl?.note ?: "")
        noteText?.setSelection(highlightImpl?.note?.length ?: 0)
        noteText?.addTextChangedListener(this)
        Handler().postDelayed({
            noteText.requestFocus()
            (context?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
                try {
                    this.showSoftInput(noteText, InputMethodManager.SHOW_IMPLICIT)
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                }
            }
        }, 100)
    }

    fun setData(parentFragment: FolioPageFragment, highlightImpl: HighlightImpl) {
        this.parentFragment = parentFragment
        this.highlightImpl = highlightImpl
    }

    override fun onDestroyView() {
        try {
            val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.viewTreeObserver?.addOnGlobalLayoutListener(null)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (true == s.toString()?.isNullOrEmpty()){
            save?.setTextColor(Color.parseColor("#B1B7C5"))
        }else{
            save?.setTextColor(Color.BLACK)
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }
}
