package dev.pimentel.chucknorris.shared.mvi

open class Event<T>(private val content: T) {

    private var hasBeenHandled = false

    val value: T?
        get() {
            return if (hasBeenHandled) {
                null
            } else {
                hasBeenHandled = true
                content
            }
        }

    object NoContent : Event<Unit>(Unit)
}

fun <T> T.toEvent() = Event(this)