package br.com.leandro.clima.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class City(
    @PrimaryKey(autoGenerate = false) val id: Long,
    var latitude: Double,
    var longitude: Double
) : Parcelable
