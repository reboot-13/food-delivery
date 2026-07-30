package com.example.fooddeliveryandroid.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddeliveryandroid.data.remote.dto.request.AuthRequest
import com.example.fooddeliveryandroid.data.remote.dto.request.RegisterRequest
import com.example.fooddeliveryandroid.domain.model.User

@Composable
fun ProfileScreen (
    viewModel: ProfileViewModel = hiltViewModel(),
    onShowOrders: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val validationState = viewModel.validationState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        is ProfileUIState.Loading ->
            CircularProgressIndicator()

        is ProfileUIState.UnauthorizedRegister ->
            RegisterForm(
                validationState.value,
                onClickRegisterButton = { request ->
                    viewModel.register(request)
                },
                onClickNavigateButton = {
                    viewModel.showAuthForm()
                }
            )

        is ProfileUIState.UnauthorizedAuth ->
            AuthForm(
                validationState.value,
                onClickAuthButton = {request ->
                    viewModel.auth(request)
                },
                onClickNavigateButton = {
                    viewModel.showRegisterForm()
                }
            )

        is ProfileUIState.Authorized ->
            Profile(
                state.user,
                onShowOrders = onShowOrders,
                onLogout = {
                    viewModel.logout()
                }
            )

        is ProfileUIState.Error ->
            Text(state.message)
    }
}
@Composable
fun RegisterForm(
    validationState: ProfileValidationState,
    onClickRegisterButton: (RegisterRequest) -> Unit,
    onClickNavigateButton: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column() {
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            isError = validationState.name != null,
            supportingText = {
                validationState.name?.let { errorMessage ->
                    Text(errorMessage)
                }
            }
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
            },
            isError = validationState.phoneNumber != null,
            supportingText = {
                validationState.phoneNumber?.let { errorMessage ->
                    Text(errorMessage)
                }
            }

        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            isError = validationState.password != null,
            supportingText = {
                validationState.password?.let { errorMessage ->
                    Text(errorMessage)
                }
            }
        )
        Button(
            onClick = {
                onClickRegisterButton(
                    RegisterRequest(
                        name = name,
                        phoneNumber = phoneNumber,
                        password = password
                    )
                )
            }

        ) {
            Text("Зарегистрироваться")
        }
        Button(
            onClick = onClickNavigateButton
        ) {
            Text("Уже есть аккаунт, авторизоваться")
        }

    }
}

@Composable
fun AuthForm(
    validationState: ProfileValidationState,
    onClickAuthButton: (AuthRequest) -> Unit,
    onClickNavigateButton: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column() {
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
            },
            isError = validationState.phoneNumber != null,
            supportingText = {
                validationState.phoneNumber?.let { errorMessage ->
                    Text(errorMessage)
                }
            }
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            isError = validationState.password != null,
            supportingText = {
                validationState.password?.let { errorMessage ->
                    Text(errorMessage)
                }
            }
        )
        Button(
            onClick = {
                onClickAuthButton(
                    AuthRequest(
                        phoneNumber = phoneNumber,
                        password = password
                    )
                )
            }

        ) {
            Text("Войти")
        }
        Button(
            onClick = onClickNavigateButton
        ) {
            Text("Нет аккаунта, зарегистрироваться")
        }

    }
}

@Composable
fun Profile(
    user: User,
    onShowOrders: () -> Unit,
    onLogout: () -> Unit
    ){
    Column() {
        Text(user.name)
        Button(
            onClick = onShowOrders
        ) {
            Text("Мои заказы")
        }
        Button(
            onClick = onLogout
        ) {
            Text("Выйти из профиля")
        }
    }
}