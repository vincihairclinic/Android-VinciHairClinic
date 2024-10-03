package com.example.vinciandroidv2.helper

import android.content.Context
import android.content.DialogInterface
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.appcompat.app.AlertDialog
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.network.respons.Country

fun showSimpleListDialog(
    context: Context?,
    list: Array<String>,
    listener: DialogInterface.OnClickListener,
) {
    if (null == context) return
    AlertDialog.Builder(context).setItems(list, listener).create().show()
}

fun showSingleRememberDialog(
    context: Context?,
    title: String?,
    list: Array<String>,
    checkedPosition: Int,
    newCheckedPosition: (newCheckedPosition: Int) -> Unit
) {
    if (null == context) return
    var tempCheckedPosition = checkedPosition

//    val adapter = ArrayAdapter(context, R.layout.item_row_check_box_sample_one, list)
    val alert = AlertDialog.Builder(context)
        .setCustomTitle(title)
        .setSingleChoiceItems(list, tempCheckedPosition) { _, which ->
            tempCheckedPosition = which
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton("Confirm") { dialog, _ ->
            newCheckedPosition(tempCheckedPosition)
            dialog.dismiss()
        }
        .create()
    alert.setCancelable(false)
    alert.setCanceledOnTouchOutside(false)
//    alert.window?.setBackgroundDrawableResource(R.drawable.background_alert_dialog_rounded_corners_8)
    alert.show()
}

fun showMultiRememberDialog(
    context: Context,
    title: String?,
    list: Array<String>,
    checkedList: BooleanArray,
    selectedItemList: (checkedList: BooleanArray) -> Unit
) {
    val checkedItems = BooleanArray(list.size).apply {
        checkedList.forEachIndexed { index, b -> this[index] = b }
    }

    val alert = AlertDialog.Builder(context)
        .setCustomTitle(title)
        .setMultiChoiceItems(list, checkedItems) { dialog, which, isChecked ->
            checkedItems[which] = isChecked
        }
        .setNeutralButton("Clear") { _, _ ->
            for (i in checkedItems.indices) checkedItems[i] = false
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton("Confirm") { dialog, _ ->
            selectedItemList(checkedItems)
            dialog.dismiss()
        }
        .create()
    alert.setCancelable(false)
    alert.setCanceledOnTouchOutside(false)
//    alert.window?.setBackgroundDrawableResource(R.drawable.background_alert_dialog_rounded_corners_8)
    alert.show()
}

private fun AlertDialog.Builder.setCustomTitle(text: String?): AlertDialog.Builder {
    if (!text.isNullOrEmpty()) {
//        setCustomTitle(
//            View.inflate(context, R.layout.view_alert_dialog_title_sample_one, null)
//                ?.apply { this.title?.text = text }
//        )
    }
    return this
}