package br.com.miovini.models

data class Vintage(
    val vintage: String,
    val purchaseID: String
)

fun genetareVintageist(): List<Vintage> {
    val tempList: MutableList<Vintage> = mutableListOf()
    val safras = listOf("2008", "2010", "2021", "2015", "1999")

    while (tempList.size <= 5) {
        tempList.add(Vintage(vintage = safras.random(), purchaseID = "1"))
    }

    return tempList
}
