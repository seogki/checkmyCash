package cashcheck.skh.com.availablecash.Util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


/**
 * Created by Seogki on 2018. 8. 8..
 */
class EstimateDBHelper(c: Context, name: String, factory: SQLiteDatabase.CursorFactory? = null, version: Int) : SQLiteOpenHelper(c, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS ${Const.DbEstimateName} (_id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, categories TEXT, money TEXT, days TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertData(date: String, cate: String, money: String, days: String) {
        val db = writableDatabase


        db.execSQL("INSERT INTO ${Const.DbEstimateName} VALUES(null, '$date', '$cate', '$money', '$days');")
        db.close()
    }

    fun deleteData(id: String) {
        val db = writableDatabase

        db.execSQL("DELETE FROM ${Const.DbEstimateName} WHERE _id=$id;")
        db.close()

    }

    fun updateData(id: String, cate: String, money: String, ill: String) {
        val db = writableDatabase
        db.execSQL("UPDATE ${Const.DbEstimateName} SET categories='$cate', money='$money', days='$ill' WHERE _id=$id")
        db.close()
    }

    fun checkTable(table: String, column: String) {
        val db = writableDatabase

        db.execSQL("ALTER TABLE $table ADD COLUMN $column TEXT;")
        db.close()
    }

    fun isColumnExists(table: String, column: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("PRAGMA table_info($table)", null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex("name"))
                if (column.equals(name, ignoreCase = true)) {
                    return true
                }
            }
        }

        return false
    }

    fun getResult(): String {
        // 읽기가 가능하게 DB 열기
        val db = readableDatabase
        var result = ""

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        val cursor = db.rawQuery("SELECT * FROM ${Const.DbEstimateName}", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0) +
                    "  날짜: " + cursor.getString(1) +
                    " 분류: " + cursor.getString(2) +
                    " " + cursor.getString(3) +
                    ":원 " + cursor.getString(4) +
                    "\n"
        }

        return result;
    }

    fun reset() {
        val db = writableDatabase
        db.execSQL("DROP TABLE IF EXISTS ${Const.DbEstimateName}")
        db.close()
        this.onCreate(db)
    }

}