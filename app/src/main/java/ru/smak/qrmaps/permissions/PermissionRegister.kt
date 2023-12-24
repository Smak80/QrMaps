package ru.smak.qrmaps.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

abstract class PermissionRegister {

    protected lateinit var permissionRequester:
            ActivityResultLauncher<Array<String>>

    fun registerInActivity(
        activity: ComponentActivity,
        action: (Map<String, Boolean>)->Unit
    ){
        permissionRequester = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(), action
        )
    }

    protected fun isPermissionsGranted(vararg permissions: String, context: Context) =
        permissions.fold(true) { acc, perm ->
            acc && context.checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED
        }

}