package com.example.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.databinding.WeatherFragmentBinding
import com.google.android.material.snackbar.Snackbar


class WeatherFragment : Fragment() {

    private var _binding: WeatherFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherFragmentBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

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

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val weatherData = appState.weatherData
                binding.loadingLayout.visibility = View.GONE
                setData(weatherData.get(viewModel.currentIndexWeather))
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
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