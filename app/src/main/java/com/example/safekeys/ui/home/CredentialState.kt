package com.example.safekeys.ui.home

import com.example.safekeys.data.model.Credential
import com.example.safekeys.data.model.SortType

data class CredentialState(
    val credential: List<Credential> = emptyList(),
    val username: String = "",
    val website: String = "",
    val password: String = "",
    val title: String = "",
    val usernameError: String? = null,
    val websiteError: String? = null,
    val passwordError: String? = null,
    val titleError: String? = null,
    val decryptedPassword: MutableMap<Int, String> = mutableMapOf(),
    val isAddingCredential: Boolean = false,
    val sortType: SortType = SortType.TITLE
)
