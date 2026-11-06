package com.rojas.consumodatos.ui.contact

import com.rojas.consumodatos.domain.model.Contact

data class ContactUiState(
    val contacts: List<Contact> = emptyList(),
    val nameInput: String = "",
    val phoneInput: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
