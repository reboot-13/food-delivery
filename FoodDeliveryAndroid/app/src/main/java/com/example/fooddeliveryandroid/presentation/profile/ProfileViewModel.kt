package com.example.fooddeliveryandroid.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.remote.dto.request.AuthRequest
import com.example.fooddeliveryandroid.data.remote.dto.request.RegisterRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.ProfileRepository
import com.example.fooddeliveryandroid.datastore.SessionManager
import com.example.fooddeliveryandroid.domain.validation.AuthValidator
import com.example.fooddeliveryandroid.domain.validation.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (
    private val profileRepository: ProfileRepository,
    private val sessionManager: SessionManager,
): ViewModel(){
    private val _uiState = MutableStateFlow<ProfileUIState>(ProfileUIState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _validationState =
        MutableStateFlow(ProfileValidationState())
    val validationState = _validationState.asStateFlow()

    private val validator = AuthValidator()

    init {
            viewModelScope.launch {
            if (!sessionManager.hasToken()) {
                _uiState.value = ProfileUIState.UnauthorizedRegister
            }

            else {
                loadUserData()
            }
        }
    }

    fun showAuthForm() {
        _uiState.value = ProfileUIState.UnauthorizedAuth
        _validationState.value = ProfileValidationState()
    }

    fun showRegisterForm() {
        _uiState.value = ProfileUIState.UnauthorizedRegister
        _validationState.value = ProfileValidationState()

    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearToken()
            _uiState.value = ProfileUIState.UnauthorizedAuth
        }
    }

    fun auth(authRequest: AuthRequest) {
        val validation = validateAuth(authRequest)
        _validationState.value = validation
        Log.d("Auth request", "ads")
        if (!validation.isValid) return
        viewModelScope.launch {
            Log.d("Auth request", "ads")
            when (val authResult = profileRepository.auth(authRequest)) {
                is NetworkResult.Error ->
                    _uiState.value = ProfileUIState.Error(authResult.exception.message ?: "Неизвестная ошибка")
                is NetworkResult.Success ->
                    loadUserData()
            }
        }
    }

    private fun validateAuth(authRequest: AuthRequest): ProfileValidationState {
        with(authRequest) {
            return ProfileValidationState(
                phoneNumber = (validator.validatePhone(phoneNumber) as? ValidationResult.Error)?.message,
                password = (validator.validatePassword(password) as? ValidationResult.Error)?.message
            )
        }
    }

    fun register(registerRequest: RegisterRequest){
        val validation = validateRegister(registerRequest)
        _validationState.value = validation
        if (!validation.isValid) return

        viewModelScope.launch {
            when (
                val registerResult = profileRepository.register(registerRequest)
            ) {
                is NetworkResult.Error ->
                    _uiState.value = ProfileUIState.Error(registerResult.exception.message ?: "Неизвестная ошибка")
                is NetworkResult.Success ->
                    loadUserData()

            }
        }
    }
    private fun validateRegister(registerRequest: RegisterRequest): ProfileValidationState {
        with(registerRequest) {
            return ProfileValidationState(
                name = (validator.validateName(name) as? ValidationResult.Error)?.message,
                phoneNumber = (validator.validatePhone(phoneNumber) as? ValidationResult.Error)?.message,
                password = (validator.validatePassword(password) as? ValidationResult.Error)?.message
            )
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            when (val userResult = profileRepository.getCurrentUser()) {
                is NetworkResult.Error ->
                    _uiState.value = ProfileUIState.Error(userResult.exception.message ?: "Неизвестная ошибка")
                is NetworkResult.Success ->
                    _uiState.value = ProfileUIState.Authorized(userResult.data)
            }
        }
    }
}