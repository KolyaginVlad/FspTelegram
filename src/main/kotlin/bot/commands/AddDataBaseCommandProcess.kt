package bot.commands

import bot.CommandWithData
import bot.constants.ConstantsKeyboards
import bot.constants.ConstantsKeyboards.empty
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.IdChatIdentifier
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery
import kotlinx.coroutines.flow.first

class AddDataBaseCommandProcess(
    private val api: Api
) : CommandWithData {
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery) {
        addDataBase(data.message.chat.id, this)
        sendTextMessage(
            data.message.chat,
            "Выберите действие",
            replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                api.getDataBaseList(data.message.chat.id.chatId).fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
            )
        )
    }
    suspend fun addDataBase(chatId: IdChatIdentifier, context: BehaviourContext) {
        val name = context.waitText(
            SendTextMessage(
                chatId,
                "Введите имя базы данных",
               // replyMarkup = addDBBack
            )
        ).first().text
        //if (name == "назад") return
        val host = context.waitText(
            SendTextMessage(
                chatId,
                "Введите host",
               // replyMarkup = addDBBack
            )
        ).first().text
        if (host == "назад") return
        val port = context.waitText(
            SendTextMessage(
                chatId,
                "Введите port",
               // replyMarkup = addDBBack
            )
        ).first().text
       // if (port == "назад") return
        val database = context.waitText(
            SendTextMessage(
                chatId,
                "Введите database",
               // replyMarkup = addDBBack
            )
        ).first().text
        //if (database == "назад") return
        val username = context.waitText(
            SendTextMessage(
                chatId,
                "Введите username для подключения к базе данных",
             //   replyMarkup = addDBBack
            )
        ).first().text
        //if (username == "назад") return
        val password = context.waitText(
            SendTextMessage(
                chatId,
                "Введите password для подключения к базе данных",
            //    replyMarkup = addDBBack
            )
        ).first().text
       // if (password == "назад") return
        context.sendTextMessage(chatId, "Подождите, пробуем получить доступ...")
        api.sendConfig(chatId.chatId, name, host, port, database, username, password).fold(
            onSuccess = {
                context.sendTextMessage(
                    chatId,
                    "Доступ успешно получен",
                    replyMarkup = empty
                )
            },
            onFailure = {
                context.sendTextMessage(
                    chatId,
                    "Ошибка получения доступа, попробуйте снова добавить базу данных",
                    replyMarkup = empty
                )
            }
        )
    }
}



