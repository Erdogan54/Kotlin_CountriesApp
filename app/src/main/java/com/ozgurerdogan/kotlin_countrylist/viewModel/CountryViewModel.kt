package com.ozgurerdogan.kotlin_countrylist.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ozgurerdogan.kotlin_countrylist.model.Country
import com.ozgurerdogan.kotlin_countrylist.service.CountryDatabase
import kotlinx.coroutines.launch

class CountryViewModel(application:Application): BaseViewModel(application) {
    val countryLiveData=MutableLiveData<Country>()

    fun getDataFromRoom(uuid:Int){
        launch {
            val country=CountryDatabase(getApplication()).countryDao().getCountry(uuid)
            countryLiveData.value=country
        }

    }

}