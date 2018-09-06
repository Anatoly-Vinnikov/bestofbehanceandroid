package com.inostudio.avinnikov.bestofbehanceandroid.utilities

import android.databinding.BindingAdapter
import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView

object ProjectModulesBindingAdapter {

    @JvmStatic
    @BindingAdapter("moduleText")
    fun setModuleText(view: TextView, text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT).trim()
        } else {
            @Suppress("DEPRECATION")
            view.text = Html.fromHtml(text).trim()
        }
        view.movementMethod = LinkMovementMethod.getInstance()
    }

    @JvmStatic
    @BindingAdapter("webView")
    fun setWebView(view: WebView, url: String) {
        view.settings.javaScriptEnabled = true
        view.settings.setSupportZoom(false)

        view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
            }
        }
        view.loadUrl(url)
    }
}