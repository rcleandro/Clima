package br.com.leandro.clima.presentation.recyclerview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.leandro.clima.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlin.math.cos

class PlacesResultAdapter(
    context: Context,
    private val location: LatLng,
    val onClick: (prediction: AutocompletePrediction) -> Unit,
) :
    RecyclerView.Adapter<PlacesResultAdapter.ViewHolder>(), Filterable {

    private var resultList: ArrayList<AutocompletePrediction>? = arrayListOf()
    private val placesClient: PlacesClient = Places.createClient(context)
    private lateinit var itemListText: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        itemListText = view.findViewById(R.id.itemListText)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return resultList!!.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener {
            onClick(resultList?.get(position)!!)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(position: Int) {
            val res = resultList?.get(position)

            itemView.apply {
                itemListText.text = res?.getFullText(null)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val results = FilterResults()
                resultList = getPredictions(constraint)
                if (resultList != null) {
                    results.values = resultList
                    results.count = resultList!!.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {}
        }
    }

    private fun getCoordinate(lat0: Double, lng0: Double, dy: Long, dx: Long): LatLng {
        val lat = lat0 + 180 / Math.PI * (dy / 6378137)
        val lng = lng0 + 180 / Math.PI * (dx / 6378137) / cos(lat0)
        return LatLng(lat, lng)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getPredictions(constraint: CharSequence): ArrayList<AutocompletePrediction>? {
        val result: ArrayList<AutocompletePrediction> = arrayListOf()
        val token = AutocompleteSessionToken.newInstance()

        val bounds = RectangularBounds.newInstance(
            getCoordinate(location.latitude, location.longitude, -1000, -1000),
            getCoordinate(location.latitude, location.longitude, 1000, 1000)
        )

        val request = FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.CITIES)
            .setLocationBias(bounds)
            .setSessionToken(token)
            .setQuery(constraint.toString())
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                result.addAll(response.autocompletePredictions)
                notifyDataSetChanged()
            }.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {
                    Log.e("TAG", "Place not found: " + exception.statusCode)
                }
            }
        return result
    }
}