package bot

import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext

interface Command {
    suspend fun start(context: BehaviourContext) {
        with(context) {
            process()
        }
    }

    suspend fun BehaviourContext.process()

}