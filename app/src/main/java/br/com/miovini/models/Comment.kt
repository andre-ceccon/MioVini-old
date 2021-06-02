package br.com.miovini.models

import io.github.serpro69.kfaker.Faker

data class Comment(
    val data: String,
    val comment: String
)

fun genetareCommentist(): List<Comment> {
    val tempList: MutableList<Comment> = mutableListOf()
    val faker = Faker()
    while (tempList.size <= 5) {
        tempList.add(
            Comment(
                data = faker.backToTheFuture.dates(),
                comment = faker.backToTheFuture.quotes()
            )
        )
    }

    return tempList
}