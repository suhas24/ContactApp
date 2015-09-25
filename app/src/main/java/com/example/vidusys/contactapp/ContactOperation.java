package com.example.vidusys.contactapp;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * Created by Vidusys on 09/08/2015.
 */
public class ContactOperation {
    Context con;
    private String GroupTitle = "MyNew";
    ContactOperation(Context con){
        this.con=con;
    }
    //## Add Photo To Contact
//    void addPhoto(ArrayList<ContentProviderOperation> ops){
//
//        ContentValues cv = new ContentValues();
//        cv.put(Photo.PHOTO,ImageContact(R.drawable.ic_launcher));
//        cv.put(Photo.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
//        Builder insertOp = createInsertForContact(-1, cv);
//        ops.add(insertOp.build());
//    }
//    byte[] ImageContact(int img){
//        Resources res = con.getResources();
//        Drawable drawable = res.getDrawable(img);
//        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] bitMapData = stream.toByteArray();
//        return bitMapData;
//    }
//    private Builder createInsertForContact(long rawContactId, ContentValues cv) {
//        Builder insertOp = ContentProviderOperation.newInsert(Data.CONTENT_URI).withValues(cv);
//        if (rawContactId == -1) {
//            insertOp.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
//        } else {
//            insertOp.withValue(Data.RAW_CONTACT_ID, rawContactId);
//        }
//        return insertOp;
//    }
    //## Function to get Group Id
    public String getGroupId()
    {
        String GroupId = ifGroup(GroupTitle);
        if (GroupId == null)
        {

            ArrayList<ContentProviderOperation> opsGroup = new ArrayList<ContentProviderOperation>();
            opsGroup.add(ContentProviderOperation.newInsert(ContactsContract.Groups.CONTENT_URI)
                    .withValue(ContactsContract.Groups.TITLE, GroupTitle)
                    .withValue(ContactsContract.Groups.GROUP_VISIBLE, true)
                    .withValue(ContactsContract.Groups.ACCOUNT_NAME, "suhasgaji@gmail.com")
                    .withValue(ContactsContract.Groups.ACCOUNT_TYPE, "com.google")
                    .build());
            try
            {

                con.getContentResolver().applyBatch(ContactsContract.AUTHORITY, opsGroup);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return ifGroup(GroupTitle);
    }
    //### Function return group id by Group Title
    private String ifGroup(String $name)
    {
        String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
        String[] selectionArgs = { "0", "1" };
        Cursor cursor =con.getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
        cursor.moveToFirst();
        int len = cursor.getCount();

        String GroupId = null;
        for (int i = 0; i < len; i++)
        {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));
            String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));

            if (title.equals(GroupTitle))
            {
                GroupId = id;
                break;
            }

            cursor.moveToNext();
        }
        cursor.close();

        return GroupId;
    }
    // Add Contact To Group
    public void addContactToGroup(ArrayList<ContentProviderOperation> ops){
        String GroupId = getGroupId();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)

                .withValue(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, GroupId)
                .build());
    }

}
