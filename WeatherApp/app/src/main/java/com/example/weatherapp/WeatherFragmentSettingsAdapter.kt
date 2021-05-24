package com.example.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.WeatherFragmentBinding

class WeatherFragmentSettingsAdapter :
    RecyclerView.Adapter<WeatherFragmentSettingsAdapter.MainViewHolder>() {

    private var weatherData: List<Weather> = listOf()
    //private var _binding: WeatherFragmentBinding? = null

    fun setWeather(data: List<Weather>) {
        weatherData = data
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

            //_binding
            itemView.findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text = weather.city.cityName
            itemView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    weather.city.cityName,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
