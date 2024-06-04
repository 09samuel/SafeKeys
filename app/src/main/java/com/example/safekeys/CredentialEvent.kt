package com.example.safekeys

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
}