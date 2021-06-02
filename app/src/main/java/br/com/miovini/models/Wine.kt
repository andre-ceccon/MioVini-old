package br.com.miovini.models

import io.github.serpro69.kfaker.Faker

data class Wine(
    val wineId: Int,
    val name: String,
    val rating: Float,
    val country: String,
    val vintage: String,
    val pahtImage: String,
    val wineHouse: String,
    val bookmark: Boolean
)

fun genetareWineList(): List<Wine> {
    val tempList: MutableList<Wine> = mutableListOf()
    val faker = Faker()
    val list = listOf(true, false)
    val adega = listOf("0", "5", "10", "999")
    val safras = listOf("2008", "2010", "2021", "2015", "1999")

    while (tempList.size <= 10) {
        tempList.add(
            Wine(
                pahtImage = "",
                wineId = tempList.size,
                bookmark = list.random(),
                vintage = safras.random(),
                wineHouse = adega.random(),
                country = faker.address.country(),
                rating = tempList.size.toFloat() / 2,
                name = faker.backToTheFuture.quotes()
            )
        )
    }

    return tempList
}
