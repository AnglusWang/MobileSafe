package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.angluswang.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactActivity extends Activity {

    private ListView lvContact;
    private ArrayList<HashMap<String, String>> readContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        lvContact = (ListView) findViewById(R.id.lv_contact);

        readContact = readContact();
        System.out.println(readContact);

        lvContact.setAdapter(new SimpleAdapter(this, readContact, R.layout.contact_list_item,
                new String[]{"name", "phone"}, new int[]{R.id.tv_name, R.id.tv_phone}));

    }

    /**
     * 1.首先，从raw_contacts中读取联系人的id（contact_id）
     * 2.根据contact_id 从data表中查询出相应的电话号码和联系人名称
     * 3.根据mimetype来区分哪个是联系人，哪个是电话号码
     */
    private ArrayList<HashMap<String, String>> readContact() {

//      首先从raw_contacts表中读取出contact_id
        Uri rawContactsUri = Uri
                .parse("content://com.android.provides.contacts");
        Uri dataUri = Uri.parse("content://com.android.provides.contacts");

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        Cursor rawContactsCursor = getContentResolver().query(rawContactsUri,
                new String[]{"contact_id"}, null, null, null);

        if (rawContactsCursor != null) {
            while (rawContactsCursor.moveToNext()) {
                String contactId = rawContactsCursor.getString(0);
                System.out.println("contact_id:" + contactId);

                //根据contact_id 从data表中查询出相应的电话号码和联系人名称,实际上查询的是view_data
                Cursor dataCursor = getContentResolver().query(dataUri,
                        new String[]{"data1", "mimetype"}, "contact_id = ?",
                        new String[]{contactId}, null);

                if (dataCursor != null) {

                    HashMap<String, String> map = new HashMap<>();
                    while (dataCursor.moveToNext()) {
                        String data1 = dataCursor.getString(0);
                        String mimetype = dataCursor.getString(1);
                        System.out.println(contactId + ";" + data1 + ";" + mimetype);

                        if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                            map.put("phone", data1);
                        } else if ("vnd.android.cursor.item/name".equals(mimetype)) {
                            map.put("name", data1);
                        }

                        list.add(map);
                        dataCursor.close();
                    }
                }

                rawContactsCursor.close();
            }
        }
        return list;
    }
}
