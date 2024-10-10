package co.geisyanne.volleymatch.util

import android.content.Context
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

object Ad {

    fun initialize(context: Context) {
        MobileAds.initialize(context) {}
    }

    fun loadBannerAd(adView: AdView) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.visibility = View.VISIBLE
    }

}