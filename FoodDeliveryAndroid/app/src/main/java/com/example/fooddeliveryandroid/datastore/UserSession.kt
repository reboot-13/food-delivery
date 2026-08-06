package com.example.fooddeliveryandroid.datastore

import com.example.fooddeliveryandroid.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor(){

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    val isAuthorized = _currentUser.map { it != null }

    fun setUser(user: User?) {
        _currentUser.value = user
    }

    fun clear (){
        _currentUser.value = null
    }

}