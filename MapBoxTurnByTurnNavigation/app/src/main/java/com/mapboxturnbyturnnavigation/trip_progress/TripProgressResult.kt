package com.mapboxturnbyturnnavigation.tripprogress

internal sealed class TripProgressResult {
    data class RouteProgressCalculation(
        val estimatedTimeToArrival: Long,
        val distanceRemaining: Double,
        val currentLegTimeRemaining: Double,
        val totalTimeRemaining: Double,
        val percentRouteTraveled: Double
    ) : TripProgressResult()
}
