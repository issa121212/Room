package com.rojas.consumodatos

import com.rojas.consumodatos.data.local.*
import com.rojas.consumodatos.data.remote.*
import com.rojas.consumodatos.data.repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class RepositoryTest {
    private class Contacts : ContactLocalDataSource {
        val rows = MutableStateFlow<List<ContactEntity>>(emptyList())
        override fun getContacts() = rows
        override suspend fun saveContact(contact: ContactEntity) {
            rows.value = rows.value + contact.copy(id = 1)
        }
        override suspend fun saveContacts(contacts: List<ContactEntity>) {
            rows.value = contacts
        }
        override suspend fun deleteContact(id: Long) {
            rows.value = rows.value.filterNot { it.id == id }
        }
    }

    private class Remote : ContactRemoteDataSource {
        var reads = 0
        override suspend fun fetchContacts(): List<ContactDto> {
            reads++
            return listOf(ContactDto(9, "Remoto", "555"))
        }
        override suspend fun addContact(contact: ContactDto): ContactDto =
            error("El guardado del formulario debe ser local")
    }

    @Test
    fun contactCreationAndDeletionUseLocalStorageWithoutNetwork() = runBlocking {
        val local = Contacts()
        val remote = Remote()
        val repository = ContactRepositoryImpl(local, remote)
        repository.addContact("Ana", "123")
        val contact = repository.getContacts().first().single()
        assertEquals("Ana", contact.name)
        assertEquals("123", contact.phone)
        assertEquals(0, remote.reads)
        repository.deleteContact(contact.id)
        assertTrue(repository.getContacts().first().isEmpty())
    }

    @Test
    fun explicitSynchronizationPersistsRemoteContactsLocally() = runBlocking {
        val local = Contacts()
        val remote = Remote()
        val repository = ContactRepositoryImpl(local, remote)
        repository.syncRemoteContacts()
        assertEquals(1, remote.reads)
        assertEquals(9L, repository.getContacts().first().single().id)
        assertEquals("Remoto", local.rows.value.single().name)
    }

    @Test
    fun newTasksAreStoredAsPendingAndExposedThroughTheRepository() = runBlocking {
        val rows = MutableStateFlow<List<TaskEntity>>(emptyList())
        val local = object : TaskLocalDataSource {
            override fun getTasks() = rows
            override suspend fun saveTask(task: TaskEntity) {
                rows.value = rows.value + task.copy(id = 1)
            }
        }
        val repository = TaskRepositoryImpl(local)
        repository.addTask("Estudiar")
        val task = repository.getTasks().first().single()
        assertEquals("Estudiar", task.name)
        assertFalse(task.isCompleted)
        assertEquals(1L, task.id)
    }
}
