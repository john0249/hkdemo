package com.hkfactorydemo.hkfactoryjohncampusano.ui.view.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.hkfactorydemo.hkfactoryjohncampusano.R
import com.hkfactorydemo.hkfactoryjohncampusano.domain.model.Details
import com.hkfactorydemo.hkfactoryjohncampusano.domain.model.Purchase
import com.hkfactorydemo.hkfactoryjohncampusano.ui.view.adapters.PdfDocumentAdapter
import com.hkfactorydemo.hkfactoryjohncampusano.ui.viewModels.PurchaseViewModel
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import kotlin.jvm.Throws

@AndroidEntryPoint
class ReportActivity : AppCompatActivity() {
    private val viewModel: PurchaseViewModel by viewModels()
    private val fileName = "report.pdf"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)


            createPdfFile(getAppPath(this@ReportActivity) + fileName, viewModel.detailsModelList.value!!)




    }


    private fun getAppPath(context: Context, name: String = "report.pdf"): String {
        val separator = File.separator
            val dir = File(
                context.applicationContext.cacheDir.toString()
                        + File.separator
                        + context.resources.getString(R.string.app_name)
                        + separator
            )
            if (dir.exists())
                dir.delete()

            dir.mkdir()


            return dir.path + separator + name
        }


    private fun createPdfFile(path: String, details: List<Details> ) {
      if(File(path).exists()){
          File(path).delete()
      }
        try{

            val document = Document()
            val bundle = intent.extras
            val seller = bundle?.getString("seller")
            val customerName = bundle?.getString("customerName")
            val vatID = bundle?.getString("vatId")
            val ncf = bundle?.getString("ncf")
            val totalSold = bundle?.getString("totalSold")
            val totalItems = bundle?.getString("totalItems")
            val details = bundle?.getParcelableArray("detailList")

            PdfWriter.getInstance(document, FileOutputStream(path))

            document.open()
            document.pageSize = PageSize.A4
            document.addCreationDate()
            document.addAuthor("John Campusano")
            document.addCreator("")

            val colorAccent = BaseColor(0, 153, 204, 255)
            val headingFontSize = 20.0f


            val fontName = BaseFont.createFont("assets/fonts/brandom_medium.otf", "UTF-8", BaseFont.EMBEDDED)
            val titleStyle = Font(fontName, 36.0f, Font.NORMAL, BaseColor.BLACK)
            val valueStyle = Font(fontName, 28.0f, Font.NORMAL, BaseColor.BLACK)
            addNewItem(document, seller.toString(), Element.ALIGN_CENTER, titleStyle)

            val rncStyle = Font(fontName, 34.0f, Font.NORMAL, BaseColor.BLACK)
            val headingStyle = Font(fontName, headingFontSize, Font.NORMAL, colorAccent)
            addNewItem(document, ncf.toString(), Element.ALIGN_CENTER, rncStyle)


            addNewItem(document, "Cliente: ", Element.ALIGN_LEFT, headingStyle)
            addNewItem(document, customerName.toString(), Element.ALIGN_LEFT, headingStyle)

            addNewItem(document, "Cedula: ", Element.ALIGN_LEFT, headingStyle)
            addNewItem(document, vatID.toString(), Element.ALIGN_LEFT, headingStyle)


            //product detail
            addLineSpace(document)
            addNewItem(document, "Detalle de ventas", Element.ALIGN_CENTER, titleStyle)
            addLineSeparator(document)

            //item
            var acummulative = 0

          /*  details?.forEach {

            }
            details.forEach {
               acummulative =  it.productQuantity * it.productPrice
                addNewItem(document, "Código: ${it.productCode}", Element.ALIGN_LEFT, titleStyle)
                addNewItem(document, "${it.productQuantity} x ${it.productPrice}", Element.ALIGN_CENTER, titleStyle)
                addNewItemLeftRight(document, it.productName, "$acummulative", titleStyle, valueStyle)
            }*/
            addLineSeparator(document)
            addLineSeparator(document)

            addNewItemLeftRight(document, "Total", "$$totalSold}", titleStyle, valueStyle)

            document.close()

            Toast.makeText(this, "Documento generado", Toast.LENGTH_SHORT).show()
            printPDF()
        }catch (e: Exception){
            Log.e("", e.message.toString())
        }
    }
    @Throws(DocumentException::class)
    private fun addNewItemLeftRight(document: Document, textLeft: String, textRight: String, leftStyle: Font, rightStyle: Font) {
        var chunkTextLeft = Chunk(textLeft, leftStyle)
        var chunkTextRight= Chunk(textRight, rightStyle)
        val p = Paragraph(chunkTextLeft)
        p.add(Chunk(VerticalPositionMark()))
            p.add(chunkTextRight)
        addLineSpace(document)
    }

    private fun printPDF() {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        try {
            val printAdapter = PdfDocumentAdapter(this@ReportActivity, getAppPath(this@ReportActivity) + fileName)
            printManager.print("Document", printAdapter, PrintAttributes.Builder().build())
        } catch (e: Exception) {
            Log.e("message", e.message ?: "")
        }
    }

    private fun addLineSeparator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0, 60)
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    private fun addLineSpace(document: Document) {

        document.add(Paragraph(""))

    }

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, align: Int, style: Font) {
        val chunk = Chunk(text, style)
        val p = Paragraph(chunk)
        p.alignment = align
        document.add(p)

    }
}