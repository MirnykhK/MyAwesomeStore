package ru.adminmk.myawesomestore.repositories.remote_data

import ru.adminmk.myawesomestore.model.CategoriesRemoteAnswer
import ru.adminmk.myawesomestore.model.MainPageRemoteAnswer
import ru.adminmk.myawesomestore.model.Result

interface RemoteDataContract {
    suspend fun getCategoriesData(): Result<CategoriesRemoteAnswer>
    fun getCacheCategoriesRemoteAnswer(): CategoriesRemoteAnswer?

    suspend fun getMainPageData(): Result<MainPageRemoteAnswer>
    fun getCacheMainPageRemoteAnswer(): MainPageRemoteAnswer
    fun setCacheMainPageRemoteAnswer(cacheMainPageRemoteAnswer: MainPageRemoteAnswer)
}
