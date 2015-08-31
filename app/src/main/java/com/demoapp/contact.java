package com.demoapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class contact extends Activity {
    TextView textDetail;
    private ListView contactlist;
    private ContactAdapter contactadapter;
    ArrayList<ContactItem> contacts = new ArrayList<>();
    private ProgressDialog dialog;
    private Button startimport;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        textDetail = (TextView) findViewById(R.id.textView1);
        startimport = (Button) findViewById(R.id.startimport);
        contactlist = (ListView) findViewById(R.id.contactlistview);

        dialog = ProgressDialog.show(contact.this,
                "Import is in progress ..", "Please wait...", true);


        startimport.performClick();


        //fileNameList = getFileListfromSDCard();

    }

    public void showcontacts(View view) {



        Toast.makeText(getApplicationContext(), "Creating your contacts list .." ,
                Toast.LENGTH_LONG).show();



       // SystemClock.sleep(6000);


        //readContacts();

        new ImageUploadTask().execute();

        //contactadapter = new ContactAdapter(this, R.layout.contact_list_item, readContacts());
        //contactlist.setAdapter(contactadapter);

    }

    public  ArrayList<ContactItem>  readContacts() {


        //StringBuffer sb = new StringBuffer();
        //sb.append("......Contact Details.....");
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String phone = null;
        String emailContact = null;
        String emailType = null;
        String image_uri = "";
        Bitmap bitmap = null;

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                //image_uri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    //System.out.println("name : " + name + ", ID : " + id);
                    //sb.append("\n Contact Name:" + name);
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //sb.append("\n Phone number:" + phone);
                       /* Toast.makeText(getApplicationContext(), "phone=" + phone + "name=" + name + "email=" + emailContact,
                                Toast.LENGTH_LONG).show();*/
                        //System.out.println("phone" + phone);
                    }
                    pCur.close();
                    Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        emailContact = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        //sb.append("\nEmail:" + emailContact + "Email type:" + emailType);
                        //System.out.println("Email " + emailContact + " Email Type : " + emailType);
                       /* Toast.makeText(getApplicationContext(), "phone=" + phone + "name=" + name + "email=" + emailContact,
                                Toast.LENGTH_LONG).show();*/

                        contacts.add(new ContactItem(emailContact,  name));


                    }
                    emailCur.close();

                }
                if (image_uri != null) {
                    //System.out.println(Uri.parse(image_uri));
                }
                /*bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                        Uri.parse(image_uri));*/
                //sb.append("\n Image in Bitmap:" + bitmap);
                // System.out.println(bitmap);
            }


        }

        try {
            if (dialog.isShowing())
                dialog.dismiss();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }

        Intent intent = new Intent(this, MainActivity.class);


        startActivity(intent);

        return contacts;
    }




    class ImageUploadTask extends AsyncTask<Void, Void, String> {
        @SuppressWarnings("unused")
        @Override
        protected String doInBackground(Void... unsued) {


            //StringBuffer sb = new StringBuffer();
            //sb.append("......Contact Details.....");
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            String phone = null;
            String emailContact = null;
            String emailType = null;
            String image_uri = "";
            Bitmap bitmap = null;

            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //image_uri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        //System.out.println("name : " + name + ", ID : " + id);
                        //sb.append("\n Contact Name:" + name);
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //sb.append("\n Phone number:" + phone);
                       /* Toast.makeText(getApplicationContext(), "phone=" + phone + "name=" + name + "email=" + emailContact,
                                Toast.LENGTH_LONG).show();*/
                            //System.out.println("phone" + phone);
                        }
                        pCur.close();
                        Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                        while (emailCur.moveToNext()) {
                            emailContact = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                            //sb.append("\nEmail:" + emailContact + "Email type:" + emailType);
                            //System.out.println("Email " + emailContact + " Email Type : " + emailType);
                       /* Toast.makeText(getApplicationContext(), "phone=" + phone + "name=" + name + "email=" + emailContact,
                                Toast.LENGTH_LONG).show();*/

                            contacts.add(new ContactItem(emailContact,  name));


                        }
                        emailCur.close();

                    }
                    if (image_uri != null) {
                        //System.out.println(Uri.parse(image_uri));
                    }
                /*bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                        Uri.parse(image_uri));*/
                    //sb.append("\n Image in Bitmap:" + bitmap);
                    // System.out.println(bitmap);
                }


            }

            try {
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }

            Context context = contact.this;
            Intent cameraintent = new Intent(context, MainActivity.class);


            // Launch default browser
            context.startActivity(cameraintent);

            //return contacts;

            return "Success";
            // (null);
        }

        @Override
        protected void onProgressUpdate(Void... unused) {

        }

        @Override
        protected void onPostExecute(String sResponse) {
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }

    }





    public class ContactAdapter extends ArrayAdapter<ContactItem> {

        private ArrayList<ContactItem> fLst;
        private Context adapContext;

        public ContactAdapter(Context context, int textViewResourceId,
                              ArrayList<ContactItem> fLst) {
            super(context, textViewResourceId, fLst);
            this.fLst = fLst;
            adapContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            FHolder fHolder = null;

            if (convertView == null) {
                view = View.inflate(adapContext, R.layout.contact_list_item, null);

                fHolder = new FHolder();

                fHolder.name = (TextView) view.findViewById(R.id.name);
                fHolder.email = (TextView) view.findViewById(R.id.email);
               // fHolder.picon = (ImageView) view.findViewById(R.id.picon);

                view.setTag(fHolder);
            } else {
                fHolder = (FHolder) view.getTag();
            }
            ContactItem fileName = fLst.get(position);
            fHolder.name.setText(fileName.getTitle());
            fHolder.email.setText(fileName.getEmail());





            //resultIAV.add(new ImageItem(bitmap,  path));


            return view;
        }
    }

    static class FHolder {
        public TextView name;
        public TextView email;

    }
}