package io.tmn.sanntidsappenfagdagdemoandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.tmn.sanntidsappenfagdagdemoandroid.views.MainView

@Composable
fun SearchView() {
    var searchQuery by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchViewPreview() {
    SearchView()
}

class MainActivity : ComponentActivity() {

    private val locationPermissionGranted = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            getLocationPermission()
            if (locationPermissionGranted.value) {
                Column {
                    MainView()
                }
            }else{
                Text("Need permission")
            }
        }
    }

    companion object {
        // Used to load the 'sanntidsappenfagdagdemoandroid' library on application startup.
        init {
            System.loadLibrary("sanntidsappenfagdagdemoandroid")
        }

        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234

    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted.value = true//we already have the permission
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED){
            locationPermissionGranted.value=true
        }
    }
}