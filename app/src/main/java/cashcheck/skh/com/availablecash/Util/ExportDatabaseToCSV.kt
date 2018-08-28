package cashcheck.skh.com.availablecash.Util

import android.content.Context
import android.os.Environment
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter


/**
 * Created by Seogki on 2018. 8. 28..
 */
class ExportDatabaseToCSV(internal var context: Context) {


    fun exportDataBaseIntoCSV(date: String) {


        val db = DBHelper(context.applicationContext, "${Const.DbName}.db", null, 1)//here CredentialDb is my database. you can create your db object.
        val exportDir = File(Environment.getExternalStorageDirectory(), "")

        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "$date.csv")

        try {
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))
            val sql_db = db.readableDatabase//here create a method ,and return SQLiteDatabaseObject.getReadableDatabase();
            val curCSV = sql_db.rawQuery("SELECT * FROM ${Const.DbName}", null)
            csvWrite.writeNext(curCSV.columnNames)

            while (curCSV.moveToNext()) {
                //Which column you want to export you can add over here...
                val arrStr = arrayOf<String>(curCSV.getString(0), curCSV.getString(1), curCSV.getString(2),curCSV.getString(3),curCSV.getString(4))
                csvWrite.writeNext(arrStr)
            }

            csvWrite.close()
            curCSV.close()
        } catch (sqlEx: Exception) {
            DLog.e("Error: "+ sqlEx.message)
        }

    }
}