package com.ozgurerdogan.kotlin_countrylist.service

import com.ozgurerdogan.kotlin_countrylist.model.Country
import io.reactivex.Single
import retrofit2.http.GET

interface CountryAPI {

    //BASE_URL -> https://raw.githubusercontent.com/
    // EXT ->  atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json

    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")
    fun getCountries(): Single<List<Country>>

}