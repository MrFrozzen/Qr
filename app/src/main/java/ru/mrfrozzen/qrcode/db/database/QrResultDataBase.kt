package ru.mrfrozzen.qrcode.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mrfrozzen.qrcode.db.dao.QrResultDao
import ru.mrfrozzen.qrcode.db.entities.QrResult
import androidx.room.Room

@Database(entities = [QrResult::class], version = 1,exportSchema = false)
abstract class QrResultDataBase : RoomDatabase() {
    abstract fun getQrDao(): QrResultDao

    companion object {
        private const val DB_NAME = "QrResultDatabase"
        private var qrResultDataBase: QrResultDataBase? = null
        fun getAppDatabase(context: Context): QrResultDataBase? {
            if (qrResultDataBase == null) {
                qrResultDataBase =
                    Room.databaseBuilder(context.applicationContext, QrResultDataBase::class.java, DB_NAME)
                        .allowMainThreadQueries()
                        .build()
            }
            return qrResultDataBase
        }

        fun destroyInstance() {
            qrResultDataBase = null
        }
    }
}