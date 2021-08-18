package ru.adminmk.myawesomestore.model

class Sale(
    val discountPercent: Int,
    rating: Float,
    ratingReviews: Int,
    brand: String,
    name: String,
    val oldPrice: Int,
    val newPrice: Int
) : Item(rating, ratingReviews, brand, name) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Sale) return false
        if (!super.equals(other)) return false

        if (discountPercent != other.discountPercent) return false
        if (oldPrice != other.oldPrice) return false
        if (newPrice != other.newPrice) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + discountPercent
        return result
    }

    override fun toString(): String {
        return "${super.toString()} Sale(discountPercent=$discountPercent, oldPrice=$oldPrice, newPrice=$newPrice)"
    }
}

class Product(
    rating: Float,
    ratingReviews: Int,
    brand: String,
    name: String,
    val isNew: Boolean,
    val Price: Int
) : Item(rating, ratingReviews, brand, name) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Product) return false
        if (!super.equals(other)) return false

        if (isNew != other.isNew) return false
        if (Price != other.Price) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + Price
        return result
    }

    override fun toString(): String {
        return "${super.toString()} Product(isNew=$isNew, Price=$Price)"
    }
}

abstract class Item(
    val rating: Float,
    val ratingReviews: Int,
    val brand: String,
    val name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Item) return false

        if (brand != other.brand) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = brand.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    override fun toString(): String {
        return "Item(brand='$brand', name='$name')"
    }
}
