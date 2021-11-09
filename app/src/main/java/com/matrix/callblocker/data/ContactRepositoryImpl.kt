package com.matrix.callblocker.data

import android.util.Log
import com.matrix.callblocker.domain.model.Contact
import com.matrix.callblocker.domain.repository.ContactRepository
import com.matrix.callblocker.utils.Utils
import kotlinx.coroutines.flow.Flow

class ContactRepositoryImpl(
    private val dao: BlockedContactDao
) : ContactRepository {

    override fun getContacts(): Flow<List<Contact>> {
        return dao.getContacts()
    }

    override suspend fun getContactById(id: Int): Contact? {
        return dao.getContactById(id)
    }

    override suspend fun insertContact(contact: Contact) {
        dao.insertContact(contact)
    }

    override suspend fun deleteContact(contact: Contact) {
        dao.deleteContact(contact)
    }

    override suspend fun searchContact(phoneNumber: String): List<Contact> {
        Log.d("phoneNumber", Utils().convertNumber(phoneNumber))
        return dao.searchContact(Utils().convertNumber(phoneNumber))
    }
}