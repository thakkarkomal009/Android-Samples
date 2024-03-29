package com.mapboxturnbyturnnavigation.tripprogress.api

import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapboxturnbyturnnavigation.tripprogress.TripProgressAction
import com.mapboxturnbyturnnavigation.tripprogress.TripProgressProcessor
import com.mapboxturnbyturnnavigation.tripprogress.TripProgressResult
import com.mapboxturnbyturnnavigation.tripprogress.model.TripProgressUpdateFormatter
import com.mapboxturnbyturnnavigation.tripprogress.model.TripProgressUpdateValue

/**
 * Used for calculating data needed for trip progress.
 *
 * @param formatter an instance of [TripProgressUpdateFormatter]
 * @param processor an instance of [TripProgressProcessor]
 */
class MapboxTripProgressApi internal constructor(
    var formatter: TripProgressUpdateFormatter,
    private val processor: TripProgressProcessor,
) {

    /**
     * @param formatter contains various instances for use in formatting trip related data
     * for display in the UI
     *
     * @return a [MapboxTripProgressApi]
     */
    constructor(formatter: TripProgressUpdateFormatter) : this(formatter, TripProgressProcessor())

    /**
     * Calculates a trip progress update based on a [RouteProgress]
     *
     * @param routeProgress a [RouteProgress] object
     * @return an update to be rendered
     */
    fun getTripProgress(routeProgress: RouteProgress): TripProgressUpdateValue {
        val action = TripProgressAction.CalculateTripProgress(routeProgress)
        val result = processor.process(action) as TripProgressResult.RouteProgressCalculation

        return TripProgressUpdateValue(
            result.estimatedTimeToArrival,
            result.distanceRemaining,
            result.currentLegTimeRemaining,
            result.totalTimeRemaining,
            result.percentRouteTraveled,
            -1,
            formatter
        )
    }
}
