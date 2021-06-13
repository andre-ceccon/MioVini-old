package br.com.miovini.utils

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import br.com.miovini.R

object NavigationExtension {
    private val slideLeftOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    fun NavController.navigateWithAnimations(
        destinationId: Int, bundle: Bundle? = null,
        animation: NavOptions = slideLeftOptions
    ) = this.navigate(destinationId, bundle, animation)

    fun NavController.navigateWithAnimations(
        directions: NavDirections, animation: NavOptions = slideLeftOptions
    ) = this.navigate(directions, animation)
}
