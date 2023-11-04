package bot.commands

import bot.Command
import bot.RuntimeStorage
import bot.constants.ConstantsKeyboards
import bot.constants.ConstantsString
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText

class OffCheckPointRealtimeCommandsProcess : Command {
    override suspend fun BehaviourContext.process() {
        onText({
            it.content.text == ConstantsString.offRealTime
        }) { info ->
            RuntimeStorage.userRealtimeMap[info.chat.id.chatId]= false
            sendTextMessage(
                info.chat,
                ConstantsString.realtimeOff,
                replyMarkup = ConstantsKeyboards.getDataBasesCommands("database")
            )
        }
    }
}