package ru.adminmk.myawesomestore.repositories.local_settings

import ru.adminmk.myawesomestore.model.TopCategorie

interface LocalSettingsContract {
    fun getTopCategory(): TopCategorie
    fun saveTopCategory(topCategorie: TopCategorie)
}
