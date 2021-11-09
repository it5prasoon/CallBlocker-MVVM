package com.matrix.callblocker.presentation.forms

import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.matrix.callblocker.presentation.forms.components.CustomTextField
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun AddEditContactScreen(
    navController: NavController,
    viewModel: AddEditContactViewModel = hiltViewModel()
) {
    val titleState = viewModel.contactTitle.value
    val contentState = viewModel.contactContent.value

    var name: String? = null
    var phoneNumber: String? = null

    val scaffoldState = rememberScaffoldState()

    val pickContact = rememberLauncherForActivityResult(
        PickContact(LocalContext.current.contentResolver)
    ) { contactResult ->
        name = contactResult.name
        phoneNumber = contactResult.phoneNumber
        Log.d(TAG, "AddEditContactScreen: Reached $name $phoneNumber")
        if (name != null && phoneNumber != null)
            viewModel.onEvent(AddEditContactEvent.ChooseContact(name = name!!, phoneNumber = phoneNumber!!))
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditContactViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditContactViewModel.UiEvent.SaveContact -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditContactEvent.SaveContact)
                },
                backgroundColor = MaterialTheme.colors.primary

            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save Contact")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                "Add contacts manually to the list", style = TextStyle(
                    fontWeight = FontWeight.Bold
                ), color = Color.Black, textAlign = TextAlign.Center, fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(30.dp))
            CustomTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditContactEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditContactEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditContactEvent.EnteredContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditContactEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                singleLine = true
            )

            Spacer(Modifier.height(60.dp))
            Text(
                "Or", style = TextStyle(
                    fontWeight = FontWeight.Normal
                ), color = Color.Gray, textAlign = TextAlign.Center, fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(30.dp))

            Button(
                onClick = {
                    pickContact.launch()
                },
                content = {
                    Text(text = "Choose from phone", color = Color.White)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            )
        }
    }
}