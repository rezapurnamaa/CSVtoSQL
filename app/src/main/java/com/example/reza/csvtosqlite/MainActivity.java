package com.example.reza.csvtosqlite;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity {

    private static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private boolean isCSVFileNeedToInitialize = true;
    private static final int DOWNLOAD_PROGRESS = 0;
    private DBHelper mDBHelper;
    private ProgressDialog progressDialog;
    private static int totalRowsUpdate = 0;

    public static final String external_sd = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final File sdCard = Environment.getExternalStorageDirectory();
    public static final String sdcardBaseDir = sdCard.getAbsolutePath();
    public static final String externalPath = "/Android/data/com.example.reza.csvtodqlite";
    public static final String csvFileName = "stolpersteine.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DBHelper(this);
        mDBHelper.open();
        totalRowsUpdate = 0;

        //create empty dir if not exist
        File appDir = new File((sdcardBaseDir + externalPath));
        if (!appDir.exists()){
            appDir.mkdirs();
        }

        File externalResourceFile = new File(sdcardBaseDir + externalPath + csvFileName);
        isCSVFileNeedToInitialize = externalResourceFile.exists();
        TextView labelResult = (TextView) findViewById(R.id.showResult);

        if(isCSVFileNeedToInitialize){
            new InitializeCSVFileAsync().execute("");
            labelResult.setText(totalRowsUpdate + " fetched from 'stumbl.csv' into database successful.");
        }
        else {
            labelResult.setText("csv not found.");
            PopIt("exit Application", "csv not found.");
        }
    }

    public static void setTotalRecord(int counter){
        totalRowsUpdate = counter;
    }

    private void PopIt(String title, String message){
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setTitle(title);
        alertbox.setMessage(message);
        alertbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        alertbox.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id){
        Resources res = getResources();
        String reader = "";
        int counter = 0;
        try {
            File f = new File(sdcardBaseDir + externalPath + csvFileName);
            BufferedReader in = new BufferedReader(new FileReader(f));
            while ((reader = in.readLine()) != null){
                counter++;
            }
            setTotalRecord(counter);
        }
        catch (Exception e){
            e.getMessage();
        }
        switch (id){
            case DIALOG_DOWNLOAD_PROGRESS:
                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressDrawable(res.getDrawable(R.drawable.initialize_progress_bar_states));
                progressDialog.setMessage("Initializing...");
                progressDialog.setMax(counter);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();
                return progressDialog;
            default:
                return null;
        }
    }

    // Display Initialize progress bar for uploading CSVFiles to database
    class InitializeCSVFileAsync extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... aurl){
            try{
                float total = 0F;
                float fcounter = 1F;
                String reader = "";
                int counter = 0;
                boolean skipheader = true;
                File f = new File(sdcardBaseDir + externalPath + csvFileName);
                BufferedReader in = new BufferedReader(new FileReader(f));

                while ((reader = in.readLine()) != null){
                    // skip header column name from csv
                    if(skipheader) {
                        skipheader = false;
                        continue;
                    }
                    String[] rowData = reader.split(",");
                    mDBHelper.insertDB(rowData);
                    total+= fcounter;
                    publishProgress("" + (int)total);
                    //publishProgress((int)(total*100/lenghtOfFile));
                }
                in.close();
            }
            catch (Exception e){
                e.getMessage();
            }
            return null;
        }

        protected void onProgressUpdate(String... progress){
            //Log.d("ANDRO_ASYNC",progress[0]);
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }
        @Override
        protected void onPostExecute(String unused){
            File f = new File(sdcardBaseDir + externalPath + csvFileName);
            boolean result = f.delete();
            if(isCSVFileNeedToInitialize) dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            mDBHelper.close();
        }
        protected void onDestroy(){
            if(mDBHelper != null) mDBHelper.close();
        }
    }
}
