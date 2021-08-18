package ru.adminmk.myawesomestore.repositories.local_settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import ru.adminmk.myawesomestore.model.TopCategorie
import javax.inject.Inject

private const val FILE_NAME = "myawesomestore"
private const val TOP_CATEGORIE = "TopCategorie"

class LocalSettingsImpl @Inject constructor(private val context: Context) : LocalSettingsContract {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun getTopCategory(): TopCategorie {
        return sharedPreferences.getInt(TOP_CATEGORIE, -1).run {
            when (this) {
                -1 ->  TopCategorie.WOMEN
                else -> TopCategorie.values().get(this)
            }
        }
    }

    override fun saveTopCategory(topCategorie: TopCategorie) {
        sharedPreferences.edit { putInt(TOP_CATEGORIE, topCategorie.ordinal) }
    }
}
