package di

import WeatherRepository
import data.repository.LocationRepositoryImpl
import data.repository.WeatherRepositoryImpl
import domain.repository.LocationRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import org.koin.dsl.module

val repositoryModule = module {
    single { HttpClient(CIO) }
    single<LocationRepository> { LocationRepositoryImpl() }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
}