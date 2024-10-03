package com.example.vinciandroidv2.ui.global

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Typeface
import android.view.Display
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.random.Random

fun String.sha256(): String {
    val md = MessageDigest.getInstance("SHA256")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun Any.objectScopeName() = "${javaClass.simpleName}_${hashCode()}"

fun getStatusBarHeight(context: Context): Int {
    var result = 0
    val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) result = context.resources.getDimensionPixelSize(resourceId)
    return result
}

fun getScreenHeight(activity: Activity?): Int = getScreenSize(activity).y
fun getScreenWidth(activity: Activity?): Int = getScreenSize(activity).x


fun TextView.setLineSpacingCustom(activity: Activity?) {
    setLineSpacing((getScreenSize(activity).y * 0.001).toFloat(), 1.0f)
}

fun getScreenSize(activity: Activity?): Point {
    val display: Display? = activity?.windowManager?.defaultDisplay
    val size = Point()
    display?.getSize(size)
    return size
}

fun randomInt() = Random(Calendar.getInstance().timeInMillis).nextInt()
fun String?.isPasswordValid(): Boolean = Pattern.compile(
    "^" +
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[0-9])" +         //at least 1 digit
//            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$"
).matcher(this ?: "").matches()

fun Date?.setToSimpleFormatOfLocation(outFormat: String = "yyyy-MM-dd"): String =
    SimpleDateFormat(outFormat).format(this ?: Date()) ?: ""

fun getFontStyle(context: Context?, @FontRes res: Int) = getTypeFace(context, res).style

fun getTypeFace(context: Context?, @FontRes res: Int): Typeface {
    return context?.let { ResourcesCompat.getFont(it, res) } ?: Typeface.DEFAULT
}
fun getAge(dobString: String): Int {
    var date: Date? = null
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
    try {
        date = sdf.parse(dobString)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    if (date == null) return 0
    val dob = Calendar.getInstance()
    val today = Calendar.getInstance()
    dob.time = date
    val year = dob[Calendar.YEAR]
    val month = dob[Calendar.MONTH]
    val day = dob[Calendar.DAY_OF_MONTH]
    dob[year, month] = day
    var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
    if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
        age--
    }
    return age
}


