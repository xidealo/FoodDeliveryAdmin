# Промт: Перенос Fragment в Presentation Module

Я работаю над проектом FoodDeliveryAdmin (Kotlin Multiplatform с Compose Multiplatform) и переношу весь UI из app модуля в presentation module. Уже перенесли следующие экраны по аналогии: LoginRoute, SettingsRoute, CategoryList, CreateCategory, EditCategory, Statistic, Gallery, AdditionGroupList, и другие.

## Задача

Перенести следующие Fragment-ы из `app/src/main/java/com/bunbeauty/fooddeliveryadmin/screen/` в `presentation/src/commonMain/kotlin/com/bunbeauty/presentation/feature/`:

```
[список фрагментов для переноса]
```

## Структура проекта

```
app/src/main/java/com/bunbeauty/fooddeliveryadmin/screen/
  └── [screen_name]/
      └── [ScreenName]Fragment.kt  (который нужно перенести)

presentation/src/commonMain/kotlin/com/bunbeauty/presentation/feature/
  └── [screen_name]/
      ├── [ScreenName]ViewModel.kt  (уже существует)
      ├── [ScreenName].kt           (DataState interfaces, уже существует)
      ├── [ScreenName]Route.kt      (нужно создать)
      ├── [ScreenName]ViewState.kt  (нужно создать)
      └── navigation/
          └── [ScreenName]ScreenNavigation.kt  (нужно создать)
```

## Требования к реализации

### 1. Создать Route файл по паттерну LoginRoute с обязательными компонентами

```kotlin
@Composable
fun [ScreenName]RouteScreen(
    viewModel: [ScreenName]ViewModel = koinViewModel(),
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
    // другие параметры навигации при необходимости
)
```

Требования:
- Использовать `collectAsStateWithLifecycle` для state
- Создавать `remember` для onAction
- Обрабатывать эффекты через LaunchedEffect
- Преобразовывать DataState в ViewState через `.toViewState()`

### 2. Создать ViewState файл с конвертацией

```kotlin
@Immutable
data class [ScreenName]ViewState(...) : BaseViewState
```

Требования:
- Добавить функцию расширения: `@Composable internal fun DataState.toViewState(): ViewState`

### 3. Создать Navigation файл

```kotlin
@Serializable
data class [ScreenName]ScreenDestination([params])
```

Требования:
- Добавить функции: `navigateTo[ScreenName]Screen` и `[screenName]ScreenRoute`

### 4. Обновить FoodDeliveryNavGraphBuilder.kt

Требования:
- Добавить импорты для новых navigation файлов
- Добавить вызов `[screenName]ScreenRoute` с передачей необходимых параметров
- Настроить навигацию в родительских экранах при необходимости

### 5. Удалить старые файлы

Требования:
- Удалить `[ScreenName]Fragment.kt` из app модуля
- Удалить `[ScreenName]ViewState.kt` из app модуля (если существует)

### 6. Добавить в git

Требования:
- Добавить новые файлы в git
- Не добавлять build директории (`build/`, `.kotlin/`, `buildSrc/`, `build-logic/`)

## Важные детали

- ✅ Использовать `showInfoMessage: (String, Int) -> Unit` (НЕ `(String) -> Unit`)
- ✅ Для навигации использовать `navController.popBackStack()` для возврата назад
- ✅ Использовать `emptyNavOptions` при необходимости
- ✅ При передаче параметров через навигацию использовать `backStackEntry.arguments?.getString()` или `backStackEntry.toRoute<>()`
- ✅ Все строки должны использовать Compose Resources: `Res.string.*`
- ✅ Использовать `remember(viewState) { viewState.toViewState() }` для ViewState
- ✅ Для эффектов использовать `LaunchedEffect(effects) { effects.forEach { ... } consumeEffects() }`

## Пример аналогичного переноса (для справки)

### Было (app module)

```kotlin
class StatisticDetailsFragment :
    BaseComposeFragment<DataState, ViewState, Action, Event>() {
    override val viewModel by viewModel()

    @Composable
    override fun Screen(state: ViewState, onAction: Action) { ... }
}
```

### Стало (presentation module)

```kotlin
// StatisticDetailsRoute.kt
@Composable
fun StatisticDetailsRouteScreen(...) { ... }

// StatisticDetailsViewState.kt
@Immutable
data class StatisticDetailsViewState(...) : BaseViewState

@Composable
internal fun DataState.toViewState(): StatisticDetailsViewState = ...

// navigation/StatisticDetailsScreenNavigation.kt
@Serializable
data object StatisticDetailsScreenDestination
```

## Статистика проекта

- ✅ Успешно перенесено: 19 экранов
- ⏳ Осталось: 11 фрагментов
- 📝 Каждому фрагменту соответствует ViewModel в presentation модуле

---

## Оставшиеся фрагменты

### Пример списка:

1. **CreateAdditionFragment** (322 строки)
2. **EditAdditionFragment** (323 строки)
3. **CreateAdditionGroupForMenuProductFragment** (253 строки)
4. **EditAdditionGroupForMenuProductFragment** (243 строки)
5. **SelectAdditionGroupFragment** (314 строки)
6. **SelectAdditionListFragment** (469 строк)
7. **MapDeliveryZoneFragment** (308 строк)
8. **CreateMenuProductFragment** (450 строк)
9. **EditMenuProductFragment** (570 строк)
10. **OrderDetailsFragment** (476 строк)
11. **StatisticDetailsFragment** (41 строка) - уже перенесен

---

## Порядок выполнения для каждого фрагмента

1. Прочитайте существующий Fragment файл из app модуля
2. Проверьте наличие ViewModel и DataState в presentation модуле
3. Создайте ViewState.kt с конвертацией из DataState
4. Создайте Route.kt по паттерну LoginRoute
5. Создайте navigation/[ScreenName]ScreenNavigation.kt
6. Обновите FoodDeliveryNavGraphBuilder.kt с новым маршрутом
7. Удалите старые Fragment и ViewState файлы из app модуля
8. Добавьте файлы в git (исключая build директории)

**ВАЖНО:** Проект не обязательно должен успешно собираться, главное - следовать архитектуре и добавить навигацию.
