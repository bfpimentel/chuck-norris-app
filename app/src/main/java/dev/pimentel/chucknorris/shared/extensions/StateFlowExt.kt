package dev.pimentel.chucknorris.shared.extensions

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<T>.update(block: (T) -> T) {
    value = block(value)
}
