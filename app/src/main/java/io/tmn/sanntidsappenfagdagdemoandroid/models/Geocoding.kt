package io.tmn.sanntidsappenfagdagdemoandroid.models

data class Geocoding(
    var features: List<Feature>
)

data class Properties(
    var id: String,
    var name: String,
    var locality: String,
    var county: String,
)

data class Geometry(
    var coordinates: Array<Double>
)

data class Feature(
    var geometry: Geometry,
    var properties: Properties
)