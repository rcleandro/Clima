package br.com.leandro.clima.repository

import br.com.leandro.clima.dao.CityDao
import br.com.leandro.clima.data.City

class AddCityRepository(private val cityDao: CityDao) {
    fun save(id: Long, latitude: Double, longitude: Double) {
        cityDao.save(City(id, latitude, longitude))
    }
}