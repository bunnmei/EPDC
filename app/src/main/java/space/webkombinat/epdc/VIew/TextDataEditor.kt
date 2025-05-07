package space.webkombinat.epdc.VIew

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import space.webkombinat.epdc.ViewModel.CanvasVM
import space.webkombinat.epdc.ViewModel.Parameter_Sign
import space.webkombinat.epdc.ViewModel.Text_Parameter

@Composable
fun TextDataEditor(
    modifier: Modifier = Modifier,
    vm: CanvasVM
) {
    val uiState by vm.uiState.collectAsState()
    val cData = uiState.textItems[uiState.operateIndex-1]
    Column {
        Text_Parameter(
            text = cData.text
        ) { newText ->
            vm.updateText(newText)
        }
        Parameter(
            text = "Offset X = ${cData.x}",
            clickMinus = {
                vm.textParameterUpdate(
                    updateParameter = Text_Parameter.X,
                    sign = Parameter_Sign.Minus
                )
            },
            clickPlus = {
                vm.textParameterUpdate(
                    updateParameter = Text_Parameter.X,
                    sign = Parameter_Sign.Plus
                )
            }
        )

        Parameter(
            text = "Offset Y = ${cData.y}",
            clickMinus = {
                vm.textParameterUpdate(
                    updateParameter = Text_Parameter.Y,
                    sign = Parameter_Sign.Minus
                )
            },
            clickPlus = {
                vm.textParameterUpdate(
                    updateParameter = Text_Parameter.Y,
                    sign = Parameter_Sign.Plus
                )
            }
        )

        Parameter(
            text = "Font Size = ${cData.fontSize}",
            clickMinus = {
                vm.textParameterUpdate(
                    updateParameter = Text_Parameter.Size,
                    sign = Parameter_Sign.Minus
                )
            },
            clickPlus = {
                vm.textParameterUpdate(
                    updateParameter = Text_Parameter.Size,
                    sign = Parameter_Sign.Plus
                )
            }
        )

        Parameter(
            text = "Font Weight = ${cData.fontWeight}",
            clickMinus = {
                vm.textParameterUpdate(
                    updateParameter = Text_Parameter.Weight,
                    sign = Parameter_Sign.Minus
                )
            },
            clickPlus = {
                vm.textParameterUpdate(
                    updateParameter = Text_Parameter.Weight,
                    sign = Parameter_Sign.Plus
                )
            }
        )

        FontDropDownMenu(vm = vm)

    }
}

@Composable
fun Text_Parameter(
    modifier: Modifier = Modifier,
    text: String,
    change: (String) -> Unit
) {
    Row(
        modifier = modifier.padding(8.dp),
    ) {
        Spacer(modifier = modifier.weight(1f))
        TextField(
            modifier = modifier
                .fillMaxWidth(fraction = 0.8f),
            value = text,
            onValueChange = change
        )
        Spacer(modifier = modifier.weight(1f))
    }

}

@Composable
fun Parameter(
    modifier: Modifier = Modifier,
    text: String,
    clickMinus: () -> Unit,
    clickPlus: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(66.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = modifier.width(8.dp))
        Button(
            onClick = clickMinus,
            modifier = modifier
                .size(50.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.secondaryContainer)
            ,
            contentPadding = PaddingValues(0.dp),

            ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Minus"
            )
        }
        Spacer(modifier = modifier.weight(1f))
        Text(text)
        Spacer(modifier = modifier.weight(1f))
        Button(
            onClick = clickPlus,
            modifier = modifier
                .size(50.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add"
            )
        }
        Spacer(modifier = modifier.width(8.dp))

    }
}
