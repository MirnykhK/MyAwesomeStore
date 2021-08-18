package ru.adminmk.myawesomestore.repositories.remote_data

import android.content.Context
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import ru.adminmk.myawesomestore.R
import ru.adminmk.myawesomestore.model.*
import javax.inject.Inject

class RemoteDataFakeImpl @Inject constructor(private val context: Context) : RemoteDataContract {

    private var cacheCategoriesRemoteAnswer: CategoriesRemoteAnswer? = null

    override fun getCacheCategoriesRemoteAnswer(): CategoriesRemoteAnswer?
        = cacheCategoriesRemoteAnswer

    private var cacheMainPageRemoteAnswer: MainPageRemoteAnswer? = null

    override fun getCacheMainPageRemoteAnswer(): MainPageRemoteAnswer =
        cacheMainPageRemoteAnswer!!

    override suspend fun getCategoriesData(): Result<CategoriesRemoteAnswer> {
        delay(1000L)

        val listOMainCategories = getMainCategories()

        with(
            CategoriesRemoteAnswer(
                listOMainCategories,
                SaleCategorie("SUMMER SALES", "Up to 50% off")
            )
        ) {
            cacheCategoriesRemoteAnswer = this
            return Result.Success(this)
        }
    }

    private fun getMainCategories(): List<Categorie> {
        val testCateforyPic = ContextCompat.getDrawable(context, R.drawable.categorie)!!
        return listOf(
            Categorie(
                "New",
                testCateforyPic
            ),
            Categorie(
                "Clothes",
                testCateforyPic
            ),
            Categorie(
                "Shoes",
                testCateforyPic
            ),
            Categorie(
                "Shoes1",
                testCateforyPic
            ),
            Categorie(
                "Shoes2",
                testCateforyPic
            ),
            Categorie(
                "Shoes3",
                testCateforyPic
            ),
            Categorie(
                "Shoes4",
                testCateforyPic
            ),
            Categorie(
                "Accesories",
                testCateforyPic
            )
        )
    }

    override suspend fun getMainPageData(): Result<MainPageRemoteAnswer> {

        with(
            MainPageRemoteAnswer(
                getListOSaleItems(),
                getListONewItems(),
                getSaleContentString(),
                getNewContentString(),
                getBigBanner()
            )
        ) {
            cacheMainPageRemoteAnswer = this
            return Result.Success(this)
        }
    }

    private fun getListOSaleItems() =
        listOf(
            Sale(10, 4.2f, 10,
                "Dorothy Perkins", "Evening Dress", 15, 12),
            Sale(15, 5f, 199,
                "Dorothy Perkins1", "Evening Dress123", 1100, 1002),
            Sale(20, 2f, 0,
                "Dorothy Perkins2", "Evening Dress234", 15, 12),
            Sale(25, 0f, 0,
                "Dorothy Perkins3", "Evening Dress345", 15, 12)
        )

    private fun getListONewItems() =
        listOf(
            Product(4.2f, 10,
                "Dorothy Perkins", "Evening Dress", true, 103),
            Product(5f, 199,
                "Dorothy Perkins1", "Evening Dress123", true, 22),
            Product(2f, 0,
                "Dorothy Perkins2", "Evening Dress234", true, 44),
            Product(0f, 0,
                "Dorothy Perkins3", "Evening Dress345", true, 46)
        )

    private fun getSaleContentString() = "Super summer sale"
    private fun getNewContentString() = "You’ve never seen it before!"

    private fun getBigBanner() = BigBanner("Fashion sale", false,
        ContextCompat.getDrawable(context, R.drawable.big_banner)!!)
}
