package bot.commands

import bot.Command
import bot.RuntimeStorage
import bot.User
import bot.constants.ButtonType
import bot.constants.ConstantsKeyboards
import bot.constants.ConstantsSting
import bot.constants.toButtonType
import dev.inmo.tgbotapi.extensions.api.answers.answer
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onMessageDataCallbackQuery
import dev.inmo.tgbotapi.extensions.utils.types.buttons.dataButton
import dev.inmo.tgbotapi.types.IdChatIdentifier
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.instance

class ProcessInlineButtons : Command {
    private val checkPointProcess: CheckPointCommandProcess by Dependencies.di.instance()
    private val onCheckPointProcess: OnCheckPointRealtimeCommandProcess by Dependencies.di.instance()
    private val offCheckPointProcess: OffCheckPointRealtimeCommandsProcess by Dependencies.di.instance()
    private val scope: CoroutineScope by Dependencies.di.instance()
    private val addDataBaseCommandProcess: AddDataBaseCommandProcess by Dependencies.di.instance()
    private val processInlineButtons: ProcessInlineButtons by Dependencies.di.instance()
    override suspend fun BehaviourContext.process() {
        onMessageDataCallbackQuery { message ->
            val args = message.data.split(" ")
            println(args[0].toButtonType())
            when (args[0].toButtonType()) {
                ButtonType.SELECT_DATABASE -> {
                    message.message.chat.id.takeIf { id ->
                        RuntimeStorage.userData.none { it.id == id.chatId }
                    }?.let { id -> RuntimeStorage.userData.add(User(id.chatId, args[1])) }

                    //RuntimeStorage.userData.first { it.id == message.message.chat.id.chatId }.currentDb = args[1]
                        // todo добавить
                    println(RuntimeStorage.userData)
                    sendTextMessage( // Todo Не открывается
                        message.message.chat,
                        "Выберите действие",
                        replyMarkup = ConstantsKeyboards.dataBaseCommands
                    )
                }

                ButtonType.DB_OPTIONS -> RuntimeStorage.userData.find { user -> user.id == message.message.chat.id.threadId }?.let { it1 ->
                    DB_OPTIONS(
                        args[1],
                        it1.currentDb,
                        message.message.chat.id,
                        this
                    )
                }

                ButtonType.BACK -> TODO()
                ButtonType.MAIN_OPTIONS -> TODO()
                ButtonType.ADD_DB -> TODO()
                ButtonType.COMMAND -> TODO()
            }
            answer(message)
        }
    }

    fun DB_OPTIONS(method: String, dataBase: String, chatId: IdChatIdentifier, context: BehaviourContext) = when (method) {
        ConstantsSting.checkPointBtn -> checkPointProcess.checkPoint(chatId, dataBase, context)
        ConstantsSting.checkPointDatetBtn -> checkPointProcess.checkPointDate(chatId, dataBase, context)
        ConstantsSting.onRealTime -> TODO()
        ConstantsSting.backBtn -> TODO()
        ConstantsSting.monitorCommon -> TODO()
        else -> {}
    }

    fun MAIN_OPTIONS(method: String) = when (method) {
        else -> {}
    }
}