package br.com.leandro.clima.presentation.viewmodel

import androidx.lifecycle.ViewModel
import br.com.leandro.clima.data.UserSessionManager.city
import br.com.leandro.clima.repository.AddCityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchCityViewModel(
    private val cityRepository: AddCityRepository
) : ViewModel() {

    fun save() {
        CoroutineScope(Dispatchers.IO).launch {
            cityRepository.save(
                id = 1,
                latitude = city.value?.latitude ?: return@launch,
                longitude = city.value?.longitude ?: return@launch
            )
        }
    }
}
