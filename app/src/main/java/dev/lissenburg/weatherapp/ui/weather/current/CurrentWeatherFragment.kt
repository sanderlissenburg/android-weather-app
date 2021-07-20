package dev.lissenburg.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import dev.lissenburg.weatherapp.R
import dev.lissenburg.weatherapp.ui.base.ScopedFragment
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance

class CurrentWeatherFragment : ScopedFragment(), DIAware {

    override val di by closestDI()
    private val viewModelFactory by instance<CurrentWeatherViewModelFactory>()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        currentWeather.observe(this@CurrentWeatherFragment, Observer { it ->
            if (it == null) return@Observer

            val groupLoading = view?.findViewById<Group>(R.id.group_loading)

            groupLoading?.visibility = View.GONE


            updateDateToToday()

            updateTemperature(
                it.temperature,
                it.feelsLikeTemperature
            )

            updateCondition(it.conditionText)
            updatePrecipitation(it.precipitationVolume)
            updateWind(it.windDirection, it.windSpeed)
            updateVisibility(it.visibilityDistance)

            val imageView = view?.findViewById<ImageView>(R.id.imageView_condition_icon);
            Glide.with(this@CurrentWeatherFragment)
                .load("https:${it.conditionIconUrl}")
                .into(imageView!!)
        })

        weatherLocation.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer
            updateLocation(it.name)
        })
    }

    private fun updateLocation(location: String) {
        (activity as AppCompatActivity).supportActionBar?.title = location
        // val appActivity = activity as AppCompatActivity;
        // appActivity.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as AppCompatActivity).supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperature(temperature: Double, feelsLike: Double) {
        val unit = if (viewModel.isMetric) "°C" else "°F"

        val temperatureTextView = view?.findViewById<TextView>(R.id.textView_temperature)
        val feelsLikeTextView = view?.findViewById<TextView>(R.id.textView_feels_like_temperature)

        temperatureTextView?.text = "$temperature $unit"
        feelsLikeTextView?.text = "Feels like $feelsLike $unit"
    }

    private fun updateCondition(condition: String) {
        val conditionTextView = view?.findViewById<TextView>(R.id.textView_condition)
        conditionTextView?.text = condition
    }

    private fun updatePrecipitation(precipitation: Double) {
        val unit = if (viewModel.isMetric) "mm" else "in"

        val precipitationTextView = view?.findViewById<TextView>(R.id.textView_precipitation)
        precipitationTextView?.text = "Percipitation: $precipitation $unit"
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unit = if (viewModel.isMetric) "kph" else "mph"

        val windTextView = view?.findViewById<TextView>(R.id.textView_wind)
        windTextView?.text = "Wind: $windDirection $windSpeed $unit"
    }

    private fun updateVisibility(visibility: Double) {
        val unit = if (viewModel.isMetric) "km" else "mi"

        val visibilityTextView = view?.findViewById<TextView>(R.id.textView_visibility)
        visibilityTextView?.text = "Visibility: $visibility $unit"
    }
}