package br.com.leandro.clima.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.leandro.clima.data.CurrentWeather
import br.com.leandro.clima.data.WeatherForecast
import br.com.leandro.clima.databinding.MainFragmentBinding
import br.com.leandro.clima.helper.LocationHelper
import br.com.leandro.clima.presentation.recyclerview.adapter.ForecastAdapter
import br.com.leandro.clima.presentation.recyclerview.adapter.WeatherDetailsAdapter
import br.com.leandro.clima.presentation.util.Helper
import br.com.leandro.clima.presentation.util.Helper.Companion.convertTime
import br.com.leandro.clima.presentation.util.Helper.Companion.listDetailsMock
import br.com.leandro.clima.presentation.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private lateinit var locationHelper: LocationHelper
    private lateinit var currentWeatherAdapter: WeatherDetailsAdapter
    private lateinit var currentWeatherLayoutManager: LinearLayoutManager
    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(layoutInflater)

        initListener()
        initObserver()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getLocation()
        viewModel.update()
        updating(true)
    }

    private fun initListener() {
        binding.search.setOnClickListener {
            val direction = MainFragmentDirections.actionMainFragmentToAddCityFragment()
            findNavController().navigate(direction)
        }
    }

    private fun initObserver() {
        viewModel.currentWeatherList.observe(viewLifecycleOwner) {
            it?.let {
                setCurrentWeather(it)
            }
        }

        viewModel.weatherForecastList.observe(viewLifecycleOwner) {
            it?.let {
                setWeatherForecast(it)
            }
        }
    }

    private fun getLocation() {
        locationHelper = LocationHelper(requireActivity(), object : LocationHelper.Callback {
            override fun onLocationAcquired() {
                viewModel.update()
                updating(true)
            }
        })
        locationHelper.getLocation()
    }

    @SuppressLint("SetTextI18n")
    private fun setCurrentWeather(weather: CurrentWeather) {
        binding.cityName.text = weather.name
        binding.descriptionText.text = weather.weather.first().description
        binding.tempText.text = weather.main.temp.toInt().toString()
        binding.feelsLike.text = "Sensação: ${weather.main.feels_like.toInt()}ºC"
        binding.minTempText.text = "Mín: ${weather.main.temp_min.toInt()}ºC"
        binding.maxTempText.text = "Max: ${weather.main.temp_max.toInt()}ºC"

        val duringTheDay = Helper.timeIsBetween(
            convertTime(weather.sys.sunrise ?: return),
            convertTime(weather.sys.sunset ?: return),
            null
        )
        Helper.weatherImage(weather.weather.first().description, duringTheDay, binding.image)

        currentWeatherAdapter =
            WeatherDetailsAdapter(listDetailsMock(requireActivity(), weather))
        currentWeatherLayoutManager =
            LinearLayoutManager(Activity(), LinearLayoutManager.HORIZONTAL, false)
        binding.currentWeatherDetailsRecycler.layoutManager = currentWeatherLayoutManager
        binding.currentWeatherDetailsRecycler.adapter = currentWeatherAdapter

        updating(false)
    }

    private fun setWeatherForecast(forecast: WeatherForecast) {
        val adapter = ForecastAdapter()
        binding.weatherForecastDetailsRecycler.layoutManager =
            LinearLayoutManager(Activity(), LinearLayoutManager.HORIZONTAL, false)
        binding.weatherForecastDetailsRecycler.adapter = adapter
        adapter.addHeadersAndSubmitList(forecast.list)

        updating(false)
    }

    private fun updating(visible: Boolean) {
        binding.progressBar.visibility = if (visible) View.VISIBLE else View.GONE
        binding.weather.visibility = if (visible) View.GONE else View.VISIBLE
    }
}
