package com.ozgurerdogan.kotlin_countrylist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ozgurerdogan.kotlin_countrylist.R
import com.ozgurerdogan.kotlin_countrylist.adapter.CountryAdapter
import com.ozgurerdogan.kotlin_countrylist.viewModel.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.*


class FeedFragment : Fragment() {

    private lateinit var viewModel:FeedViewModel
    private var countryAdapter=CountryAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel= ViewModelProvider(this).get(FeedViewModel::class.java)
        viewModel.refreshData()

        recycylerCountryList.layoutManager=LinearLayoutManager(context)
        recycylerCountryList.adapter=countryAdapter

        swipeRefreshlayout.setOnRefreshListener {
            recycylerCountryList.visibility=View.GONE
            errorTextFeed.visibility=View.GONE
            countryLoadingFeed.visibility=View.VISIBLE
            viewModel.refreshFromAPI()
            swipeRefreshlayout.isRefreshing=false
        }

        observeLiveData()

    }

    private fun observeLiveData(){
        viewModel.countries.observe(viewLifecycleOwner, Observer {
            it?.let {
                recycylerCountryList.visibility=View.VISIBLE
                countryAdapter.updateCountryList(it)
            }
        })

        viewModel.countryError.observe(viewLifecycleOwner, Observer {
            if(it){
                errorTextFeed.visibility=View.VISIBLE
            }else{
                errorTextFeed.visibility=View.GONE
            }
        })

        viewModel.countryLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                countryLoadingFeed.visibility=View.VISIBLE
                errorTextFeed.visibility=View.GONE
                recycylerCountryList.visibility=View.GONE
            }else{
                countryLoadingFeed.visibility=View.GONE
            }
        })
    }


}