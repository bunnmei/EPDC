package space.webkombinat.epdc.VIew

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import space.webkombinat.epdc.ViewModel.CanvasVM
import space.webkombinat.epdc.ViewModel.Parameter_Sign
import space.webkombinat.epdc.ViewModel.Rect_Parameter

@Composable
fun RectDataEditor(
    modifier: Modifier = Modifier,
    vm: CanvasVM
) {

    val uiState by vm.uiState.collectAsState()
    val cData = uiState.rectItems[uiState.operateIndex-1]
    Column {
        Parameter(
            text = "Offset X = ${cData.x}",
            clickMinus = {
                vm.rectParameterUpdate(
                    updateParameter = Rect_Parameter.X,
                    sign = Parameter_Sign.Minus
                )
            },
            clickPlus = {
                vm.rectParameterUpdate(
                    updateParameter = Rect_Parameter.X,
                    sign = Parameter_Sign.Plus
                )
            }
        )

        Parameter(
            text = "Offset Y = ${cData.y}",
            clickMinus = {
                vm.rectParameterUpdate(
                    updateParameter = Rect_Parameter.Y,
                    sign = Parameter_Sign.Minus
                )
            },
            clickPlus = {
                vm.rectParameterUpdate(
                    updateParameter = Rect_Parameter.Y,
                    sign = Parameter_Sign.Plus
                )
            }
        )

        Parameter(
            text = "Rect width ${cData.size_w}",
            clickMinus = {
                vm.rectParameterUpdate(
                    updateParameter = Rect_Parameter.WIDTH,
                    sign = Parameter_Sign.Minus
                )
            },
            clickPlus = {
                vm.rectParameterUpdate(
                    updateParameter = Rect_Parameter.WIDTH,
                    sign = Parameter_Sign.Plus
                )
            }
        )

        Parameter(
            text = "Rect height = ${cData.size_h}",
            clickMinus = {
                vm.rectParameterUpdate(
                    updateParameter = Rect_Parameter.HEIGHT,
                    sign = Parameter_Sign.Minus
                )
            },
            clickPlus = {
                vm.rectParameterUpdate(
                    updateParameter = Rect_Parameter.HEIGHT,
                    sign = Parameter_Sign.Plus
                )
            }
        )

        Parameter(
            text = "Rect degree = ${cData.degree}",
            clickMinus = {
                vm.rectParameterUpdate(
                    updateParameter = Rect_Parameter.DEGREE,
                    sign = Parameter_Sign.Minus
                )
            },
            clickPlus = {
                vm.rectParameterUpdate(
                    updateParameter = Rect_Parameter.DEGREE,
                    sign = Parameter_Sign.Plus
                )
            }
        )

    }
}