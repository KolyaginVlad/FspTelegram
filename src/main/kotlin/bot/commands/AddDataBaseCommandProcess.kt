package bot.commands

import bot.Command
import bot.constants.ConstantsKeyboards
import bot.constants.ConstantsSting
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.IdChatIdentifier
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AddDataBaseCommandProcess(
    private val api: Api
) : Command {
    override suspend fun BehaviourContext.process() {}
    fun addDataBase(chatId: IdChatIdentifier, context: BehaviourContext) {
        runBlocking {
            val name = context.waitText(
                SendTextMessage(
                    chatId,
                    "Введите имя базы данных"
                )
            ).first().text
            val host = context.waitText(
                SendTextMessage(
                    chatId,
                    "Введите host"
                )
            ).first().text
            val port = context.waitText(
                SendTextMessage(
                    chatId,
                    "Введите port",
                )
            ).first().text
            val database = context.waitText(
                SendTextMessage(
                    chatId,
                    "Введите database",
                )
            ).first().text
            val username = context.waitText(
                SendTextMessage(
                    chatId,
                    "Введите username для подключения к базе данных",
                )
            ).first().text
            val password = context.waitText(
                SendTextMessage(
                    chatId,
                    "Введите password для подключения к базе данных",
                )
            ).first().text
            context.sendTextMessage(chatId, "Подождите, пробуем получить доступ...")
            api.sendConfig(chatId.chatId, name, host, port, database, username, password).fold(
                onSuccess = {
                    context.sendTextMessage(
                        chatId,
                        "Доступ успешно получен",
                        replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                            api.getDataBaseList(chatId.chatId).fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                        )
                    )
                },
                onFailure = {
                   context.sendTextMessage(
                        chatId,
                        "Ошибка получения доступа, попробуйте снова добавить базу данных",
                        replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                            api.getDataBaseList(chatId.chatId).fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                        )
                    )
                }
            )
        }
    }
}

