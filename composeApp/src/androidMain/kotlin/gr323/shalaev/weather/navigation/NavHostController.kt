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
            route = Screens.GraphScreen.route,
            arguments = listOf(
                navArgument("city_id"){
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument("city_name"){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ){
            val city_id: Int? = it.arguments?.getInt("city_id")
            val city_name: String? = it.arguments?.getString("city_name")
            GraphScreen(city_id?: 0, city_name?: "Undefined")
        }
        composable(
            route = Screens.MapScreen.route
        ){
            MapScreen(navHostController)
        }
    }
}