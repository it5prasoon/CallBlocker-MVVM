package com.matrix.callblocker.domain.use_case

data class ContactUseCases(
    val getContacts: GetContacts,
    val deleteContact: DeleteContact,
    val addContact: AddContact,
    val getContact: GetContact,
    val searchContact: SearchContact
)
