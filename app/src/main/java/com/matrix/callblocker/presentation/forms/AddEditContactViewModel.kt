package com.matrix.callblocker.presentation.forms

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrix.callblocker.domain.model.Contact
import com.matrix.callblocker.domain.model.InvalidContactException
import com.matrix.callblocker.domain.use_case.ContactUseCases
import com.matrix.callblocker.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditContactViewModel @Inject constructor(
    private val contactUseCases: ContactUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _contactTitle = mutableStateOf(
        ContactTextFieldState(
            hint = "Enter name"
        )
    )

    val contactTitle: State<ContactTextFieldState> = _contactTitle

    private val _ContactContent = mutableStateOf(
        ContactTextFieldState(
            hint = "Enter number with country code"
        )
    )
    val contactContent: State<ContactTextFieldState> = _ContactContent

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentContactId: Int? = null

    init {
        savedStateHandle.get<Int>("ContactId")?.let { ContactId ->
            if (ContactId != -1) {
                viewModelScope.launch {
                    contactUseCases.getContact(ContactId)?.also { Contact ->
                        currentContactId = Contact.id
                        _contactTitle.value = contactTitle.value.copy(
                            text = Contact.name,
                            isHintVisible = false
                        )
                        _ContactContent.value = _ContactContent.value.copy(
                            text = Contact.phoneNumber,
                            isHintVisible = false
                        )
                    }
                }
            }
        }
    }


    fun onEvent(event: AddEditContactEvent) {
        when (event) {
            is AddEditContactEvent.EnteredTitle -> {
                _contactTitle.value = contactTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditContactEvent.ChangeTitleFocus -> {
                _contactTitle.value = contactTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            contactTitle.value.text.isBlank()
                )
            }
            is AddEditContactEvent.EnteredContent -> {
                _ContactContent.value = _ContactContent.value.copy(
                    text = event.value
                )
            }
            is AddEditContactEvent.ChangeContentFocus -> {
                _ContactContent.value = _ContactContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _ContactContent.value.text.isBlank()
                )
            }

            is AddEditContactEvent.ChooseContact -> {
                viewModelScope.launch {
                    try {
                        contactUseCases.addContact(
                            Contact(
                                name = event.name,
                                phoneNumber = Utils().convertNumber(event.phoneNumber),
                                id = currentContactId
                            )
                        )
                        Log.d(ContentValues.TAG, "AddEditContactScreen: Reached ${event.name} ${event.phoneNumber}")
                        _eventFlow.emit(UiEvent.SaveContact)
                    } catch (e: InvalidContactException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save Contact"
                            )
                        )
                    }

                }
            }

            is AddEditContactEvent.SaveContact -> {
                viewModelScope.launch {
                    try {
                        contactUseCases.addContact(
                            Contact(
                                name = contactTitle.value.text,
                                phoneNumber = Utils().convertNumber(contactContent.value.text),
                                id = currentContactId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveContact)
                    } catch (e: InvalidContactException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save Contact"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveContact : UiEvent()
    }

}
