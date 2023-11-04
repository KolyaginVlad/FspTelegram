package bot.commands

import Dependencies
import bot.Command
import bot.constants.ButtonType
import bot.constants.ConstantsKeyboards
import bot.constants.ConstantsSting
import bot.constants.toButtonType
import data.Api
import data.foldMsg
import dev.inmo.tgbotapi.extensions.api.answers.answer
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onMessageDataCallbackQuery
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.instance

class ProcessInlineButtons(private val api: Api) : Command {
    private val checkPointProcess: CheckPointCommandProcess by Dependencies.di.instance()
    private val onCheckPointProcess: OnCheckPointRealtimeCommandProcess by Dependencies.di.instance()
    private val offCheckPointProcess: OffCheckPointRealtimeCommandsProcess by Dependencies.di.instance()
    private val killTransactionCommandProcess: KillTransactionCommandProcess by Dependencies.di.instance()
    private val checkPointDateProcess: CheckPointDateCommandProcess by Dependencies.di.instance()
    private val scope: CoroutineScope by Dependencies.di.instance()
    private val addDataBaseCommandProcess: AddDataBaseCommandProcess by Dependencies.di.instance()
    private val processCustomQuery: ProcessCustomQuery by Dependencies.di.instance()
    private val metrixCommand: MetrixCommand by Dependencies.di.instance()
    override suspend fun BehaviourContext.process() {
        onMessageDataCallbackQuery { message ->
            val args = message.data.split(ConstantsSting.DELIMITER)
            println(args[0].toButtonType())
            when (args[0].toButtonType()) {
                ButtonType.SELECT_DATABASE -> {
                    sendTextMessage(
                        message.message.chat.id,
                        "Выберите действие",
                        replyMarkup = ConstantsKeyboards.getDataBasesCommands(args[1])
                    )
                }

                ButtonType.DB_OPTIONS -> {
                    println(args[2])
                    DB_OPTIONS(
                        args[2],
                        args[1],
                        message,
                        this
                    )
                }

                ButtonType.REPAIR -> {
                    when (args[1]) {
                        "-1" -> killTransactionCommandProcess.start(this, message, args[2])
                        else -> {}
                    }
                }

                ButtonType.BACK -> BACK(args[2], this, message)

                ButtonType.MAIN_OPTIONS -> TODO()
                ButtonType.ADD_DB -> addDataBaseCommandProcess.addDataBase(
                    message.message.chat.id,
                    this
                )

                ButtonType.COMMAND -> TODO()
                ButtonType.CUSTOM_QUERY -> {
                    processCustomQuery.start(this, message)
                }

                ButtonType.LOG_SETTINGS -> TODO()
            }
            answer(message)
        }
    }

    private suspend fun DB_OPTIONS(method: String, dataBase: String, message: MessageDataCallbackQuery, context: BehaviourContext) {
        when (method) {
            "1" -> checkPointProcess.start(context, message, dataBase)
            "2" -> checkPointDateProcess.start(context, message, dataBase)
            "3" -> onCheckPointProcess.start(context)
            "4" -> TODO()
            "5" -> TODO()
            "6" -> metrixCommand.start(context, message, dataBase)
            "7" -> api.vacuum(message.message.chat.id.chatId, dataBase).foldMsg(context, message, api, dataBase)
            else -> {}
        }
    }

    suspend fun MAIN_OPTIONS(method: String) {
        when (method) {
            else -> {}
        }
    }

    suspend fun REPAIR(method: String, dataBase: String, message: MessageDataCallbackQuery, context: BehaviourContext) {

    }

    suspend fun BACK(now: String, context: BehaviourContext, message: MessageDataCallbackQuery) {
        when (now) {
            "DB_OPTIONS" -> {
                context.sendTextMessage(
                    message.message.chat.id,
                    "Выберите действие",
                    replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                        api.getDataBaseList(message.message.chat.id.chatId)
                            .fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                    )
                )
            }

            else -> {}
        }
    }
}