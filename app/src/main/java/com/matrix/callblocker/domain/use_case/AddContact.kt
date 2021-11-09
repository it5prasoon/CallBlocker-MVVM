package com.matrix.callblocker.domain.use_case

import com.matrix.callblocker.domain.model.InvalidContactException
import com.matrix.callblocker.domain.model.Contact
import com.matrix.callblocker.domain.repository.ContactRepository

class AddContact(
    private val repository: ContactRepository
) {

    @Throws(InvalidContactException::class)
    suspend operator fun invoke(contact: Contact) {
        if(contact.phoneNumber.isBlank()) {
            throw InvalidContactException("The phone number of the contact can't be empty.")
        }
        repository.insertContact(contact)
    }
}