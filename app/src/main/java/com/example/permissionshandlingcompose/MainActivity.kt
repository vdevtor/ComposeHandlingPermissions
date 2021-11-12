package com.example.permissionshandlingcompose

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.permissionshandlingcompose.ui.theme.PermissionsHandlingComposeTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class MainActivity : ComponentActivity() {
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionsHandlingComposeTheme {
                // A surface container using the 'background' color from the theme
                val permissions = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA
                    )
                )
                    val lifeCycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(key1 = lifeCycleOwner, effect = {
                        val observer = LifecycleEventObserver{_,event ->
                            if (event == Lifecycle.Event.ON_START){
                                permissions.launchMultiplePermissionRequest()
                            }
                        }
                        lifeCycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifeCycleOwner.lifecycle.removeObserver(observer)
                        }
                    })
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                        permissions.permissions.forEach { perm ->
                            when(perm.permission){
                                Manifest.permission.CAMERA ->{
                                    when{
                                        perm.hasPermission ->{
                                            Text(text = "Camera Permission Accepted")
                                        }
                                        perm.shouldShowRationale ->{
                                            Text(text = "Camera Permission is needed to access the camera")
                                        }
                                        !perm.isPermanentlyDenied() ->{
                                            Text(text = "Camera Permission was permanently denied," +
                                                    " you can enable it on app settings")
                                        }
                                    }
                                }
                                Manifest.permission.RECORD_AUDIO ->{

                                    when{
                                        perm.hasPermission ->{
                                            Text(text = "Audio Permission Accepted")
                                        }
                                        perm.shouldShowRationale ->{
                                            Text(text = "Audio Permission is needed to record audios")
                                        }
                                        !perm.isPermanentlyDenied() ->{
                                            Text(text = "Audio Permission was permanently denied," +
                                                    " you can enable it on app settings")
                                        }
                                    }

                                }
                            }
                        }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PermissionsHandlingComposeTheme {
        Greeting("Android")
    }
}