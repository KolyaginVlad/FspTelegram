package bot.constants

import dev.inmo.tgbotapi.extensions.utils.types.buttons.dataButton
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.utils.matrix
import dev.inmo.tgbotapi.utils.row

object ConstantsKeyboards {

    val empty = dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup(
        matrix {
            row {
            }
        }
    )

    val onlyAddDatabase = inlineKeyboard {
        row { dataButton(ConstantsSting.addDatabase, ButtonType.ADD_DB.toString()) }
    }

    val dataBaseCommands = inlineKeyboard {
        row {
            dataButton(ConstantsSting.checkPointBtn, ButtonType.DB_OPTIONS.toString() + " " + ConstantsSting.checkPointBtn)
        }
        row {
            dataButton(ConstantsSting.checkPointDatetBtn, ButtonType.DB_OPTIONS.toString() + " " + ConstantsSting.checkPointDatetBtn)
        }
        row {
            dataButton(ConstantsSting.onRealTime, ButtonType.DB_OPTIONS.toString() + " " + ConstantsSting.onRealTime)
        }
        row {
            dataButton(ConstantsSting.backBtn, ButtonType.BACK.toString()+" "+ ButtonType.DB_OPTIONS.toString())
        }
        row {
            dataButton(ConstantsSting.monitorCommon, ButtonType.DB_OPTIONS.toString() + " " + ConstantsSting.monitorCommon)
        }

    }

    val checkAndAddWithOffRealtime = inlineKeyboard {
        row { dataButton(ConstantsSting.addDatabase, ConstantsSting.addDatabase) }
        row { dataButton(ConstantsSting.checkPointBtn, ConstantsSting.checkPointBtn) }
        row { dataButton(ConstantsSting.checkPointDatetBtn, ConstantsSting.checkPointDatetBtn) }
        row { dataButton(ConstantsSting.offRealTime, ConstantsSting.offRealTime) }
    }

    fun getDataBasesKeyBoard(dataBases: List<String>) = inlineKeyboard {
        dataBases.forEach {
            row {
                dataButton(it, ButtonType.SELECT_DATABASE.toString() + " " + it)
            }
        }
        row { dataButton(ConstantsSting.addDatabase, ButtonType.ADD_DB.toString()) }
    }

    fun getLogInlineKeyboard(data: String) = inlineKeyboard {
        row {
            dataButton(ConstantsSting.showSolution, ButtonType.COMMAND.toString() + " " + data)
        }
    }
}

enum class ButtonType {
    SELECT_DATABASE, DB_OPTIONS, BACK, MAIN_OPTIONS, ADD_DB, COMMAND
}

fun String.toButtonType() = when (this) {
    "SELECT_DATABASE" -> ButtonType.SELECT_DATABASE
    "DB_OPTIONS" -> ButtonType.DB_OPTIONS
    "BACK" -> ButtonType.BACK
    "MAIN_OPTIONS" -> ButtonType.MAIN_OPTIONS
    "ADD_DB" -> ButtonType.ADD_DB
    else -> ButtonType.COMMAND
}