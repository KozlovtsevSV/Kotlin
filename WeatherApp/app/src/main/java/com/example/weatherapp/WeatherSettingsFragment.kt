package com.example.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.databinding.FragmentWeatherSettingsBinding
import com.google.android.material.snackbar.Snackbar

class WeatherSettingsFragment : Fragment() {

    private var _binding: FragmentWeatherSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private val adapter = WeatherFragmentSettingsAdapter()
    private var isDataSetRus: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherSettingsBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.weatherSettingsFragmentRecyclerView.adapter = adapter
        binding.weatherSettingsFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeatherFromLocalSource()//getWeatherFromLocalSourceRus()
    }

    private fun changeWeatherDataSet() {
        isDataSetRus = !isDataSetRus
        if (! isDataSetRus) {
            binding.weatherSettingsFragmentFAB.setImageResource(R.drawable.ic_earth)
        } else {
            binding.weatherSettingsFragmentFAB.setImageResource(R.drawable.ic_russia)
        }
        viewModel.getWeatherFromLocalSource(isDataSetRus)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.weatherSettingsFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.weatherSettingsFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.weatherSettingsFragmentLoadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.weatherSettingsFragmentFAB, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.reload)) { viewModel.getWeatherFromLocalSource()}//getWeatherFromLocalSourceRus() }
                    .show()
            }
        }
    }

    companion object {
        fun newInstance() = WeatherSettingsFragment()
    }
}