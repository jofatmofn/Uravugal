package org.sakuram.relation.composable

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.sakuram.relation.utility.Constants
import org.sakuram.relation.viewmodel.SearchPersonDialogViewModel

@Composable
fun SearchResultsDialog(
    navController: NavController,
    searchPersonDialogViewModel: SearchPersonDialogViewModel
) {
    val context = LocalContext.current
    val searchResultsDialogUiState by searchPersonDialogViewModel.searchResultsDialogUiState.collectAsState()

    val localDensity = LocalDensity.current
    var lazyRowWidthDp by remember { mutableStateOf(0.dp) }

    Column(
        modifier = Modifier
            .background(Color(Constants.COLOR_CUSTOM_WHITE), shape = RoundedCornerShape(15.dp))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
            .onGloballyPositioned { layoutCoordinates ->  // This function will get called once the layout has been positioned
                lazyRowWidthDp =
                    with(localDensity) { layoutCoordinates.size.width.toDp() }  // with Density is required to convert to correct Dp
            }
    ) {

        Button(onClick = {
            navController.popBackStack(route = "home", inclusive = false)
        }) {
            Text("Close")
        }

        Row(
            Modifier
                .width(lazyRowWidthDp)
                .height(IntrinsicSize.Min)
        ) {
            if (searchResultsDialogUiState.tableContent == null) {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
            } else if (searchResultsDialogUiState.tableContent!!.isNotEmpty()) {
                searchResultsDialogUiState.tableContent!![0].map {
                    AttributeCell(
                        Modifier.background(Color.Red, shape = RectangleShape)
                    ) { Text(text = it, color = Color.White) }
                }
            }
        }

        val scrollState = rememberScrollState()

        LazyColumn(
            Modifier
                // .padding(16.dp)
                .width(2000.dp)
                .height(2000.dp)
                .horizontalScroll(scrollState),
        ) {

            if (searchResultsDialogUiState.tableContent == null) {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
            } else {
                searchResultsDialogUiState.tableContent!!.drop(1).withIndex().map { (rowIndex, value) ->
                    item {
                        Row(
                            Modifier
                                .width(lazyRowWidthDp)
                                .height(IntrinsicSize.Min)
                        ) {
                            value.withIndex().map { (colIndex, value) ->
                                AttributeCell(
                                    Modifier
                                        .then(
                                            if (rowIndex % 2 == colIndex % 2)
                                                Modifier.background(Color(Constants.COLOR_FLORAL_WHITE), shape = RectangleShape)
                                            else
                                                Modifier.background(Color(Constants.COLOR_GHOST_WHITE), shape = RectangleShape)
                                        )
                                ) { Text(text = value, color = Color.Black) }
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun AttributeCell(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.width(100.dp),  // Background color coming from parent composable
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        content()
        Spacer(modifier = Modifier.weight(1f, true))
    }
}
