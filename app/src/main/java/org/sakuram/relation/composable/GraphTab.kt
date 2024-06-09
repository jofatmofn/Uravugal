package org.sakuram.relation.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sakuram.relation.utility.Constants
import org.sakuram.relation.viewmodel.MainScreenViewModel

@Composable
fun GraphTab
(
    modifier: Modifier = Modifier
        .fillMaxHeight()
        .background(color = Color(0xFFFFD700))
        .fillMaxWidth(),
    mainScreenViewModel: MainScreenViewModel,
) {
    val textMeasurer = rememberTextMeasurer()
    var selectedPersonIdMS by rememberSaveable { mutableStateOf<String?>(null) }
    val graphTabUiState by mainScreenViewModel.graphTabUiState.collectAsState()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
    ) {
        Canvas(
            modifier = modifier.fillMaxSize()
                .width(2500.dp)
                .height(1000.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {selectedPosition ->
                            selectedPersonIdMS = graphTabUiState.nodesMap?.entries?.find { it.value.xStart < selectedPosition.x && selectedPosition.x < it.value.xEnd  &&
                                    it.value.yStart < selectedPosition.y && selectedPosition.y < it.value.yEnd }?.key
                        }
                    )
                }
        ) {
            graphTabUiState.nodesMap?.entries?.map {nodeEntry ->
                drawRect(
                    color = Color.Magenta,
                    topLeft = Offset(nodeEntry.value.xStart, nodeEntry.value.yStart),
                    size = androidx.compose.ui.geometry.Size(
                        Constants.PERSON_NODE_SIZE_X,
                        Constants.PERSON_NODE_SIZE_Y
                    ),
                    style = Stroke(width = 1.dp.toPx()),
                )
                drawText(
                    textMeasurer = textMeasurer,
                    text = "${nodeEntry.value.label}",
                    topLeft = Offset(nodeEntry.value.xStart, nodeEntry.value.yStart),
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Color.Black,
                    )
                )
            }
        }

        if(selectedPersonIdMS != null) {
            try {
                mainScreenViewModel.switchTab(Constants.TAB_INDEX_DETAILS)
                mainScreenViewModel.retrievePersonAttributes(selectedPersonIdMS!!.toLong())
            } catch(e: NumberFormatException) {
                println("Invalid Person Id")
            }
        }
    }
}
