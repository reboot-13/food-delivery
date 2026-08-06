package com.example.fooddeliveryandroid.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.remote.dto.request.AuthRequest
import com.example.fooddeliveryandroid.data.remote.dto.request.RegisterRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.ProfileRepository
import com.example.fooddeliveryandroid.datastore.SessionManager
import com.example.fooddeliveryandroid.datastore.UserSession
import com.example.fooddeliveryandroid.domain.validation.AuthValidator
import com.example.fooddeliveryandroid.domain.validation.ValidationResult
import com.example.fooddeliveryandroid.presentation.profile.formState.AuthFormState
import com.example.fooddeliveryandroid.presentation.profile.formState.RegisterFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (
    private val profileRepository: ProfileRepository,
    private val sessionManager: SessionManager,
    private val userSession: UserSession
): ViewModel(){
    private val _uiState = MutableStateFlow<ProfileUIState>(ProfileUIState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _validationState =
        MutableStateFlow(ProfileValidationState())
    val validationState = _validationState.asStateFlow()

    private val _authFormState = MutableStateFlow(AuthFormState())
    val authFormState: StateFlow<AuthFormState> = _authFormState

    private val _registerFormState = MutableStateFlow(RegisterFormState())
    val registerFormState: StateFlow<RegisterFormState> = _registerFormState

    private val validator = AuthValidator()

    init {
        viewModelScope.launch {
            userSession.currentUser.collect { user ->
                if (user == null) {
                    _uiState.value = ProfileUIState.UnauthorizedRegister
                } else {
                    _uiState.value = ProfileUIState.Authorized(user)
                }
            }

        }
    }

    private fun clearAuthError(){
        _validationState.value = _validationState.value.copy(
            authError = null
        )
    }

    private fun clearErrorFields() {
        _validationState.value = _validationState.value.copy(
            phoneNumber = null,
            name = null,
            password = null
        )
    }

    fun showAuthForm() {
        _uiState.value = ProfileUIState.UnauthorizedAuth
        clearErrorFields()
    }

    fun showRegisterForm() {
        _uiState.value = ProfileUIState.UnauthorizedRegister
        clearErrorFields()

    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearToken()
            userSession.clear()
            clearErrorFields()
        }
    }

    fun auth() {
        val authRequest = AuthRequest(
            phoneNumber = _authFormState.value.phoneNumber,
            password = _authFormState.value.password,
            )
        val validation = validateAuth(authRequest)
        _validationState.value = validation

        if (!validation.isValid) return


        viewModelScope.launch {
            when (val authResult = profileRepository.auth(authRequest)) {
                is NetworkResult.Error -> {
                    when (authResult.code) {
                        401 -> {
                            _validationState.value =
                                _validationState.value.copy(
                                    authError = "Неверный номер телефона или пароль"
                                )
                        }

                        else -> _uiState.value = ProfileUIState.Error(
                            authResult.exception.message ?: "Неизвестная ошибка"
                        )
                    }
                }

                is NetworkResult.Success -> {
                    loadUserData()
                    cleanAuthForm()
                }
            }
        }
    }

    private fun validateAuth(authRequest: AuthRequest): ProfileValidationState {
        with(authRequest) {
            val profileValidationState =  ProfileValidationState(
                phoneNumber = (validator.validatePhone(phoneNumber) as? ValidationResult.Error)?.message,
                password = (validator.validatePassword(password) as? ValidationResult.Error)?.message
            )
            return profileValidationState
        }
    }

    fun register(){
        val registerRequest = RegisterRequest(
            name = _registerFormState.value.name,
            phoneNumber = _registerFormState.value.phoneNumber,
            password = _registerFormState.value.password,

        )
        val validation = validateRegister(registerRequest)
        _validationState.value = validation
        if (!validation.isValid) return

        viewModelScope.launch {
            when (
                val registerResult = profileRepository.register(registerRequest)
            ) {
                is NetworkResult.Error -> {
                    when(registerResult.code) {
                        401 -> {
                            _validationState.value =
                                _validationState.value.copy(registerError = "Неправильные номер телефона или пароль")
                        }
                        409 -> {
                            _validationState.value =
                                _validationState.value.copy(registerError = "Пользователь с таким номером телефона уже существует")
                        }
                        else -> _uiState.value = ProfileUIState.Error(registerResult.exception.message ?: "Неизвестная ошибка")
                    }
                }
                is NetworkResult.Success -> {
                    loadUserData()
                    cleanRegisterForm()
                }
            }
            clearErrorFields()
        }
    }

    private fun cleanRegisterForm() {
        _registerFormState.value = RegisterFormState()
    }

    private fun cleanAuthForm() {
        _authFormState.value = AuthFormState()
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

    private suspend fun loadUserData() {
        when (val userResult = profileRepository.getCurrentUser()) {
            is NetworkResult.Error ->
                _uiState.value = ProfileUIState.Error(userResult.exception.message ?: "Неизвестная ошибка")
            is NetworkResult.Success ->
                userSession.setUser(userResult.data)
        }
    }

    fun onRegisterPhoneChanged(phone: String) {
        _registerFormState.update { formState ->
            formState.copy(phoneNumber = phone)
        }
    }

    fun onRegisterPasswordChanged(password: String){
        _registerFormState.update { formState ->
            formState.copy(password = password)
        }
    }

    fun onRegisterNameChanged(name: String){
        _registerFormState.update { formState ->
            formState.copy(name = name)
        }
    }

    fun onAuthPhoneChanged(phone: String){
        _authFormState.update { formState ->
            formState.copy(phoneNumber = phone)
        }
        clearAuthError()
    }

    fun onAuthPasswordChanged(password: String) {
        _authFormState.update { formState ->
            formState.copy(password = password)
        }
        clearAuthError()
    }
}