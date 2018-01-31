package net.dvinfosys.circleprogress.Library

import android.content.res.Resources


object Utils {

    fun dp2px(resources: Resources, dp: Float): Float {
        val scale = resources.getDisplayMetrics().density
        return dp * scale + 0.5f
    }

    fun sp2px(resources: Resources, sp: Float): Float {
        val scale = resources.getDisplayMetrics().scaledDensity
        return sp * scale
    }
}