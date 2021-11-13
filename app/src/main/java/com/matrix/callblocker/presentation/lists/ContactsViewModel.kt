package com.matrix.callblocker.presentation.lists

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrix.callblocker.domain.model.Contact
import com.matrix.callblocker.domain.use_case.ContactUseCases
import com.matrix.callblocker.domain.util.ContactOrder
import com.matrix.callblocker.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactUseCases: ContactUseCases
) : ViewModel() {

    private val _state = mutableStateOf(ContactsState())
    val state: State<ContactsState> = _state

    private var recentlyDeletedContact: Contact? = null

    private var getContactsJob: Job? = null

    init {
        getContacts(ContactOrder.Name(OrderType.Descending))
    }

    fun onEvent(event: ContactsEvent) {
        when (event) {
            is ContactsEvent.Order -> {
                if (state.value.contactOrder::class == event.ContactOrder::class &&
                    state.value.contactOrder.orderType == event.ContactOrder.orderType
                ) {
                    return
                }
                getContacts(event.ContactOrder)
            }
            is ContactsEvent.DeleteContact -> {
                viewModelScope.launch {
                    contactUseCases.deleteContact(event.Contact)
                    recentlyDeletedContact = event.Contact
                }
            }
            is ContactsEvent.RestoreContact -> {
                viewModelScope.launch {
                    contactUseCases.addContact(recentlyDeletedContact ?: return@launch)
                    recentlyDeletedContact = null
                }
            }
            is ContactsEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getContacts(contactOrder: ContactOrder) {
        getContactsJob?.cancel()
        getContactsJob = contactUseCases.getContacts(contactOrder)
            .onEach { Contacts ->
                _state.value = state.value.copy(
                    contacts = Contacts,
                    contactOrder = contactOrder
                )
            }
            .launchIn(viewModelScope)
    }
}