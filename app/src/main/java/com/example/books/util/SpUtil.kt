package com.example.books.util

import android.content.Context
import android.content.SharedPreferences
import java.util.*

object SpUtil {
    const val PREF_NAME = "BooksPreferences"
    const val POSITION = "position"
    const val QUERY = "query"
    fun getPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getPreferenceString(context: Context, key: String?): String? {
        return getPref(context).getString(key, "")
    }

    fun getPreferenceInt(context: Context, key: String?): Int {
        return getPref(context).getInt(key, 0)
    }

    fun setPreferenceString(context: Context, key: String?, value: String?) {
        val editor = getPref(context).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setPreferenceInt(context: Context, key: String?, value: Int) {
        val editor = getPref(context).edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getQueryList(context: Context): ArrayList<String> {
        val queryList = ArrayList<String>()
        for (i in 1..5) {
            var query = getPref(context).getString(QUERY + i, "")
            if (query!!.isNotEmpty()) {
                query = query.replace(",", " ")
                queryList.add(query.trim { it <= ' ' })
            }
        }
        return queryList
    }
}