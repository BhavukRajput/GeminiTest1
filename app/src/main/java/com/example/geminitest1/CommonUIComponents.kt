package com.example.geminitest1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    navController: NavController?,
    showBackArrow: Boolean = false,
    onHomeScreen: Boolean = false,
    color: Color,
    textColor: Color
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = TopAppBarDefaults.topAppBarColors(color),
        title = {
            Layout(
                content = {
                    if (showBackArrow) {
                        IconButton(onClick = { navController?.popBackStack() }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back_black),
                                contentDescription = "Back",
                                tint = textColor
                            )
                        }
                    }
                    Text(
                        text = title,
                        maxLines = 1,
                        fontSize = 24.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = textColor,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    if (onHomeScreen) {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu Icon",
                                tint = textColor
                            )
                        }
                    }
                }
            ) { measurables, constraints ->
                // Check the number of elements in measurable
                val navigationIconPlaceable = if (showBackArrow) measurables.getOrNull(0)?.measure(constraints) else null
                val titlePlaceable = measurables.getOrNull(if (showBackArrow) 1 else 0)?.measure(constraints)
                val actionIconPlaceable = if (onHomeScreen) measurables.getOrNull(if (showBackArrow) 2 else 1)?.measure(constraints) else null

                layout(constraints.maxWidth, constraints.maxHeight) {
                    val navigationIconWidth = navigationIconPlaceable?.width ?: 0
                    val actionIconWidth = actionIconPlaceable?.width ?: 0

                    navigationIconPlaceable?.placeRelative(0, (constraints.maxHeight - navigationIconPlaceable.height) / 2)
                    titlePlaceable?.placeRelative(
                        x = (constraints.maxWidth - titlePlaceable.width) / 2,
                        y = (constraints.maxHeight - titlePlaceable.height) / 2
                    )
                    actionIconPlaceable?.placeRelative(
                        x = constraints.maxWidth - actionIconWidth,
                        y = (constraints.maxHeight - actionIconPlaceable.height) / 2
                    )
                }
            }
        }
    )
}

@Composable
fun DialogBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        content()
    }
}

@Composable
fun LoadingState(message: String,modifier: Modifier=Modifier) {
    var dotCount by remember { mutableIntStateOf(1) }
    val maxDots = 3

    LaunchedEffect(key1 = dotCount) {
        delay(200)
        dotCount = (dotCount % maxDots) + 1
    }

    val dots = ".".repeat(dotCount)
    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        Text(
            text = "$message$dots",
            fontSize = 18.sp,
            fontFamily = FontFamily.SansSerif,
            color = MaterialTheme.colorScheme.onTertiary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    AppBar(title = "Preview", navController = null,
        showBackArrow = true,
        onHomeScreen = true,
        color = MaterialTheme.colorScheme.tertiary,
        textColor = MaterialTheme.colorScheme.onTertiary)
}
