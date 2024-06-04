//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import com.example.safekeys.navigation.Screen
//import com.example.safekeys.screens.CredentialsScreen
//import com.example.safekeys.screens.LoginScreen
//import com.example.safekeys.screens.SignUpScreen
//import com.example.safekeys.ui.auth.AuthViewModel
//import com.example.safekeys.ui.home.CredentialViewModel
//
//@Composable
//fun CupcakeApp(
//    authViewModel: AuthViewModel = viewModel(),
//    credentialViewModel: CredentialViewModel = viewModel(),
//    navController: NavHostController = rememberNavController()
//) {
//    // Get current back stack entry
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    // Get the name of the current screen
////    val currentScreen = CupcakeScreen.valueOf(
////        backStackEntry?.destination?.route ?: CupcakeScreen.Start.name
////    )
//
//    Scaffold(
//        topBar = {
//            CupcakeAppBar(
//                currentScreen = currentScreen,
//                canNavigateBack = navController.previousBackStackEntry != null,
//                navigateUp = { navController.navigateUp() }
//            )
//        }
//    ) { innerPadding ->
//
//        val isRegistered by authViewModel.isRegistered.collectAsState()
//        val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
//        val credentialState by credentialViewModel.state.collectAsState()
//
//        val uiState by viewModel.uiState.collectAsState()
//
//        NavHost(
//            navController = navController,
//            startDestination = CupcakeScreen.Start.name,
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .padding(innerPadding)
//        ) {
//            composable<Screen> {
//                StartOrderScreen(
//                    quantityOptions = DataSource.quantityOptions,
//                    onNextButtonClicked = {
//                        viewModel.setQuantity(it)
//                        navController.navigate(CupcakeScreen.Flavor.name)
//                    },
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(dimensionResource(R.dimen.padding_medium))
//                )
//            }
//            composable(route = CupcakeScreen.Flavor.name) {
//                val context = LocalContext.current
//                SelectOptionScreen(
//                    subtotal = uiState.price,
//                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Pickup.name) },
//                    onCancelButtonClicked = {
//                        cancelOrderAndNavigateToStart(viewModel, navController)
//                    },
//                    options = DataSource.flavors.map { id -> context.resources.getString(id) },
//                    onSelectionChanged = { viewModel.setFlavor(it) },
//                    modifier = Modifier.fillMaxHeight()
//                )
//            }
//            composable(route = CupcakeScreen.Pickup.name) {
//                SelectOptionScreen(
//                    subtotal = uiState.price,
//                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Summary.name) },
//                    onCancelButtonClicked = {
//                        cancelOrderAndNavigateToStart(viewModel, navController)
//                    },
//                    options = uiState.pickupOptions,
//                    onSelectionChanged = { viewModel.setDate(it) },
//                    modifier = Modifier.fillMaxHeight()
//                )
//            }
//            composable(route = CupcakeScreen.Summary.name) {
//                val context = LocalContext.current
//                OrderSummaryScreen(
//                    orderUiState = uiState,
//                    onCancelButtonClicked = {
//                        cancelOrderAndNavigateToStart(viewModel, navController)
//                    },
//                    onSendButtonClicked = { subject: String, summary: String ->
//                        shareOrder(context, subject = subject, summary = summary)
//                    },
//                    modifier = Modifier.fillMaxHeight()
//                )
//            }
//        }
//    }
//}
//
//if (isRegistered) {
//    if (!isAuthenticated) {
//        NavHost(navController = navController, startDestination = Screen.Login) {
//            composable<Screen.Login> {
//                LoginScreen(onClick = {
//                    navController.navigate(Screen.Home)
//                })
//            }
//            composable<Screen.Home> {
//                CredentialsScreen(
//                    state = credentialState,
//                    onEvent = credentialViewModel::onEvent
//                )
//            }
//        }
//    } else {
//        NavHost(navController = navController, startDestination = Screen.Home) {
//            composable<Screen.Home> {
//                CredentialsScreen(
//                    state = credentialState,
//                    onEvent = credentialViewModel::onEvent
//                )
//            }
//        }
//    }
//} else {
//    NavHost(navController = navController, startDestination = Screen.SignUp) {
//        composable<Screen.SignUp> {
//            SignUpScreen()
////                            if (isAuthenticated) {
////                                navController.navigate(Screen.Home)
////                            }
//        }
//        composable<Screen.Home> {
//            CredentialsScreen(
//                state = credentialState,
//                onEvent = credentialViewModel::onEvent
//            )
//        }
//    }
//}