package com.mapboxcustomviewnavigation

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.parseColor
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.TimeUnit

class GenerateRouteActivity : AppCompatActivity(), MapboxMap.OnMapClickListener {
    private val ORIGIN_ICON_ID = "origin-icon-id"
    private val DESTINATION_ICON_ID = "destination-icon-id"
    private val ROUTE_LAYER_ID = "route-layer-id"
    private val ROUTE_LINE_SOURCE_ID = "route-source-id"
    private val ICON_LAYER_ID = "icon-layer-id"
    private val ICON_SOURCE_ID = "icon-source-id"
    private lateinit var mapView: MapView
    private var mapboxMap: MapboxMap? = null
    private var client: MapboxDirections? = null
    private val ORIGIN_POINT = Point.fromLngLat(-117.735249, 33.679820)
    private var DESTINATION_POINT = Point.fromLngLat(-117.739626, 33.680659)
    private val LINE_WIDTH = 6f
    private val ORIGIN_COLOR = "#2096F3"
    private val DESTINATION_COLOR = "#F84D4D"
    private lateinit var mActivity: Activity
    private lateinit var imgStart: ImageView
    private var destinationLatitude: Double = 0.0
    private var destinationLongitude: Double = 0.0
    private var rlCar: RelativeLayout? = null
    private var rlWalk: RelativeLayout? = null
    private var rlCycle: RelativeLayout? = null
    private var txtByDriving: TextView? = null
    private var txtByWalk: TextView? = null
    private var txtByCycle: TextView? = null
    private var lastSelectedDirectionsProfile = DirectionsCriteria.PROFILE_DRIVING
    private var drivingRoute: DirectionsRoute? = null
    private var walkingRoute: DirectionsRoute? = null
    private var cyclingRoute: DirectionsRoute? = null
    private val profiles = arrayOf(
        DirectionsCriteria.PROFILE_DRIVING,
        DirectionsCriteria.PROFILE_CYCLING,
        DirectionsCriteria.PROFILE_WALKING
    )
    private var firstRouteDrawn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_generate_route)

        mActivity = this@GenerateRouteActivity

        // Setup the MapView
        mapView = findViewById(R.id.mapView)
        imgStart = findViewById(R.id.imgStart)
        rlCar = findViewById(R.id.rlCar)
        rlWalk = findViewById(R.id.rlWalk)
        rlCycle = findViewById(R.id.rlCycle)
        txtByDriving = findViewById(R.id.txtByDriving)
        txtByWalk = findViewById(R.id.txtByWalk)
        txtByCycle = findViewById(R.id.txtByCycle)
        txtByDriving!!.setTextColor(resources.getColor(android.R.color.holo_blue_dark))

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMapView ->
            mapboxMap = mapboxMapView

            val position = CameraPosition.Builder()
                .target(LatLng(33.679820, -117.735249)) // Sets the new camera position
                .zoom(17.0) // Sets the zoom
                .bearing(180.0) // Rotate the camera
                .tilt(30.0) // Set the camera tilt
                .build() // Creates a CameraPosition from the builder

            mapboxMap!!.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(position), 7000
            )

            mapboxMap!!.setStyle(
                Style.Builder()
                    .fromUri(Style.DARK)
                    .withImage(
                        ORIGIN_ICON_ID,
                        BitmapUtils.getBitmapFromDrawable(
                            resources.getDrawable(R.drawable.blue_marker)
                        )!!
                    )
                    .withImage(
                        DESTINATION_ICON_ID,
                        BitmapUtils.getBitmapFromDrawable(
                            resources.getDrawable(R.drawable.red_marker)
                        )!!
                    ), object : Style.OnStyleLoaded {
                    override fun onStyleLoaded(@NonNull style: Style) {
                        initSources(style)
                        initLayers(style)

                        // Get the directions route from the Mapbox Directions API
                        getAllRoutes(false);
                        mapboxMap!!.addOnMapClickListener(mActivity as GenerateRouteActivity)
                        Toast.makeText(
                            mActivity,
                            getString(R.string.tap_map_instruction_gradient_directions),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

            initViewClickListeners()
        }
        imgStart.setOnClickListener {
            var mIntent: Intent = Intent(mActivity, StartNavigationActivity::class.java)
            startActivity(mIntent)
        }
    }

    /**
     * Add the route and marker sources to the map
     */
    private fun initSources(loadedMapStyle: Style) {
        loadedMapStyle.addSource(
            GeoJsonSource(
                ROUTE_LINE_SOURCE_ID,
                GeoJsonOptions().withLineMetrics(true)
            )
        )
        loadedMapStyle.addSource(
            GeoJsonSource(
                ICON_SOURCE_ID,
                getOriginAndDestinationFeatureCollection()
            )
        )
    }

    /**
     * Util method that returns a [FeatureCollection] with the latest origin and destination locations.
     *
     * @return a [FeatureCollection] to be used for creating a new [GeoJsonSource] or
     * updating a source's GeoJSON.
     */
    private fun getOriginAndDestinationFeatureCollection(): FeatureCollection? {
        val originFeature: Feature = Feature.fromGeometry(ORIGIN_POINT)
        originFeature.addStringProperty("originDestination", "origin")
        val destinationFeature: Feature = Feature.fromGeometry(DESTINATION_POINT)
        destinationFeature.addStringProperty("originDestination", "destination")
        return FeatureCollection.fromFeatures(arrayOf<Feature>(originFeature, destinationFeature))
    }

    /**
     * Add the route and marker icon layers to the map
     */
    private fun initLayers(loadedMapStyle: Style) {
        // Add the LineLayer to the map. This layer will display the directions route.
        loadedMapStyle.addLayer(
            LineLayer(ROUTE_LAYER_ID, ROUTE_LINE_SOURCE_ID).withProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(LINE_WIDTH),
                lineGradient(
                    interpolate(
                        linear(),
                        lineProgress(),  // This is where the gradient color effect is set. 0 -> 1 makes it a two-color gradient
                        // See LineGradientActivity for an example of a 2+ multiple color gradient line.
                        stop(0f, color(parseColor(ORIGIN_COLOR))),
                        stop(1f, color(parseColor(DESTINATION_COLOR)))
                    )
                )
            )
        )

        // Add the SymbolLayer to the map to show the origin and destination pin markers
        loadedMapStyle.addLayer(
            SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(
                    match(
                        get("originDestination"), literal("origin"),
                        stop("origin", ORIGIN_ICON_ID),
                        stop("destination", DESTINATION_ICON_ID)
                    )
                ),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconOffset(arrayOf(0f, -4f))
            )
        )
    }

    override fun onMapClick(mapClickPoint: LatLng): Boolean {
        // Move the destination point to wherever the map was tapped
        DESTINATION_POINT =
            Point.fromLngLat(mapClickPoint.getLongitude(), mapClickPoint.getLatitude())

        destinationLatitude = mapClickPoint.getLatitude()
        destinationLongitude = mapClickPoint.getLongitude()

        // Get a new Directions API route to that new destination and eventually re-draw the gradient route line.
        getAllRoutes(true)
        return true
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the Directions API request
        if (client != null) {
            client!!.cancelCall()
        }
        if (mapboxMap != null) {
            mapboxMap!!.removeOnMapClickListener(this)
        }
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private lateinit var responseBody: String
        fun getCurrentRoute(): String? {
            return responseBody
        }
    }

    /**
     * Set up the click listeners on the buttons for each Directions API profile.
     */
    private fun initViewClickListeners() {
        rlCar!!.setOnClickListener(View.OnClickListener {
            txtByDriving!!.setTextColor(resources.getColor(android.R.color.holo_blue_dark))
            txtByWalk!!.setTextColor(Color.BLACK)
            txtByCycle!!.setTextColor(Color.BLACK)
            lastSelectedDirectionsProfile = DirectionsCriteria.PROFILE_DRIVING
            showRouteLine()
        })
        rlWalk!!.setOnClickListener(View.OnClickListener {
            txtByDriving!!.setTextColor(Color.BLACK)
            txtByWalk!!.setTextColor(resources.getColor(android.R.color.holo_blue_dark))
            txtByCycle!!.setTextColor(Color.BLACK)
            lastSelectedDirectionsProfile = DirectionsCriteria.PROFILE_WALKING
            showRouteLine()
        })
        rlCycle!!.setOnClickListener(View.OnClickListener {
            txtByDriving!!.setTextColor(Color.BLACK)
            txtByWalk!!.setTextColor(Color.BLACK)
            txtByCycle!!.setTextColor(resources.getColor(android.R.color.holo_blue_dark))
            lastSelectedDirectionsProfile = DirectionsCriteria.PROFILE_CYCLING
            showRouteLine()
        })
    }

    /**
     * Display the Directions API route line depending on which profile was last
     * selected.
     */
    private fun showRouteLine() {
        if (mapboxMap != null) {
            mapboxMap!!.getStyle { style -> // Retrieve and update the source designated for showing the directions route
                val routeLineSource =
                    style.getSourceAs<GeoJsonSource>(ROUTE_LINE_SOURCE_ID)


                // Create a LineString with the directions route's geometry and reset the GeoJSON source for the route LineLayer source
                if (routeLineSource != null) {
                    when (lastSelectedDirectionsProfile) {
                        DirectionsCriteria.PROFILE_DRIVING -> routeLineSource.setGeoJson(
                            LineString.fromPolyline(
                                drivingRoute!!.geometry()!!,
                                PRECISION_6
                            )
                        )
                        DirectionsCriteria.PROFILE_WALKING -> routeLineSource.setGeoJson(
                            LineString.fromPolyline(
                                walkingRoute!!.geometry()!!,
                                PRECISION_6
                            )
                        )
                        DirectionsCriteria.PROFILE_CYCLING -> routeLineSource.setGeoJson(
                            LineString.fromPolyline(
                                cyclingRoute!!.geometry()!!,
                                PRECISION_6
                            )
                        )
                        else -> {
                        }
                    }
                }
            }
        }
    }

    /**
     * Load route info for each Directions API profile.
     *
     * @param fromMapClick whether the route loading is being triggered from tapping
     * on the map
     */
    private fun getAllRoutes(fromMapClick: Boolean) {
        for (profile in profiles) {
            getSingleRoute(profile, fromMapClick)
        }
    }

    /**
     * Make a request to the Mapbox Directions API. Once successful, pass the route to the
     * route layer.
     *
     * @param profile the directions profile to use in the Directions API request
     */
    private fun getSingleRoute(profile: String, fromMapClick: Boolean) {
        client = MapboxDirections.builder()
            .origin(ORIGIN_POINT)
            .destination(DESTINATION_POINT)
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .profile(profile)
            .accessToken(getString(R.string.mapbox_access_token))
            .steps(true)
            .bannerInstructions(true)
            .voiceInstructions(true )
            .build()
        client!!.enqueueCall(object : Callback<DirectionsResponse?> {
            override fun onResponse(
                call: Call<DirectionsResponse?>,
                response: Response<DirectionsResponse?>
            ) {
                // You can get the generic HTTP info about the response
                Timber.d("Response code: " + response.code())
                if (response.body() == null) {
                    Timber.e("No routes found, make sure you set the right user and access token.")
                    return
                } else if (response.body()!!.routes().size < 1) {
                    Timber.e("No routes found")
                    return
                }

                responseBody = response.body()!!.routes().get(0).toJson()
                when (profile) {
                    DirectionsCriteria.PROFILE_DRIVING -> {
                        drivingRoute = response.body()!!.routes()[0]
                        txtByDriving!!.setText(
                            TimeUnit.SECONDS.toMinutes(drivingRoute!!.duration().toLong())
                                .toString() + "m"
                        )

                        Log.d(
                            "Driving Time:::",
                            TimeUnit.SECONDS.toMinutes(drivingRoute!!.duration().toLong())
                                .toString()
                        )
                        if (!firstRouteDrawn) {
                            showRouteLine()
                            firstRouteDrawn = true
                        }
                    }
                    DirectionsCriteria.PROFILE_WALKING -> {
                        walkingRoute = response.body()!!.routes()[0]

                        Log.d(
                            "Walking Time:::",
                            TimeUnit.SECONDS
                                .toMinutes(walkingRoute!!.duration().toLong()).toString()
                        )
                        txtByWalk!!.setText(
                            TimeUnit.SECONDS
                                .toMinutes(walkingRoute!!.duration().toLong()).toString() + "m"
                        )

                    }
                    DirectionsCriteria.PROFILE_CYCLING -> {
                        cyclingRoute = response.body()!!.routes()[0]

                        Log.d(
                            "Cycling Time:::",
                            TimeUnit.SECONDS
                                .toMinutes(cyclingRoute!!.duration().toLong()).toString()
                        )
                        txtByCycle!!.setText(
                            TimeUnit.SECONDS
                                .toMinutes(cyclingRoute!!.duration().toLong()).toString() + "m"
                        )
                    }
                    else -> {
                    }
                }
                if (fromMapClick) {
                    showRouteLine()
                }
            }

            override fun onFailure(call: Call<DirectionsResponse?>, throwable: Throwable) {
                Timber.e("Error: " + throwable.message)
                Toast.makeText(
                    this@GenerateRouteActivity,
                    "Error: " + throwable.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}