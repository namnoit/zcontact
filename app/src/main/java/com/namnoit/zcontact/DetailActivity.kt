package com.namnoit.zcontact

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.io.BufferedInputStream


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (Build.VERSION.SDK_INT < 21) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        if (Build.VERSION.SDK_INT >= 21) {
            val windowParams = window.attributes
            windowParams.flags = windowParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
            window.attributes = windowParams
            window.statusBarColor = Color.TRANSPARENT
        }

        val toolbar = findViewById<Toolbar>(R.id.detailToolBar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)


        val callButton = findViewById<View>(R.id.callButton)
        val messageButton = findViewById<View>(R.id.messageButton)

        val nameDetail = findViewById<TextView>(R.id.nameDetail)
        val phoneDetail = findViewById<TextView>(R.id.phoneDetail)
        val emailDetail = findViewById<TextView>(R.id.emailDetail)
        val cardEmail = findViewById<View>(R.id.cardEmail)
        val image = findViewById<ImageView>(R.id.backdrop)

        val myContactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, intent.getStringExtra(
            CONTACT_ID))
        val photoStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, myContactUri)
        if (photoStream != null) {
            val buf = BufferedInputStream(photoStream)
            val myBtmp = BitmapFactory.decodeStream(buf)
            image.setImageBitmap(myBtmp)
        }
        else image.setImageResource(R.mipmap.account)

        nameDetail.text = intent.getStringExtra(CONTACT_NAME) ?: ""
        phoneDetail.text = intent.getStringExtra(PHONE_NUMBER) ?: ""
        callButton.setOnClickListener {
            val callUri = Uri.parse("tel:" + phoneDetail.text)
            val intent = Intent(Intent.ACTION_DIAL, callUri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        messageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("smsto:" + Uri.encode(phoneDetail.text.toString()))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        val email = intent.getStringExtra(CONTACT_EMAIL)
        if (email != "")
            emailDetail.text = email
        else
            cardEmail.visibility = View.GONE
    }
}
