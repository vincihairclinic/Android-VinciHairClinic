package com.folioreader.ui.base;

import android.content.Context;
import com.folioreader.Config;
import com.folioreader.Constants;
import com.folioreader.R;

/**
 * @author gautam chibde on 14/6/17.
 */

public final class HtmlUtil {

    /**
     * Function modifies input html string by adding extra css,js and font information.
     *
     * @param context     Activity Context
     * @param htmlContent input html raw data
     * @return modified raw html string
     */
    public static String getHtmlContent(Context context, String htmlContent, Config config) {

        String cssPath =
                String.format(context.getString(R.string.css_tag), "file:///android_asset/css/Style.css");

        String jsPath = String.format(context.getString(R.string.script_tag),
                "file:///android_asset/js/jsface.min.js") + "\n";

        jsPath = jsPath + String.format(context.getString(R.string.script_tag),
                "file:///android_asset/js/jquery-3.4.1.min.js") + "\n";

        jsPath = jsPath + String.format(context.getString(R.string.script_tag),
                "file:///android_asset/js/rangy-core.js") + "\n";

        jsPath = jsPath + String.format(context.getString(R.string.script_tag),
                "file:///android_asset/js/rangy-highlighter.js") + "\n";

        jsPath = jsPath + String.format(context.getString(R.string.script_tag),
                "file:///android_asset/js/rangy-classapplier.js") + "\n";

        jsPath = jsPath + String.format(context.getString(R.string.script_tag),
                "file:///android_asset/js/rangy-serializer.js") + "\n";

        jsPath = jsPath + String.format(context.getString(R.string.script_tag),
                "file:///android_asset/js/Bridge.js") + "\n";

        jsPath = jsPath + String.format(context.getString(R.string.script_tag),
                "file:///android_asset/js/rangefix.js") + "\n";

        jsPath = jsPath + String.format(context.getString(R.string.script_tag),
                "file:///android_asset/js/readium-cfi.umd.js") + "\n";

        jsPath = jsPath + String.format(context.getString(R.string.script_tag_method_call),
                "setMediaOverlayStyleColors('#C0ED72','#C0ED72')") + "\n";

        jsPath = jsPath
                + "<meta name=\"viewport\" content=\"height=device-height, user-scalable=no\" />";

        String toInject = "\n" + cssPath + "\n" + jsPath + "\n</head>";
        htmlContent = htmlContent.replace("</head>", toInject);

//
//        html = html.replacingOccurrences(of: "<img", with: "<img onclick=\"showFullScreen(this.src)\"")

//        html = html.replacingOccurrences(of: "</html>", with: "<div id=\"myModal\" class=\"modal\"><span class=\"close\">&times;</span><img class=\"modal-content\" id=\"img01\"></div></html>")
//        htmlContent = htmlContent.replace("<img","<img onclick=\"showFullScreen(this.src)\"");
//        htmlContent = htmlContent.replace("</html>","<div id=\"myModal\" class=\"modal\"><span class=\"close\">&times;</span><img class=\"modal-content\" id=\"fullscreenImg\"/></div></html>");

        String classes = "";
        switch (config.getFont()) {
            case Constants.FONT_ANDADA:
                classes = "andada";
                break;
            case Constants.FONT_LATO:
                classes = "lato";
                break;
            case Constants.FONT_LORA:
                classes = "lora";
                break;
            case Constants.FONT_RALEWAY:
                classes = "raleway";
                break;
            default:
                break;
        }

        if (config.isNightMode()) {
            classes += " nightMode";
        }

        switch (config.getFontSize()) {
            case 0:
                classes += " textSizeOne";
                break;
            case 1:
                classes += " textSizeTwo";
                break;
            case 2:
                classes += " textSizeThree";
                break;
            case 3:
                classes += " textSizeFour";
                break;
            case 4:
                classes += " textSizeFive";
                break;
            default:
                break;
        }

        switch (config.getBackgroundColor()){
            case "FFFFFF":
                classes += " backgroundColorFFFFFF";
                break;
            case "DACCB7":
                classes += " backgroundColorDACCB7";
                break;
            case "95C5E0":
                classes += " backgroundColor95C5E0";
                break;
            case "6985A3":
                classes += " backgroundColor6985A3";
                break;
            case "898D90":
                classes += " backgroundColor898D90";
                break;
            case "4A5B6D":
                classes += " backgroundColor4A5B6D";
                break;
        }

        htmlContent = htmlContent.replace("<html", "<html class=\"" + classes + "\"" +
                " onclick=\"onClickHtml()\"");
//        htmlContent = htmlContent.replace("id=\"sigil_toc","class=\"section123\" id=\"sigil_toc");
        return htmlContent;
    }
}
