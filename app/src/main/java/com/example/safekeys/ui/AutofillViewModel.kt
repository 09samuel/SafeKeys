package com.example.safekeys.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safekeys.data.model.Credential
import com.example.safekeys.domain.repository.CredentialRepository
import com.example.safekeys.domain.repository.UserRepository
import com.example.safekeys.utils.CryptoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class AutofillViewModel(
    private val credentialRepository: CredentialRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val usernameHints = arrayOf("username", "email")
    val passwordHints = arrayOf("password", "pass")

    fun fetchCredentials(viewWebDomain: String, callback: (Result<List<Credential>>) -> Unit) {
        viewModelScope.launch {
            try {
                val credentials = credentialRepository.getCredentialsForAutofill(viewWebDomain)
                callback(Result.success(credentials))
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }

    fun saveCredential(credential: Credential, callback: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                credentialRepository.upsertCredential(credential)
                callback(Result.success(Unit))
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return userRepository.isUserLoggedIn()
    }
}
