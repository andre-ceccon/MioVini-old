package vinho.andre.android.com.gerenciadorvinho.util.function

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun backupDatabase(
    context: Context,
    databaseName: String
) {
    try {
        val sd = Environment.getExternalStorageDirectory()
        val data = Environment.getDataDirectory()

        val packageName = context.applicationInfo.packageName

        if (sd.canWrite()) {
            val currentDBPath =
                String.format(
                    "//data//%s//databases//%s",
                    packageName,
                    databaseName
                )

            val backupDBPath = String.format(
                "debug_%s.sqlite",
                packageName
            )

            val currentDB = File(data, currentDBPath)
            val backupDB = File(sd, backupDBPath)

            if (currentDB.exists()) {
                val src = FileInputStream(currentDB).channel
                val dst = FileOutputStream(backupDB).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
            }
        }
    } catch (e: Exception) {
        throw Error(e)
    }
}