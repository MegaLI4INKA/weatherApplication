package gr323.shalaev.weather.navigation

sealed class Screens(val route: String){

    object GraphScreen: Screens("graph_screen")

    object MapScreen : Screens("map_screen")

}