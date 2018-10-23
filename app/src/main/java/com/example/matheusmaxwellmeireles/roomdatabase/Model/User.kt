package com.example.matheusmaxwellmeireles.roomdatabase.Model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import io.reactivex.annotations.NonNull
import java.io.Serializable


@Entity(tableName = "users")
class User: Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Int=0

    @ColumnInfo(name = "name")
    var name:String? = null

    @ColumnInfo(name = "email")
    var email:String? = null

    constructor(){}

    override fun toString(): String {

        return StringBuilder(name)
                .append("\n").append(email).toString()


    }


}