package com.jycra.filmaico.core.ui.util.transform

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.graphics.scale
import androidx.core.view.drawToBitmap
import coil3.size.Size
import coil3.transform.Transformation

@RequiresApi(Build.VERSION_CODES.S)
class ModernBlurTransformation(
    private val context: Context,
    private val radius: Float = 25f,
    private val sampling: Int = 4
) : Transformation() {

    override val cacheKey: String = "${javaClass.name}-$sampling-$radius"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        // 1. Encogemos el bitmap de entrada
        val scaledWidth = (input.width * sampling).toInt()
        val scaledHeight = (input.height * sampling).toInt()
        if (scaledWidth == 0 || scaledHeight == 0) return input

        val downscaledBitmap = input.scale(scaledWidth, scaledHeight, filter = true)

        // 2. Creamos una ImageView en memoria para actuar como "host" del RenderEffect
        val imageView = ImageView(context)
        imageView.setImageBitmap(downscaledBitmap)

        // 3. Creamos y aplicamos el RenderEffect
        val blurEffect = RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.MIRROR)
        imageView.setRenderEffect(blurEffect)

        // 4. Medimos y dibujamos la vista en memoria para que se aplique el efecto
        imageView.measure(
            View.MeasureSpec.makeMeasureSpec(downscaledBitmap.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(downscaledBitmap.height, View.MeasureSpec.EXACTLY)
        )
        imageView.layout(0, 0, downscaledBitmap.width, downscaledBitmap.height)

        // 5. Capturamos el resultado (un bitmap pequeño y borroso) y lo devolvemos
        val blurredBitmap = imageView.drawToBitmap(downscaledBitmap.config!!)
        downscaledBitmap.recycle()

        return blurredBitmap
    }

}