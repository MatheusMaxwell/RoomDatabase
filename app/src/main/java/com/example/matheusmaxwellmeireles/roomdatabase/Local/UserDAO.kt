package com.example.matheusmaxwellmeireles.roomdatabase.Local

import android.arch.persistence.room.*
import com.example.matheusmaxwellmeireles.roomdatabase.Model.User
import io.reactivex.Flowable

@Dao
interface UserDAO {

    @get:Query(value = "SELECT * FROM users")
    val allUsers: Flowable<List<User>>

    @Query(value = "SELECT * FROM users WHERE id=:userId")
    fun getUserById(userId: Int): Flowable<User>

    @Insert
    fun insertUser(vararg users: User)

    @Update
    fun updateUser(vararg users: User)

    @Delete
    fun deleteUser(user: User)

    @Query(value = "DELETE FROM users")
    fun deleteAllUsers()

}