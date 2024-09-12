package com.example.geminitest1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.geminitest1.data.DataStoreManager
import com.example.geminitest1.viewmodel.BikeViewModel


@Composable
fun UserManualScreenLayout(
    bikeViewModel: BikeViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val bikes by bikeViewModel.bikes.collectAsState()
    val uiState by bikeViewModel.uiStateBike.collectAsState()
    var feedback by remember {mutableStateOf("")}

    Column(modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()) {
        Card(
            colors = CardDefaults.cardColors(
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier= Modifier
                .padding(vertical = 50.dp, horizontal = 5.dp)
                .fillMaxWidth()
                .wrapContentHeight())
        {
            Text(text = "Share Feedback",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Cursive,
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally))

            OutlinedTextField(value = feedback,
                onValueChange = {feedback=it},
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(12.dp),
                label = {Text("Feedback",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    )},
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
        Card(colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
            modifier = Modifier
                .padding(top = 100.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
                .fillMaxWidth()
                .wrapContentHeight())
        {
            Text(text = "RATE OUR APP",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 15.dp, bottom = 5.dp))

            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier=Modifier.fillMaxWidth()
                .wrapContentHeight().padding(20.dp)) {
                Icon(imageVector = Icons.Outlined.Star, contentDescription = "Star1",
                    modifier = Modifier.padding(10.dp))
                Icon(imageVector = Icons.Outlined.Star, contentDescription = "Star2",
                    modifier = Modifier.padding(10.dp))
                Icon(imageVector = Icons.Outlined.Star, contentDescription = "Star3",
                    modifier = Modifier.padding(10.dp))
                Icon(imageVector = Icons.Outlined.Star, contentDescription = "Star4",
                    modifier = Modifier.padding(10.dp))
                Icon(imageVector = Icons.Outlined.Star, contentDescription = "Star5",
                    modifier = Modifier.padding(10.dp))
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ECRScreenLayoutPreview(){
    UserManualScreenLayout(navController = rememberNavController(), bikeViewModel = BikeViewModel(
        LocalContext.current,DataStoreManager(LocalContext.current)), innerPadding = PaddingValues())
}
