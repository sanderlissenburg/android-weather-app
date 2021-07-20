package dev.lissenburg.weatherapp.ui.weather.future.list


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.lissenburg.weatherapp.R
import dev.lissenburg.weatherapp.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeather
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle


class FutureWeatherItemAdapter(
    var weatherEntries: List<UnitSpecificSimpleFutureWeather>,
    var isMetric: Boolean,
    var onClick: (UnitSpecificSimpleFutureWeather) -> Unit
): RecyclerView.Adapter<FutureWeatherItemAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textViewDate: TextView
        val textViewTemperature: TextView
        val textViewCondition: TextView
        val imageViewConditionIcon: ImageView

        init {
            textViewDate = view.findViewById(R.id.textView_date)
            textViewTemperature = view.findViewById(R.id.textView_temperature)
            textViewCondition = view.findViewById(R.id.textView_condition)
            imageViewConditionIcon = view.findViewById(R.id.imageView_condition_icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_future_weather, parent, false)



        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val unit = if (isMetric) "°C" else "°F"

        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        var entry = weatherEntries[position]
        holder.textViewDate.text = entry.date.format(formatter)
        holder.textViewTemperature.text = "${entry.avgTemperature.toString()} $unit"
        holder.textViewCondition.text = entry.conditionText

        Glide.with(holder.itemView)
            .load("https:${entry.conditionIconUrl}")
            .into(holder.imageViewConditionIcon!!)

        holder.itemView.setOnClickListener {
            onClick(entry)
        }

    }

    override fun getItemCount(): Int {
        return weatherEntries.count()
    }

    fun updateWeatherEntries(entries: List<UnitSpecificSimpleFutureWeather>) {
        weatherEntries = entries
        notifyDataSetChanged()
    }
}