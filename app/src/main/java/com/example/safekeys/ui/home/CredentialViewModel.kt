package com.example.safekeys.ui.home

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safekeys.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class CredentialViewModel(private val dao: CredentialDao) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.TITLE)
    private val cryptoManager = CryptoManager()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _credentials = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            SortType.TITLE -> dao.getCredentialsOrderedByTitle()
            SortType.DATE_CREATED -> dao.getCredentialsOrderedByDateCreated()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(CredentialState())
    val state = combine(_state, _sortType, _credentials) { state, sortType, credentials ->
        state.copy(
            credential = credentials,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CredentialState())

    fun onEvent(event: CredentialEvent) {
        when (event) {
            is CredentialEvent.DeleteCredential -> {
                viewModelScope.launch {
                    dao.deleteCredential(event.credential)
                }
            }

            CredentialEvent.HideDialog -> {
                _state.update {
                    it.copy(isAddingCredential = false)
                }
            }

            CredentialEvent.SaveCredential -> {
                val username = state.value.username
                val password = state.value.password
                val website = state.value.website
                val title = state.value.title

                if (username.isBlank() || password.isBlank() || website.isBlank() || title.isBlank()) {
                    return
                }

                try {
                    val bytes = password.encodeToByteArray()
                    val encryptedData = cryptoManager.encrypt(bytes)

                    Log.i("EncryptedBytes", Base64.encodeToString(encryptedData.encryptedBytes, Base64.DEFAULT))
                    Log.i("IV", Base64.encodeToString(encryptedData.iv, Base64.DEFAULT))

                    val credential = Credential(
                        username = username,
                        password = encryptedData.encryptedBytes,
                        website = website,
                        title = title,
                        iv = encryptedData.iv,
                        dateCreated = LocalDateTime.now()
                    )

                    viewModelScope.launch {
                        dao.upsertCredential(credential)
                    }

                    _state.update {
                        it.copy(isAddingCredential = false, username = "", password = "", website = "", title = "")
                    }
                } catch (e: Exception) {
                    Log.e("SaveCredentialError", "Error saving credential", e)
                }
            }

            is CredentialEvent.SetPassword -> {
                _state.update { it.copy(password = event.password) }
            }

            is CredentialEvent.SetTitle -> {
                _state.update { it.copy(title = event.title) }
            }

            is CredentialEvent.SetUsername -> {
                _state.update { it.copy(username = event.username) }
            }

            is CredentialEvent.SetWebsite -> {
                _state.update { it.copy(website = event.website) }
            }

            CredentialEvent.ShowDialog -> {
                _state.update { it.copy(isAddingCredential = true) }
            }

            is CredentialEvent.SortCredentials -> {
                _sortType.value = event.sortType
            }
        }
    }

    fun decryptCredential(credential: Credential): String {
        return try {
            val decryptedBytes = cryptoManager.decrypt(credential.iv, credential.password)
            decryptedBytes.decodeToString()
        } catch (e: Exception) {
            Log.e("DecryptError", "Error during decryption", e)
            ""
        }
    }
}
