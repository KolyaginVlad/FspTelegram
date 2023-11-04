package bot.constants

import bot.constants.ConstantsString.DELIMITER
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
    fun getMemory(database: String) = inlineKeyboard {
        row {
            dataButton(ConstantsString.changeMemory, "${ButtonType.MEMORY}${DELIMITER}1${DELIMITER}${database}")
        }
        row {
            dataButton(ConstantsString.showMemory, "${ButtonType.MEMORY}${DELIMITER}2${DELIMITER}${database}")
        }
        row {
            dataButton(ConstantsString.backBtn, "${ButtonType.BACK}${DELIMITER}-1${DELIMITER}${database}${DELIMITER}${ButtonType.MEMORY}")
        }
    }
    fun getChangeMemory(database:String) = inlineKeyboard {
        row {
            dataButton(ConstantsString.maintenanceWorkMemory, "${ButtonType.CHANGE_MEMORY}${DELIMITER}1${DELIMITER}${database}")
        }
        row {
            dataButton(ConstantsString.memoryLimit, "${ButtonType.CHANGE_MEMORY}${DELIMITER}2${DELIMITER}${database}")
        }
        row {
            dataButton(ConstantsString.workMemory, "${ButtonType.CHANGE_MEMORY}${DELIMITER}3${DELIMITER}${database}")
        }
        row {
            dataButton(ConstantsString.effectiveCacheSize, "${ButtonType.CHANGE_MEMORY}${DELIMITER}4${DELIMITER}${database}")
        }
        row {
            dataButton(ConstantsString.backBtn, "${ButtonType.BACK}${DELIMITER}-1${DELIMITER}${database}${DELIMITER}${ButtonType.CHANGE_MEMORY}")
        }
    }
    fun  getStop(dataBase:String) = inlineKeyboard {
        row {
            dataButton(ConstantsString.stopBtn, "${ButtonType.STOP_MONITORING}${DELIMITER}1${DELIMITER}$dataBase")
        }
    }

    val selectAddDatabaseMethodKeyboard = inlineKeyboard {
        row {
            dataButton(ConstantsString.databaseConnectionConstructor, "${ButtonType.SELECT_DATABASE_ADD}${DELIMITER}1")
        }
        row {
            dataButton(ConstantsString.databaseConnectionSsh, "${ButtonType.SELECT_DATABASE_ADD}${DELIMITER}2")
        }
        row {
            dataButton(ConstantsString.databaseConnectionString, "${ButtonType.SELECT_DATABASE_ADD}${DELIMITER}3")
        }
        row {
            dataButton(ConstantsString.databaseConnectionFile, "${ButtonType.SELECT_DATABASE_ADD}${DELIMITER}4")
        }
    }


    val onlyAddDatabase = inlineKeyboard {
        row { dataButton(ConstantsString.addDatabase, ButtonType.ADD_DB.toString()) }
    }

    fun getDataBasesCommands(dataBase: String) = inlineKeyboard {
        row {
            dataButton(
                ConstantsString.checkPointBtn,
                ButtonType.DB_OPTIONS.toString() + DELIMITER + dataBase + DELIMITER + "1"
            )
        }
//        row {
//            dataButton(
//                ConstantsSting.checkPointDatetBtn,
//                ButtonType.DB_OPTIONS.toString() + DELIMITER + dataBase + DELIMITER + "2"
//            )
//        }
        row {
            dataButton(
                ConstantsString.onRealTime,
                ButtonType.DB_OPTIONS.toString() + DELIMITER + dataBase + DELIMITER + "3"
            )
        }

//        row {
//            dataButton(
//                ConstantsSting.monitorCommon,
//                ButtonType.DB_OPTIONS.toString() + DELIMITER + dataBase + DELIMITER + "5"
//            )
//        }

        row {
            dataButton(ConstantsString.metrix, ButtonType.DB_OPTIONS.toString() + DELIMITER + dataBase + DELIMITER + "6")
        }
        row {
            dataButton(
                ConstantsString.vacuumClean,
                ButtonType.DB_OPTIONS.toString() + DELIMITER + dataBase + DELIMITER + "7"
            )
        }
        row {
            dataButton(
                ConstantsString.memory,
                ButtonType.DB_OPTIONS.toString() + DELIMITER + dataBase + DELIMITER + "8"
            )
        }
        row {
            dataButton(
                ConstantsString.backBtn,
                ButtonType.BACK.toString() + DELIMITER + dataBase + DELIMITER + "4" + DELIMITER + ButtonType.DB_OPTIONS.toString()
            )
        }
    }

    val checkAndAddWithOffRealtime = inlineKeyboard {
        row { dataButton(ConstantsString.addDatabase, ConstantsString.addDatabase) }
        row { dataButton(ConstantsString.checkPointBtn, ConstantsString.checkPointBtn) }
        row { dataButton(ConstantsString.checkPointDatetBtn, ConstantsString.checkPointDatetBtn) }
        row { dataButton(ConstantsString.offRealTime, ConstantsString.offRealTime) }
    }

    fun getDataBasesKeyBoard(dataBases: List<String>) = inlineKeyboard {
        dataBases.forEach {
            row {
                dataButton(it, ButtonType.SELECT_DATABASE.toString() + DELIMITER + it)
            }
        }
        row { dataButton(ConstantsString.addDatabase, ButtonType.ADD_DB.toString()) }
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

    fun repairTransactionKeyboard(database: String) = inlineKeyboard {
        row {
            dataButton(ConstantsString.restartDb, ButtonType.REPAIR.toString() + DELIMITER + "-1" + DELIMITER + database)
        }
    }

    fun getLogSettingsKeyboard(database: String, settings: List<Pair<String, Boolean>>) =
        inlineKeyboard {
            settings.forEachIndexed { index, (setting, isEnabled) ->
                row {
                    dataButton(
                        "$setting${if (isEnabled) ConstantsString.onEmoji else ConstantsString.offEmoji}",
                        "${ButtonType.LOG_SETTINGS}$DELIMITER$database$DELIMITER$index"
                    )
                }
            }
        }
}

enum class ButtonType {
    SELECT_DATABASE_ADD, SELECT_DATABASE, DB_OPTIONS, BACK, MAIN_OPTIONS,
    ADD_DB, COMMAND, CUSTOM_QUERY, LOG_SETTINGS, REPAIR, STOP_MONITORING,
    MEMORY, CHANGE_MEMORY

}

fun String.toButtonType() = ButtonType.valueOf(this)