package com.example.fooddeliveryandroid.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
                viewModel,
                validationState.value,
                onClickNavigateButton = {
                    viewModel.showAuthForm()
                }
            )

        is ProfileUIState.UnauthorizedAuth -> {
            AuthForm(
                viewModel,
                validationState.value,
                onClickNavigateButton = {
                    viewModel.showRegisterForm()
                }
            )
        }

        is ProfileUIState.Authorized ->
            UserProfile(
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
    viewModel: ProfileViewModel,
    validationState: ProfileValidationState,
    onClickNavigateButton: () -> Unit,
) {
    val registerForm by viewModel.registerFormState.collectAsStateWithLifecycle()

    Column() {
        OutlinedTextField(
            value = registerForm.name,
            label = {
                Text("Имя")
            },
            onValueChange = viewModel::onRegisterNameChanged,
            isError = validationState.name != null,
            supportingText = {
                validationState.name?.let { errorMessage ->
                    Text(errorMessage)
                }
            }
        )
        OutlinedTextField(
            value = registerForm.phoneNumber,
            label = {
                Text("Номер телефона")
                    },
            onValueChange = viewModel::onRegisterPhoneChanged,
            isError = validationState.phoneNumber != null,
            supportingText = {
                validationState.phoneNumber?.let { errorMessage ->
                    Text(errorMessage)
                }
            }

        )
        OutlinedTextField(
            value = registerForm.password,
            label = {
                Text("Пароль")
            },
            onValueChange = viewModel::onRegisterPasswordChanged,
            isError = validationState.password != null,
            supportingText = {
                validationState.password?.let { errorMessage ->
                    Text(errorMessage)
                }
            }
        )

        if (!validationState.registerError.isNullOrBlank())
            Text(
                color = Color.Red,
                text = validationState.registerError
            )

        Button(
            onClick = {
                viewModel.register()
            }

        ) {
            Text("Зарегистрироваться")
        }
        Button(
            onClick = onClickNavigateButton
        ) {
            Text("Уже есть аккаунт, войти")
        }

    }
}

@Composable
fun AuthForm(
    viewModel: ProfileViewModel,
    validationState: ProfileValidationState,
    onClickNavigateButton: () -> Unit
) {
    val authForm by viewModel.authFormState.collectAsStateWithLifecycle()
    Column() {
        OutlinedTextField(
            value = authForm.phoneNumber,
            label = {
                Text("Номер телефона")
            },
            onValueChange = viewModel::onAuthPhoneChanged,
            isError = validationState.phoneNumber != null,
            supportingText = {
                validationState.phoneNumber?.let { errorMessage ->
                    Text(errorMessage)
                }
            }
        )
        OutlinedTextField(
            value = authForm.password,
            label = {
                Text("Пароль")
            },
            onValueChange = viewModel::onAuthPasswordChanged,
            isError = validationState.password != null,
            supportingText = {
                validationState.password?.let { errorMessage ->
                    Text(errorMessage)
                }
            }
        )
        if (!validationState.authError.isNullOrBlank())
            Text(
                color = Color.Red,
                text = validationState.authError
            )

        Button(
            onClick = {
                viewModel.auth()
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
fun UserProfile(
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