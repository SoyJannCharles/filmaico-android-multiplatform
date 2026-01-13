@file:Suppress("DEPRECATION")

package com.jycra.filmaico.core.ui.util.transform

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.core.graphics.scale
import coil3.size.Size
import coil3.transform.Transformation
import androidx.core.graphics.createBitmap

class LegacyBlurTransformation(
    private val context: Context,
    private val radius: Float = 25f,
    private val sampling: Int = 4
) : Transformation() {

    init {
        require(radius in 0.1f..25f) { "Radius must be between 0.1 and 25 for RenderScript." }
        require(sampling >= 1) { "Sampling must be >= 1." }
    }

    override val cacheKey: String = "${javaClass.name}-$radius-$sampling"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val scaledWidth = input.width / sampling
        val scaledHeight = input.height / sampling

        if (scaledWidth == 0 || scaledHeight == 0) {
            return input
        }

        val workingBitmap = input.scale(scaledWidth, scaledHeight, false)
        val outputBitmap =
            createBitmap(workingBitmap.width, workingBitmap.height, workingBitmap.config!!)

        var rs: RenderScript? = null
        try {
            rs = RenderScript.create(context)
            val inputAlloc = Allocation.createFromBitmap(rs, workingBitmap)
            val outputAlloc = Allocation.createFromBitmap(rs, outputBitmap)
            val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

            blurScript.setRadius(radius)
            blurScript.setInput(inputAlloc)
            blurScript.forEach(outputAlloc)
            outputAlloc.copyTo(outputBitmap)
        } finally {
            rs?.destroy()
        }

        return outputBitmap
    }

}