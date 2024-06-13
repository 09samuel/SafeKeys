package com.example.safekeys.ui.home

import com.example.safekeys.data.model.Credential
import com.example.safekeys.data.model.SortType

sealed interface CredentialEvent {
    data object SaveCredential: CredentialEvent
    data class SetUsername(val username: String): CredentialEvent
    data class SetPassword(val password: String): CredentialEvent
    data class SetWebsite(val website: String): CredentialEvent
    data class SetTitle(val title: String): CredentialEvent
    data object ShowDialog: CredentialEvent
    data object HideDialog: CredentialEvent
    data class SortCredentials(val sortType: SortType): CredentialEvent
    data class DeleteCredential(val credential: Credential): CredentialEvent

    data class DecryptCredentials(val credential: Credential): CredentialEvent
}