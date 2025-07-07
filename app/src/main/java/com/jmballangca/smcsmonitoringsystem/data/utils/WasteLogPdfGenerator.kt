package com.jmballangca.smcsmonitoringsystem.data.utils

import android.os.Environment



import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfDocument
import android.text.TextPaint
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.graphics.scale

/**
 * Converts a drawable resource ID to a Bitmap.
 */
fun Int.toBitmap(context: Context): Bitmap {
    val drawable = ContextCompat.getDrawable(context, this)
        ?: throw IllegalArgumentException("Resource ID $this is not a valid drawable.")

    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }

    val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: 1
    val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: 1

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, 24, canvas.height)
    drawable.draw(canvas)

    return bitmap
}
data class WasteLogHeaderData(
    val reportTitle: String,
    val dateRange: String,
    val generatedDate: String,
    val generatedBy: String,
    val totalItems: Int,
    val logoBitmap: Bitmap? = null
)
class WasteLogPdfGenerator(private val context: Context) {

    fun generatePdf(
        wasteLogs: List<WasteLog>,
        headerData: WasteLogHeaderData,

        fileName: String = "waste_log_${System.currentTimeMillis()}"
    ): File {
        val pdfDocument = PdfDocument()
        val pageWidth = 842
        val pageHeight = 595
        val margin = 40f
        val topMargin = 50f
        val headerHeight = 150f
        val bottomMargin = 40f
        val rowHeight = 28f
        val cellPadding = 4f

        val paint = Paint().apply {
            textSize = 12f
            color = Color.BLACK
        }
        val boldPaint = Paint().apply {
            textSize = 13f
            color = Color.BLACK
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val headers = listOf("Date", "Type", "Generator", "Bags", "Weight", "Hauler", "Remarks", "Amount")
        val columnWeights = listOf(1.2f, 1.2f, 2.2f, 0.8f, 0.8f, 2f, 1.2f, 0.8f)
        val usableWidth = pageWidth - 2 * margin
        val totalWeight = columnWeights.sum()
        val columnWidths = columnWeights.map { usableWidth * (it / totalWeight) }

        val availableHeight = pageHeight - topMargin - headerHeight - bottomMargin
        val maxRowsPerPage = (availableHeight / rowHeight).toInt()
        val totalPages = (wasteLogs.size + maxRowsPerPage - 1) / maxRowsPerPage

        for (pageIndex in 0 until maxOf(1, totalPages)) {
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageIndex + 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            var yPos = topMargin

            if (pageIndex == 0) {
                drawHeader(canvas, headerData, margin, pageWidth, boldPaint, paint)
                yPos += headerHeight
            }

            drawRow(canvas, headers, margin, yPos, columnWidths, boldPaint, cellPadding, alignRightLast = false)
            yPos += rowHeight

            val logs = wasteLogs.drop(pageIndex * maxRowsPerPage).take(maxRowsPerPage)
            logs.forEach { log ->
                val row = listOf(
                    log.date,
                    log.type.name,
                    log.generatorName ?: "-",
                    log.numberOfBags.toString(),
                    String.format("%.2f", log.weightKg),
                    log.haulerName ?: "-",
                    log.remarks.name,
                    log.totalAmount?.let { String.format("%.2f", it) } ?: "-"
                )
                drawRow(canvas, row, margin, yPos, columnWidths, paint, cellPadding, alignRightLast = true)
                yPos += rowHeight
            }
            if (pageIndex == totalPages - 1) {
                val footerY = pageHeight - bottomMargin + 10f
                val totalLabel = "Total Items : ${wasteLogs.size}"
                val textWidth = paint.measureText(totalLabel)
                val footerX = pageWidth - margin - textWidth
                canvas.drawText(totalLabel, footerX, footerY, boldPaint)
            }

            pdfDocument.finishPage(page)
        }

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists()) downloadsDir.mkdirs()

        val file = File(downloadsDir, "$fileName.pdf")
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        return file
    }

    private fun drawHeader(
        canvas: Canvas,
        data: WasteLogHeaderData,
        margin: Float,
        pageWidth: Int,
        boldPaint: Paint,
        paint: Paint
    ) {
        val rightX = pageWidth - margin
        val logoSize = 40f

        data.logoBitmap?.let {
            val scaled = it.scale(logoSize.toInt(), logoSize.toInt())
            canvas.drawBitmap(scaled, margin, 50f, null)
        } ?: run {
            val grayPaint = Paint().apply { color = Color.LTGRAY }
            canvas.drawRect(margin, 50f, margin + logoSize, 50f + logoSize, grayPaint)
            canvas.drawText("Logo", margin + 5f, 50f + 25f, paint)
        }

        canvas.drawText("SMCS - MRF Monitoring System", rightX - 250f, 60f, boldPaint)
        canvas.drawText(data.generatedDate, rightX - 250f, 80f, paint)
        canvas.drawText("Generated By: ${data.generatedBy}", rightX - 250f, 100f, paint)

        canvas.drawText(data.reportTitle, pageWidth / 2f - 50f, 160f, boldPaint)
        canvas.drawText(data.dateRange, pageWidth / 2f - 50f, 180f, paint)

    }

    private fun drawRow(
        canvas: Canvas,
        cells: List<String>,
        startX: Float,
        y: Float,
        columnWidths: List<Float>,
        paint: Paint,
        padding: Float,
        alignRightLast: Boolean
    ) {
        var x = startX
        cells.forEachIndexed { index, text ->
            val cellWidth = columnWidths.getOrElse(index) { 0f }
            val clipped = TextUtils.ellipsize(
                text,
                TextPaint(paint),
                cellWidth - 2 * padding,
                TextUtils.TruncateAt.END
            ).toString()

            val isLastColumn = alignRightLast && index == cells.lastIndex
            val drawX = if (isLastColumn) {
                val textWidth = paint.measureText(clipped)
                x + cellWidth - textWidth - padding
            } else {
                x + padding
            }

            canvas.drawText(clipped, drawX, y, paint)
            x += cellWidth
        }
    }
}
//class WasteLogPdfGenerator(private val context: Context) {
//
//    fun generatePdf(
//        wasteLogs: List<WasteLog>,
//        headerData: WasteLogHeaderData,
//        fileName: String = "waste_log_${System.currentTimeMillis()}"
//    ): File {
//        val pdfDocument = PdfDocument()
//
//        val pageWidth = 842
//        val pageHeight = 595
//        val margin = 40f
//        val topMargin = 50f
//        val headerHeight = 150f
//        val bottomMargin = 40f
//        val rowHeight = 24f
//        val padding = 4f
//
//        val paint = Paint().apply { textSize = 12f; color = Color.BLACK }
//        val boldPaint = Paint().apply {
//            textSize = 13f
//            color = Color.BLACK
//            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
//        }
//
//        val headers = listOf("Date", "Type", "Generator", "Bags", "Weight", "Hauler", "Remarks", "Amount")
//        val columnCount = headers.size
//        val usableWidth = pageWidth - 2 * margin
//        val columnWidth = usableWidth / columnCount
//
//        val availableHeight = pageHeight - topMargin - headerHeight - bottomMargin
//        val maxRowsPerPage = (availableHeight / rowHeight).toInt()
//        val totalPages = (wasteLogs.size + maxRowsPerPage - 1) / maxRowsPerPage
//
//        for (pageIndex in 0 until maxOf(1, totalPages)) {
//            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageIndex + 1).create()
//            val page = pdfDocument.startPage(pageInfo)
//            val canvas = page.canvas
//
//            var yPos = topMargin
//
//            if (pageIndex == 0) {
//                drawHeader(canvas, headerData, margin, pageWidth, boldPaint, paint)
//                yPos += headerHeight
//            }
//
//            // Draw table header
//            drawRow(canvas, headers, margin, yPos, columnWidth, rowHeight, boldPaint, padding)
//            yPos += rowHeight
//
//            val pageLogs = wasteLogs.drop(pageIndex * maxRowsPerPage).take(maxRowsPerPage)
//            pageLogs.forEach { log ->
//                val row = listOf(
//                    log.date,
//                    log.type.name,
//                    log.generatorName ?: "-",
//                    log.numberOfBags.toString(),
//                    String.format("%.2f", log.weightKg),
//                    log.haulerName ?: "-",
//                    log.remarks.name,
//                    log.totalAmount?.let { String.format("%.2f", it) } ?: "-"
//                )
//                drawRow(canvas, row, margin, yPos, columnWidth, rowHeight, paint, padding)
//                yPos += rowHeight
//            }
//
//            pdfDocument.finishPage(page)
//        }
//
//        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        if (!downloadsDir.exists()) downloadsDir.mkdirs()
//
//        val file = File(downloadsDir, "$fileName.pdf")
//        pdfDocument.writeTo(FileOutputStream(file))
//        pdfDocument.close()
//
//        return file
//    }
//
//    private fun drawHeader(
//        canvas: Canvas,
//        data: WasteLogHeaderData,
//        margin: Float,
//        pageWidth: Int,
//        boldPaint: Paint,
//        paint: Paint
//    ) {
//        val rightX = pageWidth - margin
//
//        data.logoBitmap?.let {
//            val resized = Bitmap.createScaledBitmap(it, 80, 80, false)
//            canvas.drawBitmap(resized, margin, 50f, null)
//        } ?: run {
//            val bgPaint = Paint().apply { color = Color.LTGRAY }
//            canvas.drawRect(margin, 50f, margin + 80f, 130f, bgPaint)
//            canvas.drawText("Logo", margin + 20f, 95f, paint)
//        }
//
//        canvas.drawText("SMCS - MRF Monitoring System", rightX - 250f, 60f, boldPaint)
//        canvas.drawText(data.generatedDate, rightX - 250f, 80f, paint)
//        canvas.drawText("Generated By: ${data.generatedBy}", rightX - 250f, 100f, paint)
//
//        canvas.drawText(data.reportTitle, pageWidth / 2f - 50, 160f, boldPaint)
//        canvas.drawText(data.dateRange, pageWidth / 2f - 50, 180f, paint)
//        canvas.drawText("Total Items : ${data.totalItems}", rightX - 150, 200f, paint)
//    }
//
//    private fun drawRow(
//        canvas: Canvas,
//        cells: List<String>,
//        startX: Float,
//        y: Float,
//        columnWidth: Float,
//        rowHeight: Float,
//        paint: Paint,
//        padding: Float
//    ) {
//        var x = startX
//        cells.forEach { text ->
//            val clipped = TextUtils.ellipsize(
//                text,
//                TextPaint(paint),
//                columnWidth - 2 * padding,
//                TextUtils.TruncateAt.END
//            ).toString()
//            canvas.drawText(clipped, x + padding, y, paint)
//            x += columnWidth
//        }
//    }
//}