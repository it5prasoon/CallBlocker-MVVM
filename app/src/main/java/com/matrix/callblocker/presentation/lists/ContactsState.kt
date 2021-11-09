package com.matrix.callblocker.presentation.lists

import com.matrix.callblocker.domain.model.Contact
import com.matrix.callblocker.domain.util.ContactOrder
import com.matrix.callblocker.domain.util.OrderType

data class ContactsState(
    val contacts: List<Contact> = emptyList(),
    val contactOrder: ContactOrder = ContactOrder.Name(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
