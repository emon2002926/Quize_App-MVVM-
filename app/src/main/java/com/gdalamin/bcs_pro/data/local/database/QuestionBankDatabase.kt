package com.gdalamin.bcs_pro.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gdalamin.bcs_pro.data.local.dao.QuestionBankDao
import com.gdalamin.bcs_pro.data.model.BcsYearName

@Database(entities = [BcsYearName::class], version = 1)
abstract class QuestionBankDatabase : RoomDatabase() {
    abstract fun questionBankDao(): QuestionBankDao

//    companion object {
//        @Volatile
//        private var INSTANCE: QuestionBankDatabase? = null
//
//        fun getDatabase(context: Context): QuestionBankDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    QuestionBankDatabase::class.java,
//                    "bcs_year_name_database_one"
//                ).fallbackToDestructiveMigration().build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}
