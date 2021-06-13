package br.com.miovini.models

data class Comment(
    val data: String,
    val comment: String
)

fun genetareCommentist(): List<Comment> {
    val tempList: MutableList<Comment> = mutableListOf()
    while (tempList.size <= 5) {
        tempList.add(
            Comment(data = "21/02/2020", comment = "Bom Vinho!")
        )
    }

    return tempList
}