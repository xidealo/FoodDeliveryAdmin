# API: POST `/client/push`

Ручка на бэкенде (`backPapa`). Документ — контракт для реализации на **FoodDeliveryAdmin**.

**Назначение:** менеджер/админ отправляет FCM-пуш одному клиенту своей компании по номеру телефона.

**Права:** JWT с ролью `MANAGER`, `COURIER` или `ADMIN`.

---

## Запрос

| | |
|---|---|
| **Method** | `POST` |
| **Path** | `/client/push` |
| **Auth** | `Authorization: Bearer <token>` |
| **Query** | `phoneNumber` — телефон клиента в формате `+7XXXXXXXXXX` (обязательный) |

### Body

```json
{
  "title": "Заголовок",
  "body": "Текст уведомления"
}
```

| Поле | Тип | Обязательное | Описание |
|------|-----|--------------|----------|
| `title` | `String` | да | Заголовок пуша, не пустой |
| `body` | `String` | да | Текст пуша, не пустой |

Модель на бэке / в admin data: `PostNotification` / `PostClientPushServer`.

### Пример

```http
POST /client/push?phoneNumber=+79001234567
Authorization: Bearer <manager_jwt>
Content-Type: application/json

{
  "title": "Акция",
  "body": "Скидка на комбо до конца дня"
}
```

---

## Ответ

**HTTP 200** — без тела.

---

## Ошибки

| Код | Когда |
|-----|--------|
| **401** | Нет / невалидный JWT |
| **403** | Роль не manager/courier/admin (например, client) |
| **400** | Нет query `phoneNumber`, битый body, пустые `title`/`body`, у клиента нет FCM-токена |
| **404** | Клиент не найден **или** клиент из другой компании менеджера (бэкенд маскирует mismatch как not found) |

Проверка компании: `company` менеджера из JWT должна совпадать с `company` клиента.

---

## Бизнес-логика (кратко)

1. По JWT менеджера берётся `companyUuid`.
2. По `phoneNumber` находится активный клиент этой компании.
3. Берётся `notificationToken` клиента и отправляется FCM notification (`title` + `body`).
4. Если токена нет — 400, пуш не считается успешным.

Связанные ручки:

- `PATCH /client/discount?phoneNumber=` — скидка по тому же идентификатору
- `POST /notification` — пуш на топик всей компании (не персональный)
- `POST /client/notification` — клиент шлёт пуш себе по JWT

---

## Чеклист реализации (Admin)

### Data

1. Request-модель `PostClientPushServer(title: String, body: String)`.
2. В `FoodDeliveryApi` / `FoodDeliveryApiImpl`:

```kotlin
suspend fun postClientPush(
    token: String,
    phoneNumber: String,
    body: PostClientPushServer,
): ApiResult<Unit>
```

```kotlin
post(
    path = "client/push",
    parameters = listOf("phoneNumber" to phoneNumber),
    body = body,
    token = token,
)
```

3. Метод в `ClientUserRepo` / `ClientUserRepository`.

### Domain

4. Валидации: телефон `^\+7[0-9]{10}$`, `title` и `body` не пустые.
5. Use case: отправить пуш по `phoneNumber` + `title` + `body`.

### Presentation / UI

6. Точка вызова: экран статистики клиента — карточка рядом со скидкой.
7. Поля заголовка и текста, действие «Отправить».
