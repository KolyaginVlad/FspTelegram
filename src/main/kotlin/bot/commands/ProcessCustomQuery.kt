package bot.commands

import bot.constants.ConstantsKeyboards
import data.Api
import data.foldMsg
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery
import kotlinx.coroutines.flow.first

class ProcessCustomQuery(
    private val api: Api
) {

    suspend fun process(
        context: BehaviourContext,
        data: MessageDataCallbackQuery,
        queryTemplate: String,
        database: String
    ) {
        with(context) {
            val intPlaceholder = "&i"
            val stringPlaceholder = "&s"
            var query = queryTemplate
            var placeholderIndex = query.indexOfFirst { it == '&' }
            while (placeholderIndex != -1) {
                if (placeholderIndex + 2 < query.length) {
                    val placeholderContent = query.substring(placeholderIndex, placeholderIndex + 2)
                    if (placeholderContent != intPlaceholder && placeholderContent != stringPlaceholder) {
                        sendMessage(data.message.chat, "Ошибка обработки запроса")
                        return
                    }
                    val placeholder = waitText(
                        SendTextMessage(
                            data.message.chat.id,
                            "Введите заполнитель для $placeholderContent"
                        )
                    ).first().text
                    if (placeholderContent == intPlaceholder) {
                        var parsePlaceholder = placeholder.toIntOrNull()
                        while (parsePlaceholder == null) {
                            parsePlaceholder = waitText(
                                SendTextMessage(
                                    data.message.chat.id,
                                    "Ошибка обработки запроса, попробуйте ввести целое число ещё раз"
                                )
                            ).first().text.toIntOrNull()
                        }
                        query = query.replaceFirst(placeholderContent, parsePlaceholder.toString())
                    } else {
                        query = query.replaceFirst(placeholderContent, placeholder)
                    }
                    placeholderIndex = query.indexOfFirst { it == '&' }
                } else {
                    sendMessage(data.message.chat, "Ошибка обработки запроса")
                    return
                }
            }
            val ans = waitText(
                SendTextMessage(
                    data.message.chat.id,
                    "Результат заполнения: $query. Всё верно?",
                    replyMarkup = ConstantsKeyboards.simpleAnswerKeyboard
                )
            ).first().text
            if (ans.lowercase() == "Да") {
                api.sendCustomQuery(query, data.message.chat.id.chatId, database).foldMsg(this, data, api, database)
            } else {
                process(this, data, queryTemplate, database)
            }
        }
    }

}