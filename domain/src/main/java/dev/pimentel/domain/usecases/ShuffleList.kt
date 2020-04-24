package dev.pimentel.domain.usecases

class ShuffleList {

    operator fun <T> invoke(params: Params<T>): List<T> = params.list.shuffled()

    data class Params<T>(
        val list: List<T>
    )
}
