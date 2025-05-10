package di

import domain.use_cases.GetCurrentWeatherUseCase
import domain.use_cases.GetLocationUseCase
import domain.use_cases.SuggestClothesBasedOnWeatherUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetCurrentWeatherUseCase(get()) }
    factory { GetLocationUseCase() }
    factory { SuggestClothesBasedOnWeatherUseCase() }
}