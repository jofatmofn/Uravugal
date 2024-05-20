package org.sakuram.relation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sakuram.relation.ui.theme.UravugalTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UravugalTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    UravugalScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun UravugalScreen(modifier: Modifier = Modifier) {
    UravugalTopBar()
    UravugalScreenBody()
    UravugalBottomBar()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UravugalTopBar(modifier: Modifier = Modifier) {
    var showMenu by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val mContext = LocalContext.current

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
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
                    Text(
                        stringResource(R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {showMenu = !showMenu}) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.menu_24dp),
                            contentDescription = stringResource(R.string.actions)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { // innerPadding ->
        // ScrollContent(innerPadding)
    }
    // Don't refactor the menu code into a separate Composable as the Action Icon and Menu need to share a single State for recomposition
    if (showMenu) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.menu_1)) },
                onClick = { showMenu = false },
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
                onClick = { showMenu = false },
                leadingIcon = {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null
                    )
                })
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.menu_5)) },
                onClick = { showMenu = false },
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
fun UravugalScreenBody(modifier: Modifier = Modifier
    .height(500.dp)
    .background(color = Color(0xFFFFD700))
    .fillMaxWidth()) {
}
fun UravugalBottomBar(modifier: Modifier = Modifier
    .height(100.dp)
    .background(color = Color.Blue)
    .fillMaxWidth()) {
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UravugalTheme {
        UravugalScreen()
    }
}
