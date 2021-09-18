package com.ozgurerdogan.kotlin_countrylist.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ozgurerdogan.kotlin_countrylist.model.Country
import com.ozgurerdogan.kotlin_countrylist.service.CountryAPIService
import com.ozgurerdogan.kotlin_countrylist.service.CountryDatabase
import com.ozgurerdogan.kotlin_countrylist.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : BaseViewModel(application) {
    private val countryApiService=CountryAPIService()
    private val disposable=CompositeDisposable()
    private var customPreferences=CustomSharedPreferences(getApplication())
    private var refreshTime=10*60*1000*1000*1000L

    val countries=MutableLiveData<List<Country>>()
    val countryError=MutableLiveData<Boolean>()
    val countryLoading=MutableLiveData<Boolean>()

    fun refreshData(){
        val updateTime=customPreferences.getTime()

        if (updateTime != null && updateTime != 0L && System.nanoTime()-updateTime<refreshTime){
            getDataFromSQLite()
        }else{
            getDataFromAPI()
        }

    }

    fun refreshFromAPI(){
        getDataFromAPI()
    }



    private fun getDataFromSQLite(){
        countryLoading.value=true
        launch {
            val countries=CountryDatabase(getApplication()).countryDao().getAllCountry()
            showCountries(countries)
            Toast.makeText(getApplication(),"Countries From SQLite",Toast.LENGTH_LONG).show()
        }
    }

    private fun getDataFromAPI(){
        countryLoading.value=true

        disposable.add(
            countryApiService.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {
                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"Countries From API",Toast.LENGTH_LONG).show()
                    }


                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        countryError.value=true
                        countryLoading.value=false
                    }

                })
        )

    }
    private fun showCountries(t: List<Country>){
        countries.value=t
        countryError.value=false
        countryLoading.value=false
    }

    private fun storeInSQLite(list:List<Country>){
        launch {
            val dao=CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountry()
            val listlong=dao.insertAll(*list.toTypedArray()) // *list.toTypedArray() bu kod listeyi tekil hale getiriyor , değerleri tek tek veriyor
            // bu fonksiyon country'lerin database kayıt sıralarının bir listesini bize döndürüyor.
            // bizim ön belleğimizde tutulan country listesinde API ile alınan country verileri mevcut olup,  uuid bilgisi mevcut olmadığından, burada bize verilen ıd listesindeki ıd'leri tek tek ön bellekte tutulmaya devam eden country listesinin uuid sütununa while loops ile ekliyoruz...

            var i=0
            while (i<list.size){
                list[i].uuid=listlong[i].toInt()
                i++
            }

            showCountries(list)

        }

        customPreferences.saveTime(System.nanoTime())

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}