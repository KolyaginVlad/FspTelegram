package bot

import dev.inmo.tgbotapi.types.buttons.SimpleKeyboardButton
import dev.inmo.tgbotapi.utils.matrix
import dev.inmo.tgbotapi.utils.row

object ConstantsKeyboards {
    val onlyAddDatabase = dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup(
        matrix {
            row {
                +SimpleKeyboardButton("Добавить базу данных")
            }
        }
    )

    val checkAndAddWithOnRealtime = dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup(
        matrix {
            row {
                +SimpleKeyboardButton("Добавить базу данных")
                +SimpleKeyboardButton("Проверить текущее состояние базы данных")
                +SimpleKeyboardButton("Показать состояние базы данных в момент времени")
                +SimpleKeyboardButton("Включить realtime отображение состояния базы данных")
            }
        }
    )

    val checkAndAddWithOffRealtime = dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup(
        matrix {
            row {
                +SimpleKeyboardButton("Добавить базу данных")
                +SimpleKeyboardButton("Проверить текущее состояние базы данных")
                +SimpleKeyboardButton("Показать состояние базы данных в момент времени")
                +SimpleKeyboardButton("Выключить realtime отображение состояния базы данных")
            }
        }
    )
}