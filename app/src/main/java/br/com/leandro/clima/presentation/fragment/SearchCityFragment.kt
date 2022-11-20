package br.com.leandro.clima.presentation.fragment

import android.app.Activity
import android.content.res.Resources
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.leandro.clima.R
import br.com.leandro.clima.data.City
import br.com.leandro.clima.data.UserSessionManager
import br.com.leandro.clima.databinding.SearchCityFragmentBinding
import br.com.leandro.clima.helper.LocationHelper
import br.com.leandro.clima.helper.hideKeyboard
import br.com.leandro.clima.presentation.recyclerview.adapter.PlacesResultAdapter
import br.com.leandro.clima.presentation.viewmodel.SearchCityViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.libraries.places.api.Places
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.*

class SearchCityFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: SearchCityFragmentBinding
    private lateinit var locationHelper: LocationHelper
    private val viewModel: SearchCityViewModel by viewModel()
    private val controller by lazy { findNavController() }
    private var placeAdapter: PlacesResultAdapter? = null
    private var isMapReady = false
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchCityFragmentBinding.inflate(layoutInflater)

        initListener()
        getAutocompletePlaces()

        val supportMapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        if (googleMap != null) setCameraPosition(googleMap!!)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        locationHelper = LocationHelper(requireActivity(), object : LocationHelper.Callback {
            override fun onLocationAcquired() {
                getAutocompletePlaces()
            }
        })
        locationHelper.getLocation()
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        isMapReady = true

        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireActivity(),
                    R.raw.map_style
                )
            )
            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("MapsActivityRaw", "Can't find style.", e)
            return
        }

        setCameraPosition(map)
        googleMap?.setOnCameraIdleListener { getLocationOnMap() }
    }

    private fun initListener() {
        binding.back.setOnClickListener {
            controller.popBackStack()
        }

        binding.buttonMap.setOnClickListener {
            viewModel.save()
            controller.popBackStack()
        }
    }

    private fun setCameraPosition(map: GoogleMap?) {
        map?.let {
            val location = LatLng(
                UserSessionManager.location.value?.latitude ?: return,
                UserSessionManager.location.value?.longitude ?: return
            )
            adjustCameraPosition(location, map)
        }
    }

    private fun adjustCameraPosition(location: LatLng, map: GoogleMap) {
        val cameraPosition = CameraPosition.Builder().target(location).zoom(12F).build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun clearEditText() {
        binding.editTextSearch.setText("")
        binding.editTextSearch.clearFocus()
        view?.hideKeyboard()
    }

    private fun getLocationOnMap() {
        clearEditText()

        val location = googleMap?.cameraPosition?.target

        if (location != null) {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (addresses.size > 0) {
                    val city = City(
                        id = 0,
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                    UserSessionManager.updateCity(city)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun searchLocationOnMap(address: String) {
        if (address.isNotBlank()) {
            val geoCoder = Geocoder(requireContext(), Locale.getDefault())
            try {
                val addressList = geoCoder.getFromLocationName(address, 1)
                if (addressList.first() != null) {
                    val city = City(
                        id = 0,
                        latitude = addressList.first().latitude,
                        longitude = addressList.first().longitude
                    )
                    UserSessionManager.updateCity(city)

                    adjustCameraPosition(
                        LatLng(
                            addressList.first().latitude,
                            addressList.first().longitude
                        ), googleMap!!
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getAutocompletePlaces() {
        if (!Places.isInitialized()) Places.initialize(
            requireContext(),
            getString(R.string.google_maps_key)
        )

        val location = LatLng(
            UserSessionManager.location.value?.latitude ?: 0.0,
            UserSessionManager.location.value?.longitude ?: 0.0,
        )
        placeAdapter = PlacesResultAdapter(requireContext(), location) { prediction ->
            getLatLng(prediction.getFullText(null).toString())
            binding.editTextSearch.setText("")
            searchLocationOnMap(prediction.getFullText(null).toString())
            view?.hideKeyboard()
        }

        val layoutManager =
            LinearLayoutManager(Activity(), LinearLayoutManager.VERTICAL, false)

        binding.searchCitiesRecycler.layoutManager = layoutManager
        binding.searchCitiesRecycler.adapter = placeAdapter

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(text: Editable?) {
                if (text.toString().isNotEmpty()) {
                    placeAdapter!!.filter.filter(text.toString())
                    if (binding.searchCitiesRecycler.visibility == View.GONE)
                        binding.searchCitiesRecycler.visibility = View.VISIBLE
                } else {
                    if (binding.searchCitiesRecycler.visibility == View.VISIBLE)
                        binding.searchCitiesRecycler.visibility = View.GONE
                }
            }
        })
    }

    private fun getLatLng(address: String): LatLng {
        if (address.isNotBlank()) {
            val geoCoder = Geocoder(requireContext(), Locale.getDefault())
            try {
                val addressList = geoCoder.getFromLocationName(address, 1)
                if (addressList.first() != null) {
                    val city = City(
                        id = 0,
                        latitude = addressList.first().latitude,
                        longitude = addressList.first().longitude
                    )
                    UserSessionManager.updateCity(city)

                    return LatLng(addressList.first().latitude, addressList.first().longitude)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return LatLng(
            UserSessionManager.location.value?.latitude ?: 0.0,
            UserSessionManager.location.value?.longitude ?: 0.0
        )
    }
}
