package data

import bot.constants.ConstantsKeyboards
import data.models.CheckPointDto
import data.models.DataBaseResponseDto
import data.models.MetrixDto
import data.models.VacuumDto
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

    suspend fun checkPoint(userId: Long, dataBase: String): Result<List<CheckPointDto>>
    suspend fun checkPointOnDate(userId: Long, dataBase: String, date: String): Result<Unit>

    suspend fun getDataBaseList(userId: Long): Result<List<DataBaseResponseDto>>

    suspend fun killTransaction(userId: Long, database: String): Result<Unit>

    suspend fun getMetrix(dataBase: String,userId: Long): Result<MetrixDto>

    suspend fun vacuum(userId: Long, dataBase: String): Result<VacuumDto>

    suspend fun link(userId: Long, database: String): Result<List<String>>

    suspend fun visual(userId: Long, database: String, link: String): Result<String>

    suspend fun connectBySsh(userId: Long, ssh: String): Result<Unit>

    suspend fun connectByConnectionString(userId: Long, connectionString: String): Result<Unit>

    suspend fun createUrl(name: String, url: String, chatId: Long, database: String): Result<Unit>

}


suspend fun Result<Unit>.foldMsg(context: BehaviourContext, data: MessageDataCallbackQuery, api: Api, dataBase: String) = this.fold(
    onSuccess = { response ->
        context.sendTextMessage(
            data.message.chat.id,
            "Операция выполнена",
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
