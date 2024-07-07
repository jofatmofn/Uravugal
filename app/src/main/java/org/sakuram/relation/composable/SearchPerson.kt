package org.sakuram.relation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.sakuram.relation.apimodel.AttributeValueVO
import org.sakuram.relation.apimodel.DomainValueVO
import org.sakuram.relation.apimodel.PersonSearchCriteriaVO
import org.sakuram.relation.utility.AppValues
import org.sakuram.relation.utility.Constants
import org.sakuram.relation.viewmodel.SearchPersonDialogViewModel

@Composable
fun SearchPersonDialog(
    navController: NavController,
    searchPersonDialogViewModel: SearchPersonDialogViewModel
) {
    val searchPersonDialogUiState by searchPersonDialogViewModel.searchPersonDialogUiState.collectAsState()

    val localDensity = LocalDensity.current
    var lazyRowWidthDp by remember { mutableStateOf(0.dp) }

    Column(
        modifier = Modifier
            .background(Color(Constants.COLOR_BEIGE), shape = RoundedCornerShape(15.dp))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
            .onGloballyPositioned { layoutCoordinates ->  // This function will get called once the layout has been positioned
                lazyRowWidthDp =
                    with(localDensity) { layoutCoordinates.size.width.toDp() }  // with Density is required to convert to correct Dp
            }
    ) {

        Row(modifier = Modifier.padding(8.dp)) {
            Button(onClick = {
                searchPersonDialogViewModel.addSearchCriterion()
            }) {
                Text("+")
            }

            VerticalDivider(modifier = Modifier.width(10.dp))

            Button(onClick = {
                // TODO Validate inputs
                searchPersonDialogViewModel.searchPerson(
                    PersonSearchCriteriaVO(
                        searchPersonDialogUiState.isLenient,
                        searchPersonDialogUiState.searchCriteria.map {
                            AttributeValueVO(attributeDvId = it.attributeDvId, attributeValue = it.attributeValue, maybeNotRegistered = it.isMaybeNotRegistered)
                        }
                    )
                )
                navController.navigate("searchResults")
            }) {
                Text("Search")
            }

            VerticalDivider(modifier = Modifier.width(10.dp))

            Text(text = "Lenient",
                modifier = Modifier.align(CenterVertically)
            )
            Checkbox(
                checked = searchPersonDialogUiState.isLenient,
                onCheckedChange =
                {
                    searchPersonDialogViewModel.uiToStateIsLenient(it)
                },
                enabled = true,
                colors = CheckboxDefaults.colors(Color(Constants.COLOR_SANDY_BROWN))
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .width(320.dp)
                .padding(horizontal = 0.dp),
            color = Color(Constants.COLOR_SANDY_BROWN)
        )

        val scrollState = rememberScrollState()

        LazyColumn(
            Modifier
                .padding(1.dp)
                .width(500.dp)
                .height(800.dp)
                .horizontalScroll(scrollState),
        ) {
            searchPersonDialogUiState.searchCriteria.mapIndexed { index, _ ->
                item {
                    SearchCriterionComponent(searchPersonDialogViewModel, index)
                }
            }
        }
    }
}

@Composable
fun SearchCriterionComponent(
    searchPersonDialogViewModel: SearchPersonDialogViewModel,
    searchCriterionindex: Int
) {
    val searchPersonDialogUiState by searchPersonDialogViewModel.searchPersonDialogUiState.collectAsState()
    val personAttributeValueDvVoList = AppValues.categorywiseDomainValueVOListMap[Constants.CATEGORY_PERSON_ATTRIBUTE]!!
        .filter { it.isInputAsAttribute != null && it.isInputAsAttribute }
        .map { it }
    var selectedAttributeDvVo: DomainValueVO?

    if (searchCriterionindex < 0 || searchCriterionindex >= searchPersonDialogUiState.searchCriteria.size) {
        // TODO Throw Error
        return
    }
    val searchCriterion = searchPersonDialogUiState.searchCriteria[searchCriterionindex]

    Column (
        modifier = Modifier
            .then(
                if(searchCriterionindex % 2 == 0)
                    Modifier.background(Color(Constants.COLOR_GOLD), shape = RectangleShape)
                else
                    Modifier.background(Color(Constants.COLOR_BEIGE), shape = RectangleShape)
            )
            // .border(2.dp, SolidColor(Color.Blue), shape = RoundedCornerShape(15.dp))
    )
    {
        Row {
            LargeDropdownMenu(
                label = "Attribute-$searchCriterionindex",
                items = personAttributeValueDvVoList.map { it.value },
                selectedIndex = searchCriterion.selectedAttributeIndex,
                onItemSelected =
                { index, _ ->
                    if (searchCriterion.selectedAttributeIndex != index) {
                        searchPersonDialogViewModel.uiToStateSelectedAttributeIndex(
                            searchCriterionindex,
                            index,
                        )
                        searchPersonDialogViewModel.uiToStateAttributeValue(
                            searchCriterionindex,
                            -1,
                            "",
                        )
                    }
                },
            )

            Button(onClick = {
                searchPersonDialogViewModel.deleteSearchCriterion(searchCriterionindex)
            }) {
                Text("-")
            }

        }

        selectedAttributeDvVo = null
        if (searchCriterion.selectedAttributeIndex > -1) {
            selectedAttributeDvVo =
                AppValues.domainValueVOMap[personAttributeValueDvVoList[searchCriterion.selectedAttributeIndex].id]
            searchPersonDialogViewModel.uiToStateAttributeDvId(
                searchCriterionindex,
                selectedAttributeDvVo!!.id,
            )
            if (selectedAttributeDvVo!!.attributeDomain != "") {
                val selectedAttributeMemberDvList =
                    AppValues.categorywiseDomainValueVOListMap[selectedAttributeDvVo!!.attributeDomain]
                LargeDropdownMenu(
                    label = selectedAttributeDvVo!!.value,
                    items = selectedAttributeMemberDvList!!.map { it.value },
                    selectedIndex = searchCriterion.selectedValueIndex,
                    onItemSelected =
                    { index, _ ->
                        searchPersonDialogViewModel.uiToStateAttributeValue(
                            searchCriterionindex,
                            index,
                            selectedAttributeMemberDvList[index].id.toString()
                        )
                    },
                )
            }
        }
        if (searchCriterion.selectedAttributeIndex == -1 || selectedAttributeDvVo!!.attributeDomain == "") {
            OutlinedTextField(
                label = { Text(text = if (searchCriterion.selectedAttributeIndex == -1) "" else selectedAttributeDvVo!!.value, color = Color.Black) },
                value = searchCriterion.attributeValue,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(Constants.COLOR_SADDLE_BROWN),
                    unfocusedBorderColor = Color(Constants.COLOR_ROSY_BROWN)
                ),
                onValueChange =
                {
                    searchPersonDialogViewModel.uiToStateAttributeValue(
                        searchCriterionindex,
                        -1,
                        it
                    )
                },
            )
        }

        Row(modifier = Modifier.padding(8.dp)) {
            Checkbox(
                checked = searchCriterion.isMaybeNotRegistered,
                onCheckedChange =
                {
                    searchPersonDialogViewModel.uiToStateIsMaybeNotRegistered(
                        searchCriterionindex,
                        it
                    )
                },
                enabled = true,
                colors = CheckboxDefaults.colors(Color(Constants.COLOR_SANDY_BROWN))
            )
            Text(
                text = "Maybe NOT registered",
                modifier = Modifier.align(CenterVertically)
            )
        }

        // HorizontalDivider(modifier = Modifier.width(280.dp), color = Color.Red)
    }
}