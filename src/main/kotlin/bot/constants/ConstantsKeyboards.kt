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

    val dataBaseCommands = dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup(
        matrix {
            row {
                +SimpleKeyboardButton(ConstantsSting.checkPointBtn)
            }
            row {
                +SimpleKeyboardButton(ConstantsSting.checkPointDatetBtn)
            }
            row {
                +SimpleKeyboardButton(ConstantsSting.onRealTime)
            }
            row {
                +SimpleKeyboardButton(ConstantsSting.backBtn)
            }
            row{
                +SimpleKeyboardButton(ConstantsSting.monitorCommon)
            }
        },
        resizeKeyboard = true
    )

    val checkAndAddWithOffRealtime = dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup(
        matrix {
            row {
                +SimpleKeyboardButton(ConstantsSting.addDatabase)
                +SimpleKeyboardButton(ConstantsSting.checkPointBtn)
                +SimpleKeyboardButton(ConstantsSting.checkPointDatetBtn)
                +SimpleKeyboardButton(ConstantsSting.offRealTime)
            }
        },
        resizeKeyboard = true
    )

    fun getDataBasesKeyBoard(dataBases: List<String>) = dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup(
        matrix {
            dataBases.forEach {
                row {
                    +SimpleKeyboardButton(it)
                }
            }
            row {
                +SimpleKeyboardButton(ConstantsSting.addDatabase)
            }
        },
        resizeKeyboard = true
    )
}