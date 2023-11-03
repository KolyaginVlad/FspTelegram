package bot

import data.Api
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import kotlinx.coroutines.flow.first

class CheckPointCommandProcess(
    private val api: Api
) : Command {
    override suspend fun BehaviourContext.process() {
        onCommand(ConstantsSting.checkPointBtn) {
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
           val response = api.checkPointOnDate( it.chat.id.chatId, database,date)
            SendTextMessage(
                it.chat.id,
                response.toString(),
            )
        }
        onCommand(ConstantsSting.checkPoinDatetBtn) {
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
            val response = api.checkPointOnDate( it.chat.id.chatId, database,date)
            SendTextMessage(
                it.chat.id,
                response.toString(),
            )
        }
    }
}