package com.matrix.callblocker.presentation.lists

import com.matrix.callblocker.domain.model.Contact
import com.matrix.callblocker.domain.util.ContactOrder

sealed class ContactsEvent {
    data class Order(val ContactOrder: ContactOrder): ContactsEvent()
    data class DeleteContact(val Contact: Contact): ContactsEvent()
    object RestoreContact: ContactsEvent()
    object ToggleOrderSection: ContactsEvent()
}
