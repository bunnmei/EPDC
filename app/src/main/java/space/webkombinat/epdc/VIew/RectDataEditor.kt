package space.webkombinat.epdc.VIew

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import space.webkombinat.epdc.ViewModel.CanvasVM
import space.webkombinat.epdc.ViewModel.Parameter_Sign
import space.webkombinat.epdc.ViewModel.Rect_Parameter
import space.webkombinat.epdc.ViewModel.Text_Parameter

@Composable
fun RectDataEditor(
    modifier: Modifier = Modifier,
    vm: CanvasVM
) {
    val cData = vm.rect_items[vm.operate_data_id.value-1]
    Column {
        Parameter(
            text = "Offset X = ${cData.x}",
            clickMinus = {
                vm.rect_parameter_update(
                    updateParameter = Rect_Parameter.X,
                    sign = Parameter_Sign.Minus
                )
            },
            clickPlus = {
                vm.rect_parameter_update(
                    updateParameter = Rect_Parameter.X,
                    sign = Parameter_Sign.Plus
                )
            }
        )

        Parameter(
            text = "Offset Y = ${cData.y}",
            clickMinus = {
                vm.rect_parameter_update(
                    updateParameter = Rect_Parameter.Y,
                    sign = Parameter_Sign.Minus
                )
            },
            clickPlus = {
                vm.rect_parameter_update(
                    updateParameter = Rect_Parameter.Y,
                    sign = Parameter_Sign.Plus
                )
            }
        )

        Parameter(
            text = "Rect width ${cData.size_w}",
            clickMinus = {
                vm.rect_parameter_update(
                    updateParameter = Rect_Parameter.WIDTH,
                    sign = Parameter_Sign.Minus
                )
            },
            clickPlus = {
                vm.rect_parameter_update(
                    updateParameter = Rect_Parameter.WIDTH,
                    sign = Parameter_Sign.Plus
                )
            }
        )

        Parameter(
            text = "Rect height = ${cData.size_h}",
            clickMinus = {
                vm.rect_parameter_update(
                    updateParameter = Rect_Parameter.HEIGHT,
                    sign = Parameter_Sign.Minus
                )
            },
            clickPlus = {
                vm.rect_parameter_update(
                    updateParameter = Rect_Parameter.HEIGHT,
                    sign = Parameter_Sign.Plus
                )
            }
        )

        Parameter(
            text = "Rect degree = ${cData.degree}",
            clickMinus = {
                vm.rect_parameter_update(
                    updateParameter = Rect_Parameter.DEGREE,
                    sign = Parameter_Sign.Minus
                )
            },
            clickPlus = {
                vm.rect_parameter_update(
                    updateParameter = Rect_Parameter.DEGREE,
                    sign = Parameter_Sign.Plus
                )
            }
        )

    }
}