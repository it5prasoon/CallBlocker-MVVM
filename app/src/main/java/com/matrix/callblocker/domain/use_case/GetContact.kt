package com.matrix.callblocker.domain.use_case

import com.matrix.callblocker.domain.model.Contact
import com.matrix.callblocker.domain.repository.ContactRepository

class GetContact(
    private val repository: ContactRepository
) {

    suspend operator fun invoke(id: Int): Contact? {
        return repository.getContactById(id)
    }
}