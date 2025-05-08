package di

import GetCurrentWeatherUseCase
import domain.use_cases.GetLocationUseCase
import domain.use_cases.SuggestClothesBasedOnWeatherUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetCurrentWeatherUseCase(get()) }
    single { GetLocationUseCase() }
    single { SuggestClothesBasedOnWeatherUseCase() }
}