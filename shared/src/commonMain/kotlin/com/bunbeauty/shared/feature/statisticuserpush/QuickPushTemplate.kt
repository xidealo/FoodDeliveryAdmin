package com.bunbeauty.shared.feature.statisticuserpush

import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_statistic_user_push_new_menu
import fooddeliveryadmin.shared.generated.resources.action_statistic_user_push_rare_orders
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_push_new_menu_body_1
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_push_new_menu_title_1
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_push_new_menu_title_2
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_push_new_menu_title_3
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_push_rare_orders_body_1
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_push_rare_orders_title_1
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_push_rare_orders_title_2
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_push_rare_orders_title_3
import org.jetbrains.compose.resources.StringResource

/**
 * Шаблон быстрого пуша. Единственный источник текстов: из него берётся подпись кнопки,
 * блок с перечислением вариантов на экране и сам отправляемый пуш.
 */
enum class QuickPushTemplate(
    val buttonTextResource: StringResource,
    val titleVariants: List<StringResource>,
    val bodyVariants: List<StringResource>,
) {
    RARE_ORDERS(
        buttonTextResource = Res.string.action_statistic_user_push_rare_orders,
        titleVariants =
            listOf(
                Res.string.msg_statistic_user_push_rare_orders_title_1,
                Res.string.msg_statistic_user_push_rare_orders_title_2,
                Res.string.msg_statistic_user_push_rare_orders_title_3,
            ),
        bodyVariants =
            listOf(
                Res.string.msg_statistic_user_push_rare_orders_body_1,
            ),
    ),

    NEW_MENU(
        buttonTextResource = Res.string.action_statistic_user_push_new_menu,
        titleVariants =
            listOf(
                Res.string.msg_statistic_user_push_new_menu_title_1,
                Res.string.msg_statistic_user_push_new_menu_title_2,
                Res.string.msg_statistic_user_push_new_menu_title_3,
            ),
        bodyVariants =
            listOf(
                Res.string.msg_statistic_user_push_new_menu_body_1,
            ),
    ),
}
