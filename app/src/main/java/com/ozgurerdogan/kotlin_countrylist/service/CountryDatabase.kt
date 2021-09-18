package com.ozgurerdogan.kotlin_countrylist.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ozgurerdogan.kotlin_countrylist.model.Country

@Database(entities = arrayOf(Country::class),version = 1)
abstract class CountryDatabase :RoomDatabase(){
    abstract fun countryDao():CountryDao

    //Singleton

    companion object{// diğer sınıflardan da çarılabilir hale getiriyor...

        @Volatile private var instance:CountryDatabase?=null

        //Volatile önüne geldiği fonksiyonu farklı thread lerden de görülebilir hale getiriyor. eğer corutinue kullanmasaydık ve farklı threadlerde işlem yapmasaydık volatile kullanmaya gerek yoktu..

        //Bu sınıfı singleton yapma amacımız da, farklı threadler de bu fonksiyonu çalıştırdığımız için farklı thread lerde birden fazla obje oluşturulmasını engellemektir.


        private val lock=Any()

        //

        operator fun invoke(context:Context)= instance?: synchronized(lock){
            instance?: makeDatabase(context).also {
                instance=it
            }
        }


        private fun makeDatabase(context:Context)= Room.databaseBuilder(context,CountryDatabase::class.java,"countrydatabase").build()



    }
}