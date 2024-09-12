package com.example.geminitest1.ui.screens

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.geminitest1.AppBar
import com.example.geminitest1.AppConstants
import com.example.geminitest1.DialogBackground
import com.example.geminitest1.LoadingState
import com.example.geminitest1.R
import com.example.geminitest1.data.Bike
import com.example.geminitest1.data.DataStoreManager
import com.example.geminitest1.data.UiState
import com.example.geminitest1.viewmodel.BikeViewModel
import java.io.IOException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardLayout(
    bikeViewModel: BikeViewModel = hiltViewModel(),
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val scrollState = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val bikes by bikeViewModel.bikes.collectAsState()
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var bikeToDelete by remember { mutableStateOf<Bike?>(null) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollState.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.onPrimary,
        topBar = {
            AppBar(
                title = stringResource(id = R.string.app_title),
                showBackArrow = false, navController = navController,
                color = MaterialTheme.colorScheme.tertiaryContainer,
                textColor = MaterialTheme.colorScheme.tertiary,
                onHomeScreen = true
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AppConstants.Screens.ADDBIKESCREEN)
                },
                shape = FloatingActionButtonDefaults.shape
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .height(65.dp)
                    .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
            ) {
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.tertiary,
                        selectedTextColor = MaterialTheme.colorScheme.tertiary,
                        indicatorColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = null
                        )
                    },
                    label = { Text("Home") },
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                    }
                )
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.tertiary,
                        selectedTextColor = MaterialTheme.colorScheme.tertiary,
                        indicatorColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null
                        )
                    },
                    label = { Text("Give FeedBack") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                    }
                )
            }
        }
    ) { innerPadding ->
        when (selectedItem) {
            0 -> HomeScreen(innerPadding = innerPadding, bikes, onDeleteClick = { bike ->
                bikeToDelete = bike
                showDeleteConfirm = true
            })

            1 -> UserManualScreenLayout(navController = navController, innerPadding = innerPadding)
        }

        if (showDeleteConfirm && bikeToDelete != null) {
            DeleteConfirmDialog(
                onDismiss = { showDeleteConfirm = false },
                onConfirm = {
                    bikeViewModel.deleteBike(bikeToDelete!!)
                    showDeleteConfirm = false
                    // Check if there are no bikes left and navigate to Add Bike screen
                    if (bikeViewModel.bikes.value.isEmpty()) {
                        navController.navigate(AppConstants.Screens.ADDBIKESCREEN) {
                            popUpTo(AppConstants.Screens.DASHBOARDSCREEN) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun HomeScreen(innerPadding: PaddingValues, bikes: List<Bike>, onDeleteClick: (Bike) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            shape = CardDefaults.elevatedShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ),
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.dashboard_main_image),
                contentDescription = "DashBoard Main Image",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(id = R.string.app_description),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onTertiary,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
            )
        }

        Text(
            text = "ADDED BIKES",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onTertiary)
        ) {
            items(bikes.size) { index ->
                BikeCard(bike = bikes[index], onDeleteClick = onDeleteClick)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BikeCard(
    bike: Bike,
    bikeViewModel: BikeViewModel = hiltViewModel(),
    onDeleteClick: (Bike) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val uiState by bikeViewModel.uiStateBike.collectAsState()
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showPopup by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var isSheetVisible by rememberSaveable { mutableStateOf(false) }

    val galleryPhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImage = uri
            uri?.let {
                bitmap = loadBitmapFromUri(context, it)?.let { originalBitmap ->
                    resizeBitmap(originalBitmap, 500, 500)
                }
            }
        }
    )
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { takenBitmap ->
            takenBitmap?.let {
                bitmap = resizeBitmap(it, 500, 500)
            }
        }
    )
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                cameraLauncher.launch(null)
            } else {
                Log.e("Permission", "Camera permission denied")
            }
        }
    )
    LaunchedEffect(bitmap) {
        bitmap?.let {
            isSheetVisible = true
            bikeViewModel.bikeErrorSolver(bike, bitmap!!)
        }
    }

    Box(
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Card(
            shape = CardDefaults.outlinedShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300
                        )
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        state = rememberTooltipState(),
                        tooltip = {
                            PlainTooltip {
                                Text(text = "Bike Details")
                            }
                        }
                    ) {
                        IconButton(
                            onClick = { expanded = !expanded },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Info"
                            )
                        }
                    }
                    Text(
                        text = bike.customName ?: "${bike.make} ${bike.model}",
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    IconButton(
                        onClick = { onDeleteClick(bike) },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete Bike"
                        )
                    }
                }

                if (expanded) {
                    Image(
                        painter = painterResource(id = R.drawable.dashboard__logo),
                        contentDescription = "DashBoard Bike Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Make: ${bike.make}",
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = "Model: ${bike.model}",
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = "Year: ${bike.year}",
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { showPopup = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(stringResource(id = R.string.button_text))
                }
            }
        }
        if (isSheetVisible) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    selectedImage = null
                    isSheetVisible = false
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gemini_icon),
                    contentDescription = "Ai ICON",
                    alignment = Alignment.TopStart,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(80.dp)
                )
                when (val currentState = uiState) {
                    is UiState.Initial -> {
                        isSheetVisible = false
                    }

                    is UiState.Loading -> {
                        LoadingState(message = "Gemini Is Responding", modifier = Modifier.fillMaxSize())
                    }

                    is UiState.Error -> {
                        Text(
                            text = currentState.errorMessage,
                            textAlign = TextAlign.Start,
                            fontFamily = FontFamily.Default,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(16.dp)
                        )
                    }

                    is UiState.Success -> {
                        val scrollState = rememberScrollState()
                        Text(
                            text = currentState.outputText,
                            textAlign = TextAlign.Start,
                            fontFamily = FontFamily.Default,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(16.dp)
                                .verticalScroll(scrollState)
                        )
                    }
                    else -> {
                        isSheetVisible = false
                    }
                }
            }
        }
    }
    if (showPopup) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.5f))
        ) {
            ImagePickerPopup(
                onGalleryClick = {
                    galleryPhotoPicker.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                onCameraClick = {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                },
                onDismiss = { showPopup = false }
            )
        }
    }
}


@Composable
fun ImagePickerPopup(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    DialogBackground {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = CardDefaults.outlinedShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CHOOSE IMAGE FROM",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "GALLERY",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily.Default,
                        modifier = Modifier
                            .clickable {
                                onGalleryClick()
                                onDismiss()
                            }
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "CAMERA",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily.Default,
                        modifier = Modifier
                            .clickable {
                                onCameraClick()
                                onDismiss()
                            }
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}


@Composable
fun DeleteConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.5f))
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Delete Bike",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete this bike?",
                    fontSize = 18.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(
                        "Yes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(
                        "No",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            },
            shape = RoundedCornerShape(8.dp),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            textContentColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .fillMaxWidth() // Padding to ensure full width
        )
    }
}

fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(bitmap, width, height, true)
}

fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CardPreview() {
    val dummyBike = Bike(
        make = "Bosch",
        model = "Performance Line CX",
        year = "2022",
        customName = "Speedster"
    )
    BikeCard(
        bike = dummyBike,
        onDeleteClick = {},
        modifier = Modifier.fillMaxSize()
    )
}


@Preview
@Composable
fun MainLayoutPreview() {
    DashBoardLayout(
        navController = rememberNavController(),
        bikeViewModel = BikeViewModel(LocalContext.current,DataStoreManager(LocalContext.current))
    )
}
