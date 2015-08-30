package com.demoapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public  class MainActivity extends Activity implements OnItemLongClickListener {

    private ListView mListView;
    private List<String> fileNameList;
    private FlAdapter mAdapter;
    private File file;
    private File tfile;
    String state = Environment.getExternalStorageState();
    List<String> flLst = new ArrayList<String>();
    List<String> fnamelLst = new ArrayList<String>();
    int dircount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flist);
        mListView = (ListView) findViewById(R.id.listView1);
        file = Environment.getExternalStorageDirectory();
        dircount= StringUtils.countMatches(file.getAbsolutePath(), "/");
        fileNameList = getFileListfromSDCard();
        mAdapter = new FlAdapter(this, R.layout.f_list_item, fileNameList);
        mListView.setAdapter(mAdapter);
    }

    private List<String> getFileListfromSDCard() {

        if (Environment.MEDIA_MOUNTED.equals(state) && file.isDirectory()) {
            File[] fileArr = file.listFiles();
            int length = fileArr.length;
            for (int i = 0; i < length; i++) {
                File f = fileArr[i];
                String filetype;
                if(f.isDirectory()){
                    filetype="directory";
                    populatefilelist(f.getAbsolutePath());
                   // flLst.add(f.getName());

                }
                else  filetype="file";
                /* Toast.makeText(getApplicationContext(),"filename="+ f.getName()+"filetype="+filetype+"getabspath="+f.getAbsolutePath(),
                        Toast.LENGTH_LONG).show();*/
                if(f.getName().contains(".JPEG")||
                        f.getName().contains(".jpeg")||
                        f.getName().contains(".png")||
                        f.getName().contains(".PNG")||
                        f.getName().contains(".JPG")||
                        f.getName().contains(".jpg")||
                        f.getName().contains(".bmp")||
                        f.getName().contains(".BMP")||
                        f.getName().contains(".GIF")||
                        f.getName().contains(".gif")||
                        f.getName().contains(".mp4")||
                        f.getName().contains(".MP4")
                        ){

                    if(!Arrays.asList(fnamelLst).contains(f.getName())){
                        flLst.add(f.getAbsolutePath());
                        fnamelLst.add(f.getName());

                    }
                }


            }
        }

        return flLst;
    }

    public void openuploadlist(View view) {
        Intent intent = new Intent(this, upload.class);


        startActivity(intent);
    }
public void opencontactlist(View view) {
        Intent intent = new Intent(this, contact.class);


        startActivity(intent);
    }


public void populatefilelist(String fpath){

    tfile=new File(fpath);
    File[] tfileArr = tfile.listFiles();
    int tlength = tfileArr.length;
    for (int i = 0; i < tlength; i++) {
        File tf = tfileArr[i];
        String filetype;
        if(tf.isDirectory()){
            filetype="directory";
           // flLst.add(tf.getName());
            int count = StringUtils.countMatches(tf.getAbsolutePath(), "/");
           /* Toast.makeText(getApplicationContext(), "/ count=" + count,
                    Toast.LENGTH_LONG).show();*/
            if(count< (dircount+4))populatefilelist(tf.getAbsolutePath());
        }
        else  filetype="file";
        /*Toast.makeText(getApplicationContext(),"filename="+ tf.getName()+"filetype="+filetype+"getabspath="+tf.getAbsolutePath(),
                Toast.LENGTH_LONG).show();*/
        if(tf.getName().contains(".JPEG")||
                tf.getName().contains(".jpeg")||
                tf.getName().contains(".png")||
                tf.getName().contains(".PNG")||
                tf.getName().contains(".JPG")||
                tf.getName().contains(".jpg")||
                tf.getName().contains(".bmp")||
                tf.getName().contains(".BMP")||
                tf.getName().contains(".GIF")||
                tf.getName().contains(".gif")||
                tf.getName().contains(".mp4")||
                tf.getName().contains(".MP4")
                ){


            if(!Arrays.asList(fnamelLst).contains(tf.getName())){
                flLst.add(tf.getAbsolutePath());
                fnamelLst.add(tf.getName());

            }

        }
    }

}
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    public class FlAdapter extends ArrayAdapter<String> {

        private List<String> fLst;
        private Context adapContext;

        public FlAdapter(Context context, int textViewResourceId,
                         List<String> fLst) {
            super(context, textViewResourceId, fLst);
            this.fLst = fLst;
            adapContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            FHolder fHolder = null;

            if (convertView == null) {
                view = View.inflate(adapContext, R.layout.f_list_item, null);

                fHolder = new FHolder();

                fHolder.fNameView = (TextView) view.findViewById(R.id.fname);
                fHolder.Fimgview = (ImageView) view.findViewById(R.id.imgIcon);
                fHolder.picon = (ImageView) view.findViewById(R.id.picon);

                view.setTag(fHolder);
            } else {
                fHolder = (FHolder) view.getTag();
            }
            String fileName = fLst.get(position);
            Bitmap bitmap ;
            if(fileName.contains(".mp4")||fileName.contains(".MP4")){
                bitmap = ThumbnailUtils.createVideoThumbnail(fileName,
                        MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                fHolder.fNameView.setText(fileName);
                fHolder.Fimgview.setImageBitmap(bitmap);
                fHolder.picon.setVisibility(View.VISIBLE);

            }
            else {
                bitmap = BitmapFactory.decodeFile(fileName);
                int h = bitmap.getHeight();
                int w = bitmap.getWidth();
                if(h>400 && w>400){
                    fHolder.fNameView.setText(fileName);
                    fHolder.picon.setVisibility(View.INVISIBLE);
                    fHolder.Fimgview.setImageURI(Uri.parse(fileName));

                }

            }




            //resultIAV.add(new ImageItem(bitmap,  path));


            return view;
        }
    }

    static class FHolder {
        public TextView fNameView;
        public ImageView Fimgview;
        public  ImageView picon;
    }
}