package com.example.apiwithrecycleview

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.apiwithrecycleview.server.ServerApi
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val TAG="MainActivity"
class MainActivity : AppCompatActivity() {
    private var progressBarStatus:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_layout)
        Log.i(TAG,"This is MainActivity")
        getData()
        refreshButton.setOnClickListener {
            progressBarStatus=0
            setProgressValue(progressBarStatus);
            getData()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadUi(results: ArrayList<DataClass.Results>) {
        for (i in results){
            UserName.text="${i.name?.first} ${i.name?.last}"
            Picasso.get().load(i.picture?.large).into(profile_image)
            locationId.text="${i.location?.state},${i.location?.city},${i.location?.country}"
            GenderId.text="Gender : ${i.gender.toString()}"
            Log.i(TAG,"Gender : ${i.gender}")
        }
    }
    private fun getData(){
        val gson=GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();
        val baseUrl="https://randomuser.me/"
        val retrofit=Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val serverApi=retrofit.create(ServerApi::class.java)
        val call=serverApi.getUser()
        call.enqueue(object : Callback<DataClass>{
            override fun onResponse(call: Call<DataClass>, response: Response<DataClass>) {
                if (!response.isSuccessful){
                    Log.i(TAG,"Failed ${response.code()}")
                }
                Log.i(TAG,"Sussess ${response.body()}")
                response.body()?.let { loadUi(it.results) }

            }

            override fun onFailure(call: Call<DataClass>, t: Throwable) {
                Log.i(TAG,"Failed ${t.message}")
            }

        })
    }
    private fun setProgressValue(progress: Int) {

        // set the progress
        simpleProgressBar.progress = progress
        // thread is used to change the progress value
        val thread = Thread {
            try {
                Thread.sleep(500)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            setProgressValue(progress + 33)
        }
        thread.start()

    }

}
