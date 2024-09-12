package com.example.geminitest1.viewmodel


import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminitest1.BuildConfig
import com.example.geminitest1.data.Bike
import com.example.geminitest1.data.DataStoreManager
import com.example.geminitest1.data.ErrorCode
import com.example.geminitest1.data.UiState
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BikeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _uiStateBike: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiStateBike: StateFlow<UiState> = _uiStateBike.asStateFlow()

    private val _validationAttempted = MutableStateFlow(false)
    val validationAttempted: StateFlow<Boolean> = _validationAttempted

    private val _bikes = MutableStateFlow<List<Bike>>(emptyList())
    val bikes: StateFlow<List<Bike>> = _bikes.asStateFlow()

    private val _errorCodes = MutableStateFlow<List<ErrorCode>>(emptyList())
    val errorCodes: StateFlow<List<ErrorCode>> = _errorCodes.asStateFlow()

    private val generativeModelBikeValidation = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.bikesFlow.collect { bikeList ->
                _bikes.value = bikeList

                val jsonString = loadJsonFromAsset("error_code.json")
                val errorCodeType = object : TypeToken<List<ErrorCode>>() {}.type
                val errorCodesList: List<ErrorCode> = Gson().fromJson(jsonString, errorCodeType)
                _errorCodes.value = errorCodesList
            }
        }
    }

    private suspend fun loadJsonFromAsset(fileName: String): String = withContext(Dispatchers.IO) {
        context.assets.open(fileName).use { inputStream ->
            try {
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                String(buffer, Charsets.UTF_8)

            }catch (e: IOException){
                Log.e("BikeViewModel", "Error loading JSON file: ${e.message}")
                ""
            }
        }
    }

    fun validateBike(bike: Bike) {
        _validationAttempted.value = true
        _uiStateBike.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModelBikeValidation.generateContent(
                    content {
                        text(
                            """
                            Validate the following electric bike details:
                            Make: ${bike.make}
                            Model: ${bike.model}
                            Year: ${bike.year}
                            Is the information correct? Does an electric bike with these details exist?
                            Give answer in Yes or No
                        """.trimIndent()
                        )
                    }
                )

                response.text?.let { outputContent ->
                    if (outputContent.contains("Yes", ignoreCase = true)) {
                        _uiStateBike.value = UiState.Success(
                            outputText = "Yes, the bike exists.",
                        )
                    } else {
                        _uiStateBike.value = UiState.Error(
                            errorMessage = "Invalid Electric Bike Details"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiStateBike.value = UiState.Error(
                    errorMessage = e.localizedMessage ?: "Error validating bike"
                )
            }
        }
    }

    fun saveBike(bike: Bike) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1500)
            val currentBikes = bikes.value
            val existingBike = currentBikes.find { it.make == bike.make && it.model == bike.model && it.year == bike.year && it.customName == bike.customName }

            if (existingBike != null) {
                _uiStateBike.value = UiState.Error(
                    errorMessage = "A bike with similar details already exists."
                )
            } else {
                dataStoreManager.saveBike(bike)
                _uiStateBike.value = UiState.SaveSuccess
            }
        }
    }

    fun bikeErrorSolver(bike: Bike, bitmap: Bitmap) {
        _uiStateBike.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val errorCodesList = _errorCodes.value
                val errorCodesJson = Gson().toJson(errorCodesList)

                val response = generativeModelBikeValidation.generateContent(
                    content {
                        text(
                            """
                            How do I fix this make-${bike.make}, model-${bike.model}, year- ${bike.year}, e-bike. Error code in the screen display image? Here is the JSON data for error codes: $errorCodesJson
                            Please provide a detailed solution including:
                            1. A description of what the error code means.
                            2. Possible causes of the error.
                            3. Step-by-step troubleshooting steps to fix the error.
                            4. Any tools or parts that might be needed.
                            5. Preventive measures to avoid this error in the future.
                            6. When to consider professional help if the issue persists.
                            """.trimIndent()
                        )
                        image(bitmap)
                    }
                )

                response.text?.let { outputContent ->
                    _uiStateBike.value = UiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiStateBike.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    fun deleteBike(bike: Bike) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataStoreManager.deleteBike(bike)
            } catch (e: Exception) {
                _uiStateBike.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    fun hasBikes(): StateFlow<Boolean> = bikes.map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
