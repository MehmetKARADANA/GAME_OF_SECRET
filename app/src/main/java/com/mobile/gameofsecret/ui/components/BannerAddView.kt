package com.mobile.gameofsecret.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAddView(adUnitId: String) {
    AndroidView(factory = {context->
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            this.adUnitId=adUnitId
            loadAd(AdRequest.Builder().build())
        }
    })
}

@Composable
fun BannerAdCard(adUnitId: String) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                this.adUnitId = adUnitId
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
