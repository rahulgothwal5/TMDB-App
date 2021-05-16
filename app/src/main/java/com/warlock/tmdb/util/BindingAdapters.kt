package com.warlock.tmdb.util

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.warlock.tmdb.data.db.entity.GenreX
import com.warlock.tmdb.ui.MainActivity.Companion.genreList
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("visibleGone")
    public fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }


    @JvmStatic
    @BindingAdapter("imageUrl")
    fun imageUrl(view: ImageView, url: String?) {
        if (url.isNullOrEmpty().not())
            Glide.with(view).load(url).into(view)
    }

    @JvmStatic
    @BindingAdapter("fancyDate")
    fun getFancyDate(view: TextView, releaseDate: String?) {
        try {
            return if (releaseDate.isNullOrEmpty().not()) {
                val date = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).parse(
                    releaseDate ?: ""
                )
                val calendar = Calendar.getInstance()
                calendar.time = date


                val dayOfTheWeekNumber = calendar.get(Calendar.DAY_OF_WEEK) // Thursday
                val day = calendar.get(Calendar.DAY_OF_MONTH)// 20
                val monthNumber = calendar.get(Calendar.MONTH) // 06
                val year = calendar.get(Calendar.YEAR)  // 2013

                var monthString = "January"
                var dayOfTheWeek = "Sunday"
                val dfs = DateFormatSymbols()
                val months: Array<String> = dfs.months
                val days: Array<String> = dfs.weekdays
                if (monthNumber in 0..11) {
                    monthString = months[monthNumber]
                }
                if (dayOfTheWeekNumber in 0..6) {
                    dayOfTheWeek = days[dayOfTheWeekNumber]
                }
                view.text = "$dayOfTheWeek, $monthString $day,$year"
            } else view.text = "Coming soon"
        } catch (e: Exception) {
            view.text = "Coming soon"
        }


    }


    @JvmStatic
    @BindingAdapter("isRefreshing")
    fun isRefreshing(view: SwipeRefreshLayout, refresh: Boolean) {
        view.isRefreshing = refresh
    }


    @JvmStatic
    @BindingAdapter("genreFromIds")
    fun genreFromIds(view: TextView, idList: List<Int>) {
        if (genreList.isEmpty().not()) {
            var genreStr = ""
            for (item in idList) {

                val gen = genreList[item]
                genreStr += if (genreStr.isEmpty())
                    gen
                else
                    ", $gen"
            }
            view.text = genreStr
        }
    }

    @JvmStatic
    @BindingAdapter("getGenreStr")
    fun getGenreStr(view: TextView, idList: List<GenreX>?) {
        if (idList.isNullOrEmpty().not()) {
            var genreStr = ""
            for (item in idList!!) {
                val gen = item.name
                genreStr += if (genreStr.isEmpty())
                    gen
                else
                    ", $gen"
            }
            view.text = genreStr
        }
    }

    @JvmStatic
    @BindingAdapter("til:errorText")
    fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
        if (errorMessage.isNullOrEmpty()) {
            view.isErrorEnabled = false
        } else {
            view.isErrorEnabled = true
            view.error = errorMessage
        }
    }


    @JvmStatic
    @BindingAdapter("getDurationFromMin")
    fun getDurationFromMin(view: TextView, duration: Int?) {
        if (duration!=null){
            val hours: Int = duration / 60
            val minutes: Int = duration % 60
            view.text = "" + hours + "Hr " + minutes + "Min"
        }
    }
}
