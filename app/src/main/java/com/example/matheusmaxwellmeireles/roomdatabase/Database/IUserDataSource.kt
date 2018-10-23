package com.example.matheusmaxwellmeireles.roomdatabase.Database

import com.example.matheusmaxwellmeireles.roomdatabase.Model.User
import io.reactivex.Flowable

interface IUserDataSource {

    val allUsers:Flowable<List<User>>
    fun getUserById(userId:Int):Flowable<User>
    fun insertUser(vararg users:User)
    fun updateUser(vararg users:User)
    fun deleteUser(user:User)
    fun deleteAllUsers()

}