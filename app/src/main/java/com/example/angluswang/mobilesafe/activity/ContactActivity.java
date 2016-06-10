package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.angluswang.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactActivity extends Activity {

    private ListView lvContact;
    private ArrayList<HashMap<String, String>> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        lvContact = (ListView) findViewById(R.id.lv_contact);

        contacts = readContact();
//        System.out.println(contacts);

        lvContact.setAdapter(new SimpleAdapter(this, contacts, R.layout.contact_list_item,
                new String[]{"name", "phone"}, new int[]{R.id.tv_name, R.id.tv_phone}));

    }

    /**
     * 用新的API获取联系人信息
     */
    private ArrayList<HashMap<String, String>> readContact() {

        //首先，从raw_contacts中读取联系人的id（contact_id）
        Cursor rawContactCur = getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[] {ContactsContract.RawContacts._ID},
                null, null, null);

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        if (rawContactCur != null) {
            while (rawContactCur.moveToNext()) {
//                System.out.println("contact_id: " + rawContactCur.getString(0));
                String contact_id = rawContactCur.getString(0);

                Cursor dataContactCur = getContentResolver().query(
                        ContactsContract.RawContactsEntity.CONTENT_URI,
                        new String[]{"data1", "mimetype"}, "contact_id = ?",
                        new String[]{contact_id},
                        null);

                if (dataContactCur != null) {

                    HashMap<String, String> map = new HashMap<>();

                    while (dataContactCur.moveToNext()) {
//                        System.out.println("data1: " + dataContactCur.getString(0)
//                                + "; mimetype: " + dataContactCur.getString(1));

                        String data1 = dataContactCur.getString(0);
                        String mimetype = dataContactCur.getString(1);

                        if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                            map.put("phone", data1);
                        } else if ("vnd.android.cursor.item/name".equals(mimetype)) {
                            map.put("name", data1);
                        }

                    }
                    list.add(map);
                    dataContactCur.close();
                }
            }
            rawContactCur.close();
        }
        return  list;
    }
}
