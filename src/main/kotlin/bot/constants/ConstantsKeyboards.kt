package bot.constants

import bot.constants.ConstantsSting.DELIMITER
import dev.inmo.tgbotapi.extensions.utils.types.buttons.dataButton
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
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

    val onlyAddDatabase = inlineKeyboard {
        row { dataButton(ConstantsSting.addDatabase, ButtonType.ADD_DB.toString()) }
    }

    fun getDataBasesCommands(dataBase: String) = inlineKeyboard {
        row {
            dataButton(
                ConstantsSting.checkPointBtn,
                ButtonType.DB_OPTIONS.toString() + DELIMITER + dataBase + DELIMITER + "1"
            )
        }
        row {
            dataButton(
                ConstantsSting.checkPointDatetBtn,
                ButtonType.DB_OPTIONS.toString() + DELIMITER + dataBase + DELIMITER + "2"
            )
        }
        row {
            dataButton(
                ConstantsSting.onRealTime,
                ButtonType.DB_OPTIONS.toString() + DELIMITER + dataBase + DELIMITER + "3"
            )
        }
        row {
            dataButton(
                ConstantsSting.monitorCommon,
                ButtonType.DB_OPTIONS.toString() + DELIMITER + dataBase + DELIMITER + "5"
            )
        }
        row {
            dataButton(
                ConstantsSting.monitorCommon,
                ButtonType.DB_OPTIONS.toString() + DELIMITER + dataBase + DELIMITER + "7"
            )
        }
        row {
            dataButton(ConstantsSting.metrix, ButtonType.DB_OPTIONS.toString() + DELIMITER + dataBase + DELIMITER + "6")
        }
        row {
            dataButton(
                ConstantsSting.backBtn,
                ButtonType.BACK.toString() + DELIMITER + dataBase + DELIMITER + "4" + ButtonType.DB_OPTIONS.toString()
            )
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
                dataButton(it, ButtonType.SELECT_DATABASE.toString() + DELIMITER + it)
            }
        }
        row { dataButton(ConstantsSting.addDatabase, ButtonType.ADD_DB.toString()) }
    }

    fun getLogInlineKeyboard(data: String) = inlineKeyboard {
        row {
            dataButton(ConstantsSting.showSolution, ButtonType.COMMAND.toString() + DELIMITER + data)
        }
    }

    val simpleAnswerKeyboard = dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup(
        matrix {
            row {
                +SimpleKeyboardButton("Да")
                +SimpleKeyboardButton("Нет")
            }
        },
        oneTimeKeyboard = true
    )

    fun repairKeyboard(variants: List<String>) = inlineKeyboard {
        variants.forEachIndexed { index, it ->
            row {
                dataButton(it, ButtonType.REPAIR.toString() + DELIMITER + index)
            }
        }
        row {
            dataButton(ConstantsSting.restartDb, ButtonType.REPAIR.toString() + DELIMITER + "-1")
        }
    }

    fun repairTransactionKeyboard(database: String) = inlineKeyboard {
        row {
            dataButton(ConstantsSting.restartDb, ButtonType.REPAIR.toString() + DELIMITER + "-1" + DELIMITER + database)
        }
    }

    fun getLogSettingsKeyboard(database: String, settings: List<Pair<String, Boolean>>) =
        inlineKeyboard {
            settings.forEachIndexed { index, (setting, isEnabled) ->
                row {
                    dataButton(
                        "$setting${if (isEnabled) ConstantsSting.onEmoji else ConstantsSting.offEmoji}",
                        "${ButtonType.LOG_SETTINGS}$DELIMITER$database$DELIMITER$index"
                    )
                }
            }
        }
}

enum class ButtonType {
    SELECT_DATABASE, DB_OPTIONS, BACK, MAIN_OPTIONS, ADD_DB, COMMAND, CUSTOM_QUERY, LOG_SETTINGS, REPAIR
}

fun String.toButtonType() = when (this) {
    "SELECT_DATABASE" -> ButtonType.SELECT_DATABASE
    "DB_OPTIONS" -> ButtonType.DB_OPTIONS
    "BACK" -> ButtonType.BACK
    "MAIN_OPTIONS" -> ButtonType.MAIN_OPTIONS
    "ADD_DB" -> ButtonType.ADD_DB
    else -> ButtonType.COMMAND
}