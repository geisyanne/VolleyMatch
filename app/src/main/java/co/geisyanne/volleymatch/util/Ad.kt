package co.geisyanne.volleymatch.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import co.geisyanne.volleymatch.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.firebase.crashlytics.FirebaseCrashlytics

object Ad {

    fun loadBannerAd(
        adBanner: AdView,
        containerAd: FrameLayout,
        onAdLoaded: (Boolean) -> Unit
    ) {

        if (!isInternetAvailable(containerAd.context)) {
            containerAd.visibility = View.GONE
            onAdLoaded(false)
            return
        }

        val adRequest = AdRequest.Builder().build()
        adBanner.loadAd(adRequest)

        adBanner.adListener = object : AdListener() {
            override fun onAdLoaded() {
                containerAd.visibility = View.VISIBLE
                containerAd.setBackgroundColor(
                    ContextCompat.getColor(
                        containerAd.context,
                        R.color.gray_light_2
                    )
                )

                onAdLoaded(true)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                containerAd.visibility = View.GONE

                onAdLoaded(false)

                FirebaseCrashlytics.getInstance().log("Ad failed to load: ${adError.message}")
                FirebaseCrashlytics.getInstance().setCustomKey("adErrorCode", adError.code)
            }
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(it)
        }
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

}