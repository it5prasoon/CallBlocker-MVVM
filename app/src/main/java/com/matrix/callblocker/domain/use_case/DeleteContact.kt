package com.matrix.callblocker.domain.use_case

import com.matrix.callblocker.domain.model.Contact
import com.matrix.callblocker.domain.repository.ContactRepository

class DeleteContact(
    private val repository: ContactRepository
) {

    suspend operator fun invoke(contact: Contact) {
        repository.deleteContact(contact)
    }
}