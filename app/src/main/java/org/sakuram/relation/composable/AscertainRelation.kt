package org.sakuram.relation.composable

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.sakuram.relation.apimodel.RelatedPersonsVO
import org.sakuram.relation.viewmodel.MainScreenViewModel

@Composable
fun AscertainRelationDialog(
    navController: NavController,
    mainScreenViewModel: MainScreenViewModel
) {
    val context = LocalContext.current
    val ascertainRelationDialogUiState by mainScreenViewModel.ascertainRelationDialogUiState.collectAsState()
    val graphTabUiState by mainScreenViewModel.graphTabUiState.collectAsState()

    if (graphTabUiState.nodesMap == null || graphTabUiState.nodesMap!!.isEmpty()) {
        Toast.makeText(
            context,
            "Required persons should be present in the graph",
            Toast.LENGTH_LONG
        ).show()
    } else {
        val dropdownLabels =  graphTabUiState.nodesMap?.entries!!.map { it.value.label }
        val dropdownKeys = graphTabUiState.nodesMap?.entries!!.map { it.key }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(Color(0xFFF5F5DC), shape = RoundedCornerShape(15.dp))
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            LargeDropdownMenu(
                label = "Person 1",
                items = dropdownLabels,
                selectedIndex = ascertainRelationDialogUiState.selectedPerson1Index,
                onItemSelected =
                { index, _ ->
                    if (ascertainRelationDialogUiState.selectedPerson1Index != index) {
                        mainScreenViewModel.uiToStateSelectedPerson1Index(index)
                    }
                },
            )

            LargeDropdownMenu(
                label = "Person 2",
                items = dropdownLabels,
                selectedIndex = ascertainRelationDialogUiState.selectedPerson2Index,
                onItemSelected =
                { index, _ ->
                    if (ascertainRelationDialogUiState.selectedPerson2Index != index) {
                        mainScreenViewModel.uiToStateSelectedPerson2Index(index)
                    }
                },
            )

            OutlinedTextField(
                label = { Text("Exclude Relations", color = Color.Black) },
                value = ascertainRelationDialogUiState.excludeRelationIdCsv,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Green,
                    unfocusedBorderColor = Color.Green
                ),
                onValueChange = { mainScreenViewModel.uiToStateExcludeRelationIdCsv(it) },
            )

            OutlinedTextField(
                label = { Text("Exclude Persons", color = Color.Black) },
                value = ascertainRelationDialogUiState.excludePersonIdCsv,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Green,
                    unfocusedBorderColor = Color.Green
                ),
                onValueChange = { mainScreenViewModel.uiToStateExcludePersonIdCsv(it) },
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    if (ascertainRelationDialogUiState.selectedPerson1Index != -1 &&
                        ascertainRelationDialogUiState.selectedPerson2Index != -1 &&
                        ascertainRelationDialogUiState.selectedPerson1Index != ascertainRelationDialogUiState.selectedPerson2Index
                    ) {
                        mainScreenViewModel.retrieveRelationPath(
                            RelatedPersonsVO(
                                person1Id = dropdownKeys[ascertainRelationDialogUiState.selectedPerson1Index],
                                person2Id = dropdownKeys[ascertainRelationDialogUiState.selectedPerson2Index],
                                excludeRelationIdCsv = ascertainRelationDialogUiState.excludeRelationIdCsv,
                                excludePersonIdCsv = ascertainRelationDialogUiState.excludePersonIdCsv
                            )
                        )
                    }
                    // TODO On Error, don't popBackStack
                    navController.popBackStack()
                }) {
                    Text("Ascertain")
                }
                Button(onClick = {
                    navController.popBackStack()
                }) {
                    Text("Cancel")
                }
            }
        }
    }
}