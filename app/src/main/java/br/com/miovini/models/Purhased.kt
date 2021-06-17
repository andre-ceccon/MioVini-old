package br.com.miovini.models

data class Purhased(
    val id: String,
    val amount: Int,
    val price: Float,
    val vintage: String,
    val note: String,
    val storeName: String,
    val purchasedDate: Long,
)