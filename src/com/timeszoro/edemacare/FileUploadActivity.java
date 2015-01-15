package com.timeszoro.edemacare;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.edemacare.R;
import com.timeszoro.fragment.FileListFragment;
import com.timeszoro.mode.FilesLab;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/1/15.
 */
public class FileUploadActivity extends FragmentActivity implements View.OnClickListener{
    private static final String TAG = "File Upload";
    private FileListFragment mFileListFragment;
    private FilesLab mFileLab;
    private ArrayList<File> mFileList ;
    private Button mSelectAll;
    private Button mUpload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        //init the file list
        String strPath = getApplicationContext().getFilesDir().getAbsolutePath();
        String dbPath = strPath.substring(0,strPath.length() - 5) + "databases/";
        File[] files = new File(dbPath).listFiles();
        mFileLab = FilesLab.getFileLab(this);
        mFileList = mFileLab.getFileList();
        if(files != null){
            for(File f: files){
                if(!f.getName().endsWith("journal")){
                    mFileList.add(f);
                }
            }

        }
        mFileListFragment = new FileListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_fileList,mFileListFragment).commit();
        Log.d(TAG, dbPath);


        //init the buttons
        mSelectAll = (Button) findViewById(R.id.selecte_all);
        mUpload = (Button)findViewById(R.id.upload);
        mSelectAll.setOnClickListener(this);
        mUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selecte_all:
                mFileListFragment.selectAll();
                break;
            case R.id.upload:
                //get the list of the files




                ArrayList<File> files = mFileListFragment.getSendFiles();

                Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Data of the Edema Measurement");
                intent.putExtra(Intent.EXTRA_TEXT, "Mail with multiple attachments");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"jygjhappy@gmail.com"});

                ArrayList<Uri> uris = new ArrayList<Uri>();
                for(File f: files){
                    uris.add(Uri.parse("file://" + f.getAbsolutePath()));
                }

                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                startActivity(Intent.createChooser(intent, "Send mail"));


                break;
            default:

                break;
        }
    }
}
