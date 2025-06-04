package com.example.reflect.presentation.common

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.reflect.R
import com.example.reflect.domain.model.StatisticTagModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream

object ExportStatisticUtils {

    fun exportToExcel(
        context: Context,
        time: TimeRange,
        lineChartData: List<Entry>,
        pieChartData: List<PieEntry>,
        emotionalTagData: List<StatisticTagModel>,
        tagData: List<StatisticTagModel>,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var i = 0
                val workbook = HSSFWorkbook()
                val sheet = when (time) {
                    TimeRange.WEEK -> workbook.createSheet(context.resources.getString(R.string.exportStatisticForAWeek))
                    TimeRange.MONTH -> workbook.createSheet(context.resources.getString(R.string.exportStatisticForAMonth))
                    TimeRange.YEAR -> workbook.createSheet(context.resources.getString(R.string.exportStatisticForAYear))
                }
                sheet.defaultColumnWidth = 15

                // Linechart
                sheet.createRow(i++).apply {
                    createCell(0).apply {
                        setCellValue(context.resources.getString(R.string.exportStatisticLineChartTitle))
                    }
                }
                sheet.createRow(i++).apply {
                    createCell(0).setCellValue(context.resources.getString(R.string.exportStatisticLineChartHeaderData))
                    createCell(1).setCellValue(context.resources.getString(R.string.exportStatisticLineChartHeadreAverageValue))
                }
                lineChartData.forEachIndexed { index, entry ->
                    val row = sheet.createRow(index +  i)
                    row.createCell(0).setCellValue(entry.data.toString())
                    row.createCell(1).setCellValue(entry.y.toDouble())
                }
                i += lineChartData.size + 1

                // PieChart
                sheet.createRow(i++).apply {
                    createCell(0).apply {
                        setCellValue(context.resources.getString(R.string.exportStatisticPieChartTitle))
                    }
                }
                sheet.createRow(i++).apply {
                    createCell(0).setCellValue(context.resources.getString(R.string.exportStatisticPieChartHeaderFreq))
                    createCell(1).setCellValue(context.resources.getString(R.string.exportStatisticPieChartHeaderMood))
                }
                val sum = pieChartData.map { it.value }.sum()
                pieChartData.forEachIndexed { index, pieEntry ->
                    val row = sheet.createRow(index + i)
                    row.createCell(0).setCellValue((pieEntry.value / sum * 100).toInt().toString() + " %")
                    row.createCell(1).setCellValue(pieEntry.label)
                }
                i += pieChartData.size + 1

                // Emotional Tags
                sheet.createRow(i++).apply {
                    createCell(0).setCellValue(context.resources.getString(R.string.exportStatisticEmotionalTagTitle))
                }
                sheet.createRow(i++).apply {
                    createCell(0).setCellValue(context.resources.getString(R.string.exportStatisticEmotionalTagHeaderName))
                    createCell(1).setCellValue(context.resources.getString(R.string.exportStatisticEmotionalTagHeaderEmoji))
                    createCell(2).setCellValue(context.resources.getString(R.string.exportStatisticEmotionalTagHeaderFreq))
                }
                emotionalTagData.forEachIndexed { index, model ->
                    val row = sheet.createRow(index + i)
                    row.createCell(0).setCellValue(model.name)
                    row.createCell(1).setCellValue(model.emoji)
                    row.createCell(2).setCellValue(model.freq.toDouble())
                }
                i += emotionalTagData.size + 1

                // Tags
                sheet.createRow(i++).apply {
                    createCell(0).setCellValue(context.resources.getString(R.string.exportStatisticTagTitle))
                }
                sheet.createRow(i++).apply {
                    createCell(0).setCellValue(context.resources.getString(R.string.exportStatisticTagHeaderName))
                    createCell(1).setCellValue(context.resources.getString(R.string.exportStatisticTagHeaderEmoji))
                    createCell(2).setCellValue(context.resources.getString(R.string.exportStatisticTagHeaderFreq))
                }
                tagData.forEachIndexed { index, model ->
                    val row = sheet.createRow(index + i)
                    row.createCell(0).setCellValue(model.name)
                    row.createCell(1).setCellValue(model.emoji)
                    row.createCell(2).setCellValue(model.freq.toDouble())
                }
                i += tagData.size + 1

                withContext(Dispatchers.Main) {
                    saveExcelFile(context, time, workbook)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveExcelFile(context: Context, time: TimeRange, workbook: HSSFWorkbook) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val filename = when (time) {
                    TimeRange.WEEK -> "week_statistics.xls"
                    TimeRange.MONTH -> "month_statistics.xls"
                    TimeRange.YEAR -> "year_statistics.xls"
                }
                val filePath = File(context.getExternalFilesDir(null), filename)
                val fileOutputStream = FileOutputStream(filePath)
                workbook.write(fileOutputStream)
                fileOutputStream.close()
                workbook.close()

                withContext(Dispatchers.Main) {
                    Log.d("Ok excel", "Excel файл сохранен: ${filePath.absolutePath}")
                    shareExcelFile(context, time, filePath)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    ToastUtils.showErrorExportStatisticToast(context)
                }
            }
        }
    }

    private fun shareExcelFile(context: Context, time: TimeRange, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val message = when (time) {
            TimeRange.WEEK -> context.resources.getString(R.string.exportStatisticExportMessageWeek)
            TimeRange.MONTH -> context.resources.getString(R.string.exportStatisticExportMessageMonth)
            TimeRange.YEAR -> context.resources.getString(R.string.exportStatisticExportMessageYear)
        }

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.ms-excel"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, message)
            putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.exportStatisticExportSubject))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val timeTitle = when(time) {
            TimeRange.WEEK -> context.resources.getString(R.string.exportStatisticExportIntentTimeWeek)
            TimeRange.MONTH -> context.resources.getString(R.string.exportStatisticExportIntentTimeMonth)
            TimeRange.YEAR -> context.resources.getString(R.string.exportStatisticExportIntentTimeYear)
        }
        context.startActivity(
            Intent.createChooser(shareIntent,
                context.resources.getString(R.string.exportStatisticExportIntentTitle, timeTitle)))
    }
}