package com.matrix.callblocker.presentation.forms

import androidx.compose.ui.focus.FocusState

sealed class AddEditContactEvent{
    data class EnteredTitle(val value: String): AddEditContactEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditContactEvent()
    data class EnteredContent(val value: String): AddEditContactEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditContactEvent()
    data class ChooseContact(val name: String, val phoneNumber: String): AddEditContactEvent()
    object SaveContact: AddEditContactEvent()
}

