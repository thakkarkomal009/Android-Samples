package com.mapboxturnbyturnnavigation.tripprogress.view

import android.content.Context
import android.content.res.TypedArray
import android.provider.CalendarContract
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.mapboxturnbyturnnavigation.databinding.MapboxTripProgressLayoutBinding
import com.mapbox.navigation.ui.tripprogress.model.TripProgressUpdateValue
import com.mapboxturnbyturnnavigation.R

/**
 * A view that can be added to activity layouts which displays trip progress.
 */
class MapboxTripProgressView : FrameLayout {

    /**
     *
     * @param context Context
     * @constructor
     */
    constructor(context: Context) : super(context)

    /**
     *
     * @param context Context
     * @param attrs AttributeSet?
     * @constructor
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttributes(attrs)
    }

    /**
     *
     * @param context Context
     * @param attrs AttributeSet?
     * @param defStyleAttr Int
     * @constructor
     */
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initAttributes(attrs)
    }

    private val binding =
        MapboxTripProgressLayoutBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    /**
     * Allows you to change the style of [MapboxTripProgressView].
     * @param style Int
     */
    fun updateStyle(@StyleRes style: Int) {
        val typedArray = context.obtainStyledAttributes(style, R.styleable.MapboxTripProgressView)
        applyAttributes(typedArray)
        typedArray.recycle()
    }

    /**
     * Applies the necessary view side effects based on the input.
     *
     * @param result a [Expected<TripProgressUpdateValue, TripProgressUpdateError>]
     * containing the data that should be rendered.
     */
    fun render(result: com.mapboxturnbyturnnavigation.tripprogress.model.TripProgressUpdateValue) {
        binding.distanceRemainingText.setText(
            result.formatter.getDistanceRemaining(result.distanceRemaining),
            TextView.BufferType.SPANNABLE
        )

        binding.estimatedTimeToArriveText.setText(
            result.formatter.getEstimatedTimeToArrival(
                result.estimatedTimeToArrival
            ),
            TextView.BufferType.SPANNABLE
        )

        binding.timeRemainingText.setText(
            result.formatter.getTimeRemaining(result.currentLegTimeRemaining),
            TextView.BufferType.SPANNABLE
        )

        binding.txtName!!.setText("Chloe Brooklyn")
        binding.txtPickupPoint!!.setText("Pickup point")
        binding.txtPickOutPoint!!.setText("Pickout point")
    }

    fun setOriginAddress(address: String) {
        if (address != null && address != "")
            binding.txtOriginLocation!!.setText(address)
        else
            binding.txtOriginLocation!!.setText("465 Huntington Ave, Boston, MA 02115, USA")
    }

    fun setDestinationAddress(address: String) {
        if (address != null && address != "")
            binding.txtDestinationLocation!!.setText(address)
        else
            binding.txtDestinationLocation!!.setText("160 Norfolk Ave, Boston, MA 02119, USA")
    }

    private fun initAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.MapboxTripProgressView,
            0,
            R.style.MapboxStyleTripProgressView
        )
        applyAttributes(typedArray)
        typedArray.recycle()
    }

    private fun applyAttributes(typedArray: TypedArray) {
        val textColor = typedArray.getColor(
            R.styleable.MapboxTripProgressView_tripProgressTextColor,
            ContextCompat.getColor(
                context,
                R.color.mapbox_trip_progress_text_color
            )
        )
        binding.timeRemainingText.setTextColor(resources.getColor(R.color.text_grey))
        binding.distanceRemainingText.setTextColor(resources.getColor(R.color.black))
        binding.estimatedTimeToArriveText.setTextColor(resources.getColor(R.color.text_grey))
        binding.txtName!!.setTextColor(resources.getColor(R.color.dark_grey))
        binding.txtDestinationLocation!!.setTextColor(textColor)
        binding.txtOriginLocation!!.setTextColor(textColor)
        binding.txtPickupPoint!!.setTextColor(resources.getColor(R.color.text_grey));
        binding.txtPickOutPoint!!.setTextColor(resources.getColor(R.color.text_grey))

        val dividerColor = typedArray.getColor(
            R.styleable.MapboxTripProgressView_tripProgressDividerColor,
            ContextCompat.getColor(
                context,
                R.color.mapbox_trip_progress_divider_color
            )
        )
        binding.tripProgressDivider?.setBackgroundColor(dividerColor)
        binding.tripProgressDividerLeft?.setTextColor(dividerColor)
        binding.tripProgressDividerRight?.setTextColor(dividerColor)

        setBackgroundColor(
            typedArray.getColor(
                R.styleable.MapboxTripProgressView_tripProgressViewBackgroundColor,
                ContextCompat.getColor(
                    context,
                    R.color.mapbox_trip_progress_view_background_color
                )
            )
        )
    }
}
