import di.presentationModule
import di.repositoryModule
import di.useCaseModule
import kotlinx.coroutines.*
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.mp.KoinPlatform.getKoin
import presentation.ClothesSuggesterConsoleUI

fun main() {
    var exit = true
    GlobalScope.launch {
        startKoin {
            modules(repositoryModule, useCaseModule, presentationModule)
        }

        val mainUi: ClothesSuggesterConsoleUI = getKoin().get()
        mainUi.start()
        mainUi.uiFlow.collect {
            exit = true
            stopKoin()
        }
    }
    runBlocking {
        while (exit) {
            delay(1000)
        }
    }
}