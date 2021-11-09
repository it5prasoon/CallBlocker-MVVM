package com.matrix.callblocker.domain.repository

import com.matrix.callblocker.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    fun getContacts(): Flow<List<Contact>>

    suspend fun getContactById(id: Int): Contact?

    suspend fun insertContact(contact: Contact)

    suspend fun deleteContact(contact: Contact)

    suspend fun searchContact(phoneNumber: String): List<Contact>
}