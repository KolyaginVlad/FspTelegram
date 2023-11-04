package bot.commands

import Dependencies
import bot.Command
import bot.RuntimeStorage
import bot.constants.ButtonType
import bot.constants.ConstantsKeyboards
import bot.constants.ConstantsString
import bot.constants.toButtonType
import data.Api
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
    private val addDatabaseSshCommandProcess: AddDatabaseSshCommandProcess by Dependencies.di.instance()
    private val addDatabaseConnectingStringCommandProcess: AddDatabaseConnectingStringCommandProcess by Dependencies.di.instance()
    private val addDatabaseFileCommandProcess: AddDatabaseFileCommandProcess by Dependencies.di.instance()
    private val processCustomQuery: ProcessCustomQuery by Dependencies.di.instance()
    private val metrixCommand: MetrixCommand by Dependencies.di.instance()
    private val vacuumCommandProcess: VacuumCommandProcess by Dependencies.di.instance()

    override suspend fun BehaviourContext.process() {
        onMessageDataCallbackQuery { message ->
            val args = message.data.split(ConstantsString.DELIMITER)
            println(args[0].toButtonType())
            if (args[0] == ButtonType.STOP_MONITORING.toString()) {
                RuntimeStorage.userRealtimeMap[message.message.chat.id.chatId] = false
            }
            if (RuntimeStorage.userRealtimeMap[message.message.chat.id.chatId] != true) {
                when (args[0].toButtonType()) {
                    ButtonType.SELECT_DATABASE -> {
                        sendTextMessage(
                            message.message.chat.id,
                            "Выберите действие",
                            replyMarkup = ConstantsKeyboards.getDataBasesCommands(args[1])
                        )
                    }

                    ButtonType.STOP_MONITORING -> {
                        RuntimeStorage.userRealtimeMap[message.message.chat.id.chatId] = false
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

                    ButtonType.BACK -> BACK(args.last(), this, message)
                    ButtonType.MAIN_OPTIONS -> TODO()
                    ButtonType.ADD_DB -> sendTextMessage(
                        message.message.chat,
                        ConstantsString.selectConnectMethod,
                        replyMarkup = ConstantsKeyboards.selectAddDatabaseMethodKeyboard
                    )

                    ButtonType.COMMAND -> TODO()
                    ButtonType.CUSTOM_QUERY -> {
                        processCustomQuery.start(this, message)
                    }

                    ButtonType.LOG_SETTINGS -> TODO()
                    ButtonType.SELECT_DATABASE_ADD -> selectDatabaseAdd(args[1], this, message)
                }
                answer(message)
            }
        }
    }

    private suspend fun selectDatabaseAdd(
        method: String,
        behaviourContext: BehaviourContext,
        message: MessageDataCallbackQuery
    ) {
        when (method) {
            "1" -> addDataBaseCommandProcess.start(behaviourContext, message)
            "2" -> addDatabaseSshCommandProcess.start(behaviourContext, message)
            "3" -> addDatabaseConnectingStringCommandProcess.start(behaviourContext, message)
            "4" -> addDatabaseFileCommandProcess.start(behaviourContext, message)
        }
    }

    private suspend fun DB_OPTIONS(
        method: String,
        database: String,
        message: MessageDataCallbackQuery,
        context: BehaviourContext
    ) {
        when (method) {
            "1" -> checkPointProcess.start(context, message, database)
            //  "2" -> checkPointDateProcess.start(context, message, database)
            "3" -> onCheckPointProcess.start(context, message, database)
            "4" -> TODO()
            "5" -> TODO()
            "6" -> metrixCommand.start(context, message, database)
            "7" -> vacuumCommandProcess.start(context, message, database)
            else -> {}
        }
    }

    suspend fun MAIN_OPTIONS(method: String) {
        when (method) {
            else -> {}
        }
    }

    suspend fun BACK(now: String, context: BehaviourContext, message: MessageDataCallbackQuery) {
        println("BACK $now")
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

            "ADD_DB" -> {

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