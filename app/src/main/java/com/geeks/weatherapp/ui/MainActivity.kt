package com.geeks.weatherapp.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.Manifest
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.geeks.weatherapp.R
import com.geeks.weatherapp.data.model.CurrentWeather
import com.geeks.weatherapp.data.model.Weather
import com.geeks.weatherapp.data.repository.WeatherRepository
import com.geeks.weatherapp.databinding.ActivityMainBinding
import com.geeks.weatherapp.utils.WeatherRetrofit
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var city: String = "Bishkek"
    private val viewModel: MainViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.fetchWeather(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        binding.tvCityName.setOnClickListener {
            fetchLocation()
        }
    }

    private fun fetchLocation() {
        val task: Task<Location> = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101
            )
            return
        }

        task.addOnSuccessListener {
            val geocoder = Geocoder(this, Locale.getDefault())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    it.latitude,
                    it.longitude,
                    1,
                    object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            city = addresses[0].locality
                            getCurrentWeather()
                        }

                    })
            } else {
                val address =
                    geocoder.getFromLocation(it.latitude, it.longitude, 1) as List<Address>
                city = address[0].locality
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getCurrentWeather() {
        viewModel.fetchWeather(city)
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                WeatherRetrofit.api.getCurrentWeather(
                    city,
                    "metric",
                    applicationContext.getString(R.string.api_key)
                )
            } catch (e: IOException) {
                Toast.makeText(applicationContext, "app error ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(applicationContext, "http error ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!

                val iconID = data.weather[0].icon

                val imgUrl = "https://openweathermap.org/img/w/$iconID.png"

                Picasso.get().load(imgUrl).into(binding.imgWeather)


                binding.apply {
                    tvDesc.text = data.weather[0].description
                    tvWind.text = "${data.wind.speed.toString()} KM/H"
                    tvCityName.text = "${data.name} ${data.sys.country}"
                    tvTemp.text = "${data.main.temp?.toInt()}°C"
                    tvHumidity.text = "${data.main.humidity}"
                    tvPressure.text = "${data.main.pressure}"
                    tvFeelLike.text = "Feel like: ${data.main.feels_like?.toInt()}°C"
                    tvSunrise.text =
                        SimpleDateFormat(
                            "hh:mm a",
                            Locale.ENGLISH
                        ).format(
                            data.sys.sunrise!! * 1000
                        )
                    tvSunset.text =
                        SimpleDateFormat(
                            "hh:mm a", Locale.ENGLISH
                        ).format(
                            data.sys.sunset!! * 1000)
                }
            }
        }
    }
}
