package bot.commands

import bot.CommandWithData
import bot.constants.ConstantsKeyboards
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery
import kotlinx.coroutines.flow.first

class AddSshConnection(
    private val api: Api
) : CommandWithData {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery) {
        val ip = waitText(
            SendTextMessage(
                data.message.chat.id,
                "Введите ip",
            )
        ).first().text
        val port = waitText(
            SendTextMessage(
                data.message.chat.id,
                "Введите port",
            )
        ).first().text
        val username = waitText(
            SendTextMessage(
                data.message.chat.id,
                "Введите username для подключения к базе данных",
            )
        ).first().text
        val password = waitText(
            SendTextMessage(
                data.message.chat.id,
                "Введите password для подключения к базе данных",

                )
        ).first().text
        val credentialId = waitText(
            SendTextMessage(
                data.message.chat.id,
                "Введите username для подключения к базе данных",
            )
        ).first().text
        sendTextMessage(data.message.chat.id, "Подождите, пробуем получить доступ...")
        api.addSshConnection(
            userId = data.message.chat.id.chatId,
            ip = ip,
            port = port,
            username = username,
            password = password,
            credentialId = credentialId.toLong()
        ).fold(
            // TODO  добавить параметры
            onSuccess = {
                sendTextMessage(
                    data.message.chat.id,
                    "Доступ успешно получен",
                    replyMarkup = ConstantsKeyboards.sshConnections(
                        api.getSshConnections(data.message.chat.id.chatId).fold(onSuccess = { it.map { it } }, onFailure = { listOf() })
                    )
                )
            },
            onFailure = {
                sendTextMessage(
                    data.message.chat.id,
                    "Ошибка получения доступа",
                    replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                        api.getDataBaseList(data.message.chat.id.chatId).fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                    )
                )
            }
        )
    }
}