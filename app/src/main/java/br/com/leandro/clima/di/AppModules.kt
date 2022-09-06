package br.com.leandro.clima.di

import androidx.room.Room
import br.com.leandro.clima.api.WeatherRestApiTask
import br.com.leandro.clima.dao.CityDao
import br.com.leandro.clima.database.CityDatabase
import br.com.leandro.clima.presentation.fragment.MainFragment
import br.com.leandro.clima.presentation.fragment.SearchCityFragment
import br.com.leandro.clima.presentation.recyclerview.adapter.WeatherDetailsAdapter
import br.com.leandro.clima.presentation.viewmodel.MainViewModel
import br.com.leandro.clima.presentation.viewmodel.SearchCityViewModel
import br.com.leandro.clima.repository.AddCityRepository
import br.com.leandro.clima.repository.CurrentWeatherRepository
import br.com.leandro.clima.repository.WeatherForecastRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<MainViewModel> { MainViewModel(get()) }
    viewModel<SearchCityViewModel> { SearchCityViewModel(get()) }
}

val uiModule = module {
    factory<MainFragment> { MainFragment() }
    factory<SearchCityFragment> { SearchCityFragment() }
    factory<WeatherDetailsAdapter> { WeatherDetailsAdapter(get(), get()) }
}

val repositoryModule = module {
    single<CurrentWeatherRepository> { CurrentWeatherRepository(get()) }
    single<WeatherForecastRepository> { WeatherForecastRepository(get()) }
    single<AddCityRepository> { AddCityRepository(get()) }
}

val daoModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            CityDatabase::class.java,
            "city_db"
        ).build()
    }

    single<CityDao> { get<CityDatabase>().getCityDao() }
}

val apiModule = module {
    single<WeatherRestApiTask> { WeatherRestApiTask() }
}