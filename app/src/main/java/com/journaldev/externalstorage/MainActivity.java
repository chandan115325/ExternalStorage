package com.journaldev.externalstorage;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends Activity {
    EditText inputText;
    TextView response, outputTextView;
    Button saveButton,deleteButton;

    private String filename = "SampleFile.txt";
    private String filepath = "MyFileStorage";
    File myExternalFile;
    String myData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = (EditText) findViewById(R.id.myInputText);
        response = (TextView) findViewById(R.id.response);
        outputTextView = (TextView)findViewById(R.id.outputText);

         saveButton =
                (Button) findViewById(R.id.saveExternalStorage);
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputText.getText().toString();
                new SavingDataExternally().execute(input);

            }
        });

        deleteButton = (Button) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            myExternalFile.delete();
            }
        });

       /* if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            saveButton.setEnabled(false);
        }
        else {

        }*/


    }
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
// SavingDataExternally inner class to perform external data storage in a file
    // using AyncTask class
    public class SavingDataExternally extends AsyncTask<String, Void, String>{



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inputText.setText("");
            response.setText("SampleFile.txt saved to External Storage...");
        }

    /**
     * Performing all saving and retrieval task in background on separate thread.
     * @param params
     * @return
     */
    @Override
        protected String doInBackground(String... params) {
            String inputData = params[0];
            myExternalFile = new File(getExternalFilesDir(filepath), filename);
            try {
                FileOutputStream fos = new FileOutputStream(myExternalFile);
                fos.write(inputData.toString().getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                FileInputStream fis = new FileInputStream(myExternalFile);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    myData = myData + strLine;
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return myData;
        }

    /**
     * Displaying Text on textview
     * @param s
     */
    @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            outputTextView.setText(s);
            response.setText("SampleFile.txt data retrieved from External Storage...");
        }
    }


}
