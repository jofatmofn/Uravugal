package org.sakuram.relation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.sakuram.relation.viewmodel.MainScreenViewModel

@Composable
fun DetailsTab(
    modifier: Modifier = Modifier
        .fillMaxHeight()
        .background(color = Color(0xFFF5F5DC))
        .fillMaxWidth(),
    mainScreenViewModel: MainScreenViewModel
) {
    val detailsTabUiState by mainScreenViewModel.detailsTabUiState.collectAsState()
    val title = detailsTabUiState.title
    val attributeValueList = detailsTabUiState.attributeValueList
    val column1Weight = 0.5f
    val column2Weight = 0.5f

    val localDensity = LocalDensity.current
    var lazyRowWidthDp by remember { mutableStateOf(0.dp) }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
            .onGloballyPositioned { layoutCoordinates ->  // This function will get called once the layout has been positioned
                lazyRowWidthDp =
                    with(localDensity) { layoutCoordinates.size.width.toDp() }  // with Density is required to convert to correct Dp
            }
    ) {

        Text(
            text = title,
        )

        val scrollState = rememberScrollState()

        // The LazyColumn will be our table. Notice the use of the weights below
        LazyColumn(
            Modifier
                /* .fillMaxSize() */
                .padding(16.dp)
                // .fillMaxWidth ()
                .width(500.dp)
                .height(500.dp)
                .horizontalScroll(scrollState),
        ) {

            // Header
            item {
                Row(Modifier.background(Color.Gray).width(lazyRowWidthDp)) {
                    TableCell(text = "Value", weight = column1Weight)
                    TableCell(text = "Attribute", weight = column2Weight)
                }
            }

            // Data
            attributeValueList.map {
                item {
                    Row(Modifier.width(lazyRowWidthDp)) {
                        TableCell(text = it.attributeValue, weight = column1Weight)
                        TableCell(text = it.attributeName, weight = column2Weight)
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
    )
}
