package com.matrix.callblocker.presentation

import android.Manifest
import android.app.role.RoleManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.matrix.callblocker.presentation.forms.AddEditContactScreen
import com.matrix.callblocker.presentation.lists.ContactsScreen
import com.matrix.callblocker.presentation.util.Screen
import com.matrix.callblocker.ui.theme.CleanArchitectureContactAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val phonePermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.MODIFY_PHONE_STATE,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.ANSWER_PHONE_CALLS
        )
    } else {
        arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.MODIFY_PHONE_STATE
        )
    }

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CleanArchitectureContactAppTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.ContactsScreen.route
                    ) {
                        composable(route = Screen.ContactsScreen.route) {
                            ContactsScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditContactScreen.route +
                                    "?ContactId={ContactId}",
                            arguments = listOf(
                                navArgument(
                                    name = "ContactId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            AddEditContactScreen(
                                navController = navController
                            )
                        }
                    }
                    if (!checkPhonePermissionsGranted(this))
                        phonePermissions()
                }

            }
        }
    }

    @Composable
    fun phonePermissions(): Boolean {
        ActivityCompat.requestPermissions(this, phonePermissions, 0)

        if (checkPhonePermissionsGranted(this))
            return true
        return false
    }

    private fun checkPhonePermissionsGranted(context: Context): Boolean {
        phonePermissions.forEach {
            if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "checkPhonePermissionsGranted: All permissions are granted.")
                return false
            }
        }

        return true
    }
}
