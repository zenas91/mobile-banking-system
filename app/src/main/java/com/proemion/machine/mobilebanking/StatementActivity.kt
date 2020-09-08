package com.proemion.machine.mobilebanking

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.activity_statement.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class StatementActivity : AppCompatActivity() {
    private var dateFrom by Delegates.notNull<Long>()
    private var dateTo by Delegates.notNull<Long>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statement)

        val now = Calendar.getInstance()
        dateFrom = now.timeInMillis
        dateTo = now.timeInMillis

        setDateViewDisplay()

        from_view.setOnClickListener { openDateRangePicker(dateFrom, dateTo) }

        to_view.setOnClickListener { openDateRangePicker(dateFrom, dateTo) }

    }

    private fun openDateRangePicker(from:Long, to:Long){
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setSelection(androidx.core.util.Pair(from, to))

        val picker = builder.build()

        picker.addOnNegativeButtonClickListener { picker.dismiss()}
        picker.addOnPositiveButtonClickListener { dateFrom = it.first!!
            dateTo = it.second!!
            setDateViewDisplay()
        }

        picker.show(this.supportFragmentManager, picker.toString())

    }

    private fun setDateViewDisplay(){
        date_from_view.text = convertLongToDateString(dateFrom)
        date_to_view.text = convertLongToDateString(dateTo)
    }

    private fun convertLongToDateString(epoch:Long): String{
        val date = Date(epoch)
        val format: DateFormat = SimpleDateFormat("dd.MM.yyyy")
        format.timeZone = TimeZone.getDefault()
        return format.format(date)
    }

}