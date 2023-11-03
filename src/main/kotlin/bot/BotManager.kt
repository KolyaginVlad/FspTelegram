package bot

import Dependencies
import data.HttpRequester
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.*
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import com.github.kotlintelegrambot.extensions.filters.Filter
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.instance

class BotManager {
    private val buttons = listOf(
        listOf(KeyboardButton(ConstantsSting.firstButton)),
        listOf(KeyboardButton("Request contact")),
    )
    private val httpRequester: HttpRequester by Dependencies.di.instance()
    private val scope: CoroutineScope by Dependencies.di.instance()

    init {
        val bot = bot {
            token = "6597360611:AAFZQQfRVjVkOZAT1_Ux6z9atoCHeWNFqmo"
            dispatch {
                message(Filter.Reply) {
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = update.message?.text.toString())
                }
                command("start") {
                    val keyboardMarkup = KeyboardReplyMarkup(keyboard = buttons, resizeKeyboard = true)
                    bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        text = ConstantsSting.startMsg,
                        replyMarkup = keyboardMarkup,
                    ).fold(
                        ifSuccess = { RuntimeStorage.userIdList.add(it.chat.id) },
                        ifError = { bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = ConstantsSting.errorMsg) }
                    )
                }
                telegramError {
                    RuntimeStorage.userIdList.forEach {
                        bot.sendMessage(chatId = ChatId.fromId(it), text = ConstantsSting.errorMsg)
                    }
                }
                text(ConstantsSting.firstButton){
                    bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        text ="Hui",
                    )
                    text {
                        bot.sendMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            text ="Understand",
                        )
                    }
                }
            }
        }
        bot.startPolling()
    }
}
/* import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.pollAnswer
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton

fun main() {
    val bot = bot {
        token = "6597360611:AAFZQQfRVjVkOZAT1_Ux6z9atoCHeWNFqmo"
        dispatch {
            command("start") {
                val keyboardMarkup = KeyboardReplyMarkup(keyboard = generateUsersButton(), resizeKeyboard = true)
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = "Hello, inline buttons!",
                    replyMarkup = keyboardMarkup,
                ).fold{

                }
            }
            command("enter_server_data") {
                bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Enter server data!")
                bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = this.toString())
            }
        }
    }
    bot.startPolling()
}

fun generateUsersButton(): List<List<KeyboardButton>> {
    return listOf(
        listOf(KeyboardButton("Request location (not supported on desktop)", requestLocation = true)),
        listOf(KeyboardButton("Request contact", requestContact = true)),
    )
}*/