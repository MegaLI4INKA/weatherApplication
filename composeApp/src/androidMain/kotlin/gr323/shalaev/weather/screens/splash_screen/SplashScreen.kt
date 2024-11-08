package gr323.shalaev.weather.screens.splash_screen

import android.annotation.SuppressLint
import android.window.SplashScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import gr323.shalaev.weather.navigation.Screens
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SplashScreen(navHostController: NavHostController){

    var ip by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = ip)
        TextField(
            value = ip,
            onValueChange = {
                ip = it
            }
        )
        Button(
            onClick = {
                navHostController.navigate(Screens.MapScreen.route)
            }
        ){

        }
    }

}
