package bot.commands

import bot.Command
import bot.constants.ConstantsKeyboards
import bot.constants.ConstantsSting
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.IdChatIdentifier
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CheckPointCommandProcess(
    private val api: Api
):Command  {
    fun checkPoint(chatId: IdChatIdentifier,database:String, context: BehaviourContext) {
        runBlocking {
            api.checkPoint(chatId.chatId, database).fold(
                onSuccess = { response ->
                    context.sendTextMessage(
                        chatId,
                        response.toString(),
                        replyMarkup = ConstantsKeyboards.getDataBasesCommands(database)
                    )
                },
                onFailure = { error ->
                    context.sendTextMessage(
                        chatId,
                        "Ошибка получения доступа",
                        replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                            api.getDataBaseList(chatId.chatId).fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                        )
                    )
                }
            )
        }
    }

    fun checkPointDate(chatId: IdChatIdentifier, database:String,context: BehaviourContext) {
        runBlocking {
            val date = context.waitText(
                SendTextMessage(
                    chatId,
                    ConstantsSting.enterDate,
                )
            ).first().text
            api.checkPointOnDate(chatId.chatId, database, date).fold(
                onSuccess = { response ->
                    context.sendTextMessage(
                        chatId,
                        response.toString(),
                        replyMarkup = ConstantsKeyboards.getDataBasesCommands(database)
                    )
                },
                onFailure = { error ->
                    context.sendTextMessage(
                        chatId,
                        "Ошибка получения доступа",
                        replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                            api.getDataBaseList(chatId.chatId).fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                        )
                    )
                }
            )
        }
    }

    override suspend fun BehaviourContext.process() {}
    // }
}