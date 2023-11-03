package bot.commands

import bot.CommandWithData
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery
import kotlinx.coroutines.flow.first

class ProcessCustomQuery : CommandWithData {

    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery) {
        val intPlaceholder = "&i"
        val stringPlaceholder = "&s"

//        val query = data
        var query = "select * from table where id = &i"
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
                "Результат заполнения: $query. Всё верно? (Y/n)"
            )
        ).first().text
        if (ans.lowercase() == "y") {
            //TODO api.sendCustomQuery(query)
        } else {
            process(data)
        }
    }

}