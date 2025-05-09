package di

import org.koin.dsl.module
import presentation.ClothesSuggesterConsoleUI
import presentation.io.ConsoleIO
import presentation.io.ConsoleIOImpl
import java.util.*

val presentationModule = module {
    single { Scanner(System.`in`) }
    single<ConsoleIO> { ConsoleIOImpl(get()) }

    single { ClothesSuggesterConsoleUI(get(), get(), get(), get(), get()) }
}