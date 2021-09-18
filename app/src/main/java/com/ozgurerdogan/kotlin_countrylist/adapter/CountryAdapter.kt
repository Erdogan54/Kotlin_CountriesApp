package com.ozgurerdogan.kotlin_countrylist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ozgurerdogan.kotlin_countrylist.R
import com.ozgurerdogan.kotlin_countrylist.databinding.ItemCountryBinding
import com.ozgurerdogan.kotlin_countrylist.model.Country
import com.ozgurerdogan.kotlin_countrylist.util.downloadFromUrl
import com.ozgurerdogan.kotlin_countrylist.util.placeholderProgressbar
import com.ozgurerdogan.kotlin_countrylist.view.FeedFragmentDirections
import kotlinx.android.synthetic.main.fragment_country.view.*
import kotlinx.android.synthetic.main.item_country.view.*

class CountryAdapter(val countryList:ArrayList<Country>): RecyclerView.Adapter<CountryAdapter.CountryHolder>(),CountryClickListener {

   // class CountryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) --> data binding olmadan!
   class CountryHolder(var view: ItemCountryBinding) : RecyclerView.ViewHolder(view.root )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryHolder {
        val inflater=LayoutInflater.from(parent.context)
        //val view=inflater.inflate(R.layout.item_country,parent,false) --> data binding olmadan!
        val view=DataBindingUtil.inflate<ItemCountryBinding>(inflater,R.layout.item_country,parent,false)
        return CountryHolder(view)
    }

    override fun onBindViewHolder(holder: CountryHolder, position: Int) {

        holder.view.country=countryList[position]  // data binding ile kullanımı
        holder.view.listener=this



        /*
        holder.itemView.countryNameTxtRecyclerView.text=countryList[position].countryName
        holder.itemView.countryRegionTxtRecyclerView.text=countryList[position].countryRegion

        holder.itemView.setOnClickListener {
            val action=FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid)
            Navigation.findNavController(holder.itemView).navigate(action)
        }

        holder.itemView.itemImage.downloadFromUrl(countryList[position].countryImageUrl,
            placeholderProgressbar(holder.itemView.context))

         */
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    fun updateCountryList(newCountryList:List<Country>){
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }

    override fun onCountryClicked(v: View) {
        val uuid=v.countryUuidText.text.toString().toInt()
        val action=FeedFragmentDirections.actionFeedFragmentToCountryFragment(uuid)
        Navigation.findNavController(v).navigate(action)
    }
}