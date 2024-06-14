package org.sakuram.relation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import org.sakuram.relation.composable.DetailsTab
import org.sakuram.relation.composable.GraphTab
import org.sakuram.relation.composable.SearchPersonDialog
import org.sakuram.relation.composable.SwitchProjectDialog
import org.sakuram.relation.ui.theme.UravugalTheme
import org.sakuram.relation.utility.Constants
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
            viewModel.retrieveAppStartValues()
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

        topBar = @Composable {
            UravugalTopBar(navController, mainScreenViewModel)
        },
        content = @Composable  {
            Column(modifier.padding(it)) {
                // This Column with padding helps to solve the problem of topBar overlapping the content
                UravugalScreenBody(Modifier, mainScreenViewModel)
            }
        }
    )
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
    val tabs = listOf("Graph", "Details")
    val mainScreenUiState by mainScreenViewModel.mainScreenUiState.collectAsState()
    val tabIndex = mainScreenUiState.tabIndex

    Column(modifier = Modifier
        .fillMaxWidth()
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick =
                    {
                        mainScreenViewModel.switchTab(index)
                    },
                )
            }
        }
        when (tabIndex) {
            Constants.TAB_INDEX_GRAPH -> GraphTab(modifier, mainScreenViewModel)
            Constants.TAB_INDEX_DETAILS -> DetailsTab(modifier, mainScreenViewModel)
        }
    }
}

