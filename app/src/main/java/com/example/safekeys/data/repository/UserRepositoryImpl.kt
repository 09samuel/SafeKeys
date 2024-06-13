package com.example.safekeys.data.repository

import android.content.SharedPreferences
import com.example.safekeys.data.local.UserDao
import com.example.safekeys.data.model.User
import com.example.safekeys.domain.repository.UserRepository
import com.example.safekeys.utils.SharedPreferenceHelper
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao,  private val sharedPreferences: SharedPreferenceHelper): UserRepository {
    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override fun getUser(): User? {
        return userDao.getUser()
    }

    override fun isUserLoggedIn(): Boolean {
        return sharedPreferences.isUserLoggedIn()
    }

    override fun isUserRegistered(): Boolean {
        return sharedPreferences.isUserRegistered()
    }

    override fun setRegistration() {
        sharedPreferences.setUserRegistered()
    }

    override fun setLogin() {
        sharedPreferences.setUserLogin()
    }

    override fun setLogOut() {
        return sharedPreferences.setUserLogOut()
    }

//    override fun setCount() {
//        count
//    }
}