package com.rojas.consumodatos.ui.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rojas.consumodatos.domain.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState: StateFlow<ContactUiState> = _uiState.asStateFlow()

    init {
        observeContacts()
    }

    private fun observeContacts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getContacts()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
                .collect { list ->
                    _uiState.update { it.copy(contacts = list, isLoading = false) }
                }
        }
    }

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(nameInput = name) }
    }

    fun onPhoneChanged(phone: String) {
        _uiState.update { it.copy(phoneInput = phone) }
    }

    fun addContact() {
        val name = _uiState.value.nameInput.trim()
        val phone = _uiState.value.phoneInput.trim()

        if (name.isBlank() || phone.isBlank()) return

        viewModelScope.launch {
            repository.addContact(name, phone)
            _uiState.update { it.copy(nameInput = "", phoneInput = "") }
        }
    }

    fun deleteContact(id: Long) {
        viewModelScope.launch {
            repository.deleteContact(id)
        }
    }
}
