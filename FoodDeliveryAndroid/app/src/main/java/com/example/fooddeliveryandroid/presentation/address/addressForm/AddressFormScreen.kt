package com.example.fooddeliveryandroid.presentation.address.addressForm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fooddeliveryandroid.domain.model.Address
import com.example.fooddeliveryandroid.domain.model.AddressMapSelection
import com.example.fooddeliveryandroid.domain.model.DeliveryCities
import com.example.fooddeliveryandroid.domain.model.DeliveryCity
import com.yandex.mapkit.search.SuggestItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressFormScreen(
    modifier: Modifier = Modifier,
    selection: AddressMapSelection? = null,
    onBack: () -> Unit,
    onAddressCreated: (Address) -> Unit,
    viewModel: AddressFormViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Первоначально заполняем форму данными с карты, если они есть.
    LaunchedEffect(selection) {
        viewModel.initialize(selection)
    }

    // Реагируем на успешное создание адреса.
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is AddressFormEvent.AddressFormCreated -> {
                    onAddressCreated(event.address)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Новый адрес",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            viewModel.createAddress()
                        },
                        enabled = !uiState.isSaving
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Сохранить")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            AddressFormIntro()

            CityDropdownField(
                selectedCity = uiState.city,
                onCitySelected = viewModel::onCitySelected
            )

            StreetAndHouseField(
                uiState = uiState,
                onQueryChanged = viewModel::onAddressQueryChanged,
                onSuggestionSelected = viewModel::onSuggestionSelected,
                onDismissSuggestions = viewModel::clearAddressSuggestions
            )

            Text(
                text = "Детали доставки",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AddressFormTextField(
                    value = uiState.apartment,
                    onValueChange = viewModel::onApartmentChanged,
                    label = "Квартира",
                    modifier = Modifier.weight(1f)
                )

                AddressFormTextField(
                    value = uiState.entrance,
                    onValueChange = viewModel::onEntranceChanged,
                    label = "Подъезд",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AddressFormTextField(
                    value = uiState.floor,
                    onValueChange = viewModel::onFloorChanged,
                    label = "Этаж",
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                AddressFormTextField(
                    value = uiState.intercom,
                    onValueChange = viewModel::onIntercomChanged,
                    label = "Домофон",
                    modifier = Modifier.weight(1f)
                )
            }

            AddressFormTextField(
                value = uiState.comment,
                onValueChange = viewModel::onCommentChanged,
                label = "Комментарий курьеру",
                placeholder = "Например, вход со двора",
                singleLine = false,
                minLines = 2
            )

            AddressFormTextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChanged,
                label = "Название адреса",
                placeholder = "Например, дом, работа"
            )

            uiState.error?.let { error ->
                AddressFormError(message = error)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AddressFormIntro() {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Куда доставить заказ?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Укажите адрес и детали, чтобы курьер легко вас нашёл",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CityDropdownField(
    selectedCity: String,
    onCitySelected: (DeliveryCity) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val cities = remember {
        DeliveryCities.values()
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedCity,
            onValueChange = {},
            readOnly = true,
            label = {
                Text("Город")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    MenuAnchorType.PrimaryNotEditable
                ),
            shape = RoundedCornerShape(18.dp),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            cities.forEach { city ->
                DropdownMenuItem(
                    text = {
                        Text(city.name)
                    },
                    onClick = {
                        expanded = false
                        onCitySelected(city)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StreetAndHouseField(
    uiState: AddressFormUiState,
    onQueryChanged: (String) -> Unit,
    onSuggestionSelected: (SuggestItem) -> Unit,
    onDismissSuggestions: () -> Unit
) {
    var addressFieldValue by remember {
        mutableStateOf(TextFieldValue(uiState.addressQuery))
    }

    LaunchedEffect(uiState.addressQuery) {
        if (addressFieldValue.text != uiState.addressQuery) {
            val text = uiState.addressQuery

            addressFieldValue = TextFieldValue(
                text = text,
                selection = TextRange(text.length)
            )
        }
    }

    val expanded = uiState.isSuggestionsExpanded

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (!it) onDismissSuggestions()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = addressFieldValue,
            onValueChange = { newValue ->
                addressFieldValue = newValue
                onQueryChanged(newValue.text)
            },
            label = {
                Text("Улица и дом")
            },
            placeholder = {
                Text("Начните вводить адрес")
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    MenuAnchorType.PrimaryEditable
                ),
            shape = RoundedCornerShape(18.dp),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissSuggestions
        ) {
            uiState.suggestedAddresses?.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = item.title.text,
                                style = MaterialTheme.typography.bodyMedium,
                            )

                            item.subtitle?.text
                                ?.takeIf { it.isNotBlank() }
                                ?.let { subtitle ->
                                    Text(
                                        text = subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                        }
                    },
                    onClick = {
                        addressFieldValue = addressFieldValue.copy(
                            selection = TextRange(addressFieldValue.text.length)
                        )
                        onSuggestionSelected(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun AddressFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = {
            Text(label)
        },
        placeholder = {
            placeholder?.let { Text(it) }
        },
        singleLine = singleLine,
        minLines = minLines,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(18.dp)
    )
}

@Composable
private fun AddressFormError(
    message: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.error.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}