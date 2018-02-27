package com.example.user.android_assignment_20_3;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * Created by User on 19-02-2018.
 */

public class ContactHelper {
//created the class as contact helper
    public static Cursor getContactCursor(ContentResolver contactHelper, String startsWith) {
    //created method as cursor This interface provides random read-write access to the result set returned by a database query.
        String[] projection = { ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER };
       //string as empty and given id and display name.
        Cursor cur = null;
        //cursor is null

        try {
            //using try catch method if null is equal or not equals to we will check
            if (startsWith != null && !startsWith.equals("")) {
                cur = contactHelper.query (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like \"" + startsWith + "%\"", null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            } else {
                cur = contactHelper.query (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            }
            cur.moveToFirst();
            //cursor will move to first
        }
        catch (Exception e) {
            e.printStackTrace();
            //if any exception will come this will handle the exception
        }
        return cur;
        //it will return the cursor.
    }

    public static boolean insertContact(ContentResolver contactAdder, String firstName, String mobileNumber) {
        //inserting the contact with name and mobile number
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        //proving a array list
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,firstName).build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,mobileNumber).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());

        try {
            //using try catch we  will catch the exception if occurs
            contactAdder.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void deleteContact(ContentResolver contactHelper, String number) {
    //creating the delete contact method
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        //creating the array list and storing the list in new array list
        String[] args = new String[] { String.valueOf(getContactID(contactHelper, number)) };
        //created empty string and stroing the id and helper and number
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args).build());

        try {
            contactHelper.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
            //using try catch exception we will catch the execption if occurs.
        }
    }

    public static void updateContact(ContentResolver contactHelper, String number,String newFirstName, String newNumber) {
    //created the method of updating the contact
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        //providing new array list
        String contactId = new String( String.valueOf(getContactID(contactHelper, number)) );
        //storing the id and number
        String selectPhone = ContactsContract.Data.CONTACT_ID + "=? AND " + ContactsContract.Contacts.Data.MIMETYPE + "='"  +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'" + " AND " + ContactsContract.CommonDataKinds.Phone.TYPE + "=?";
        //for adding and updating the contact
        String[] phoneArgs = new String[]{contactId, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)};
        String[] nameArgs = new String[]{contactId, String.valueOf(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME)};
        //it displays the name
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(selectPhone, phoneArgs)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, newNumber)
                .build());
        try {
            contactHelper.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
        //catch is used to catch the execption.
    }

    private static long getContactID(ContentResolver contactHelper, String number) {
        //created the get id method as private
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        //it will look up the phone contacts
        String[] projection = { ContactsContract.PhoneLookup._ID };
        //created as empty string and looking up the id
        Cursor cursor = null;
        //given cursor is null.

        try {
            cursor = contactHelper.query(contactUri, projection, null, null,null);

            if (cursor.moveToFirst()) {
                //if cursor will move to first then it will displays the id if not it will show null
                int personID = cursor.getColumnIndex(ContactsContract.PhoneLookup._ID);
                return cursor.getLong(personID);
                //it will get the id
            }
            return -1;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return -1;
    }
}