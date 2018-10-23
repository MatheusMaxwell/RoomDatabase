package com.example.matheusmaxwellmeireles.roomdatabase.Local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.matheusmaxwellmeireles.roomdatabase.Local.UserDatabase.Companion.DATABASE_VERSION
import com.example.matheusmaxwellmeireles.roomdatabase.Model.User

@Database(entities = arrayOf(User::class), version = DATABASE_VERSION)
abstract class UserDatabase:RoomDatabase(){
    abstract fun userDAO(): UserDAO

    companion object {
        const val DATABASE_VERSION = 1
        val DATABASE_NAME="EDMT-Database-Room"

        private var mInstance:UserDatabase?=null

        fun getInstance(context: Context):UserDatabase{
            if(mInstance == null){
                mInstance = Room.databaseBuilder(context, UserDatabase::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigration().build()
            }
            return mInstance!!
        }


    }
}