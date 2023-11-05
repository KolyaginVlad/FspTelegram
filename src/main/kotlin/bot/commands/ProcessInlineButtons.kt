package bot.commands

import Dependencies
import bot.Command
import bot.RuntimeStorage
import bot.constants.ButtonType
import bot.constants.ConstantsKeyboards
import bot.constants.ConstantsString
import bot.constants.toButtonType
import data.Api
import data.foldMsg
import dev.inmo.tgbotapi.extensions.api.answers.answer
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onMessageDataCallbackQuery
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.queries.callback.MessageDataCallbackQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import org.kodein.di.instance

class ProcessInlineButtons(private val api: Api) : Command {
    private val checkPointProcess: CheckPointCommandProcess by Dependencies.di.instance()
    private val onCheckPointProcess: OnCheckPointRealtimeCommandProcess by Dependencies.di.instance()
    private val killTransactionCommandProcess: KillTransactionCommandProcess by Dependencies.di.instance()
    private val checkPointDateProcess: CheckPointDateCommandProcess by Dependencies.di.instance()
    private val scope: CoroutineScope by Dependencies.di.instance()
    private val changeMemoryCommandProcess: ChangeMemoryCommandProcess by Dependencies.di.instance()
    private val addDataBaseCommandProcess: AddDataBaseCommandProcess by Dependencies.di.instance()
    private val addDatabaseSshCommandProcess: AddDatabaseSshCommandProcess by Dependencies.di.instance()
    private val addDatabaseConnectingStringCommandProcess: AddDatabaseConnectingStringCommandProcess by Dependencies.di.instance()
    private val addDatabaseFileCommandProcess: AddDatabaseFileCommandProcess by Dependencies.di.instance()
    private val processCustomQuery: ProcessCustomQuery by Dependencies.di.instance()
    private val metrixCommand: MetrixCommand by Dependencies.di.instance()
    private val vacuumCommandProcess: VacuumCommandProcess by Dependencies.di.instance()
    private val getImageByLinkCommandProcess: GetImageByLinkCommandProcess by Dependencies.di.instance()
    private val showMemoryCommandProcess: ShowMemoryCommandProcess by Dependencies.di.instance()
    private val getLinksCommandProcess: GetLinksCommandProcess by Dependencies.di.instance()
    private val addLinkCommandProcess: AddLinkCommandProcess by Dependencies.di.instance()
    private val selectQueryCommandsProcess: SelectQueryCommandsProcess by Dependencies.di.instance()
    private val addQueryCommandProcess: AddQueryCommandProcess by Dependencies.di.instance()
    private val processQueryCommandProcess: ProcessQueryCommandProcess by Dependencies.di.instance()
    private val sshCheckDiskCommandProcess: SshCheckDiskCommandProcess by Dependencies.di.instance()
    private val sshLsofCommandProcess: SshLsofCommandProcess by Dependencies.di.instance()
    private val sshTcpDumpCommandProcess: SshTcpDumpCommandProcess by Dependencies.di.instance()
    private val addSshConnection: AddSshConnection by Dependencies.di.instance()
    private val addSshQueryCommandProcess: AddSshQueryCommandProcess by Dependencies.di.instance()
    private val removeSshQueryCommandProcess: RemoveSshQueryCommandProcess by Dependencies.di.instance()
    private val updateSshQueryCommandProcess: UpdateSshQueryCommandProcess by Dependencies.di.instance()
    private val executeSshQueryCommandProcess: ExecuteSshQueryCommandProcess by Dependencies.di.instance()

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

                    ButtonType.SSH -> {
                        when (args[1]) {
                            "1" -> sshCheckDiskCommandProcess.start(this, message, args.last())
                            "2" -> sshLsofCommandProcess.start(this, message, args.last())
                            "3" -> sshTcpDumpCommandProcess.start(this, message, args.last())
                            "4" -> sendTextMessage(
                                message.message.chat.id,
                                "Что вы хотите?",
                                replyMarkup = ConstantsKeyboards.getSshCrud(args[2])
                            )
                        }
                    }

                    ButtonType.STOP_MONITORING -> {
                        RuntimeStorage.userRealtimeMap[message.message.chat.id.chatId] = false
                        sendTextMessage(
                            message.message.chat.id,
                            "Выберите действие",
                            replyMarkup = ConstantsKeyboards.getDataBasesCommands(args.last())
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

                    ButtonType.BACK -> BACK(args.last(), this, message, args[args.lastIndex - 1])
                    ButtonType.MAIN_OPTIONS -> TODO()
                    ButtonType.ADD_DB -> sendTextMessage(
                        message.message.chat,
                        ConstantsString.selectConnectMethod,
                        replyMarkup = ConstantsKeyboards.selectAddDatabaseMethodKeyboard
                    )

                    ButtonType.COMMAND -> TODO()

                    ButtonType.LOG_SETTINGS -> TODO()
                    ButtonType.SELECT_DATABASE_ADD -> selectDatabaseAdd(args[1], this, message)
                    ButtonType.LINK -> when {
                        args[1] == "-1" -> {
                            sendTextMessage(
                                message.message.chat.id,
                                "Выберите действие",
                                replyMarkup = ConstantsKeyboards.getDataBasesCommands(args[2])
                            )
                        }

                        args[1] == "-2" -> {
                            addLinkCommandProcess.start(this, message, args[2])
                        }

                        else -> {
                            getImageByLinkCommandProcess.start(this, message, args[2])
                        }
                    }

                    ButtonType.MEMORY -> when (args[1]) {
                        "1" -> {
                            sendTextMessage(
                                message.message.chat.id,
                                "Выберите какой параметр хотите изменить",
                                replyMarkup = ConstantsKeyboards.getChangeMemory(args.last())
                            )
                        }

                        "2" -> {
                            showMemoryCommandProcess.start(this, message, args.last())
                        }
                    }

                    ButtonType.CHANGE_MEMORY -> {
                        changeMemoryCommandProcess.start(this, message, args.last(), args[1])
                    }

                    ButtonType.SELECT_QUERY -> selectQueryCommandsProcess.start(this, message, args[1])
                    ButtonType.SELECT_QUERY_NAME -> when {
                        args[2] == "-1" -> {
                            sendTextMessage(
                                message.message.chat.id,
                                "Выберите действие",
                                replyMarkup = ConstantsKeyboards.getDataBasesCommands(args[2])
                            )
                        }

                        args[2] == "-2" -> {
                            addQueryCommandProcess.start(this, message, args[1])
                        }

                        else -> {
                            processQueryCommandProcess.start(this, message, args[1])
                        }
                    }

                    ButtonType.SSH_CRUD -> sshCrud(this, message, args[1], args[2])
                    ButtonType.SSH_DELETE ->
                        api.removeSshQuery(message.message.chat.id.chatId, args[1], args[2])
                            .foldMsg(this, message, api, args[1])

                    ButtonType.SSH_UPDATE -> {
                        val newValue = waitText(
                            SendTextMessage(
                                message.message.chat.id,
                                "Введите новое значение"
                            )
                        ).first().text
                        api.updateSshQuery(message.message.chat.id.chatId, args[1], args[2], newValue)
                            .foldMsg(this, message, api, args[1])
                    }

                    ButtonType.SSH_EXECUTE ->
                        api.executeSshQuery(message.message.chat.id.chatId, args[1], args[2])
                            .foldMsg(this, message, api, args[1])
                }
                answer(message)
            }
        }
    }

    private suspend fun sshCrud(
        context: BehaviourContext,
        message: MessageDataCallbackQuery,
        method: String,
        database: String
    ) {
        when (method) {
            "1" -> {
                addSshQueryCommandProcess.start(context, message, database)
            }

            "2" -> {
                removeSshQueryCommandProcess.start(context, message, database)
            }

            "3" -> {
                updateSshQueryCommandProcess.start(context, message, database)
            }

            "4" -> {
                executeSshQueryCommandProcess.start(context, message, database)
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
            "9" -> {
                context.sendTextMessage(
                    message.message.chat.id,
                    "Выберите действие",
                    replyMarkup = ConstantsKeyboards.getMemory(database)
                )
            }

            "11" -> {
                api.hasSshConnections(message.message.chat.id.chatId, database).fold(
                    onSuccess = {
                        if (it) {
                            context.sendTextMessage(
                                message.message.chat.id,
                                "Выберите действие",
                                replyMarkup = ConstantsKeyboards.getSsh(database)
                            )
                        } else {
                            context.sendTextMessage(
                                message.message.chat.id,
                                "Добавьте соединение"
                            )
                            addSshConnection.start(context, message, database)
                        }
                    },
                    onFailure = {
                        context.sendTextMessage(
                            message.message.chat.id,
                            "Не удалось выполнить запрос",
                            replyMarkup = ConstantsKeyboards.getDataBasesCommands(database)
                        )
                    }
                )
            }

            "8" -> getLinksCommandProcess.start(context, message, database)
            else -> {}
        }
    }

    suspend fun MAIN_OPTIONS(method: String) {
        when (method) {
            else -> {}
        }
    }

    suspend fun BACK(now: String, context: BehaviourContext, message: MessageDataCallbackQuery, database: String) {
        println("BACK $now")
        when (now) {
            "CHANGE_MEMORY" -> {
                context.sendTextMessage(
                    message.message.chat.id,
                    "Выберите действие",
                    replyMarkup = ConstantsKeyboards.getMemory(database)
                )
            }

            "MEMORY" -> {
                context.sendTextMessage(
                    message.message.chat.id,
                    "Выберите действие",
                    replyMarkup = ConstantsKeyboards.getDataBasesCommands(database)
                )
            }

            "SSH_CRUD" -> {
                context.sendTextMessage(
                    message.message.chat.id,
                    "Выберите действие",
                    replyMarkup = ConstantsKeyboards.getSshCrud(database)
                )
            }

            "SSH" -> {
                context.sendTextMessage(
                    message.message.chat.id,
                    "Выберите действие",
                    replyMarkup = ConstantsKeyboards.getSsh(database)
                )
            }

            else -> {
                context.sendTextMessage(
                    message.message.chat.id,
                    "Выберите действие",
                    replyMarkup = ConstantsKeyboards.getDataBasesKeyBoard(
                        api.getDataBaseList(message.message.chat.id.chatId)
                            .fold(onSuccess = { it.map { it.name } }, onFailure = { listOf() })
                    )
                )
            }
        }
    }
}