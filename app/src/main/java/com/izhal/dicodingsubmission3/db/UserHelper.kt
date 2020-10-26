package com.izhal.dicodingsubmission3.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.izhal.dicodingsubmission3.db.DatabaseContract.UserColumns.Companion.LOGIN
import com.izhal.dicodingsubmission3.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.izhal.dicodingsubmission3.db.DatabaseContract.UserColumns.Companion._ID
import java.sql.SQLException

class UserHelper(context: Context) {
  companion object {
    private const val DB_TABLE = TABLE_NAME
    private lateinit var dbHelper: DatabaseContract.DatabaseHelper
    private lateinit var db: SQLiteDatabase

    private var INSTANCE: UserHelper? = null
    fun getInstance(context: Context): UserHelper = INSTANCE ?: synchronized(this) {
      INSTANCE ?: UserHelper(context)
    }
  }

  init {
    dbHelper = DatabaseContract.DatabaseHelper(context)
  }

  @Throws(SQLException::class)
  fun open() {
    db = dbHelper.writableDatabase
  }

  fun isOpen(): Boolean {
    return db.isOpen()
  }

  fun close() {
    dbHelper.close()

    if (db.isOpen) {
      db.close()
    }
  }

  fun getAll(): Cursor {
    return db.query(
      DB_TABLE,
      null,
      null,
      null,
      null,
      null,
      "$_ID ASC"
    )
  }

  fun getById(id: String): Cursor {
    return db.query(
      DB_TABLE,
      null,
      "$_ID = ?",
      arrayOf(id),
      null,
      null,
      null,
      null
    )
  }

  fun getByLoginId(loginId: String): Cursor {
    return db.query(
      DB_TABLE,
      null,
      "$LOGIN = ?",
      arrayOf(loginId),
      null,
      null,
      null,
      null
    )
  }

  fun insert(values: ContentValues?): Long {
    return db.insert(DB_TABLE, null, values)
  }

  fun update(id: String, values: ContentValues?): Int {
    return db.update(DB_TABLE, values, "$_ID = ?", arrayOf(id))
  }

  fun delete(id: String): Int {
    return db.delete(DB_TABLE, "$_ID =  ?", arrayOf(id))
  }

  fun deleteByLogin(loginId: String): Int {
    return db.delete(DB_TABLE, "$LOGIN =  ?", arrayOf(loginId))
  }
}