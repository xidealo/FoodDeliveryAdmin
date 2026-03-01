package com.bunbeauty.presentation.model

data class MenuListDataState(
    val visibleMenuProductItems: List<MenuProductItem> = listOf(),
    val hiddenMenuProductItems: List<MenuProductItem> = listOf(),
    val isRefreshing: Boolean = false,
    val throwable: Throwable? = null,
    val eventList: List<MenuListEvent> = emptyList(),
    val state: State = State.LOADING,
) {
    val isEmptyMenuProductListSize =
        (visibleMenuProductItems.size + hiddenMenuProductItems.size) == 0

    enum class State {
        LOADING,
        SUCCESS,
        ERROR,
    }

    operator fun plus(event: MenuListEvent) = copy(eventList = eventList + event)

    operator fun minus(events: List<MenuListEvent>) = copy(eventList = eventList - events.toSet())
}

sealed interface MenuListEvent {
    data class GoToEditMenuProductList(
        val uuid: String,
    ) : MenuListEvent
}

data class MenuListViewState(
    val state: State,
    val isRefreshing: Boolean,
    val eventList: List<MenuListEvent>,
) {
    sealed interface State {
        data class Success(
            val visibleMenuProductItems: List<MenuProductItem>,
            val hiddenMenuProductItems: List<MenuProductItem>,
        ) : State

        data object Loading : State

        data class Error(
            val throwable: Throwable?,
        ) : State
    }
}

data class MenuProductItem(
    val uuid: String,
    val name: String,
    val photoLink: String,
    val visible: Boolean,
)
