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
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import kotlinx.coroutines.flow.first

class CheckPointCommandProcess(
    private val api: Api
) : Command {
    override suspend fun BehaviourContext.process() {
        onText({
            it.content.text == ConstantsSting.checkPointBtn
        }) {
            val database = waitText(
                SendTextMessage(
                    it.chat.id,
                    ConstantsSting.enterDb,
                    replyMarkup = ConstantsKeyboards.empty
                )
            ).first().text
            api.checkPoint(it.chat.id.chatId, database).fold(
                onSuccess = { response ->
                    sendTextMessage(
                        it.chat.id,
                        response.toString(),
                        replyMarkup = ConstantsKeyboards.dataBaseCommands
                    )
                },
                onFailure = { error ->
                    sendTextMessage(
                        it.chat.id,
                        "Ошибка получения доступа",
                        replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                            api.getDataBaseList(it.chat.id.chatId).fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                        )
                    )
                }
            )
        }
        onText({
            it.content.text == ConstantsSting.checkPointDatetBtn
        }) {
            val database = waitText(
                SendTextMessage(
                    it.chat.id,
                    ConstantsSting.enterDb,
                )
            ).first().text
            val date = waitText(
                SendTextMessage(
                    it.chat.id,
                    ConstantsSting.enterDate,
                )
            ).first().text
            api.checkPointOnDate(it.chat.id.chatId, database, date).fold(
                onSuccess = { response ->
                    sendTextMessage(
                        it.chat.id,
                        response.toString(),
                        replyMarkup = ConstantsKeyboards.dataBaseCommands
                    )
                },
                onFailure = { error ->
                    sendTextMessage(
                        it.chat.id,
                        "Ошибка получения доступа",
                        replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                            api.getDataBaseList(it.chat.id.chatId).fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                        )
                    )
                }
            )
        }
    }
}