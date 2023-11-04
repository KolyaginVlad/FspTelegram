package data

import bot.constants.ConstantsKeyboards
import data.models.DataBaseResponseDto
import data.models.MetrixDto
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery


interface Api {

    suspend fun sendConfig(
        userId: Long,
        name: String,
        host: String,
        port: String,
        database: String,
        username: String,
        password: String
    ): Result<Unit>

    suspend fun checkPoint(userId: Long, dataBase: String): Result<Unit>
    suspend fun checkPointOnDate(userId: Long, dataBase: String, date: String): Result<Unit>

    suspend fun getDataBaseList(userId: Long): Result<List<DataBaseResponseDto>>

    suspend fun killTransaction(userId: Long, database: String): Result<Unit>

    suspend fun getMetrix(dataBase: String): Result<MetrixDto>

    suspend fun vacuum(userId: Long, dataBase: String): Result<Unit>

}

suspend fun Result<Unit>.foldMsg(context: BehaviourContext, data: MessageDataCallbackQuery, api: Api, dataBase: String) = this.fold(
    onSuccess = { response ->
        context.sendTextMessage(
            data.message.chat.id,
            "Процесс был завершён",
            replyMarkup = ConstantsKeyboards.getDataBasesCommands(dataBase)
        )
    },
    onFailure = { error ->
        context.sendTextMessage(
            data.message.chat.id,
            "Ошибка получения доступа",
            replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                api.getDataBaseList(data.message.chat.id.chatId)
                    .fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
            )
        )
    }
)
