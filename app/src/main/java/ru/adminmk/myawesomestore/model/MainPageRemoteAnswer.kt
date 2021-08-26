package ru.adminmk.myawesomestore.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class MainPageRemoteAnswer(
    val listOSaleItems: List<Sale>,
    val listONewItems: List<Product>,
    val saleContentString: String,
    val newContentString: String,
    val bigBanner: BigBanner
) : Serializable, Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    companion object {
        private const val serialVersionUID = 1L

        @JvmField
        val CREATOR: Parcelable.Creator<MainPageRemoteAnswer> =
            object : Parcelable.Creator<MainPageRemoteAnswer> {
                override fun createFromParcel(source: Parcel): MainPageRemoteAnswer {
                    return MainPageRemoteAnswer(source)
                }

                override fun newArray(size: Int): Array<MainPageRemoteAnswer?> {
                    return arrayOfNulls<MainPageRemoteAnswer>(size)
                }
            }
    }

    constructor(parcel: Parcel) : this(
        gatherListOSaleItems(parcel),
        gatherListONewItems(parcel),
        parcel.readString()!!,
        parcel.readString()!!,
        getBanner(parcel)
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(listOSaleItems.size)
        for (curSale in listOSaleItems) {
            dest.writeInt(curSale.discountPercent)
            dest.writeFloat(curSale.rating)
            dest.writeInt(curSale.ratingReviews)
            dest.writeString(curSale.name)
            dest.writeString(curSale.brand)
            dest.writeInt(curSale.oldPrice)
            dest.writeInt(curSale.newPrice)
        }

        dest.writeInt(listONewItems.size)
        for (curNewItem in listONewItems) {
            dest.writeInt(curNewItem.price)
            dest.writeFloat(curNewItem.rating)
            dest.writeInt(curNewItem.ratingReviews)
            dest.writeString(curNewItem.name)
            dest.writeString(curNewItem.brand)
            dest.writeInt(if (curNewItem.isNew) 1 else 0)
        }
        dest.writeString(saleContentString)
        dest.writeString(newContentString)

        dest.writeString(bigBanner.text)
        dest.writeInt(if (bigBanner.isLightTheme) 1 else 0)
        dest.writeInt(bigBanner.pic)
    }
}

private fun getBanner(parcel: Parcel): BigBanner {
    with(parcel) {
        return BigBanner(
            readString()!!,
            if (readInt() == 1) true else false,
            readInt()
        )
    }
}

private fun gatherListONewItems(parcel: Parcel): List<Product> {

    val result = mutableListOf<Product>()
    with(parcel) {
        val lenght = readInt()
        for (i in 0 until lenght) {

            Product(
                price = readInt(),
                rating = readFloat(),
                ratingReviews = readInt(),
                name = readString()!!,
                brand = readString()!!,
                isNew = if (readInt() == 1) true else false
            ).also {
                result.add(it)
            }
        }
    }

    return result
}

private fun gatherListOSaleItems(parcel: Parcel): List<Sale> {

    val result = mutableListOf<Sale>()

    with(parcel) {
        val lenght = readInt()
        for (i in 0 until lenght) {

            Sale(
                discountPercent = readInt(),
                rating = readFloat(),
                ratingReviews = readInt(),
                name = readString()!!,
                brand = readString()!!,
                oldPrice = readInt(),
                newPrice = readInt()
            ).also {
                result.add(it)
            }
        }
    }

    return result
}
