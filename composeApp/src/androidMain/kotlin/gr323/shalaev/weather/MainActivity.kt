package gr323.shalaev.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import gr323.shalaev.weather.navigation.SetNavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val changeIp = remember { mutableStateOf(true) }

            val navHostController = rememberNavController()
            if (changeIp.value){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(text = "ip address: ${ip.collectAsState().value}")
                    Spacer(modifier = Modifier.size(10.dp))
                    TextField(
                        value = ip.collectAsState().value,
                        onValueChange = {
                            runBlocking(Dispatchers.IO) {
                                ip.emit(it)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.size(50.dp))

                    Button(
                        onClick = {
                            changeIp.value = !changeIp.value
                        }
                    ){
                        Text(text = "Продолжить")
                    }
                }
            }else{
                SetNavHostController(navHostController)

            }
        }
    }
}

var ip = MutableStateFlow("")