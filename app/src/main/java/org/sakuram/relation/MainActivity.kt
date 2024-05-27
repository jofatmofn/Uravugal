package org.sakuram.relation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import org.sakuram.relation.composable.SearchPersonDialog
import org.sakuram.relation.composable.SwitchProjectDialog
import org.sakuram.relation.ui.theme.UravugalTheme
import org.sakuram.relation.utility.UravugalPreferences
import org.sakuram.relation.viewmodel.MainScreenViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: MainScreenViewModel by viewModels()
        super.onCreate(savedInstanceState)
        UravugalPreferences.setup(applicationContext)
        enableEdgeToEdge()
        setContent {
            UravugalTheme {
                val navController = rememberNavController()
                // Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    UravugalScreen(
                        modifier = Modifier, // .padding(innerPadding),
                        navController, viewModel
                    )
                // }
                // TODO https://developer.android.com/guide/navigation/design
                // instead of passing the NavController to your composables, expose an event to the NavHost
                NavHost(navController, startDestination = "home") {
                    composable("home") { UravugalTopBar(navController, viewModel) }
                    dialog("switchProject") { SwitchProjectDialog(navController, viewModel) }
                    dialog("searchPerson") { SearchPersonDialog(navController, viewModel) }
                }
            }
        }
    }
}

@Composable
fun UravugalScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    mainScreenViewModel: MainScreenViewModel
) {
    Scaffold(

        topBar = @androidx.compose.runtime.Composable {
            UravugalTopBar(navController, mainScreenViewModel)
        },
    ) {
            UravugalScreenBody(modifier.padding(it), mainScreenViewModel)
        }
    // TODO https://developer.android.com/develop/ui/compose/migrate/other-considerations
    // Instead of passing down ViewModel instances to other composables, pass only the required data and functions as parameters.
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UravugalTopBar(
    navController: NavController, mainScreenViewModel: MainScreenViewModel = viewModel(),
) {
    var showMenu by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val mainScreenUiState by mainScreenViewModel.mainScreenUiState.collectAsState()
    val projectName = mainScreenUiState.projectName
    // val mContext = LocalContext.current
    // println("At Top Bar Project name is ${viewModel.uiState.value.projectName}")
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFF4A460), // MaterialTheme.colorScheme.primaryContainer,
                // titleContentColor = Color(0xFFF4A460), // MaterialTheme.colorScheme.primary,
            ),
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = stringResource(R.string.app_logo)
                    )
                }
            },
            title = {
                Column {
                    if (projectName != null) {
                        Text(
                            projectName.toString()
                        )
                    }
                    Text(
                        stringResource(R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            actions = {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.menu_24dp),
                        contentDescription = stringResource(R.string.actions)
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )
    // Don't refactor the menu code into a separate Composable as the Action Icon and Menu need to share a single State for recomposition
    if (showMenu) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.menu_1)) },
                onClick = {
                    showMenu = false
                    navController.navigate("searchPerson")
                    println("Returned from Search Person Dialog")
                          },
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null
                    )
                })
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.menu_2)) },
                onClick = { showMenu = false },
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null
                    )
                })
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.menu_3)) },
                onClick = { showMenu = false },
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null
                    )
                })
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.menu_4)) },
                onClick = {
                    showMenu = false
                    navController.navigate("switchProject")
                    println("Returned from Switch Project Dialog")
                          },
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null
                    )
                })
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.menu_5)) },
                onClick = {
                    showMenu = false
                          },
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null
                    )
                })
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.menu_6)) },
                onClick = { showMenu = false },
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null
                    )
                })
            // HorizontalDivider()
        }
    }
}

@Composable
fun UravugalScreenBody(
    modifier: Modifier = Modifier
    .fillMaxHeight()
    .fillMaxWidth(),
    mainScreenViewModel: MainScreenViewModel
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Graph", "Details")

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.padding(top = 100.dp))
        TabRow(
            selectedTabIndex = tabIndex,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                )
            }
        }
        when (tabIndex) {
            0 -> UravugalGraphTab(modifier, mainScreenViewModel)
            1 -> UravugalDetailsTab(modifier, mainScreenViewModel)
        }
    }
}
@Composable
fun UravugalGraphTab(
    modifier: Modifier = Modifier
    .fillMaxHeight()
    .background(color = Color(0xFFFFD700))
    .fillMaxWidth(),
    mainScreenViewModel: MainScreenViewModel
) {

    val graphTabUiState by mainScreenViewModel.graphTabUiState.collectAsState()
    val graphData = graphTabUiState.graphData
    Column() {
        Text(
            text = graphData
        )
    }
}

@Composable
fun UravugalDetailsTab(
    modifier: Modifier = Modifier
    .fillMaxHeight()
    .background(color = Color(0xFFF5F5DC))
    .fillMaxWidth(),
    mainScreenViewModel: MainScreenViewModel
) {
    val detailsTabUiState by mainScreenViewModel.detailsTabUiState.collectAsState()
    val attributeValueList = detailsTabUiState.attributeValueList
    val column1Weight = .4f
    val column2Weight = .6f
    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        item {
            Row(Modifier.background(Color.Gray)) {
                TableCell(text = "Attribute", weight = column1Weight)
                TableCell(text = "Value", weight = column2Weight)
            }
        }
        println("No. of items: ${attributeValueList.size}")
        // Data
        attributeValueList.map {
            item {
                Row(Modifier.fillMaxWidth()) {
                    TableCell(text = it.attributeName, weight = column1Weight)
                    TableCell(text = it.attributeValue, weight = column2Weight)
                }
            }
        }
        /*( items(attributeValueList.size ?: 0) {
            attributeValueList.map {
                println("Compose: ${it.attributeName} ${it.attributeValue}")
                Row(Modifier.fillMaxWidth()) {
                    TableCell(text = it.attributeName, weight = column1Weight)
                    TableCell(text = it.attributeValue, weight = column2Weight)
                }
            }
        } */
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
