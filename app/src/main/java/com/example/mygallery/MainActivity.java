package com.example.mygallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    ImageButton captureBtn = null;
    final int CAMERA_CAPTURE = 1;
    private Uri picUri;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private GridView grid;
    private  List<String> listOfImagesPath;

    public static final String GridViewDemo_ImagePath =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/GridViewDemo/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captureBtn = (ImageButton)findViewById(R.id.cameraID);
        captureBtn.setOnClickListener(this);
        grid = ( GridView) findViewById(R.id.grid1);

        listOfImagesPath = null;
        listOfImagesPath = RetriveCapturedImagePath();
        if(listOfImagesPath!=null){
            grid.setAdapter(new ImageAdapter(this,listOfImagesPath));
        }
    }


    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        if (arg0.getId() == R.id.cameraID) {

            try {
                //use standard intent to capture an image
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            } catch(ActivityNotFoundException anfe){
                //display an error message
                String errorMessage = "Whoops – your device doesn’t support capturing images!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //user is returning from capturing an image using the camera
            if (requestCode == CAMERA_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");
                String imgcurTime = dateFormat.format(new Date());
                File imageDirectory = new File(GridViewDemo_ImagePath);
                imageDirectory.mkdirs();
                String _path = GridViewDemo_ImagePath + imgcurTime + ".jpg";
                try {
                    FileOutputStream out = new FileOutputStream(_path);
                    thePic.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.close();
                } catch (FileNotFoundException e) {
                    e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                listOfImagesPath = null;
                listOfImagesPath = RetriveCapturedImagePath();
                if (listOfImagesPath != null) {
                    grid.setAdapter(new ImageAdapter(this, listOfImagesPath));
                }
            }
        }
    }

    private List<String> RetriveCapturedImagePath() {
        List<String> tFileList = new ArrayList<String>();
        File f = new File(GridViewDemo_ImagePath);
        if (f.exists()) {
            File[] files=f.listFiles();
            Arrays.sort(files);
            for(int i=0; i<files.length; i++){
                File file = files[i];
                if(file.isDirectory())
                    continue;
                tFileList.add(file.getPath());
            }
        }
        return tFileList;
    }
}
