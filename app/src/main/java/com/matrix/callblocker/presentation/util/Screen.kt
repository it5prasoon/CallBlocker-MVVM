package com.matrix.callblocker.presentation.util

sealed class Screen(val route: String) {
    object ContactsScreen: Screen("contacts_screen")
    object AddEditContactScreen: Screen("add_edit_contact_screen")
}
