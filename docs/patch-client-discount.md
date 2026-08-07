# API: PATCH `/client/discount`

Ручка уже есть / будет на бэкенде (`backPapa`). Документ — контракт для реализации на **FoodDeliveryAdmin**.

**Назначение:** менеджер/админ выставляет клиенту персональную скидку (`percentDiscount`) или сбрасывает её (`0`).

**Права:** JWT с ролью `MANAGER`, `COURIER` или `ADMIN`.

---

## Запрос

| | |
|---|---|
| **Method** | `PATCH` |
| **Path** | `/client/discount` |
| **Auth** | `Authorization: Bearer <token>` |
| **Query** | `phoneNumber` — телефон клиента в формате `+7XXXXXXXXXX` (обязательный) |

### Body

```json
{
  "percentDiscount": 15
}
```

| Поле | Тип | Обязательное | Описание |
|------|-----|--------------|----------|
| `percentDiscount` | `Int` | да | `1..99` — установить скидку; `0` — очистить персональную скидку |

Модель на бэке / в admin data: `PatchClientUserDiscount` / `PatchClientUserDiscountServer`.

### Пример

```http
PATCH /client/discount?phoneNumber=+79001234567
Authorization: Bearer <manager_jwt>
Content-Type: application/json

{
  "percentDiscount": 15
}
```

---

## Ответ

**HTTP 200** — тело `GetClientSettings` (без обёртки list):

```json
{
  "uuid": "11111111-1111-1111-1111-111111111111",
  "phoneNumber": "+79001234567",
  "email": "user@example.com",
  "isActive": true,
  "isProblematic": false,
  "personalDiscountPercent": 15
}
```

| Поле | Тип | Описание |
|------|-----|----------|
| `uuid` | `String` | UUID клиента |
| `phoneNumber` | `String` | Телефон |
| `email` | `String?` | Email |
| `isActive` | `Boolean` | Активен ли клиент |
| `isProblematic` | `Boolean` | Флаг проблемного клиента |
| `personalDiscountPercent` | `Int?` | Персональная скидка после обновления; `null` если очищена |

По форме совпадает с элементом списка `GET /client/list` / `GET /client/search`, плюс поле `personalDiscountPercent`.

---

## Ошибки

| Код | Когда |
|-----|--------|
| **401** | Нет / невалидный JWT |
| **403** | Роль не manager/courier/admin (например, client) |
| **400** | Нет query `phoneNumber`, битый body, `percentDiscount` вне `0..99`, прочие ошибки |
| **404** | Клиент не найден **или** клиент из другой компании менеджера (бэкенд маскирует mismatch как not found) |

Проверка компании: `company` менеджера из JWT должна совпадать с `company` клиента.

---

## Бизнес-логика (кратко)

1. По JWT менеджера берётся `companyUuid`.
2. По `phoneNumber` находится клиент этой компании.
3. Обновляется персональная скидка:
   - `1..99` — сохранить значение;
   - `0` — очистить (`personalDiscountPercent = null`).
4. Возвращается обновлённый `GetClientSettings` с полем `personalDiscountPercent`.

### Семантика скидки

- **Разовая (one-time):** персональная скидка действует на один успешный заказ.
- **Приоритет:** персональная скидка имеет приоритет над скидкой компании на первый заказ (`company.percentDiscount`).
- **Очистка:** `percentDiscount = 0` снимает персональную скидку.
- **Списание:** после успешного создания заказа персональная скидка считается использованной и очищается на бэкенде.

Связанные ручки (уже используются в admin):

- `GET /client/list` → список `GetClientSettings`
- `GET /client/search?query=` → поиск
- `GET /client/statistic?uuid=` → статистика
- `PATCH /client/problematic` → флаг проблемного клиента

---

## Чеклист реализации (Admin)

### Data

1. Добавить `personalDiscountPercent: Int? = null` в `ClientUserSettingsServer` и domain `ClientUserSettings` (+ mapper).
2. Request-модель `PatchClientUserDiscountServer(percentDiscount: Int)`.
3. В `FoodDeliveryApi` / `FoodDeliveryApiImpl`:

```kotlin
suspend fun patchClientUserDiscount(
    token: String,
    phoneNumber: String,
    patch: PatchClientUserDiscountServer,
): ApiResult<ClientUserSettingsServer>
```

```kotlin
patch(
    path = "client/discount",
    parameters = listOf("phoneNumber" to phoneNumber),
    body = patch,
    token = token,
)
```

4. Метод в `ClientUserRepo` / `ClientUserRepository`.

### Domain

5. Валидации: телефон `^\+7[0-9]{10}$`, процент `0..99`.
6. Use case: обновить скидку по `phoneNumber` + `percentDiscount`, вернуть обновлённые settings.

### Presentation / UI

7. Точка вызова: экран статистики клиента / список клиентов — поле ввода процента и действие «сохранить / сбросить».
8. После успеха обновить локальный стейт (`personalDiscountPercent` в settings / списке).
