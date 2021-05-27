package com.example.weatherapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.Repository
import com.example.weatherapp.RepositoryImpl
import com.example.weatherapp.AppState
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSource(isRussian: Boolean = true) = getDataFromLocalSource(isRussian)

    fun getWeatherFromRemoteSource() = getDataFromLocalSource(false)

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