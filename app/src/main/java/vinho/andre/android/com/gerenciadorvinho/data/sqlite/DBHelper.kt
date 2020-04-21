package vinho.andre.android.com.gerenciadorvinho.data.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import vinho.andre.android.com.gerenciadorvinho.domain.Comment
import vinho.andre.android.com.gerenciadorvinho.domain.Purchase
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.domain.WineComplement
import java.util.*

class DBHelper(
    context: Context
) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_VERSION: Int = 1
        private const val DATABASE_NAME: String = "MioVini.db"
    }

    private val tableWine: String = "wine"
    private val tableComments: String = "comments"
    private val tablePurchase: String = "purchase"
    private val tableWineComplement: String = "wineComplement"

    private val wineColumns: Array<String> = arrayOf(
        "id_wine",
        "name",
        "country",
        "imageName",
        "imageUrl",
        "vintage",
        "wineHouse",
        "bookmark",
        "rating"
    )

    private val purchaseColumns: Array<String> = arrayOf(
        "id_purchase",
        "id_wine",
        "vintage",
        "aumount",
        "price",
        "date",
        "store",
        "commnet"
    )

    private val commentsColumns: Array<String> = arrayOf(
        "id_comment",
        "id_wine",
        "date",
        "comment"
    )

    private val wineComplementColumns: Array<String> = arrayOf(
        "id_wineComplement",
        "wine",
        "grape",
        "harmonization",
        "temperature",
        "producer",
        "dateWineHouse"
    )

    override fun onCreate(
        db: SQLiteDatabase?
    ) {
        db?.execSQL(
            "create table $tableWine (" +
                    "${wineColumns[0]} TEXT PRIMARY KEY," +
                    "${wineColumns[1]} TEXT," +
                    "${wineColumns[2]} TEXT," +
                    "${wineColumns[3]} TEXT," +
                    "${wineColumns[4]} TEXT," +
                    "${wineColumns[5]} INT," +
                    "${wineColumns[6]} INT," +
                    "${wineColumns[7]} INT," +
                    "${wineColumns[8]} FLOAT);"
        )

        db?.execSQL(
            "create table $tableWineComplement (" +
                    "${wineComplementColumns[0]} TEXT PRIMARY KEY," +
                    "${wineComplementColumns[1]} TEXT," +
                    "${wineComplementColumns[2]} TEXT," +
                    "${wineComplementColumns[3]} TEXT," +
                    "${wineComplementColumns[4]} INT," +
                    "${wineComplementColumns[5]} TEXT," +
                    "${wineComplementColumns[6]} DATE," +
                    "FOREIGN KEY(${wineComplementColumns[1]}) REFERENCES $tableWine(${wineColumns[0]}));"
        )

        db?.execSQL(
            "create table $tableComments (" +
                    "${commentsColumns[0]} TEXT PRIMARY KEY," +
                    "${commentsColumns[1]} TEXT," +
                    "${commentsColumns[2]} DATE," +
                    "${commentsColumns[3]} TEXT," +
                    "FOREIGN KEY(${commentsColumns[1]}) REFERENCES $tableWine(${wineColumns[0]}));"
        )

        db?.execSQL(
            "create table $tablePurchase (" +
                    "${purchaseColumns[0]} TEXT PRIMARY KEY," +
                    "${purchaseColumns[1]} TEXT," +
                    "${purchaseColumns[2]} INT," +
                    "${purchaseColumns[3]} INT," +
                    "${purchaseColumns[4]} DOUBLE," +
                    "${purchaseColumns[5]} DATE," +
                    "${purchaseColumns[6]} TEXT," +
                    "${purchaseColumns[7]} TEXT," +
                    "FOREIGN KEY(${purchaseColumns[1]}) REFERENCES $tableWine(${wineColumns[0]}));"
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("drop table if exists $tableWine")
        db?.execSQL("drop table if exists $tablePurchase")
        db?.execSQL("drop table if exists $tableComments")
        db?.execSQL("drop table if exists $tableWineComplement")
        onCreate(db)
    }

    fun saveWine(
        wine: Wine
    ) {
        val db: SQLiteDatabase = this.writableDatabase
        db.insert(tableWine, null, valuesWine(wine))
        db.close()
    }

    fun saveWineComplement(
        idWine: String,
        wineComplement: WineComplement
    ) {
        val db: SQLiteDatabase = this.writableDatabase
        db.insert(
            tableWineComplement,
            null,
            valuesWineComplement(
                idWine,
                wineComplement
            )
        )
        db.close()
    }

    fun savePurchase(
        idWine: String,
        purchase: Purchase
    ) {
        val db: SQLiteDatabase = this.writableDatabase
        db.insert(
            tablePurchase,
            null,
            valuesPurchase(
                idWine,
                purchase
            )
        )
        db.close()
    }

    fun saveComment(
        idWine: String,
        comment: Comment
    ) {
        val db: SQLiteDatabase = this.writableDatabase
        db.insert(tableComments, null, valuesComments(idWine, comment))
        db.close()
    }

    fun updateWine(
        wine: Wine
    ) {
        if (hasWine(wine.wineId)) {
            val db: SQLiteDatabase = this.writableDatabase
            db.update(
                tableWine,
                valuesWine(wine),
                "${wineColumns[0]} = '" + wine.wineId + "'",
                null
            )
            db.close()
        }
    }

    fun updateDateWineHouse(
        date: Date,
        idwineComplement: String
    ) {
        if (hasWineComplement(idwineComplement)) {
            val db: SQLiteDatabase = this.writableDatabase

            val values = ContentValues()
            values.put(wineComplementColumns[6], date.toString())

            db.update(
                tableWineComplement,
                values,
                "${wineComplementColumns[0]} = '" + idwineComplement + "'",
                null
            )
            db.close()
        }
    }

    fun updateWineHouse(
        idWine: String,
        wineHouse: Int
    ) {
        if (hasWine(idWine)) {
            val db: SQLiteDatabase = this.writableDatabase

            val values = ContentValues()
            values.put(wineColumns[6], wineHouse)

            db.update(
                tableWine,
                values,
                "${wineColumns[0]} = '" + idWine + "'",
                null
            )
            db.close()
        }
    }

    fun updateWineComplement(
        idWine: String,
        wineComplement: WineComplement
    ) {
        if (hasWineComplement(wineComplement.wineComplementId)) {
            val db: SQLiteDatabase = this.writableDatabase
            db.update(
                tableWineComplement,
                valuesWineComplement(
                    idWine,
                    wineComplement
                ),
                "${wineComplementColumns[0]} = '" + wineComplement.wineComplementId + "'",
                null
            )
            db.close()
        }
    }

    fun updateBookmark(
        idWine: String,
        bookmark: Boolean
    ) {
        if (hasWine(idWine)) {
            val db: SQLiteDatabase = this.writableDatabase

            val values = ContentValues()
            values.put(
                wineColumns[7],
                if (bookmark) {
                    1
                } else {
                    0
                }
            )

            db.update(
                tableWine,
                values,
                "${wineColumns[0]} = '" + idWine + "'",
                null
            )
            db.close()
        }
    }

    fun updatePurchase(
        idWine: String,
        purchase: Purchase
    ) {
        if (hasPurchase(purchase.purchaseId)) {
            val db = this.writableDatabase
            db.update(
                tablePurchase,
                valuesPurchase(
                    idWine,
                    purchase
                ),
                "${purchaseColumns[0]} = '" + purchase.purchaseId + "'", null
            )
            db.close()
        }
    }

    fun updateComment(
        idWine: String,
        comment: Comment
    ) {
        if (hasComment(comment.commentId)) {
            val db = this.writableDatabase
            db.update(
                tableComments,
                valuesComments(
                    idWine,
                    comment
                ),
                "${commentsColumns[0]} = '" + comment.commentId + "'", null
            )
            db.close()
        }
    }

    fun deleteAllWines() {
        val db = this.writableDatabase
        db.delete(
            tableWine,
            null,
            null
        )
        db.close()
    }

    fun deleteWine(
        idWine: String
    ) {
        if (hasWine(idWine)) {
            val db = this.writableDatabase
            db.delete(
                tableWine,
                "${wineColumns[0]} = '$idWine'",
                null
            )
            db.close()
        }
    }

    fun deleteAllComments() {
        val db = this.writableDatabase
        db.delete(
            tableComments,
            null,
            null
        )
        db.close()
    }

    fun deleteComment(
        idComment: String
    ) {
        if (hasComment(idComment)) {
            val db = this.writableDatabase
            db.execSQL("DELETE FROM $tableComments WHERE ${commentsColumns[0]} = '$idComment'")
            db.close()
        }
    }

    fun deleteAllWineComment(
        idWine: String
    ) {
        val db = this.writableDatabase
        db.delete(
            tableComments,
            "${commentsColumns[1]} = '$idWine'",
            null
        )
        db.close()
    }

    fun deleteAllPurchase() {
        val db = this.writableDatabase
        db.delete(
            tablePurchase,
            null,
            null
        )
        db.close()
    }

    fun deleteAllWinePurchases(
        idWine: String
    ) {
        val db = this.writableDatabase
        db.delete(
            tablePurchase,
            "${purchaseColumns[1]} = '$idWine'",
            null
        )
        db.close()
    }

    fun deletePurchase(
        idPurchase: String
    ) {
        if (hasPurchase(idPurchase)) {
            val db = this.writableDatabase
            db.delete(
                tablePurchase,
                "${purchaseColumns[0]} = '$idPurchase'",
                null
            )
            db.close()
        }
    }

    fun deleteAllWineComplement() {
        val db = this.writableDatabase
        db.delete(
            tableWineComplement,
            null,
            null
        )
        db.close()
    }

    fun deleteWineComplement(
        idWine: String
    ) {
        if (hasWineComplement(idWine)) {
            val db = this.writableDatabase
            db.delete(
                tableWineComplement,
                "${wineComplementColumns[1]} = '" + idWine + "'",
                null
            )
            db.close()
        }
    }

    fun search(
        searchquery: String
    ): List<Wine> {
        val db = this.readableDatabase
        val listWine = ArrayList<Wine>()

        val query =
            "SELECT w.${wineColumns[0]}, w.${wineColumns[1]}, w.${wineColumns[2]}, w.${wineColumns[3]}, w.${wineColumns[4]}, w.${wineColumns[5]}, w.${wineColumns[6]}, w.${wineColumns[7]}, w.${wineColumns[8]} " +
                    "from $tableWine as w " +
                    "LEFT JOIN $tableWineComplement as wc on w.${wineColumns[0]} == wc.${wineComplementColumns[1]} " +
                    "LEFT JOIN $tableComments as c on w.${wineColumns[0]} == c.${commentsColumns[1]} " +
                    "WHERE " +
                    "w.${wineColumns[2]} like '%$searchquery%' or " +
                    "w.${wineColumns[1]} like '%$searchquery%' or " +
                    "c.${commentsColumns[3]} like '%$searchquery%' or " +
                    "wc.${wineComplementColumns[3]} like '%$searchquery%' " +
                    "GROUP BY " +
                    "w.${wineColumns[0]} " +
                    "ORDER BY " +
                    "w.${wineColumns[1]}"

        val cursor = db.rawQuery(query, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val wine = Wine()
                    wine.wineId = cursor.getString(cursor.getColumnIndex(wineColumns[0]))
                    wine.name = cursor.getString(cursor.getColumnIndex(wineColumns[1]))
                    wine.image = mapOf(
                        Wine.NameImage to cursor.getString(cursor.getColumnIndex(wineColumns[3])),
                        Wine.UrlImage to cursor.getString(cursor.getColumnIndex(wineColumns[4]))
                    )
                    wine.rating = cursor.getFloat(cursor.getColumnIndex(wineColumns[8]))
                    wine.wineHouse = cursor.getInt(cursor.getColumnIndex(wineColumns[6]))
                    wine.country = cursor.getString(cursor.getColumnIndex(wineColumns[2]))
                    wine.vintage = cursor.getInt(cursor.getColumnIndex(wineColumns[5]))
                    wine.bookmark = cursor.getInt(cursor.getColumnIndex(wineColumns[7])) == 1
                    listWine.add(wine)
                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()
        return listWine
    }

    fun getVintage(
        idWine: String
    ): List<String> {
        val db = this.readableDatabase
        val listVintage = ArrayList<String>()

        val query =
            "SELECT ${purchaseColumns[2]} " +
                    "FROM $tablePurchase " +
                    "where ${wineColumns[0]} = '$idWine' " +
                    "GROUP BY ${purchaseColumns[2]} " +
                    "ORDER BY ${purchaseColumns[2]};"

        val cursor = db.rawQuery(query, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    listVintage.add(
                        cursor.getInt(cursor.getColumnIndex(purchaseColumns[2])).toString()
                    )
                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()
        return listVintage
    }

    fun hasWine(
        idWine: String
    ): Boolean {
        val db = this.readableDatabase
        val query = "SELECT ${wineColumns[0]} from $tableWine where ${wineColumns[0]} = '$idWine'"
        val cursor = db.rawQuery(query, null)
        val hasRegister = cursor.count > 0
        cursor.close()
        db.close()
        return hasRegister
    }

    private fun hasWineComplement(
        idWineComplement: String
    ): Boolean {
        val db = this.readableDatabase
        val query =
            "SELECT * from $tableWineComplement where ${wineComplementColumns[0]} = '$idWineComplement' or ${wineComplementColumns[1]} = '$idWineComplement'"
        val cursor = db.rawQuery(query, null)
        val hasRegister = cursor.count > 0
        cursor.close()
        db.close()
        return hasRegister
    }

    fun hasPurchase(
        idPurchase: String
    ): Boolean {
        val db = this.readableDatabase
        val query =
            "SELECT ${purchaseColumns[0]} from $tablePurchase where ${purchaseColumns[0]} = '$idPurchase'"
        val cursor = db.rawQuery(query, null)
        val hasRegister = cursor.count > 0
        cursor.close()
        db.close()
        return hasRegister
    }

    fun hasComment(
        idComment: String
    ): Boolean {
        val db = this.readableDatabase
        val query =
            "SELECT ${commentsColumns[0]} from $tableComments where ${commentsColumns[0]} = '$idComment'"
        val cursor = db.rawQuery(query, null)
        val hasRegister = cursor.count > 0
        cursor.close()
        db.close()
        return hasRegister
    }

    fun getSumVintage(): Int {
        val db = this.readableDatabase

        val query = "SELECT SUM(${wineColumns[6]}) FROM $tableWine;"
        var sum = 0

        val cursor = db.rawQuery(query, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    sum = cursor.getInt(0)
                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()
        return sum
    }

    private fun valuesWine(
        wine: Wine
    ): ContentValues {
        val values = ContentValues()
        values.put(wineColumns[0], wine.wineId)
        values.put(wineColumns[1], wine.name)
        values.put(wineColumns[2], wine.country)
        values.put(wineColumns[3], wine.image[Wine.NameImage])
        values.put(wineColumns[4], wine.image[Wine.UrlImage])
        values.put(wineColumns[5], wine.vintage)
        values.put(wineColumns[6], wine.wineHouse)
        values.put(
            wineColumns[7],
            if (wine.bookmark) {
                1
            } else {
                0
            }
        )
        values.put(wineColumns[8], wine.rating)
        return values
    }

    private fun valuesWineComplement(
        idWine: String,
        wineComplement: WineComplement
    ): ContentValues {
        val values = ContentValues()
        values.put(wineComplementColumns[0], wineComplement.wineComplementId)
        values.put(wineComplementColumns[1], idWine)
        values.put(wineComplementColumns[2], wineComplement.grape)
        values.put(wineComplementColumns[3], wineComplement.harmonization)
        values.put(wineComplementColumns[4], wineComplement.temperature)
        values.put(wineComplementColumns[5], wineComplement.producer)
        values.put(wineComplementColumns[6], wineComplement.dateWineHouse.toString())
        return values
    }

    private fun valuesPurchase(
        idWine: String,
        purchase: Purchase
    ): ContentValues {
        val values = ContentValues()
        values.put(purchaseColumns[0], purchase.purchaseId)
        values.put(purchaseColumns[1], idWine)
        values.put(purchaseColumns[2], purchase.vintage)
        values.put(purchaseColumns[3], purchase.amount)
        values.put(purchaseColumns[4], purchase.price)
        values.put(purchaseColumns[5], purchase.date.toString())
        values.put(purchaseColumns[6], purchase.store)
        values.put(purchaseColumns[7], purchase.comment)
        return values
    }

    private fun valuesComments(
        idWine: String,
        comment: Comment
    ): ContentValues {
        val values = ContentValues()
        values.put(commentsColumns[0], comment.commentId)
        values.put(commentsColumns[1], idWine)
        values.put(commentsColumns[2], comment.date.toString())
        values.put(commentsColumns[3], comment.comment)
        return values
    }
}