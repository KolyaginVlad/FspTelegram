package bot.commands

import bot.Command
import bot.constants.ConstantsKeyboards
import data.Api
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand

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
                        sendTextMessage(
                            info.chat,
                            "Приветствую! Добавьте вашу первую базу данных для начала работы.",
                            replyMarkup = ConstantsKeyboards.selectAddDatabaseMethodKeyboard
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