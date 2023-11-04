package bot

import Dependencies
import bot.commands.*
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.kodein.di.instance

class BotManager {
    private val startCommandProcess: StartCommandProcess by Dependencies.di.instance()
    private val checkPointProcess: CheckPointCommandProcess by Dependencies.di.instance()
    private val onCheckPointProcess: OnCheckPointRealtimeCommandProcess by Dependencies.di.instance()
    private val offCheckPointProcess: OffCheckPointRealtimeCommandsProcess by Dependencies.di.instance()
    private val scope: CoroutineScope by Dependencies.di.instance()
    private val addDataBaseCommandProcess: AddDataBaseCommandProcess by Dependencies.di.instance()
    private val processInlineButtons: ProcessInlineButtons by Dependencies.di.instance()

    init {
        val bot = telegramBot("6597360611:AAFZQQfRVjVkOZAT1_Ux6z9atoCHeWNFqmo")
        runBlocking {
            bot.buildBehaviourWithLongPolling {
                println(getMe())
                startCommandProcess.start(this)
                //checkPointProcess.start(this)
                onCheckPointProcess.start(this)
                offCheckPointProcess.start(this)
                addDataBaseCommandProcess.start(this)
                processInlineButtons.start(this)
            }.join()
        }
    }
}