package com.example.weatherapp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.databinding.WeatherFragmentBinding
import com.google.android.material.snackbar.Snackbar


class WeatherFragment : Fragment() {

    private var _binding: WeatherFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var weatherBundle: Weather
    private val onLoadListener: WeatherLoader.WeatherLoaderListener =
        object : WeatherLoader.WeatherLoaderListener {

            override fun onLoaded(weatherDTO: WeatherDTO) {
                displayWeather(weatherDTO)
            }

            override fun onFailed(throwable: Throwable) {
                //Обработка ошибки
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherFragmentBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })

        viewModel.getWeatherFromLocalSource()

        _binding?.buttonUpDate?.setOnClickListener { view ->
            viewModel.getWeatherFromLocalSource()
        }

        _binding?.buttonSettings?.setOnClickListener { view ->
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                val bundle = Bundle()
                //bundle.putParcelable(WeatherSettings.BUNDLE_EXTRA, weather)
                manager.beginTransaction()
                    .replace(R.id.container, WeatherSettingsFragment.newInstance())
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
            //viewModel.getWeatherFromLocalSource()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val weatherData = appState.weatherData
                binding.loadingLayout.visibility = View.GONE
                //setData(weatherData.get(viewModel.currentIndexWeather))
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
                weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
                val loader = WeatherLoader(onLoadListener, weatherBundle.city.latitude, weatherBundle.city.longitude)
                loader.loadWeather()
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.mainView, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.reload)) { viewModel.getWeatherFromLocalSource() }
                    .show()
            }
        }
    }

    private fun setData(weatherData: Weather) {
        binding.cityName.text = weatherData.city.cityName
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weatherData.city.latitude.toString(),
            weatherData.city.longitude.toString()
        )
        binding.temperature.text = String.format(
            getString(R.string.temperature), weatherData.temperature.toString())
        binding.descrizioneWeather.text = weatherData.descrizioneWeather.toString()
    }

    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE
            val city = weatherBundle.city
            binding.cityName.text = city.cityName
            binding.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.latitude.toString(),
                city.longitude.toString()
            )
            //weatherCondition.text = weatherDTO.fact?.condition
            binding.temperature.text = String.format(getString(R.string.temperature), weatherDTO.fact?.temp.toString())
            //feelsLikeValue.text = weatherDTO.fact?.feels_like.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle?): WeatherFragment {
            val fragment = WeatherFragment()
            
            fragment.arguments = bundle
            return fragment
        }
    }
}