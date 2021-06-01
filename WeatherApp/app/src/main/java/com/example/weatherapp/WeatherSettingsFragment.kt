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

class WeatherSettingsFragment() : Fragment() {

    private var _binding: FragmentWeatherSettingsBinding? = null
    private val binding get() = _binding!!

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }

    private lateinit var viewModel: MainViewModel
    private var isDataSetRus: Boolean = false
    private val adapter = WeatherFragmentSettingsAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                val bundle = Bundle()
                bundle.putParcelable(WeatherFragment.BUNDLE_EXTRA, weather)
                manager.beginTransaction()
                    .add(R.id.container, WeatherFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })

    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }

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
        viewModel.getWeatherFromLocalSource()
        isDataSetRus = viewModel.isRussian
        drawIcon()
    }

    private fun changeWeatherDataSet() {
        isDataSetRus = !isDataSetRus
        drawIcon()
        viewModel.getWeatherFromLocalSource()
    }

    private fun drawIcon(){
        if (! isDataSetRus) {
            binding.weatherSettingsFragmentFAB.setImageResource(R.drawable.ic_earth)
        } else {
            binding.weatherSettingsFragmentFAB.setImageResource(R.drawable.ic_russia)
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.weatherSettingsFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData, viewModel)
             }
            is AppState.Loading -> {
                binding.weatherSettingsFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.weatherSettingsFragmentLoadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.weatherSettingsFragmentFAB, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.reload)) { viewModel.getWeatherFromLocalSource()}
                    .show()
            }
        }
    }

    companion object {
        fun newInstance() = WeatherSettingsFragment()
    }
}