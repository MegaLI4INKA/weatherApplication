package gr323.shalaev.weather.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import gr323.shalaev.weather.screens.graph_screen.GraphScreen
import gr323.shalaev.weather.screens.map_screen.MapScreen

@Composable
fun SetNavHostController(
    navHostController: NavHostController,
){
    NavHost(
        navController = navHostController,
        startDestination = Screens.MapScreen.route
    ){
        composable(
            route = Screens.GraphScreen.route
        ){
            GraphScreen()
        }
        composable(
            route = Screens.MapScreen.route
        ){
            MapScreen()
        }
    }
}