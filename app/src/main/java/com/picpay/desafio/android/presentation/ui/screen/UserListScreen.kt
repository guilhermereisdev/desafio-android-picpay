package com.picpay.desafio.android.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.picpay.desafio.android.presentation.viewmodel.UserViewModel
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.presentation.ui.component.ContactCard
import com.picpay.desafio.android.presentation.ui.component.ContactDetailBottomSheet
import com.picpay.desafio.android.presentation.ui.theme.DesafioAndroidPicPayTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(userViewModel: UserViewModel = koinViewModel(), navController: NavController) {
    val userState by userViewModel.userState.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = userState.isLoading,
        onRefresh = { userViewModel.refreshUsers() }
    )
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var highContrastEnabled by remember { mutableStateOf(false) }
    var expandedMenu by remember { mutableStateOf(false) }

    DesafioAndroidPicPayTheme(
        darkTheme = isSystemInDarkTheme(),
        isHighContrast = highContrastEnabled
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Contatos",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    actions = {
                        IconButton(onClick = { expandedMenu = !expandedMenu }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Configurações",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        DropdownMenu(
                            expanded = expandedMenu,
                            onDismissRequest = { expandedMenu = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Alto Contraste",
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                onClick = {
                                    highContrastEnabled = !highContrastEnabled
                                    expandedMenu = false
                                }
                            )
                        }
                    },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            }
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .pullRefresh(pullRefreshState)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showBottomSheet = false },
                        sheetState = sheetState
                    ) {
                        selectedUser?.let { ContactDetailBottomSheet(it) }
                    }
                }
                when {
                    userState.isLoading && userState.data.isNullOrEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.testTag("loading_indicator"),
                            )
                        }
                    }

                    userState.error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Erro: ${userState.error}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.testTag("error_message"),
                            )
                        }
                    }

                    else -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(8.dp)
                            ) {
                                items(userState.data ?: emptyList()) { user ->
                                    ContactCard(
                                        user = user,
                                        onClick = {
                                            selectedUser = user
                                            showBottomSheet = true
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = userState.isLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    contentColor = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
