package com.example.systemreq

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class ReqApp(
    val id: Long,
    val name: String,
    val minSdk: Int,
    val minRamMb: Int,
    val minStorageMb: Int
)

class AppDbHelper(context: Context) : SQLiteOpenHelper(context, "apps.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE apps(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                min_sdk INTEGER NOT NULL,
                min_ram_mb INTEGER NOT NULL,
                min_storage_mb INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS apps")
        onCreate(db)
    }

    fun insertApp(name: String, minSdk: Int, minRamMb: Int, minStorageMb: Int): Long {
        val cv = ContentValues()
        cv.put("name", name)
        cv.put("min_sdk", minSdk)
        cv.put("min_ram_mb", minRamMb)
        cv.put("min_storage_mb", minStorageMb)
        return writableDatabase.insert("apps", null, cv)
    }
    fun deleteAll() {
        writableDatabase.execSQL("DELETE FROM apps")
    }
    fun getAllApps(): List<ReqApp> {
        val list = ArrayList<ReqApp>()
        val c = readableDatabase.rawQuery(
            "SELECT id, name, min_sdk, min_ram_mb, min_storage_mb FROM apps ORDER BY id DESC",
            null
        )
        c.use {
            while (it.moveToNext()) {
                list.add(
                    ReqApp(
                        id = it.getLong(0),
                        name = it.getString(1),
                        minSdk = it.getInt(2),
                        minRamMb = it.getInt(3),
                        minStorageMb = it.getInt(4)
                    )
                )
            }
        }
        return list
    }

    fun seedDemoData() {

        val c = readableDatabase.rawQuery("SELECT COUNT(*) FROM apps", null)
        val count = c.use { it.moveToFirst(); it.getInt(0) }
        if (count > 0) return

        insertApp("LoL: Wild Rift", 23, 2048, 4000)
        insertApp("PUBG Mobile", 21, 3072, 5000)
        insertApp("Among Us", 21, 1024, 500)
    }
}
