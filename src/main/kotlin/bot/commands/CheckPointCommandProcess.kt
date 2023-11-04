package bot.commands

import bot.Command
import bot.constants.ConstantsKeyboards
import bot.constants.ConstantsSting
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import dev.inmo.tgbotapi.extensions.utils.chatIdOrNull
import dev.inmo.tgbotapi.extensions.utils.fromUserOrNull
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.ChatIdentifier
import dev.inmo.tgbotapi.types.IdChatIdentifier
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import io.ktor.content.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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
                        replyMarkup = ConstantsKeyboards.dataBaseCommands
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
                        replyMarkup = ConstantsKeyboards.dataBaseCommands
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