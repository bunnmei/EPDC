package space.webkombinat.epdc.Model

import space.webkombinat.epdc.ViewModel.ColorMode

object Constants {
    fun colorModeToInt(colorMode: ColorMode): Int {
        return when (colorMode) {
            ColorMode.Black -> 1
            ColorMode.Red -> 2
        }
    }

    fun  intToColorMode(color: Int): ColorMode {
        return when (color) {
            1 -> ColorMode.Black
            2 -> ColorMode.Red
            else -> ColorMode.Black
        }
    }
}