package ru.adminmk.myawesomestore.model

class MainPageRemoteAnswer(
    val listOSaleItems: List<Sale>,
    val listONewItems: List<Product>,
    val saleContentString: String,
    val newContentString: String,
    val bigBanner: BigBanner
)
