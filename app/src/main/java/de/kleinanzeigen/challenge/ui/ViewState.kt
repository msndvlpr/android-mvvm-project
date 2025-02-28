package de.kleinanzeigen.challenge.ui

/**
 * Represents the state of a view in an application, often used to model
 * loading, success, empty, or error states in a UI.
 *
 * @param T The type of data associated with a successful state.
 */
sealed class ViewState<out T> where T : Any? {

    object Loading : ViewState<Nothing>()

    object Failure : ViewState<Nothing>()

    data class Success<T>(val data: T) : ViewState<T>()
}
