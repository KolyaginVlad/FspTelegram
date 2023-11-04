package data.models

import bot.constants.ConstantsString
import kotlinx.serialization.Serializable

@Serializable
data class MemoryDto(
    var effectiveCacheSize: String,
    var maintenanceWorkMemory: String,
    var workMemory: String,
    var memoryLimit: String
) {
    @Override
    override fun toString() ="""
    ${ConstantsString.effectiveCacheSize} $effectiveCacheSize
    ${ConstantsString.maintenanceWorkMemory} $maintenanceWorkMemory
    ${ConstantsString.workMemory} $workMemory
    ${ConstantsString.memoryLimit} $memoryLimit
    """.trimIndent()
}

@Serializable
data class MemoryResponseDto(
    val effectiveCacheSize: String,
    val maintenanceWorkMemory: String,
    val workMemory: String,
    val memoryLimit: String
)