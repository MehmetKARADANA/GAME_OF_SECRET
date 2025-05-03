package com.mobile.gameofsecret.ui.components

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(url:String){
    AndroidView(
        factory = {
            android.webkit.WebView(it).apply {
                webViewClient = webViewClient
                settings.javaScriptEnabled=true
                loadUrl(url)
            }
        }
    )
}