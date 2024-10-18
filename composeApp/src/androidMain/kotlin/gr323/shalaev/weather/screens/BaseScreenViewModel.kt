package gr323.shalaev.weather.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr323.shalaev.weather.screens.graph_screen.GraphState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseScreenViewModel<State: Any>(initState: State): ViewModel() {

    val _state = MutableStateFlow(initState)
    val stateFlow: StateFlow<State> = _state
    val state: State
        get() = _state.value

    fun reduce(updateState: (State) -> State){
        viewModelScope.launch {
            _state.update { currentState ->
                updateState(currentState)
            }
        }
    }
}