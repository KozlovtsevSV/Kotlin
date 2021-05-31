package com.example.weatherapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.Repository
import com.example.weatherapp.RepositoryImpl
import com.example.weatherapp.AppState
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()) :
    ViewModel() {

    var currentIndexWeather: Int = 0
    var isRussian : Boolean = true

    fun setCurrentWeather(IndexWeather: Int, isRussian: Boolean){
        this.currentIndexWeather = IndexWeather
        this.isRussian = isRussian
    }

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSource() = getDataFromLocalSource(isRussian)

    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(
                AppState.Success(if (isRussian) repositoryImpl.getWeatherFromLocalStorageRus() else repositoryImpl.getWeatherFromLocalStorageWorld())
            //    AppState.Success(repositoryImpl.getWeatherFromLocalStorage())
            )
        }.start()
    }
}