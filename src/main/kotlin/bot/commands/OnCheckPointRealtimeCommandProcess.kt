package bot.commands

import bot.Command
import bot.RuntimeStorage
import bot.constants.ConstantsKeyboards
import bot.constants.ConstantsSting
import data.Api
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OnCheckPointRealtimeCommandProcess(
    private val api: Api
): Command {
    override suspend fun BehaviourContext.process() {
        onText({
            it.content.text == ConstantsSting.onRealTime
        }) {
            RuntimeStorage.userRealtimeMap[it.chat.id.chatId] = true
            val database = waitText(
                SendTextMessage(
                    it.chat.id,
                    ConstantsSting.enterDb,
                    replyMarkup = ConstantsKeyboards.checkAndAddWithOffRealtime
                ),
            ).first().text
            launch {
                while (RuntimeStorage.userRealtimeMap[it.chat.id.chatId] == true) {
                    delay(15000)
                    api.checkPoint(it.chat.id.chatId, database)
                }
            }
        }
    }
}