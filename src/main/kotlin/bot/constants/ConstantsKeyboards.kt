package bot.constants

import dev.inmo.tgbotapi.types.buttons.SimpleKeyboardButton
import dev.inmo.tgbotapi.utils.matrix
import dev.inmo.tgbotapi.utils.row

object ConstantsKeyboards {

    val empty = dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup(
        matrix {
            row {
            }
        }
    )

    val onlyAddDatabase = dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup(
        matrix {
            row {
                +SimpleKeyboardButton(ConstantsSting.addDatabase)
            }
        },
        resizeKeyboard = true
    )

    val checkAndAddWithOnRealtime = dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup(
        matrix {
            row {
                +SimpleKeyboardButton(ConstantsSting.addDatabase)
                +SimpleKeyboardButton(ConstantsSting.checkPointBtn)
                +SimpleKeyboardButton(ConstantsSting.checkPoinDatetBtn)
                +SimpleKeyboardButton(ConstantsSting.onRealTime)
            }
        },
        resizeKeyboard = true
    )

    val checkAndAddWithOffRealtime = dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup(
        matrix {
            row {
                +SimpleKeyboardButton(ConstantsSting.addDatabase)
                +SimpleKeyboardButton(ConstantsSting.checkPointBtn)
                +SimpleKeyboardButton(ConstantsSting.checkPoinDatetBtn)
                +SimpleKeyboardButton(ConstantsSting.offRealTime)
            }
        },
        resizeKeyboard = true
    )
}