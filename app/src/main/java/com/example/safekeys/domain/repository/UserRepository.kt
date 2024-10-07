package com.example.safekeys.domain.repository

import com.example.safekeys.data.model.User

interface UserRepository {
    suspend fun insertUser(user: User)
    fun getUser(): User?

    fun isUserLoggedIn(): Boolean

    fun isUserRegistered(): Boolean

    fun setRegistration()

    fun setLogin()

    fun setLogOut()

//    fun setCount()
}