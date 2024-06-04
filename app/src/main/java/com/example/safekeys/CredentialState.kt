package com.example.safekeys

data class CredentialState(
    val credential: List<Credential> = emptyList(),
    val username: String = "",
    val website: String = "",
    val password: String = "",
    val title: String = "",
    val isAddingCredential: Boolean = false,
    val sortType: SortType = SortType.TITLE
)
