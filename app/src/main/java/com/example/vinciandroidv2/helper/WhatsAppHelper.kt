package com.example.vinciandroidv2.helper

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils

fun Activity.openWhatsApp(id: String) {
    val intent = Intent()
    intent.setPackage("com.whatsapp")
    intent.action = Intent.ACTION_VIEW

    intent.resolveActivity(packageManager ?: return)?.let {
        try {
            var number = id
            number = number.replace(" ", "")
                .replace("(", "")
                .replace(")", "")

            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.type = "text/plain"
            sendIntent.putExtra(Intent.EXTRA_TEXT, "")
            sendIntent.component =
                ComponentName("com.whatsapp", "com.whatsapp.Conversation")
            sendIntent.putExtra(
                "jid",
                PhoneNumberUtils.stripSeparators(number).toString() + "@s.whatsapp.net"
            )
            startActivity(sendIntent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    } ?: run {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com"))
        startActivity(browserIntent)
    }
}

val countryWhatsappPhoneList = hashMapOf(
    "UK" to "442071450112",
    "Ireland" to "442071450112",
    "Nigeria" to "2348174222227",
    "Ghana" to "233555568046",
    "Spain" to "34653111092",
    "Lebanon" to "9613112341",
)