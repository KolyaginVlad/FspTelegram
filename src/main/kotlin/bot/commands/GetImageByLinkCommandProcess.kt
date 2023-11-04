package bot.commands

import bot.CommandWithDataDataBase
import bot.constants.ConstantsKeyboards
import bot.constants.ConstantsString
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.media.sendPhoto
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery
import java.io.File
import java.time.LocalDateTime
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class GetImageByLinkCommandProcess(
    private val api: Api
): CommandWithDataDataBase {
    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun BehaviourContext.process(data: MessageDataCallbackQuery, database: String) {
        sendTextMessage(
            data.message.chat.id,
            "Получаем изображение...",
        )
        api.visual(data.message.chat.id.chatId, database, data.data.split(ConstantsString.DELIMITER)[1]).fold(
            onSuccess = {
                runCatching {
                    val file = File("/tmp_image${LocalDateTime.now()}").apply {
                        createNewFile()
                    }
                    val imageBytes = Base64.decode(it)
                    file.writeBytes(imageBytes)
                    sendPhoto(
                        data.message.chat.id,
                        file.asMultipartFile(),
                        replyMarkup = ConstantsKeyboards.getDataBasesCommands(database)
                    )

                    file.delete()
                }.onFailure { error ->
                    sendTextMessage(
                        data.message.chat.id,
                        "Ошибка отправки файла",
                        replyMarkup = ConstantsKeyboards.getDataBasesCommands(database)
                    )
                }
            },
            onFailure = { error ->
                sendTextMessage(
                    data.message.chat.id,
                    "Ошибка получения доступа",
                    replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                        api.getDataBaseList(data.message.chat.id.chatId)
                            .fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                    )
                )
            }
        )
    }
}