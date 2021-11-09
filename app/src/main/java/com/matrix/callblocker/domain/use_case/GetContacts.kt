package com.matrix.callblocker.domain.use_case

import com.matrix.callblocker.domain.model.Contact
import com.matrix.callblocker.domain.repository.ContactRepository
import com.matrix.callblocker.domain.util.ContactOrder
import com.matrix.callblocker.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetContacts(
    private val repository: ContactRepository
) {

    operator fun invoke(
        contactOrder: ContactOrder = ContactOrder.Name(OrderType.Descending)
    ): Flow<List<Contact>> {
        return repository.getContacts().map { Contacts ->
            when(contactOrder.orderType) {
                is OrderType.Ascending -> {
                    when(contactOrder) {
                        is ContactOrder.Name -> Contacts.sortedBy { it.name.lowercase() }
                    }
                }
                is OrderType.Descending -> {
                    when(contactOrder) {
                        is ContactOrder.Name -> Contacts.sortedByDescending { it.name.lowercase() }
                    }
                }
            }
        }
    }
}