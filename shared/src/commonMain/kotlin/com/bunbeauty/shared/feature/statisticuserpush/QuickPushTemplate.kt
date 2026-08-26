package com.bunbeauty.shared.feature.statisticuserpush

import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_statistic_user_push_new_menu
import fooddeliveryadmin.shared.generated.resources.action_statistic_user_push_rare_orders
import org.jetbrains.compose.resources.StringResource

/**
 * Шаблон быстрого пуша. Единственный источник текстов: из него берётся подпись кнопки,
 * блок с перечислением вариантов на экране и сам отправляемый пуш.
 */
enum class QuickPushTemplate(
    val buttonTextResource: StringResource,
    val titleVariants: List<String>,
    val bodyVariants: List<String>,
) {
    RARE_ORDERS(
        buttonTextResource = Res.string.action_statistic_user_push_rare_orders,
        titleVariants =
            listOf(
                "Давно не заказывали? Мы соскучились 💛",
                "У нас всё по-старому вкусно. Загляните снова",
                "Вернём вкусные вечера? Ваше меню ждёт вас",
            ),
        bodyVariants =
            listOf(
                "Вернитесь к своим любимым блюдам — мы уже всё приготовили, осталось нажать кнопку.",
            ),
    ),

    NEW_MENU(
        buttonTextResource = Res.string.action_statistic_user_push_new_menu,
        titleVariants =
            listOf(
                "Новинки в меню: попробуйте первым",
                "Свежие блюда уже в меню — загляните.",
                "Сегодня у нас премьера вкусов 🍽",
            ),
        bodyVariants =
            listOf(
                "Мы добавили новые позиции в вашем любимом разделе. " +
                    "Откройте приложение и выберите, что попробовать сегодня.",
            ),
    ),
}
