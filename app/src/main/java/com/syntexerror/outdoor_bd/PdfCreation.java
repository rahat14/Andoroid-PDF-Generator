package com.syntexerror.outdoor_bd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class PdfCreation extends AppCompatActivity {
    private static final String TAG = "PdfCreatorActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    ImageView imgdownload;
   // ConnectionClass connectionClass;
    ArrayList<test_model> MyList1 = new ArrayList<>();
    Context context;
    test_model name;
    test_model price;
    test_model url;
    test_model type;
    test_model date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_creation);
        imgdownload = findViewById(R.id.downloadpdf);
        context = this;


        //adding some test data to generate invoice
        MyList1.add(new test_model("Test" , "12" , "Teggst" , "Tedsst" , System.currentTimeMillis()+"")) ;
        MyList1.add(new test_model("Test1" , "33" , "Tgfest" , "Tggest" , System.currentTimeMillis()+"")) ;
        MyList1.add(new test_model("Test2" , "3" , "Tegfst" , "Tesggt" , System.currentTimeMillis()+"")) ;
        MyList1.add(new test_model("Test3" , "44" , "Tesdfst" , "Tesdst" , System.currentTimeMillis()+"")) ;
        MyList1.add(new test_model("Test4" , "4444" , "Tedsfst" , "Tggest" , System.currentTimeMillis()+"")) ;
        MyList1.add(new test_model("Test5" , "12" , "Teffdst" , "Tdfgest" , System.currentTimeMillis()+"")) ;

        imgdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void createPdfWrapper() throws FileNotFoundException, DocumentException {
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf();
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private void createPdf() throws FileNotFoundException, DocumentException {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }
        String pdfname = System.currentTimeMillis()  + ".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{3, 3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Name");
        table.addCell("Price");
        table.addCell("Type");
        table.addCell("URL");
        table.addCell("Date");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }
        for (int i = 0; i < MyList1.size(); i++) {
            name = MyList1.get(i);
            type = MyList1.get(i);
            date = MyList1.get(i);
            url = MyList1.get(i);
            price = MyList1.get(i);
            String namen = name.getItem_name();
            String pricen = price.getItem_price();
            String daten = date.getCreatedAt();
            String typen = type.getItem_type_code();
            String urln = url.getItem_URL();
            table.addCell(String.valueOf(namen));
            table.addCell(String.valueOf(pricen));
            table.addCell(String.valueOf(typen));
            table.addCell(String.valueOf(urln));
            table.addCell(String.valueOf(daten.substring(0, 10)));
        }
//        System.out.println("Done");
        PdfWriter.getInstance(document, output);
        document.open();
        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 15.0f, Font.UNDERLINE, BaseColor.BLUE);
        document.add(new Paragraph("Pdf Data Generated From App \n\n", f));

        document.add(table); // adding  the table

        document.close();

        previewPdf();
    }
    private void previewPdf() {


       try{
           Uri uri ;
           if (Build.VERSION.SDK_INT < 24) {
               uri = Uri.fromFile(pdfFile);
           } else {
               uri = Uri.parse(pdfFile.getPath()); // My work-around for new SDKs, doesn't work in Android 10.
           }
           Intent viewFile = new Intent(Intent.ACTION_VIEW);
           viewFile.setDataAndType(uri, "application/pdf");
           startActivity(viewFile);
        }
        catch (Exception e )
        {
            Toast.makeText(getApplicationContext() , "Thers is no Application to OPen this " , Toast.LENGTH_SHORT).show();
        }
    }

}