package com.example.fooddeliveryandroid.presentation.profile.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.local.datastore.UserSession
import com.example.fooddeliveryandroid.data.remote.dto.request.UpdateProfileRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.ProfileRepository
import com.example.fooddeliveryandroid.domain.model.User
import com.example.fooddeliveryandroid.domain.validation.AuthValidator
import com.example.fooddeliveryandroid.domain.validation.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userSession: UserSession
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        EditProfileState()
    )

    val uiState = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<EditProfileEvent>()
    val events = _events.asSharedFlow()


    private val validator = AuthValidator()

    fun initialize(user: User) {
        if (
            _uiState.value.name.isNotEmpty() ||
            _uiState.value.phoneNumber.isNotEmpty()
        ) {
            return
        }

        _uiState.value = EditProfileState(
            name = user.name,
            phoneNumber = user.phoneNumber
        )
    }

    fun onNameChanged(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                nameError = null,
                error = null
            )
        }
    }

    fun onPhoneChanged(phoneNumber: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phoneNumber,
                phoneError = null,
                error = null
            )
        }
    }

    fun save() {
        val state = _uiState.value

        val nameError =
            (validator.validateName(state.name)
                    as? ValidationResult.Error)
                ?.message

        val phoneError =
            (validator.validatePhone(state.phoneNumber)
                    as? ValidationResult.Error)
                ?.message

        if (nameError != null || phoneError != null) {
            _uiState.update {
                it.copy(
                    nameError = nameError,
                    phoneError = phoneError
                )
            }

            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isSaving = true,
                    error = null
                )
            }

            val request = UpdateProfileRequest(
                name = state.name,
                phoneNumber = state.phoneNumber
            )

            when (
                val result = profileRepository.updateProfile(request)
            ) {
                is NetworkResult.Success -> {
                    userSession.setUser(result.data)

                    _uiState.update {
                        it.copy(
                            isSaving = false
                        )
                    }
                    _events.emit(
                        EditProfileEvent.Saved
                    )
                }

                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = when (result.code) {
                                409 ->
                                    "Пользователь с таким номером телефона уже существует"

                                else ->
                                    "Не удалось сохранить изменения"
                            }
                        )
                    }
                }
            }
        }
    }
}