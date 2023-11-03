package bot

import Dependencies
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.kodein.di.instance

class BotManager {
    private val startCommandProcess: StartCommandProcess by Dependencies.di.instance()
    private val checkPointProcess: CheckPointCommandProcess by  Dependencies.di.instance()
    private val scope: CoroutineScope by Dependencies.di.instance()

    init {
        val bot = telegramBot("6597360611:AAFZQQfRVjVkOZAT1_Ux6z9atoCHeWNFqmo")
        runBlocking {
            bot.buildBehaviourWithLongPolling {
                println(getMe())
                startCommandProcess.start(this)
                checkPointProcess.start(this)
            }.join()
        }
    }
}