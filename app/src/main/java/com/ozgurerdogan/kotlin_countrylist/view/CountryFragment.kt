package com.ozgurerdogan.kotlin_countrylist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ozgurerdogan.kotlin_countrylist.R
import com.ozgurerdogan.kotlin_countrylist.databinding.FragmentCountryBinding
import com.ozgurerdogan.kotlin_countrylist.util.downloadFromUrl
import com.ozgurerdogan.kotlin_countrylist.util.placeholderProgressbar
import com.ozgurerdogan.kotlin_countrylist.viewModel.CountryViewModel
import kotlinx.android.synthetic.main.fragment_country.*


class CountryFragment : Fragment() {

    private lateinit var viewModel:CountryViewModel
    private var countryUuid:Int=0
    private lateinit var dataBinding:FragmentCountryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_country,container,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            countryUuid=CountryFragmentArgs.fromBundle(it).countryUuid
            println(countryUuid)

        }
        viewModel=ViewModelProvider(this).get(CountryViewModel::class.java)
        viewModel.getDataFromRoom(countryUuid)

        observeLiveData()
    }

    private fun observeLiveData(){

        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer {country->
            country?.let {
                dataBinding.selectedCountry=country

                /*
                countryNameTxt.text=country.countryName
                countryRegionTxt.text=country.countryRegion
                countryCurrencyTxt.text=country.countryCurrency
                countryLanguageTxt.text=country.countryLanguage
                countryCapitalTxt.text=country.countryCapital
                context?.let {
                    countryImage.downloadFromUrl(country.countryImageUrl, placeholderProgressbar(it))
                }

                 */




            }
        })

    }


}