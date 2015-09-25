package com.example.vidusys.contactapp;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void crHandler(View view) {

        //Method 1
//        ContentValues groupValues;
//        ContentResolver cr=this.getContentResolver();
//        groupValues = new ContentValues();
//        groupValues.put(ContactsContract.Groups.TITLE, "MyContactGroup");
//        cr.insert(ContactsContract.Groups.CONTENT_URI, groupValues);
//        Toast t=Toast.makeText(this,"Group Created",Toast.LENGTH_LONG);
//        t.show();


        //Method 2
//        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//
//        ops.add(ContentProviderOperation
//                .newInsert(ContactsContract.Groups.CONTENT_URI)
//                .withValue(ContactsContract.Groups.TITLE, "MyNewGropp")
//                .withValue(ContactsContract.Groups.GROUP_VISIBLE, true)
//                .withValue(ContactsContract.Groups.ACCOUNT_NAME, "suhasgaji@gmail.com")
//                .withValue(ContactsContract.Groups.ACCOUNT_TYPE, "com.google")
//                .build());
//        try {
//
//            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
//            Toast t=Toast.makeText(this,"Group Created",Toast.LENGTH_LONG);
//            t.show();
//
//        } catch (Exception e) {
//            Log.e("Error", e.toString());
//        }

        Toast.makeText(getApplicationContext(), createContact(), Toast.LENGTH_SHORT).show();

    }

    private String createContact() {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ContactOperation co = new ContactOperation(this);
        ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build()
        );

        co.addContactToGroup(ops);
        contactName("Demo Contact", ops);
        contactNumber("1234567890", ops);
//        addtoGr("Demo","1234",ops);
        return contactProvider(ops);


    }

    private String contactProvider(ArrayList<ContentProviderOperation> ops) {
        String what;
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            what = "Contact Created ";
        } catch (Exception e) {
            e.printStackTrace();
            what = "Unable to Create Contact ";
        }
        return what;
    }

    private void contactNumber(String no, ArrayList<ContentProviderOperation> ops) {

        ops.add(ContentProviderOperation.
                        newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, no)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()
        );
    }

    private void contactName(String name, ArrayList<ContentProviderOperation> ops) {
        ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name).build()
        );
    }

    private void addtoGr(String name, String no, ArrayList<ContentProviderOperation> ops) {
        ContactOperation co=new ContactOperation(this);
        String grid=co.getGroupId();
        ops.add(ContentProviderOperation.
                        newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, no)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                        .withValue(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, grid)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()
        );
    }
}
