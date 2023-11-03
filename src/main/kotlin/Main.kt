import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.pollAnswer
import com.github.kotlintelegrambot.entities.ChatId

fun main() {
    val bot = bot {
        token = "6597360611:AAFZQQfRVjVkOZAT1_Ux6z9atoCHeWNFqmo"
        dispatch {
            command("start") {
                val result = bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Hi there!")
                result.fold({
                    // do something here with the response
                }, {
                    // do something with the error
                })
            }
            command("enter_server_data") {
                bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Enter server data!")
                pollAnswer {

                    bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id), text = update.message.toString()
                    )
                }
            }
        }
    }
    bot.startPolling()
}