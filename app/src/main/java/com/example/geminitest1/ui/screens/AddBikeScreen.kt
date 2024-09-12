package com.example.geminitest1.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.geminitest1.AppBar
import com.example.geminitest1.AppConstants
import com.example.geminitest1.LoadingState
import com.example.geminitest1.R
import com.example.geminitest1.data.Bike
import com.example.geminitest1.data.DataStoreManager
import com.example.geminitest1.data.UiState
import com.example.geminitest1.viewmodel.BikeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBikeLayout(
    bikeViewModel: BikeViewModel = hiltViewModel(),
    navController: NavController,
    modifier : Modifier = Modifier
) {
    val dummyBike = Bike(
        make = "Trek",
        model = "Rail 7",
        year = "2022",
        customName = "Speedster"
    )

    val dummyBikeSecond = Bike(
        make = "Aventon",
        model = "Level",
        year = "2023",
        customName = "Aventon-E-Bike"
    )

    var make by remember { mutableStateOf(dummyBikeSecond.make) }
    var model by remember { mutableStateOf(dummyBikeSecond.model) }
    var year by remember { mutableStateOf(dummyBikeSecond.year ?: "") }
    var customName by remember { mutableStateOf(dummyBikeSecond.customName ?: "") }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val bikeUiState by bikeViewModel.uiStateBike.collectAsState()
    val validationAttempted by bikeViewModel.validationAttempted.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
            .clickable { focusManager.clearFocus() }
    ) {
        AppBar(
            title = stringResource(id = R.string.add_bike_title),
            showBackArrow = true,
            navController = navController,
            color = MaterialTheme.colorScheme.tertiary,
            textColor = MaterialTheme.colorScheme.onTertiary
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboard_logo),
                contentScale = ContentScale.Crop,
                contentDescription = "onBoardLogo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
                .verticalScroll(rememberScrollState())) {
                OutlinedTextField(
                    value = make,
                    onValueChange = { make = it },
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    ),
                    label = {
                        Text(
                            "Make",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    },
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onTertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onTertiary,
                        cursorColor = MaterialTheme.colorScheme.onTertiary,
                        focusedLabelColor = MaterialTheme.colorScheme.onTertiary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    ),
                    label = {
                        Text(
                            "Model",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    },
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onTertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onTertiary,
                        cursorColor = MaterialTheme.colorScheme.onTertiary,
                        focusedLabelColor = MaterialTheme.colorScheme.onTertiary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    ),
                    label = {
                        Text(
                            "Year(Optional)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    },
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onTertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onTertiary,
                        cursorColor = MaterialTheme.colorScheme.onTertiary,
                        focusedLabelColor = MaterialTheme.colorScheme.onTertiary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = customName,
                    onValueChange = { customName = it },
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    ),
                    label = {
                        Text(
                            "Custom Name(Optional)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    },
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onTertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onTertiary,
                        cursorColor = MaterialTheme.colorScheme.onTertiary,
                        focusedLabelColor = MaterialTheme.colorScheme.onTertiary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Button(
                    onClick = {
                        val bike = Bike(make, model, year, customName)
                        if (bikeViewModel.isInternetAvailable(context)) {
                            bikeViewModel.validateBike(bike)
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.no_internet),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onTertiary),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    Text(
                        text = "Validate Bike",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                if (validationAttempted) {
                    when (bikeUiState) {
                        is UiState.Loading -> LoadingState(message = "Gemini Is Analysing Your Bike Details")
                        is UiState.Success -> {
                            Button(
                                onClick = {
                                    val bike = Bike(make, model, year, customName)
                                    bikeViewModel.saveBike(bike)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onTertiary),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            ) {
                                Text(
                                    text = "Save Bike",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                            Text(
                                text = (bikeUiState as UiState.Success).outputText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }

                        is UiState.Error -> {
                            Text(
                                text = (bikeUiState as UiState.Error).errorMessage,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Red,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }

                        is UiState.SaveSuccess -> {
                            navController.navigate(AppConstants.Screens.DASHBOARDSCREEN) {
                                popUpTo(AppConstants.Screens.ADDBIKESCREEN) {
                                    inclusive = true
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddBikeLayoutPreview(){
    AddBikeLayout(bikeViewModel = BikeViewModel(LocalContext.current,DataStoreManager(LocalContext.current)), navController = NavController(LocalContext.current)
    )
}