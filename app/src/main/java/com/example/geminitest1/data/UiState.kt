package com.example.geminitest1.data

/**
 * A sealed hierarchy describing the state of the text generation.
 */
sealed interface UiState {
    /**
     * Empty state when the screen is first shown
     */
    data object Initial : UiState

    /**
     * Still loading
     */
    data object Loading : UiState

    /**
     * Text has been generated
     */
    data object SaveSuccess : UiState

    data class Success(val outputText: String) : UiState

    /**
     * There was an error generating text
     */
    data class Error(val errorMessage: String) : UiState

    data class Manual(val userManualLink: String) : UiState

}