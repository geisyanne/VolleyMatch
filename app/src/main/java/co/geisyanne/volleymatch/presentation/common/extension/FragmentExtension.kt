package co.geisyanne.volleymatch.presentation.common.extension

import android.content.res.Configuration
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import co.geisyanne.volleymatch.R

fun Fragment.getSnackbarColor(): Int {
    val currentNightMode = (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    return if (currentNightMode) {
        R.color.blue_dark_2
    } else {
        R.color.blue_dark
    }
}