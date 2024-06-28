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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import org.sakuram.relation.apimodel.RetrieveRelationsRequestVO
import org.sakuram.relation.utility.Constants
import org.sakuram.relation.viewmodel.Edge
import org.sakuram.relation.viewmodel.MainScreenViewModel
import org.sakuram.relation.viewmodel.Node
import kotlin.math.pow
import kotlin.math.sqrt

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
    var selectedPersonMS by rememberSaveable { mutableStateOf<Map.Entry<Long, Node>?>(null) }
    var selectedRelationMS by rememberSaveable { mutableStateOf<Map.Entry<Long, Edge>?>(null) }
    val graphTabUiState by mainScreenViewModel.graphTabUiState.collectAsState()
    var graphClick:Int by remember { mutableIntStateOf(0) }

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
                    graphClick = 0
                    detectTapGestures(
                        onDoubleTap = {selectedPosition ->
                            graphClick = 2
                            selectedPersonMS = graphTabUiState.nodesMap?.entries?.find { it.value.xStart < selectedPosition.x && selectedPosition.x < it.value.xEnd  &&
                                    it.value.yStart < selectedPosition.y && selectedPosition.y < it.value.yEnd }
                        },
                        onTap = {selectedPosition ->
                            graphClick = 1
                            selectedPersonMS = graphTabUiState.nodesMap?.entries?.find { it.value.xStart < selectedPosition.x && selectedPosition.x < it.value.xEnd  &&
                                    it.value.yStart < selectedPosition.y && selectedPosition.y < it.value.yEnd }
                            selectedRelationMS = graphTabUiState.edgesMap?.entries?.find { compareWithEpsilon(distance(it.value.xStart, it.value.yStart, selectedPosition.x, selectedPosition.y) + distance(selectedPosition.x, selectedPosition.y, it.value.xEnd, it.value.yEnd),
                                    distance(it.value.xStart, it.value.yStart, it.value.xEnd, it.value.yEnd))}
                        }
                    )
                }
        ) {
            graphTabUiState.nodesMap?.entries?.map { nodeEntry ->
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
                    text = nodeEntry.value.label,
                    topLeft = Offset(nodeEntry.value.xStart, nodeEntry.value.yStart),
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Color.Black,
                    )
                )
            }
            graphTabUiState.edgesMap?.map { edgeEntry ->
                println("${edgeEntry.value.xStart}, ${edgeEntry.value.xEnd} to ${edgeEntry.value.yStart}, ${edgeEntry.value.yEnd}")
                drawLine(
                    color = Color.Magenta,
                    strokeWidth = 5f,
                    start = Offset(edgeEntry.value.xStart, edgeEntry.value.yStart),
                    end = Offset(edgeEntry.value.xEnd, edgeEntry.value.yEnd),
                )
            }
        }

        if(graphClick == 1) {
            if (selectedPersonMS != null) {
                try {
                    mainScreenViewModel.switchTab(Constants.TAB_INDEX_DETAILS)
                    mainScreenViewModel.setNodeDetails(selectedPersonMS!!.value.label)
                    mainScreenViewModel.retrievePersonAttributes(selectedPersonMS!!.key)
                } catch (e: NumberFormatException) {
                    println("Invalid Person Id")
                }
            } else if (selectedRelationMS != null) {
                try {
                    mainScreenViewModel.switchTab(Constants.TAB_INDEX_DETAILS)
                    mainScreenViewModel.setEdgeDetails(
                        selectedRelationMS!!.value.label,
                        graphTabUiState.nodesMap?.get(selectedRelationMS!!.value.source)?.label
                            ?: "",
                        graphTabUiState.nodesMap?.get(selectedRelationMS!!.value.target)?.label
                            ?: ""
                    )
                    mainScreenViewModel.retrieveRelationAttributes(selectedRelationMS!!.key)
                } catch (e: NumberFormatException) {
                    println("Invalid Relation Id")
                }
            }
        } else if(graphClick == 2) {
            if (selectedPersonMS != null) {
                try {
                    mainScreenViewModel.retrieveRelations(RetrieveRelationsRequestVO(selectedPersonMS!!.key))
                } catch (e: NumberFormatException) {
                    println("Invalid Person Id")
                }
            }
        }

    }
}

fun distance(aX: Float, aY: Float, bX: Float, bY: Float): Double {
    return sqrt((aX - bX).toDouble().pow(2.0) + (aY - bY).toDouble().pow(2.0))
}

fun compareWithEpsilon(a: Double, b: Double): Boolean {
    return (a < b + 2 && a > b - 2)
}