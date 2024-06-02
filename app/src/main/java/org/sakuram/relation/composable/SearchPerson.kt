package org.sakuram.relation.composable

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.sakuram.relation.apimodel.RetrieveRelationsRequestVO
import org.sakuram.relation.viewmodel.MainScreenViewModel

@Composable
fun SearchPersonDialog(
    navController: NavController,
    mainScreenViewModel: MainScreenViewModel
) {
    val personIdMS = remember { mutableStateOf("")}

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFF5F5DC), shape = RoundedCornerShape(15.dp))
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        OutlinedTextField(
            label = { Text("Person Id", color = Color.Black) },
            value = personIdMS.value,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Green,
                unfocusedBorderColor = Color.Green
            ),
            onValueChange = { personIdMS.value = it },
        )
        val focusManager = LocalFocusManager.current
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                focusManager.clearFocus()
                if (personIdMS.value != "") {
                    try {
                        mainScreenViewModel.retrievePersonAttributes(personIdMS.value.toLong())
                        mainScreenViewModel.retrieveRelations(RetrieveRelationsRequestVO(personIdMS.value.toLong()))
                    } catch(e: NumberFormatException) {
                        println("Invalid Person Id")
                    }
                }
                // TODO On Error, don't popBackStack
                println("About to popBackStack")
                navController.popBackStack()
            }) {
                Text("Search")
            }
            Button(onClick = {
                navController.popBackStack()
            }) {
                Text("Cancel")
            }
        }
    }
}