package com.ang.acb.todolearn.util

import java.text.DateFormat
import java.text.DateFormat.SHORT
import java.text.NumberFormat
import java.text.ParseException
import java.util.*

object LocaleUtils {

    fun getFormattedDateForUserChosenLocale() : String {
        val date = Date()

        // To get the date format for the user-chosen locale call
        // DateFormat.getDateInstance(), this is equivalent to:
        // getDateInstance(DEFAULT, Locale.getDefault(Locale.Category.FORMAT)).
        val dateFormat = DateFormat.getDateInstance()

        // The result is something like "Dec 31, 2016" (for the U.S. locale)
        return dateFormat.format(date)
    }


    fun getFormattedDateForLocale(locale: Locale) : String {
        val date = Date()

        // Get the date format for any locale call DateFormat.getDateInstance(int, Locale).
        // The style is a formatting style such as SHORT for "M/d/yy" in the U.S. locale.
        val dateFormat = DateFormat.getDateInstance(SHORT, locale)

        // The result is something like "12.31.16" (for the U.S. locale)
        return dateFormat.format(date)
    }


    fun getFormattedTimeForLocale(locale: Locale) : String{
        val date = Date()
        val timeFormat = DateFormat.getTimeInstance(
            SHORT, // time style
            locale
        )

        // The result is something like "3:30pm" (for the U.S. locale).
        return timeFormat.format(date)
    }


    fun getFormattedDateTimeForLocale(locale: Locale): String {
        val date = Date()

        val dateTimeFormat = DateFormat.getDateTimeInstance(
            SHORT, // date style
            SHORT, // time style
            locale
        )

        return dateTimeFormat.format(date)
    }


    fun getFormattedNumberForUserChosenLocale(quantity: Int) :String {
        // Get the number format for this locale.
        val numberFormat: NumberFormat = NumberFormat.getInstance()
        return numberFormat.format(quantity)
    }


    fun getFormattedNumberForLocale(locale: Locale, quantity: Int) :String {
        // Get the number format for this locale.
        // This is the same as NumberFormat.getNumberInstance(Locale)
        val numberFormat: NumberFormat = NumberFormat.getInstance(locale)
        return numberFormat.format(quantity)
    }


    fun getFormattedPercentageForLocale(locale: Locale, percentage: Int) :String {
        // Get the percentage format for this locale.
        val percentageFormat: NumberFormat = NumberFormat.getPercentInstance(locale)
        return percentageFormat.format(percentage)
    }


    fun getFormattedQualityForLocale(locale: Locale, quantity: String): Int? {
        return try {
            NumberFormat.getInstance(locale).parse(quantity)?.toInt()
        } catch (e: ParseException) {
            null
        }
    }


    fun getFormattedCurrencyForLocale(locale: Locale, price: Double) :String {
        // Get locale's currency.
        val currencyFormat = NumberFormat.getCurrencyInstance(locale)
        return currencyFormat.format(price)
    }


    fun getFormattedPriceForFRorIL(price: Double) : String{
        return when (Locale.getDefault().country) {
            "FR", "IL"-> {
                // Use the currency format for France or Israel.
                val currencyFormat = NumberFormat.getCurrencyInstance()
                currencyFormat.format(price)
            }

            else -> {
                // Use the currency format for U.S.
                val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
                currencyFormat.format(price)
            }
        }
    }




}


