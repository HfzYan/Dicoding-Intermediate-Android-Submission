package com.example.storyappdicoding.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val NAME_KEY = stringPreferencesKey("name")
    private val EMAIL_KEY = stringPreferencesKey("email")
    private val PASSWORD_KEY = stringPreferencesKey("password")
    private val USERID_KEY = stringPreferencesKey("userId")
    private val TOKEN_KEY = stringPreferencesKey("token")
    private val ISLOGIN_KEY = booleanPreferencesKey("state")

    fun getUser(): Flow<User> {
        return dataStore.data.map { user->
            User(
            user[NAME_KEY] ?: "",
            user[EMAIL_KEY] ?: "",
            user[PASSWORD_KEY] ?: "",
            user[USERID_KEY] ?: "",
            user[TOKEN_KEY] ?: "",
            user[ISLOGIN_KEY] ?: false
            )
        }
    }

    suspend fun saveUser(user: User){
        dataStore.edit { userData->
            userData[NAME_KEY] = user.name
            userData[EMAIL_KEY] = user.email
            userData[PASSWORD_KEY] = user.password
            userData[USERID_KEY] = user.userId
            userData[TOKEN_KEY] = user.token
            userData[ISLOGIN_KEY] = user.isLogin
        }
    }

    suspend fun logout(){
        dataStore.edit { userData->
            userData[NAME_KEY] = ""
            userData[EMAIL_KEY] = ""
            userData[PASSWORD_KEY] = ""
            userData[USERID_KEY] = ""
            userData[TOKEN_KEY] = ""
            userData[ISLOGIN_KEY] = false
        }

    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}