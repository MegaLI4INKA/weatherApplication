package gr323.shalaev.weather.screens.graph_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import gr323.shalaev.weather.navigation.Screens
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GraphScreen(cityId: Int, cityName: String){
    val viewModel: GraphViewModel = viewModel()
    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(Unit){
        viewModel.loadData(cityId)
    }

    if (state.dailyTemperatures.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }else{
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Scaffold(
                modifier = Modifier.fillMaxSize()
            ){ paddings ->

                Column(
                    modifier = Modifier.padding(paddings)
                ) {
                    Spacer(modifier = Modifier.size(14.dp))
                    Text(text = cityName, modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold, fontSize = 22.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.size(14.dp))
                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f), thickness = 2.dp)
                    Spacer(modifier = Modifier.size(14.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Range:",
                            fontSize = 20.sp
                        )
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = DateFormatter.formatDate(state.dateStart),
                            onValueChange = {

                            }
                        )
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = DateFormatter.formatDate(state.dateEnd),
                            onValueChange = {

                            }
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Start:",
                            fontSize = 20.sp
                        )
                        Slider(
                            value = 1f,
                            onValueChange = {

                            }
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "End:",
                            fontSize = 20.sp
                        )
                        Slider(
                            value = 1f,
                            onValueChange = {

                            }
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Points:",
                            fontSize = 20.sp
                        )
                        Slider(
                            value = 1f,
                            onValueChange = {

                            }
                        )
                    }
                }
            }
        }
    }
}
