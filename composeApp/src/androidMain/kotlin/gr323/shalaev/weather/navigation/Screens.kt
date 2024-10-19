package gr323.shalaev.weather.navigation

sealed class Screens(val route: String){

    object MapScreen : Screens("map_screen")

    object GraphScreen: Screens("graph_screen/{city_id}/{city_name}"){
        fun setCity(city_id: Int, city_name: String): String {
            return "graph_screen/$city_id/$city_name"
        }
    }

}