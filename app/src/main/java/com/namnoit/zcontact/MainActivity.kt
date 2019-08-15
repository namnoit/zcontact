package com.namnoit.zcontact

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.provider.ContactsContract.CommonDataKinds.Email

const val PERMISSIONS_REQUEST_READ_CONTACTS = 100

class MainActivity : AppCompatActivity() {
    private lateinit var contacts: ArrayList<ContactModel>
    lateinit var adapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contacts = ArrayList()
        if (hasPermission()) {
            contacts = readContacts()
        }
        val recyclerView: RecyclerView = findViewById(R.id.contactsView)
        val linearLayoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        adapter = ContactsAdapter(applicationContext, contacts)
        recyclerView.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu1) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hasPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        for (result in grantResults) {
            if (result < 0) {
                finish()
                return
            }
        }
        contacts.clear()
        contacts.addAll(readContacts())
        adapter.notifyDataSetChanged()
    }

    private fun readContacts(): ArrayList<ContactModel> {
        val contacts = ArrayList<ContactModel>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            do {
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val photoURI =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI))
                val number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                val emails =
                    contentResolver.query(Email.CONTENT_URI,
                        arrayOf(Email.DATA),
                        Email.CONTACT_ID + " = " + id,
                        null,
                        null)
                var email = ""
                if (emails != null && emails.moveToNext()) {
                    email = emails.getString(emails.getColumnIndex(Email.DATA))
                }
                emails?.close()
                contacts.add(ContactModel(photoURI, name, number, email, id.toString()))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return contacts
    }
}
