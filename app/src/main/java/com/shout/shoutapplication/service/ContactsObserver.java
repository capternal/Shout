package com.shout.shoutapplication.service;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;

import com.shout.shoutapplication.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ContactsObserver extends ContentObserver {

    private final static String TAG = ContactsObserver.class.getSimpleName();

    private Context ctx;
    private List<ContactsChangeListener> listeners = new ArrayList<ContactsChangeListener>();
    ContactsChangeListener l;

    public ContactsObserver(Context ctx) {
        super(new Handler());
        this.ctx = ctx.getApplicationContext();
        ctx.getContentResolver().registerContentObserver(
                        ContactsContract.Contacts.CONTENT_URI,  // uri
                        false,                                  // notifyForDescendents
                        this);                                  // observer
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.i(TAG, "Contacs change");
        Utils.d("APP CONTROLLER","CONTACT LIST CHANGE DETECTED");
        Intent objIntent = new Intent(getApplicationContext(), ContactSyncService.class);
        getApplicationContext().startService(objIntent);
        for (ContactsChangeListener l : listeners) {
            l.onContactsChange();
        }
    }

    @Override
    public boolean deliverSelfNotifications() {
        return false; // set to true does not change anything...
    }

    public static ContactsObserver register(Context ctx) {
        Log.d(TAG, "register");
        return new ContactsObserver(ctx);
    }

    public void unregister() {
        Log.d(TAG, "unregister");
        ctx.getContentResolver().unregisterContentObserver(this);
    }

    public void addContactsChangeListener(ContactsChangeListener l) {
        System.out.println("ADD CONTACTCHANGE LISTENER CALLED");
        listeners.add(l);
    }

    public interface ContactsChangeListener {
        void onContactsChange();
    }
}