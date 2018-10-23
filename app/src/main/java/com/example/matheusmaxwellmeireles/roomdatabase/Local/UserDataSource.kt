package com.example.matheusmaxwellmeireles.roomdatabase.Local

import com.example.matheusmaxwellmeireles.roomdatabase.Database.IUserDataSource
import com.example.matheusmaxwellmeireles.roomdatabase.Model.User
import io.reactivex.Flowable


class UserDataSource(private val userDAO: UserDAO):IUserDataSource{
    override val allUsers: Flowable<List<User>>
        get() = userDAO.allUsers

    override fun getUserById(userId: Int): Flowable<User> {
        return userDAO.getUserById(userId)
    }

    override fun insertUser(vararg users: User) {
        userDAO.insertUser(*users)
    }

    override fun updateUser(vararg users: User) {
        userDAO.updateUser(*users)
    }

    override fun deleteUser(user: User) {
        userDAO.deleteUser(user)
    }

    override fun deleteAllUsers() {
        userDAO.deleteAllUsers()
    }

    companion object {
        private var mInstance : UserDataSource?=null
        fun getInstance(userDAO: UserDAO): UserDataSource {
            if(mInstance == null){
                mInstance = UserDataSource(userDAO)
            }
            return mInstance!!
        }
    }


}