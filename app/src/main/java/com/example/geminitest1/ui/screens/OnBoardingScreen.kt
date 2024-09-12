package com.example.geminitest1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.geminitest1.AppBar
import com.example.geminitest1.AppConstants
import com.example.geminitest1.R

@Composable
fun OnBoardingScreenLayout(
    navController: NavController,
    modifier: Modifier = Modifier
){
    Column {
        AppBar(
            title = stringResource(id = R.string.app_title),
            navController =navController,
            color = MaterialTheme.colorScheme.tertiary,
            textColor = MaterialTheme.colorScheme.onTertiary
        )
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiary)
                .fillMaxSize()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.onboard_logo),
                contentScale = ContentScale.Crop,
                contentDescription ="onBoardLogo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape))
            
            Text(text = stringResource(id = R.string.onBoarding_title),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Cursive,
                modifier = Modifier.padding(top = 20.dp))

            Text(text = stringResource(id = R.string.app_function_explained),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 10.dp))

            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally) {
                AICard()

                ExtendedFloatingActionButton(onClick = {
                    navController.navigate(AppConstants.Screens.ADDBIKESCREEN)
                },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.padding(top = 80.dp, bottom = 20.dp)) {
                    Text(text = stringResource(id = R.string.add_bike_button),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif)
                    Icon(imageVector = Icons.Default.Add , contentDescription ="Add_Bike" )
                }
            }
        }
    }
}

@Composable
fun AICard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(top = 100.dp, end = 16.dp, start = 16.dp, bottom = 10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(5.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.gemini_icon),
                    contentDescription = "AI Icon",
                    tint = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.card_ai_description),
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = {
                    0.5f
                },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.errorContainer,
            )
            Text(
                text = "Get diagnosed in under 30 seconds",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnBoardingScreenLayoutPreview(){
    OnBoardingScreenLayout(navController = rememberNavController())
}

@Preview
@Composable
fun AICardPreview(){
    AICard()
}