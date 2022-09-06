package br.com.leandro.clima.dao

import androidx.room.*
import br.com.leandro.clima.data.City

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(city: City)

    @Delete
    fun remove(city: City)

    @Query("SELECT * FROM City")
    fun getAll(): List<City>

    @Query("SELECT * FROM City WHERE id = :key")
    fun get(key: Long): City
}





