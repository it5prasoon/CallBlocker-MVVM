package com.matrix.callblocker.domain.use_case

import com.matrix.callblocker.domain.repository.ContactRepository

class SearchContact (
    private val repository: ContactRepository
) {
    suspend operator fun invoke(phoneNumber: String) {
        repository.searchContact(phoneNumber)
    }
}