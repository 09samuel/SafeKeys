package com.example.safekeys.data.repository

import com.example.safekeys.data.model.Credential
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class FakeCredentialRepositoryTest {

    private lateinit var repository: FakeCredentialRepository

    @Before
    fun setup() {
        repository = FakeCredentialRepository()

        val notesToInsert = mutableListOf<Credential>()
        ('a'..'z').forEachIndexed { index, c ->
            notesToInsert.add(
                Credential(
                    username = c.toString(),
                    password = c.toString().toByteArray(),
                    website = c.toString(),
                    title = c.toString(),
                    dateCreated = LocalDateTime.now(),
                    iv = "iv".toByteArray()
                )
            )
        }
        notesToInsert.shuffle()
        runBlocking {
            notesToInsert.forEach { repository.upsertCredential(it) }
        }
    }

    @Test
    fun `upsertCredential adds a credential`() = runBlocking {
        val credential = Credential("user", "password".toByteArray(), "example","example", LocalDateTime.now(),"iv".toByteArray())
        repository.upsertCredential(credential)

        val result = repository.getCredentialsForAutofill("example")
        assertThat(result).contains(credential)
    }

    @Test
    fun `Order notes by title ascending, correct order`() = runBlocking {
        val credentials = repository.getCredentialsOrderedByTitle()

        credentials.collect { credentialList ->
            for (i in 0 until credentialList.size - 1) {
                assertThat(credentialList[i].title).isLessThan(credentialList[i + 1].title)
            }
        }
    }

    @Test
    fun `Order notes by dateCreated ascending, correct order`() = runBlocking {
        val credentials = repository.getCredentialsOrderedByDateCreated()

        credentials.collect { credentialList ->
            for (i in 0 until credentialList.size - 1) {
                assertThat(credentialList[i].dateCreated).isLessThan(credentialList[i + 1].dateCreated)
            }
        }
    }


    @Test
    fun `deleteCredential removes the credential`() = runBlocking {
        val credential = Credential("user", "password".toByteArray(), "example","example", LocalDateTime.now(),"iv".toByteArray())
        repository.upsertCredential(credential)
        repository.deleteCredential(credential)

        val result = repository.getCredentialsForAutofill("example")
        assertThat(result).doesNotContain(credential)
    }
}
