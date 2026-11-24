package com.example.qr

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.camera.core.ImageProxy
import androidx.core.content.FileProvider
import com.example.domain.model.AppLanguage
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.File
import java.io.FileOutputStream

object QrCodeUtils {

    const val DEFAULT_WEB_SHARE_URL =
        "https://ais-pre-dd5ltv36pe2xwptgd7sakl-1007429655439.asia-southeast1.run.app"

    fun buildWebShareUrl(
        baseUrl: String,
        topicId: String?,
        language: AppLanguage
    ): String {
        val trimmed = baseUrl.trim().ifEmpty { DEFAULT_WEB_SHARE_URL }
        val normalized = if (trimmed.startsWith("http://", ignoreCase = true) ||
            trimmed.startsWith("https://", ignoreCase = true)
        ) {
            trimmed
        } else {
            "https://$trimmed"
        }
        val cleanBase = normalized.trimEnd('?', '&', '/')
        val separator = if ('?' in cleanBase) "&" else "?"
        val params = buildList {
            if (!topicId.isNullOrBlank()) add("topic=$topicId")
            add("lang=${language.code}")
        }.joinToString("&")
        return "$cleanBase$separator$params"
    }

    fun extractArchTopicIdFromUrl(rawText: String): String? {
        val idx = rawText.indexOf("topic=")
        if (idx == -1) return null
        val after = rawText.substring(idx + 6)
        val end = after.indexOfFirst { it == '&' || it == '#' || it == ' ' }
        val candidate = (if (end == -1) after else after.substring(0, end)).trim()
        return candidate.takeIf { it.isNotEmpty() }
    }

    fun isWebUrl(rawText: String): Boolean {
        val t = rawText.trim().lowercase()
        return t.startsWith("https://") || t.startsWith("http://") ||
            (t.contains(".") && !t.contains(" ") && !t.contains(":"))
    }

    fun normalizeOpenableUrl(rawText: String): String {
        val t = rawText.trim()
        return if (t.startsWith("http://", ignoreCase = true) ||
            t.startsWith("https://", ignoreCase = true)
        ) {
            t
        } else {
            "https://$t"
        }
    }

    fun generateQrBitmap(content: String, sizePx: Int = 640): Bitmap {
        val hints = mapOf(
            EncodeHintType.CHARACTER_SET to "UTF-8",
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
            EncodeHintType.MARGIN to 2
        )
        val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, sizePx, sizePx, hints)
        val pixels = IntArray(sizePx * sizePx)
        for (y in 0 until sizePx) {
            val offset = y * sizePx
            for (x in 0 until sizePx) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
            }
        }
        return Bitmap.createBitmap(pixels, sizePx, sizePx, Bitmap.Config.ARGB_8888)
    }

    fun decodeQrFromImageProxy(imageProxy: ImageProxy): String? {
        return try {
            val plane = imageProxy.planes.firstOrNull() ?: return null
            val buffer = plane.buffer
            val data = ByteArray(buffer.remaining())
            buffer.get(data)
            val width = imageProxy.width
            val height = imageProxy.height
            val source = PlanarYUVLuminanceSource(
                data,
                plane.rowStride.coerceAtLeast(width),
                height,
                0,
                0,
                width,
                height,
                false
            )
            decodeLuminanceSource(source)
        } catch (_: Exception) {
            null
        } finally {
            imageProxy.close()
        }
    }

    fun decodeQrFromBitmap(bitmap: Bitmap): String? {
        return try {
            val w = bitmap.width
            val h = bitmap.height
            val pixels = IntArray(w * h)
            bitmap.getPixels(pixels, 0, w, 0, 0, w, h)
            val source = RGBLuminanceSource(w, h, pixels)
            decodeLuminanceSource(source)
        } catch (_: Exception) {
            null
        }
    }

    private fun decodeLuminanceSource(source: com.google.zxing.LuminanceSource): String? {
        val reader = MultiFormatReader().apply {
            setHints(
                mapOf(
                    DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE),
                    DecodeHintType.TRY_HARDER to true
                )
            )
        }
        return try {
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            reader.decodeWithState(binaryBitmap).text
        } catch (_: Exception) {
            null
        } finally {
            reader.reset()
        }
    }

    fun shareQrBitmap(context: Context, bitmap: Bitmap, url: String) {
        val dir = File(context.cacheDir, "web").apply { mkdirs() }
        val file = File(dir, "arch3d_coa_qr.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(
                Intent.EXTRA_TEXT,
                "Scan this QR code or open link in your browser (works on Android & Apple): $url"
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Website QR Code"))
    }

    fun shareHtmlWebsiteFile(context: Context, htmlContent: String) {
        val dir = File(context.cacheDir, "web").apply { mkdirs() }
        val file = File(dir, "index.html")
        file.writeText(htmlContent, Charsets.UTF_8)
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/html"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Arch3D COA Web Companion (index.html)")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Export Standalone Website (index.html)"))
    }
}
