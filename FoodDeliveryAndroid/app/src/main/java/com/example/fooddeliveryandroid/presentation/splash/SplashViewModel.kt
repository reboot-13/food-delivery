package com.example.fooddeliveryandroid.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.data.repository.ProfileRepository
import com.example.fooddeliveryandroid.data.local.datastore.SessionManager
import com.example.fooddeliveryandroid.data.local.datastore.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val sessionManager: SessionManager,
    val userSession: UserSession,
    val profileRepository: ProfileRepository
): ViewModel(){
    private val _uiState = MutableStateFlow<SplashUIState>(SplashUIState.Loading)
    val uiState: StateFlow<SplashUIState> = _uiState

    private fun finishLoading() {
        _uiState.value = SplashUIState.GoToCatalog
    }
    init {
        viewModelScope.launch {
            val hasToken = sessionManager.hasToken()
            if (!hasToken) {
                finishLoading()
                return@launch

            }

            when (val result = profileRepository.getCurrentUser()) {
                is NetworkResult.Error -> {
                    when (result.code) {
                        401 -> {
                            sessionManager.clearToken()
                            finishLoading()
                        }
                        else -> finishLoading()
                    }
                }
                is NetworkResult.Success -> {
                    val user = result.data
                    userSession.setUser(user)
                    _uiState.value = SplashUIState.GoToCatalog
                }
            }
        }
    }
}