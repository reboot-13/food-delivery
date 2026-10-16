package com.example.fooddeliveryandroid.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddeliveryandroid.domain.model.Address
import com.example.fooddeliveryandroid.domain.model.User
import com.example.fooddeliveryandroid.domain.useCase.buildAddressText
import com.example.fooddeliveryandroid.presentation.address.AddressesScreen
import com.example.fooddeliveryandroid.presentation.profile.editProfile.EditProfileScreen
import com.example.fooddeliveryandroid.presentation.splash.LoadingProcess

@Composable
fun ProfileScreen (
    viewModel: ProfileViewModel = hiltViewModel(),
    onShowOrders: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val validationState by viewModel.validationState.collectAsStateWithLifecycle()
    val selectedAddress by viewModel.selectedAddress.collectAsStateWithLifecycle()
    when (val state = uiState.value) {

        is ProfileUIState.Loading -> LoadingProcess()

        is ProfileUIState.UnauthorizedRegister ->
            RegisterForm(
                viewModel,
                validationState,
                onClickNavigateButton = {
                    viewModel.showAuthForm()
                }
            )

        is ProfileUIState.UnauthorizedAuth -> {
            AuthForm(
                viewModel,
                validationState,
                onClickNavigateButton = {
                    viewModel.showRegisterForm()
                }
            )
        }

        is ProfileUIState.Authorized -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserProfile(
                    state.user,
                    onShowOrders = onShowOrders,
                    selectedAddress = selectedAddress,
                    modifier =  Modifier.weight(1f)
                )
                LogoutButton(
                    onClick = {
                        viewModel.logout()
                    }
                )
            }
        }

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
            },

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfile(
    user: User,
    selectedAddress: Address?,
    onShowOrders: () -> Unit,
    modifier: Modifier = Modifier,
    ){

    var isAddressSheetVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var isEditProfileSheetVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val addressSheetState  = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val editProfileSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (isAddressSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                isAddressSheetVisible = false
            },
            sheetState = addressSheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 80.dp)
        ) {
            AddressesScreen()
        }
    }

    if (isEditProfileSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                isEditProfileSheetVisible = false
            },
            sheetState = editProfileSheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 80.dp)
        ) {
            EditProfileScreen(
                user = user,
                onSaved = {
                    isEditProfileSheetVisible = false
                }
            )
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            UserHeader(
                user = user
            )
        }

        item {
           UserSection(
                icon = Icons.Outlined.PersonOutline,
                title = "Личные данные",
                subtitle = "Имя и номер телефона",
                onClick = {
                    isEditProfileSheetVisible = true
                }
            )
        }

        item {
            UserSection(
                icon = Icons.Outlined.LocationOn,
                title = "Адрес доставки",
                subtitle = selectedAddress?.let(::buildAddressText)
                    ?: "Выберите адрес",
                onClick = {
                    isAddressSheetVisible = true
                }
            )
        }

        item {
            UserSection(
                icon = Icons.Outlined.ReceiptLong,
                title = "Мои заказы",
                subtitle = "История заказов",
                onClick = onShowOrders
            )
        }
    }
}

@Composable
private fun UserHeader(
    user: User
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 12.dp,
                bottom = 12.dp
            )
    ) {
        Text(
            text = user.name,
            style = MaterialTheme.typography.labelLarge,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = user.phoneNumber,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun UserSection(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(46.dp),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.1f
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LogoutButton(
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(
                bottom = 130.dp
            ),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.error
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
        )
    ) {
        Icon(
            imageVector = Icons.Outlined.Logout,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Выйти из профиля"
        )
    }
}