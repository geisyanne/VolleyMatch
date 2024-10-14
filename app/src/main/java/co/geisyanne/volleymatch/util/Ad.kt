package co.geisyanne.volleymatch.util

import android.content.Context
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds

object Ad {

    fun initialize(context: Context) {
        MobileAds.initialize(context) {}
    }

    fun loadBannerAd(adBanner: AdView, adSeparator: View, onAdLoaded: (Boolean) -> Unit) {
        val adRequest = AdRequest.Builder().build()

        adBanner.visibility = View.GONE
        adSeparator.visibility = View.GONE

        adBanner.loadAd(adRequest)

        adBanner.adListener = object : AdListener() {
            override fun onAdLoaded() {
                adBanner.visibility = View.VISIBLE
                adSeparator.visibility = View.VISIBLE
                onAdLoaded(true)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                adBanner.visibility = View.GONE
                adSeparator.visibility = View.GONE
                onAdLoaded(false)
            }
        }
    }

}