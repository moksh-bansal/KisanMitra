package com.appdev.kisanmitra.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.appdev.kisanmitra.databinding.FragmentHomeBinding
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import com.appdev.kisanmitra.R
import com.google.android.material.bottomnavigation.BottomNavigationView
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btnRefreshWeather.setOnClickListener {

            // Rotate animation
            binding.btnRefreshWeather.animate()
                .rotationBy(360f)
                .setDuration(600)
                .start()

            // show loading state
            binding.tvTemperature.text = "-- °C"
            binding.tvWeatherDesc.text = "Refreshing..."

            // call weather API again
            fetchLocationAndWeather()
        }

        setupGreeting()
        setupTips()
        fetchLocationAndWeather()
        setupQuickActions()
        return binding.root
    }

    private fun setupGreeting() {
        val user = FirebaseAuth.getInstance().currentUser
        val name = user?.displayName ?: "Farmer"
        binding.tvUsername.text = "$name 👋"
    }

    private fun setupQuickActions() {

        val bottomNav = requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottom_navigation)

        binding.btnMarketplace.setOnClickListener {
            bottomNav.selectedItemId = R.id.marketFragment
        }

        binding.btnDisease.setOnClickListener {
            bottomNav.selectedItemId = R.id.diseaseFragment
        }
    }

    private fun fetchLocationAndWeather() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
            return
        }

        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                getWeather(location.latitude, location.longitude)
            } else {
                binding.tvLocation.text = "Location unavailable"
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1001 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            fetchLocationAndWeather()
        }
    }

    private fun getWeather(lat: Double, lon: Double) {

        val apiKey = "7049f1a861572fbc112287b7846f9536"
        val url =
            "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&units=metric&appid=$apiKey"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {

                val json = JSONObject(response.body!!.string())
                val temp = json.getJSONObject("main").getDouble("temp")
                val desc = json.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("main")
                val city = json.getString("name")

                requireActivity().runOnUiThread {

                    binding.tvLocation.text = city
                    binding.tvTemperature.text = "${temp.toInt()}°C"
                    binding.tvWeatherDesc.text = desc

                    when (desc.lowercase()) {

                        "clear" -> binding.weatherCard.setCardBackgroundColor(
                            Color.parseColor("#FF9800") // warm sunny orange
                        )

                        "clouds" -> binding.weatherCard.setCardBackgroundColor(
                            Color.parseColor("#607D8B") // modern grey-blue
                        )

                        "rain" -> binding.weatherCard.setCardBackgroundColor(
                            Color.parseColor("#1565C0") // deep rain blue
                        )

                        "thunderstorm" -> binding.weatherCard.setCardBackgroundColor(
                            Color.parseColor("#37474F") // dark storm
                        )

                        else -> binding.weatherCard.setCardBackgroundColor(
                            Color.parseColor("#2E7D32") // fallback green
                        )
                    }
                }
            }
        })
    }

    private fun setupTips() {

        val tips = listOf(
            "Check soil moisture before irrigation 💧",
            "Avoid over-fertilization to protect crops 🌱",
            "Rotate crops to improve soil nutrients 🌾",
            "Monitor pests weekly to prevent outbreaks 🐛",
            "Use drip irrigation to save water 🚿"
        )

        val adapter = TipsAdapter(tips)
        binding.tipsViewPager.adapter = adapter

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                binding.tipsViewPager.currentItem =
                    (binding.tipsViewPager.currentItem + 1) % tips.size
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(runnable, 3000)
    }
}