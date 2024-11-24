package com.gdalamin.bcs_pro.ui.fragment.RecordFragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.gdalamin.bcs_pro.utilities.GeneralUtils.convertEnglishToBangla

class CircularProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var progress = 0 // Progress value (0 to 100)
    private val strokeWidth = 20f // Stroke width for the ring
    private var progressColor = Color.BLUE // Default color
    
    private val backgroundPaint: Paint = Paint().apply {
        color = Color.rgb(236, 247, 238)
        style = Paint.Style.STROKE
        strokeWidth = this@CircularProgressBar.strokeWidth
        isAntiAlias = true
    }
    
    private val progressPaint: Paint = Paint().apply {
        color = progressColor // Set the progress color
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = this@CircularProgressBar.strokeWidth
        isAntiAlias = true
    }
    
    private val textPaint: Paint = Paint().apply {
        color = Color.BLACK // Text color
        textSize = 50f // Text size
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    
    // Method to set the progress value
    fun setProgress(progress: Int) {
        if (progress in 0..100) {
            this.progress = progress
            invalidate() // Redraw the view
        }
    }
    
    // Method to set the progress color dynamically
    fun setProgressColor(color: Int) {
        progressColor = color
        progressPaint.color = color
        invalidate() // Redraw the view with the new color
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val width = width.toFloat()
        val height = height.toFloat()
        
        val radius = Math.min(width, height) / 2 - strokeWidth / 2
        val centerX = width / 2
        val centerY = height / 2
        
        // Draw background circle
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)
        
        // Draw progress circle (arc)
        val angle = 360 * progress / 100f
        canvas.drawArc(
            centerX - radius, centerY - radius,
            centerX + radius, centerY + radius,
            -90f, angle, false, progressPaint
        )
        
        
        val progressText = "${convertEnglishToBangla(progress.toString())}%"
        
        // Draw the progress number in Bengali in the center
        canvas.drawText(progressText, centerX, centerY + (textPaint.textSize / 3), textPaint)
    }
}