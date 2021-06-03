package com.example.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

class WeatherFragmentSettingsAdapter(private var onItemViewClickListener: WeatherSettingsFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<WeatherFragmentSettingsAdapter.MainViewHolder>() {

    private var weatherData: List<Weather> = listOf()
    private lateinit var viewModel: MainViewModel

    fun removeListener() {
        onItemViewClickListener = null
    }

    fun setWeather(data: List<Weather>, viewModel: MainViewModel) {
        weatherData = data
        this.viewModel = viewModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_main_recycler_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(weather: Weather) {
            itemView.findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text = weather.city.cityName
            itemView.setOnClickListener {
                onItemViewClickListener?.onItemViewClick(weather)
            }
            itemView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    weather.city.cityName,
                    Toast.LENGTH_LONG
                ).show()
                //viewModel.currentIndexWeather = adapterPosition
                //WeatherFragment.newInstance().
            }
        }
    }
}
