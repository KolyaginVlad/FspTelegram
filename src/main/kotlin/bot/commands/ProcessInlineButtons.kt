package bot.commands

import bot.Command
import dev.inmo.tgbotapi.extensions.api.answers.answer
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onMessageDataCallbackQuery

class ProcessInlineButtons: Command {
    override suspend fun BehaviourContext.process() {
        onMessageDataCallbackQuery {
            sendTextMessage(it.message.chat, it.data)
            answer(it)
        }
    }
}