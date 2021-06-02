package br.com.miovini.models

data class Sort(
    val titleSort: String
)

fun generateSorte() = listOf(Sort("Home"))
