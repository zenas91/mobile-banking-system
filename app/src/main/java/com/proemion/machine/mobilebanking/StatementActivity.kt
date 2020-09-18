package com.proemion.machine.mobilebanking

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.OWNER_ACC_BALANCE
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.OWNER_ACC_NUM
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.OWNER_EMAIL
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.OWNER_IBAN
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.OWNER_NAME
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.getAccountUrl
import com.proemion.machine.mobilebanking.adapter.TransactionAdapter
import com.proemion.machine.mobilebanking.api.Backend
import com.proemion.machine.mobilebanking.model.Transaction
import com.proemion.machine.mobilebanking.model.TransactionSearch
import kotlinx.android.synthetic.main.activity_statement.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class StatementActivity : AppCompatActivity() {

    val mContext = this
    var ownerID by Delegates.notNull<Int>()
    var ownerAccountId by Delegates.notNull<Int>()
    private var dateFrom by Delegates.notNull<Long>()
    private var dateTo by Delegates.notNull<Long>()
    var retrievedTransactions: List<Transaction>? = null
    private var transactionListView: RecyclerView? = null
    private var mAdapter: TransactionAdapter? = null

    lateinit var ownerName:String
    lateinit var ownerEmail:String
    lateinit var ownerIBAN:String
    lateinit var ownerACCNum:String
    lateinit var ownerBalance:String

    lateinit var logo: Bitmap
    lateinit var scaledLogo: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statement)

        ownerID = intent.getIntExtra(StaticConfig.OWNER_ID, 0)
        ownerAccountId = intent.getIntExtra(StaticConfig.OWNER_ACCOUNT_ID, 0)
        ownerName = intent.getStringExtra(OWNER_NAME)!!
        ownerEmail = intent.getStringExtra(OWNER_EMAIL)!!
        ownerIBAN = intent.getStringExtra(OWNER_IBAN)!!
        ownerACCNum = intent.getStringExtra(OWNER_ACC_NUM)!!
        ownerBalance = intent.getStringExtra(OWNER_ACC_BALANCE)!!

        logo = BitmapFactory.decodeResource(resources, R.drawable.logo)
        scaledLogo = Bitmap.createScaledBitmap(logo, 100, 70, false)

        val now = Calendar.getInstance()
        dateFrom = now.timeInMillis
        dateTo = now.timeInMillis

        setDateViewDisplay()
        transactionListView = findViewById(R.id.transaction_list_view)
        from_view.setOnClickListener { openDateRangePicker(dateFrom, dateTo) }

        to_view.setOnClickListener { openDateRangePicker(dateFrom, dateTo) }

        search_button.setOnClickListener { retrieveDebitTransactions(dateFrom, dateTo) }

    }

    private fun retrieveDebitTransactions(from: Long, to: Long){
        val option = mapOf("debit" to ownerAccountId.toString())
        val call: Call<TransactionSearch> = Backend.getRetrofitApi1()!!.getUserTransactions(option)
        call.enqueue(object : Callback<TransactionSearch> {
            override fun onResponse(call: Call<TransactionSearch>, response: Response<TransactionSearch>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    retrieveCreditTransactions(from, to, result?.transactions)
                }
            }
            override fun onFailure(call: Call<TransactionSearch>, t: Throwable) { t.printStackTrace() }
        })
    }

    private fun retrieveCreditTransactions(from: Long, to: Long, transactions: List<Transaction>?){
        val option = mapOf("credit" to ownerAccountId.toString())
        val call: Call<TransactionSearch> = Backend.getRetrofitApi1()!!.getUserTransactions(option)

        call.enqueue(object : Callback<TransactionSearch> {
            override fun onResponse(call: Call<TransactionSearch>, response: Response<TransactionSearch>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val creditTransactions = result?.transactions
                    val allTransactions = ArrayList<Transaction>()
                    transactions?.forEach {
                        if (isDateValid(from, to, it.created!!))
                            allTransactions.add(it)
                    }

                    creditTransactions?.forEach {
                        if (isDateValid(from, to, it.created!!))
                            allTransactions.add(it)
                    }
                    retrievedTransactions = allTransactions

                    if (!allTransactions.isNullOrEmpty()) {
                        pdf_button.visibility = View.VISIBLE
                        pdf_button.setOnClickListener { createBankStatement(
                            convertLongToDateString(from),convertLongToDateString(to), allTransactions) }
                    } else
                        pdf_button.visibility = View.GONE

                    transactionListView!!.adapter = null
                    mAdapter = TransactionAdapter(mContext, ownerAccountId,allTransactions)
                    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(mContext)
                    transactionListView!!.layoutManager = layoutManager
                    transactionListView!!.adapter = mAdapter



                }
            }
            override fun onFailure(call: Call<TransactionSearch>, t: Throwable) { t.printStackTrace() }
        })
    }

    private fun createBankStatement(from:String, to:String, transactions: List<Transaction>?){
        val myPDFDocument = PdfDocument()
        val myPaint = Paint()

        val myPageInfo = PdfDocument.PageInfo.Builder(1200,2010,1).create()
        val myPdfPage = myPDFDocument.startPage(myPageInfo)
        val canvas = myPdfPage.canvas

        canvas?.drawBitmap(scaledLogo, myPageInfo.pageWidth - 120f, 13f, myPaint)

        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 14.0f
        canvas.drawText("Statement of Account", 20f, 30f, myPaint)

        myPaint.textSize = 10.0f
        myPaint.textScaleX = 1.5f
        myPaint.color = Color.rgb(122, 119, 119)
        canvas.drawText("Statement Period: $from - $to", 20f, 45f, myPaint)
        myPaint.textScaleX = 1f

        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 10.0f
        myPaint.color = Color.rgb(156, 34, 87)
        canvas.drawText("Statement Overview", 20f, 90f, myPaint)

        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 10.0f
        myPaint.color = Color.rgb(122, 119, 119)
        canvas.drawText("This Statement has been prepared for", 20f, 120f, myPaint)

        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 12.0f
        myPaint.color = Color.rgb(156, 34, 87)
        canvas.drawText(ownerName, 20f, 140f, myPaint)

        myPaint.textAlign = Paint.Align.RIGHT
        myPaint.textSize = 10.0f
        myPaint.color = Color.rgb(122, 119, 119)
        canvas.drawText("Reference Currency: Euro", myPageInfo.pageWidth - 20f, 120f, myPaint)

        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 8.0f
        myPaint.color = Color.BLACK

        val startXPosition = myPageInfo.pageWidth/2f
        val endXPosition = myPageInfo.pageWidth - 20f
        var startYPosition = 170f

        val informationArray = arrayOf("Account Holder", "IBAN", "Account Number", "Balance", "Email")

        val informationContent = arrayOf(ownerName, ownerIBAN, ownerACCNum, ownerBalance, ownerEmail)

        for(i in informationArray.indices){
            canvas.drawText(informationArray[i], startXPosition + 5, startYPosition -5, myPaint)
            myPaint.textAlign = Paint.Align.RIGHT
            canvas.drawText(informationContent[i], myPageInfo.pageWidth -30f, startYPosition -5, myPaint)
            myPaint.textAlign = Paint.Align.LEFT
            if (i != informationArray.size-1)
                canvas.drawLine(startXPosition, startYPosition + 3, endXPosition, startYPosition + 3, myPaint)
            startYPosition += 20
        }

        canvas.drawLine(startXPosition + 90, 150f, startXPosition + 90, startYPosition -17, myPaint)
        myPaint.style = Paint.Style.STROKE
        myPaint.strokeWidth = 1f
        canvas.drawRect(startXPosition,150f,myPageInfo.pageWidth-20f, startYPosition - 17, myPaint)


        val headerYStart = startYPosition + 13
        val headerXStart = 20f
        val headerYEnd = headerYStart + 20

        myPaint.style = Paint.Style.FILL
        canvas.drawRect(headerXStart,headerYStart,myPageInfo.pageWidth-20f, headerYEnd, myPaint)

        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 10.0f
        myPaint.color = Color.rgb(255, 255, 255)
        canvas.drawText("Trans. Date", headerXStart + 45, headerYStart + 12, myPaint)
        canvas.drawText("Reference", headerXStart + 200, headerYStart + 12, myPaint)
        canvas.drawText("Debit", headerXStart + 360, headerYStart + 12, myPaint)
        canvas.drawText("Credit", headerXStart + 505, headerYStart + 12, myPaint)
        canvas.drawText("Balance", headerXStart + 660, headerYStart + 12, myPaint)
        canvas.drawText("Remark", headerXStart + 920, headerYStart + 12, myPaint)


        myPaint.color = Color.BLACK
        myPaint.textSize = 10.0f
        myPaint.style = Paint.Style.STROKE
        myPaint.strokeWidth = 1f
        myPaint.color = Color.rgb(122, 119, 119)

        var reportYStart = headerYEnd + 10

        var totalDebit = 0
        var totalCredit = 0

        for (i in transactions!!.indices){

            val date = transactions[i].created?.replaceAfter("T", "")?.
                                replace("T", "")
            canvas.drawText(date!!,  headerXStart + 40, reportYStart + 2, myPaint)
            canvas.drawText( transactions[i].ref!!, headerXStart + 190, reportYStart + 2, myPaint)
            if(transactions[i].debit.equals(getAccountUrl(ownerAccountId))) {
                canvas.drawText(transactions[i].amount!!.toString(), headerXStart + 360, reportYStart + 2, myPaint)
                canvas.drawText(transactions[i].balafterdebit!!.toString(), headerXStart + 660, reportYStart + 2, myPaint)
                totalDebit += transactions[i].amount!!
            } else {
                canvas.drawText(transactions[i].amount.toString(), headerXStart + 505, reportYStart + 2, myPaint)
                canvas.drawText(transactions[i].balaftercredit!!.toString(), headerXStart + 660, reportYStart + 2, myPaint)
                totalCredit += transactions[i].amount!!
            }

            if (i != transactions.size-1)
                canvas.drawLine(headerXStart, reportYStart + 10, endXPosition, reportYStart + 10, myPaint)
            reportYStart += 20
        }

        val reportYEnd = reportYStart - 10
        canvas.drawLine(startXPosition + 90, 150f, startXPosition + 90, startYPosition -17, myPaint)
        myPaint.style = Paint.Style.STROKE
        myPaint.strokeWidth = 1f
        canvas.drawRect(startXPosition,150f,myPageInfo.pageWidth-20f, startYPosition - 17, myPaint)

        canvas.drawRect(20f,headerYEnd,myPageInfo.pageWidth-20f, reportYEnd, myPaint)
        canvas.drawLine(headerXStart + 150, headerYStart, headerXStart + 150, reportYEnd, myPaint)
        canvas.drawLine(headerXStart + 300, headerYStart, headerXStart + 300, reportYEnd, myPaint)
        canvas.drawLine(headerXStart + 450, headerYStart, headerXStart + 450, reportYEnd, myPaint)
        canvas.drawLine(headerXStart + 600, headerYStart, headerXStart + 600, reportYEnd, myPaint)
        canvas.drawLine(headerXStart + 750, headerYStart, headerXStart + 750, reportYEnd, myPaint)



        val totalXStart = myPageInfo.pageWidth / 2f +170
        val totalYStart = reportYEnd + 30
        canvas.drawRect(totalXStart, totalYStart,myPageInfo.pageWidth-20f, totalYStart + 40, myPaint)
        canvas.drawText("Total Debit", totalXStart + 5, totalYStart + 12, myPaint)
        canvas.drawText("Total Credit", totalXStart + 5, totalYStart + 34, myPaint)
        canvas.drawLine(totalXStart, totalYStart +20, myPageInfo.pageWidth-20f, totalYStart + 20, myPaint)

        myPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText(totalDebit.toString(), myPageInfo.pageWidth-30f, totalYStart + 12, myPaint)
        canvas.drawText(totalCredit.toString(), myPageInfo.pageWidth-30f, totalYStart + 34, myPaint)

        myPDFDocument.finishPage(myPdfPage)
        val file = File(getExternalFilesDir(null), "/Statement.pdf")

        try {
            myPDFDocument.writeTo(FileOutputStream(file))
            Toast.makeText(mContext, "Statement saved as PDF", Toast.LENGTH_SHORT).show()
        }catch (ex: Exception){}

        myPDFDocument.close()
    }

    private fun isDateValid(from: Long, to: Long, date: String): Boolean{
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val date = format.parse(date)
        val millis = date.time
        return millis in from..to
    }

    private fun openDateRangePicker(from:Long, to:Long){
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setSelection(androidx.core.util.Pair(from, to))

        val picker = builder.build()

        picker.addOnNegativeButtonClickListener { picker.dismiss()}
        picker.addOnPositiveButtonClickListener { dateFrom = it.first!!

            val now = Calendar.getInstance()
            val timestamp = now.timeInMillis
            dateTo = when {
                it.second!! > timestamp -> timestamp
                (it.second!! + 86000000) > timestamp -> timestamp
                else -> it.second!! + 60000000
            }

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