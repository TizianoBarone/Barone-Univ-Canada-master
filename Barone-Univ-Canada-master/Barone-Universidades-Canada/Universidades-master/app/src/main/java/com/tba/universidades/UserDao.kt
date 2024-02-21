package com.tba.universidades

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password")
    fun getUserByUsernameAndPassword(username: String, password: String): User?
    // Método para obtener el usuario actual desde las preferencias compartidas
    fun getCurrentUser(context: Context): User? {
        val sharedPreferences: SharedPreferences? = context.getSharedPreferences("prefs", MODE_PRIVATE)
        val username = sharedPreferences?.getString("currentUsername", null)
        val password = sharedPreferences?.getString("currentPassword", null)
        return if (username != null && password != null) {
            User(0, username, password)
        } else {
            null
        }
    }
}
