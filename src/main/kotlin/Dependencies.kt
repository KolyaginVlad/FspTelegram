import data.HttpRequester
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object Dependencies {
    val di = DI {
        bindSingleton { HttpRequester(instance()) }

        bindSingleton { CoroutineScope(Dispatchers.IO + SupervisorJob()) }

        bindSingleton { HttpClient() }
    }
}