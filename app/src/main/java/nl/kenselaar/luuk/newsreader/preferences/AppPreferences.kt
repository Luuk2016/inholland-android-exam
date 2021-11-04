package nl.kenselaar.luuk.newsreader.preferences

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val NAME = "NewsApp"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    //SharedPreferences variables
    private val IS_LOGIN = Pair("is_login", false)
    private val AUTH_TOKEN = Pair("auth_token", "")

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    //an inline function to put variable and save it
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var isLogin: Boolean
        get() = preferences.getBoolean(IS_LOGIN.first, IS_LOGIN.second)
        set(value) = preferences.edit {
            it.putBoolean(IS_LOGIN.first, value)
        }

    var authToken: String
        get() = preferences.getString(AUTH_TOKEN.first, AUTH_TOKEN.second) ?: ""
        set(value) = preferences.edit {
            it.putString(AUTH_TOKEN.first, value)
        }
}