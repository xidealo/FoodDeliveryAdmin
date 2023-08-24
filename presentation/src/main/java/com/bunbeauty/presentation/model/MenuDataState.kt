package com.bunbeauty.presentation.model

data class MenuDataState(
    val visibleMenuProductItems: List<MenuProductItem> = listOf(),
    val hiddenMenuProductItems: List<MenuProductItem> = listOf(),
    val isRefreshing: Boolean = false,
    val throwable: Throwable? = null,
    val eventList: List<MenuEvent> = emptyList(),
    val state: State = State.LOADING
) {
    val isEmptyMenuProductListSize =
        (visibleMenuProductItems.size + hiddenMenuProductItems.size) == 0

    enum class State {
        LOADING,
        SUCCESS,
        ERROR
    }

    operator fun plus(event: MenuEvent) = copy(eventList = eventList + event)
    operator fun minus(events: List<MenuEvent>) =
        copy(eventList = eventList - events.toSet())
}

sealed interface MenuEvent {
    data class GoToEditMenuProduct(val uuid: String) : MenuEvent
}

data class MenuUiState(
    val state: State,
    val isRefreshing: Boolean,
    val eventList: List<MenuEvent>
) {
    sealed interface State {
        data class Success(
            val visibleMenuProductItems: List<MenuProductItem>,
            val hiddenMenuProductItems: List<MenuProductItem>,
        ) : State

        object Loading : State
        data class Error(val throwable: Throwable?) : State
    }
}

data class MenuProductItem(
    val uuid: String,
    val name: String,
    val photoLink: String,
    val visible: Boolean,
    val newCost: String,
    val oldCost: String,
)
