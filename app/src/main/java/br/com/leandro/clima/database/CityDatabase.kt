package br.com.leandro.clima.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.leandro.clima.dao.CityDao
import br.com.leandro.clima.data.City

@Database(
    entities = [City::class],
    version = 1,
    exportSchema = false
)
abstract class CityDatabase : RoomDatabase() {
    abstract fun getCityDao(): CityDao
}