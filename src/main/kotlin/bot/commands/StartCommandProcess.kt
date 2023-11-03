package bot.commands

import bot.Command
import bot.constants.ConstantsKeyboards
import bot.RuntimeStorage
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import kotlinx.coroutines.flow.first

class StartCommandProcess(
    private val api: Api
) : Command {
    override suspend fun BehaviourContext.process() {
        onCommand("start") { info ->
            api.getDataBaseList(info.chat.id.chatId).fold(
                onSuccess = { databases ->
                    if (databases.isNotEmpty()) {
                        sendTextMessage(
                            info.chat,
                            "Базы данных загружены",
                            replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(databases.map { it.name })
                        )
                    } else {
                        val host = waitText(
                            SendTextMessage(
                                info.chat.id,
                                "Приветствую! Добавьте вашу первую базу данных для начала работы. Введите host",
                            )
                        ).first().text
                        val port = waitText(
                            SendTextMessage(
                                info.chat.id,
                                "Введите port",
                            )
                        ).first().text
                        val database = waitText(
                            SendTextMessage(
                                info.chat.id,
                                "Введите имя базы данных",
                            )
                        ).first().text
                        val username = waitText(
                            SendTextMessage(
                                info.chat.id,
                                "Введите username для подключения к базе данных",
                            )
                        ).first().text
                        val password = waitText(
                            SendTextMessage(
                                info.chat.id,
                                "Введите password для подключения к базе данных",
                            )
                        ).first().text
                        sendTextMessage(info.chat, "Подождите, пробуем получить доступ...")
                        api.sendConfig(info.chat.id.chatId, host, port, database, username, password).fold(
                            onSuccess = {
                                RuntimeStorage.userRealtimeMap[info.chat.id.chatId] = false
                                sendTextMessage(
                                    info.chat,
                                    "Доступ успешно получен",
                                    replyMarkup = ConstantsKeyboards.dataBaseCommands
                                )
                            },
                            onFailure = {
                                sendTextMessage(
                                    info.chat,
                                    "Ошибка получения доступа, попробуйте снова добавить базу данных",
                                    replyMarkup = ConstantsKeyboards.onlyAddDatabase
                                )
                            }
                        )
                    }
                },
                onFailure = {
                    sendTextMessage(
                        info.chat,
                        "Ошибка получения доступа, попробуйте снова добавить базу данных",
                        replyMarkup = ConstantsKeyboards.onlyAddDatabase
                    )
                }
            )
        }
    }
}