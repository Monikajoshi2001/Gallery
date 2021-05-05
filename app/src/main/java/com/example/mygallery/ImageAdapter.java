package com.example.mygallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    List<String> image_path;

    public ImageAdapter(Context c,List<String> i)
    {
        context=c;
        image_path=i;
    }
    @Override
    public int getCount() {
        if(image_path!=null)
        {
            return image_path.size();
        }
        else
            return  0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;
        BitmapFactory.Options bfOptions=new BitmapFactory.Options();
        bfOptions.inDither=false;                     //Disable Dithering mode
        bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
        bfOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
        bfOptions.inTempStorage=new byte[32 * 1024];
        if (convertView == null)
        {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setPadding(0, 0, 0, 0);
        }
        else
         {
            imageView = (ImageView) convertView;
         }
        FileInputStream fs = null;
        Bitmap bm;
        try {
            fs = new FileInputStream(new File(image_path.get(position).toString()));
            if(fs!=null)
            {
                bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
                imageView.setImageBitmap(bm);
                imageView.setId(position);
                imageView.setLayoutParams(new GridView.LayoutParams(200, 160));
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally{
            if(fs!=null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imageView;
    }

}
