package org.sakuram.relation.composable

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import org.sakuram.relation.apimodel.RetrieveRelationsRequestVO
import org.sakuram.relation.utility.Constants
import org.sakuram.relation.viewmodel.MainScreenViewModel
import org.sakuram.relation.viewmodel.SearchPersonDialogViewModel

@Composable
fun SearchResultsDialog(
    navController: NavController,
    mainScreenViewModel: MainScreenViewModel,
    searchPersonDialogViewModel: SearchPersonDialogViewModel
) {
    val context = LocalContext.current
    val searchResultsDialogUiState by searchPersonDialogViewModel.searchResultsDialogUiState.collectAsState()
    val scrollState = rememberScrollState()

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

        Row {
            Button(onClick = {
                if (searchResultsDialogUiState.selectedRowIndex > -1) {
                    mainScreenViewModel.retrieveRelations(
                        RetrieveRelationsRequestVO(
                            searchResultsDialogUiState.tableContent!![searchResultsDialogUiState.selectedRowIndex + 1][0].toLong()
                        )
                    )
                    navController.clearBackStack("home")
                    navController.navigate("home")  // Fresh to home
                } else {
                    Toast.makeText(
                        context,
                        "Select a row before clicking on SELECT button",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }) {
                Text("Select")
            }

            VerticalDivider(modifier = Modifier.width(10.dp))

            Button(onClick = {
                navController.popBackStack()    // Back to Search Criteria
            }) {
                Text("Back")
            }

            VerticalDivider(modifier = Modifier.width(10.dp))

            Button(onClick = {
                navController.popBackStack(route = "home", inclusive = false)   // Back home
            }) {
                Text("Close")
            }
        }

        if (searchResultsDialogUiState.tableContent == null || searchResultsDialogUiState.tableContent!!.isEmpty()) {
            Toast.makeText(
                context,
                "No person meets the specified criteria",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Row(
                Modifier
                    .width(lazyRowWidthDp)
                    .height(IntrinsicSize.Min)
            ) {
                AttributeCell(
                    Modifier
                        .background(Color.Red, shape = RectangleShape)
                        .width(25.dp)
                ) { Text(text = "", color = Color.White) }
                searchResultsDialogUiState.tableContent!![0].map {
                    AttributeCell(
                        Modifier
                            .background(Color.Red, shape = RectangleShape)
                            .width(200.dp)
                    ) { Text(text = it, color = Color.White) }
                }
            }

            LazyColumn(
                Modifier
                    .width(4000.dp)
                    .height(2000.dp)
                    .horizontalScroll(scrollState),
            ) {

                searchResultsDialogUiState.tableContent!!.drop(1).withIndex()
                    .map { (rowIndex, value) ->
                        item {
                            Row(
                                Modifier
                                    .width(lazyRowWidthDp)
                                    .height(IntrinsicSize.Min)
                            ) {
                                AttributeCell(
                                    Modifier
                                        .width(25.dp)
                                ) {
                                    RadioButton(
                                        selected = (rowIndex == searchResultsDialogUiState.selectedRowIndex),
                                        onClick = {
                                            searchPersonDialogViewModel.uiToStateSelectedRowIndex(
                                                rowIndex
                                            )
                                            println("Selected Person: ${searchResultsDialogUiState.selectedRowIndex}")
                                        }
                                    )
                                }
                                value.withIndex().map { (colIndex, value) ->
                                    AttributeCell(
                                        Modifier
                                            .then(
                                                if (rowIndex % 2 == colIndex % 2)
                                                    Modifier.background(
                                                        Color(Constants.COLOR_FLORAL_WHITE),
                                                        shape = RectangleShape
                                                    )
                                                else
                                                    Modifier.background(
                                                        Color(Constants.COLOR_GHOST_WHITE),
                                                        shape = RectangleShape
                                                    )
                                            )
                                            .width(200.dp)
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
        modifier = modifier // Background color and width coming from parent composable
            .border(width = 1.dp, color = Color.Black),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        content()
        Spacer(modifier = Modifier.weight(1f, true))
    }
}
