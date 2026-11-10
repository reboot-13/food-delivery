package com.example.fooddeliveryandroid.domain.model

data class DeliveryCity (
    val name: String,
    val southWestLatitude: Double,
    val southWestLongitude: Double,
    val northEastLatitude: Double,
    val northEastLongitude: Double
)
object DeliveryCities  {
    fun values(): List<DeliveryCity> {
        return listOf(MOSCOW, TLT)
    }

    val MOSCOW = DeliveryCity(
        name = "Москва",
        southWestLatitude = 55.4899,
        southWestLongitude = 37.3193,
        northEastLatitude = 56.0097,
        northEastLongitude = 37.9457
    )

     val TLT = DeliveryCity(
            name = "Тольятти",
            southWestLatitude = 53.4250,
            southWestLongitude = 49.2500,
            northEastLatitude = 53.6500,
            northEastLongitude = 49.6500
     )
}