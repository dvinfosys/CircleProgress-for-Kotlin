package net.dvinfosys.circleprogress.Library

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import net.dvinfosys.circleprogress.R


class DonutProgress @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var finishedPaint: Paint? = null
    private var unfinishedPaint: Paint? = null
    private var innerCirclePaint: Paint? = null

    protected lateinit var textPaint: Paint
    protected lateinit var innerBottomTextPaint: Paint

    private val finishedOuterRect = RectF()
    private val unfinishedOuterRect = RectF()

    var attributeResourceId = 0
    var isShowText: Boolean = false
    private var textSize: Float = 0.toFloat()
    private var textColor: Int = 0
    private var innerBottomTextColor: Int = 0
    var progress = 0f
        set(progress) {
            field = progress
            if (this.progress > max) {
                field %= max.toFloat()
            }
            invalidate()
        }
    var max: Int = 0
        set(max) {
            if (max > 0) {
                field = max
                invalidate()
            }
        }
    private var finishedStrokeColor: Int = 0
    private var unfinishedStrokeColor: Int = 0
    private var startingDegree: Int = 0
    private var finishedStrokeWidth: Float = 0.toFloat()
    private var unfinishedStrokeWidth: Float = 0.toFloat()
    private var innerBackgroundColor: Int = 0
    private var prefixText: String? = ""
    private var suffixText: String? = "%"
    private var text: String? = null
    private var innerBottomTextSize: Float = 0.toFloat()
    private var innerBottomText: String? = null
    private var innerBottomTextHeight: Float = 0.toFloat()

    private val default_stroke_width: Float
    private val default_finished_color = Color.rgb(66, 145, 241)
    private val default_unfinished_color = Color.rgb(204, 204, 204)
    private val default_text_color = Color.rgb(66, 145, 241)
    private val default_inner_bottom_text_color = Color.rgb(66, 145, 241)
    private val default_inner_background_color = Color.TRANSPARENT
    private val default_max = 100
    private val default_startingDegree = 0
    private val default_text_size: Float
    private val default_inner_bottom_text_size: Float
    private val min_size: Int

    private val progressAngle: Float
        get() = progress / this.max.toFloat() * 360f

    init {

        default_text_size = Utils.sp2px(getResources(), 18f)
        min_size = Utils.dp2px(getResources(), 100f).toInt()
        default_stroke_width = Utils.dp2px(getResources(), 10f)
        default_inner_bottom_text_size = Utils.sp2px(getResources(), 18f)

        val attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DonutProgress, defStyleAttr, 0)
        initByAttributes(attributes)
        attributes.recycle()

        initPainters()
    }

    protected fun initPainters() {
        if (isShowText) {
            textPaint = TextPaint()
            textPaint.setColor(textColor)
            textPaint.setTextSize(textSize)
            textPaint.setAntiAlias(true)

            innerBottomTextPaint = TextPaint()
            innerBottomTextPaint.setColor(innerBottomTextColor)
            innerBottomTextPaint.setTextSize(innerBottomTextSize)
            innerBottomTextPaint.setAntiAlias(true)
        }

        finishedPaint = Paint()
        finishedPaint!!.setColor(finishedStrokeColor)
        finishedPaint!!.setStyle(Paint.Style.STROKE)
        finishedPaint!!.setAntiAlias(true)
        finishedPaint!!.setStrokeWidth(finishedStrokeWidth)

        unfinishedPaint = Paint()
        unfinishedPaint!!.setColor(unfinishedStrokeColor)
        unfinishedPaint!!.setStyle(Paint.Style.STROKE)
        unfinishedPaint!!.setAntiAlias(true)
        unfinishedPaint!!.setStrokeWidth(unfinishedStrokeWidth)

        innerCirclePaint = Paint()
        innerCirclePaint!!.setColor(innerBackgroundColor)
        innerCirclePaint!!.setAntiAlias(true)
    }

    protected fun initByAttributes(attributes: TypedArray) {
        finishedStrokeColor = attributes.getColor(R.styleable.DonutProgress_donut_finished_color, default_finished_color)
        unfinishedStrokeColor = attributes.getColor(R.styleable.DonutProgress_donut_unfinished_color, default_unfinished_color)
        isShowText = attributes.getBoolean(R.styleable.DonutProgress_donut_show_text, true)
        attributeResourceId = attributes.getResourceId(R.styleable.DonutProgress_donut_inner_drawable, 0)

        max = attributes.getInt(R.styleable.DonutProgress_donut_max, default_max)
        progress = attributes.getFloat(R.styleable.DonutProgress_donut_progress, 0f)
        finishedStrokeWidth = attributes.getDimension(R.styleable.DonutProgress_donut_finished_stroke_width, default_stroke_width)
        unfinishedStrokeWidth = attributes.getDimension(R.styleable.DonutProgress_donut_unfinished_stroke_width, default_stroke_width)

        if (isShowText) {
            if (attributes.getString(R.styleable.DonutProgress_donut_prefix_text) != null) {
                prefixText = attributes.getString(R.styleable.DonutProgress_donut_prefix_text)
            }
            if (attributes.getString(R.styleable.DonutProgress_donut_suffix_text) != null) {
                suffixText = attributes.getString(R.styleable.DonutProgress_donut_suffix_text)
            }
            if (attributes.getString(R.styleable.DonutProgress_donut_text) != null) {
                text = attributes.getString(R.styleable.DonutProgress_donut_text)
            }

            textColor = attributes.getColor(R.styleable.DonutProgress_donut_text_color, default_text_color)
            textSize = attributes.getDimension(R.styleable.DonutProgress_donut_text_size, default_text_size)
            innerBottomTextSize = attributes.getDimension(R.styleable.DonutProgress_donut_inner_bottom_text_size, default_inner_bottom_text_size)
            innerBottomTextColor = attributes.getColor(R.styleable.DonutProgress_donut_inner_bottom_text_color, default_inner_bottom_text_color)
            innerBottomText = attributes.getString(R.styleable.DonutProgress_donut_inner_bottom_text)
        }

        innerBottomTextSize = attributes.getDimension(R.styleable.DonutProgress_donut_inner_bottom_text_size, default_inner_bottom_text_size)
        innerBottomTextColor = attributes.getColor(R.styleable.DonutProgress_donut_inner_bottom_text_color, default_inner_bottom_text_color)
        innerBottomText = attributes.getString(R.styleable.DonutProgress_donut_inner_bottom_text)

        startingDegree = attributes.getInt(R.styleable.DonutProgress_donut_circle_starting_degree, default_startingDegree)
        innerBackgroundColor = attributes.getColor(R.styleable.DonutProgress_donut_background_color, default_inner_background_color)
    }

    override fun invalidate() {
        initPainters()
        super.invalidate()
    }

    fun getFinishedStrokeWidth(): Float {
        return finishedStrokeWidth
    }

    fun setFinishedStrokeWidth(finishedStrokeWidth: Float) {
        this.finishedStrokeWidth = finishedStrokeWidth
        this.invalidate()
    }

    fun getUnfinishedStrokeWidth(): Float {
        return unfinishedStrokeWidth
    }

    fun setUnfinishedStrokeWidth(unfinishedStrokeWidth: Float) {
        this.unfinishedStrokeWidth = unfinishedStrokeWidth
        this.invalidate()
    }

    fun getTextSize(): Float {
        return textSize
    }

    fun setTextSize(textSize: Float) {
        this.textSize = textSize
        this.invalidate()
    }

    fun getTextColor(): Int {
        return textColor
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
        this.invalidate()
    }

    fun getFinishedStrokeColor(): Int {
        return finishedStrokeColor
    }

    fun setFinishedStrokeColor(finishedStrokeColor: Int) {
        this.finishedStrokeColor = finishedStrokeColor
        this.invalidate()
    }

    fun getUnfinishedStrokeColor(): Int {
        return unfinishedStrokeColor
    }

    fun setUnfinishedStrokeColor(unfinishedStrokeColor: Int) {
        this.unfinishedStrokeColor = unfinishedStrokeColor
        this.invalidate()
    }

    fun getText(): String? {
        return text
    }

    fun setText(text: String) {
        this.text = text
        this.invalidate()
    }

    fun getSuffixText(): String? {
        return suffixText
    }

    fun setSuffixText(suffixText: String) {
        this.suffixText = suffixText
        this.invalidate()
    }

    fun getPrefixText(): String? {
        return prefixText
    }

    fun setPrefixText(prefixText: String) {
        this.prefixText = prefixText
        this.invalidate()
    }

    fun getInnerBackgroundColor(): Int {
        return innerBackgroundColor
    }

    fun setInnerBackgroundColor(innerBackgroundColor: Int) {
        this.innerBackgroundColor = innerBackgroundColor
        this.invalidate()
    }


    fun getInnerBottomText(): String? {
        return innerBottomText
    }

    fun setInnerBottomText(innerBottomText: String) {
        this.innerBottomText = innerBottomText
        this.invalidate()
    }


    fun getInnerBottomTextSize(): Float {
        return innerBottomTextSize
    }

    fun setInnerBottomTextSize(innerBottomTextSize: Float) {
        this.innerBottomTextSize = innerBottomTextSize
        this.invalidate()
    }

    fun getInnerBottomTextColor(): Int {
        return innerBottomTextColor
    }

    fun setInnerBottomTextColor(innerBottomTextColor: Int) {
        this.innerBottomTextColor = innerBottomTextColor
        this.invalidate()
    }

    fun getStartingDegree(): Int {
        return startingDegree
    }

    fun setStartingDegree(startingDegree: Int) {
        this.startingDegree = startingDegree
        this.invalidate()
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec))

        //TODO calculate inner circle height and then position bottom text at the bottom (3/4)
        innerBottomTextHeight = (getHeight() - getHeight() * 3 / 4).toFloat()
    }

    private fun measure(measureSpec: Int): Int {
        var result: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = min_size
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size)
            }
        }
        return result
    }

    protected override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val delta = Math.max(finishedStrokeWidth, unfinishedStrokeWidth)
        finishedOuterRect.set(delta,
                delta,
                getWidth() - delta,
                getHeight() - delta)
        unfinishedOuterRect.set(delta,
                delta,
                getWidth() - delta,
                getHeight() - delta)

        val innerCircleRadius = (getWidth() - Math.min(finishedStrokeWidth, unfinishedStrokeWidth) + Math.abs(finishedStrokeWidth - unfinishedStrokeWidth)) / 2f
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, innerCircleRadius, innerCirclePaint)
        canvas.drawArc(finishedOuterRect, getStartingDegree().toFloat(), progressAngle, false, finishedPaint)
        canvas.drawArc(unfinishedOuterRect, getStartingDegree() + progressAngle, 360 - progressAngle, false, unfinishedPaint)

        if (isShowText) {
            val text = if (this.text != null) this.text else prefixText + this.progress + suffixText
            if (!TextUtils.isEmpty(text)) {
                val textHeight = textPaint.descent() + textPaint.ascent()
                canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, (getWidth() - textHeight) / 2.0f, textPaint)
            }

            if (!TextUtils.isEmpty(getInnerBottomText())) {
                innerBottomTextPaint.setTextSize(innerBottomTextSize)
                val bottomTextBaseline = getHeight() - innerBottomTextHeight - (textPaint.descent() + textPaint.ascent()) / 2
                canvas.drawText(getInnerBottomText(), (getWidth() - innerBottomTextPaint.measureText(getInnerBottomText())) / 2.0f, bottomTextBaseline, innerBottomTextPaint)
            }
        }

        if (attributeResourceId != 0) {
            val bitmap = BitmapFactory.decodeResource(getResources(), attributeResourceId)
            canvas.drawBitmap(bitmap, (getWidth() - bitmap.width) / 2.0f, (getHeight() - bitmap.height) / 2.0f, null)
        }
    }

    protected override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor())
        bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize())
        bundle.putFloat(INSTANCE_INNER_BOTTOM_TEXT_SIZE, getInnerBottomTextSize())
        bundle.putFloat(INSTANCE_INNER_BOTTOM_TEXT_COLOR, getInnerBottomTextColor().toFloat())
        bundle.putString(INSTANCE_INNER_BOTTOM_TEXT, getInnerBottomText())
        bundle.putInt(INSTANCE_INNER_BOTTOM_TEXT_COLOR, getInnerBottomTextColor())
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor())
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor())
        bundle.putInt(INSTANCE_MAX, max)
        bundle.putInt(INSTANCE_STARTING_DEGREE, getStartingDegree())
        bundle.putFloat(INSTANCE_PROGRESS, progress)
        bundle.putString(INSTANCE_SUFFIX, getSuffixText())
        bundle.putString(INSTANCE_PREFIX, getPrefixText())
        bundle.putString(INSTANCE_TEXT, getText())
        bundle.putFloat(INSTANCE_FINISHED_STROKE_WIDTH, getFinishedStrokeWidth())
        bundle.putFloat(INSTANCE_UNFINISHED_STROKE_WIDTH, getUnfinishedStrokeWidth())
        bundle.putInt(INSTANCE_BACKGROUND_COLOR, getInnerBackgroundColor())
        bundle.putInt(INSTANCE_INNER_DRAWABLE, attributeResourceId)
        return bundle
    }

    protected override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            textColor = state.getInt(INSTANCE_TEXT_COLOR)
            textSize = state.getFloat(INSTANCE_TEXT_SIZE)
            innerBottomTextSize = state.getFloat(INSTANCE_INNER_BOTTOM_TEXT_SIZE)
            innerBottomText = state.getString(INSTANCE_INNER_BOTTOM_TEXT)
            innerBottomTextColor = state.getInt(INSTANCE_INNER_BOTTOM_TEXT_COLOR)
            finishedStrokeColor = state.getInt(INSTANCE_FINISHED_STROKE_COLOR)
            unfinishedStrokeColor = state.getInt(INSTANCE_UNFINISHED_STROKE_COLOR)
            finishedStrokeWidth = state.getFloat(INSTANCE_FINISHED_STROKE_WIDTH)
            unfinishedStrokeWidth = state.getFloat(INSTANCE_UNFINISHED_STROKE_WIDTH)
            innerBackgroundColor = state.getInt(INSTANCE_BACKGROUND_COLOR)
            attributeResourceId = state.getInt(INSTANCE_INNER_DRAWABLE)
            initPainters()
            max = state.getInt(INSTANCE_MAX)
            setStartingDegree(state.getInt(INSTANCE_STARTING_DEGREE))
            progress = state.getFloat(INSTANCE_PROGRESS)
            prefixText = state.getString(INSTANCE_PREFIX)
            suffixText = state.getString(INSTANCE_SUFFIX)
            text = state.getString(INSTANCE_TEXT)
            super.onRestoreInstanceState(state.getParcelable<Parcelable>(INSTANCE_STATE))
            return
        }
        super.onRestoreInstanceState(state)
    }

    fun setDonut_progress(percent: String) {
        if (!TextUtils.isEmpty(percent)) {
            progress = Integer.parseInt(percent).toFloat()
        }
    }

    companion object {


        private val INSTANCE_STATE = "saved_instance"
        private val INSTANCE_TEXT_COLOR = "text_color"
        private val INSTANCE_TEXT_SIZE = "text_size"
        private val INSTANCE_TEXT = "text"
        private val INSTANCE_INNER_BOTTOM_TEXT_SIZE = "inner_bottom_text_size"
        private val INSTANCE_INNER_BOTTOM_TEXT = "inner_bottom_text"
        private val INSTANCE_INNER_BOTTOM_TEXT_COLOR = "inner_bottom_text_color"
        private val INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color"
        private val INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color"
        private val INSTANCE_MAX = "max"
        private val INSTANCE_PROGRESS = "progress"
        private val INSTANCE_SUFFIX = "suffix"
        private val INSTANCE_PREFIX = "prefix"
        private val INSTANCE_FINISHED_STROKE_WIDTH = "finished_stroke_width"
        private val INSTANCE_UNFINISHED_STROKE_WIDTH = "unfinished_stroke_width"
        private val INSTANCE_BACKGROUND_COLOR = "inner_background_color"
        private val INSTANCE_STARTING_DEGREE = "starting_degree"
        private val INSTANCE_INNER_DRAWABLE = "inner_drawable"
    }
}