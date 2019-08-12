package com.namnoit.zcontact

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import java.io.BufferedInputStream


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val w = window
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        val callButton = findViewById<View>(R.id.callButton)
        val messageButton = findViewById<View>(R.id.messageButton)
        val nameDetail = findViewById<TextView>(R.id.nameDetail)
        val phoneDetail = findViewById<TextView>(R.id.phoneDetail)
        val emailDetail = findViewById<TextView>(R.id.emailDetail)
        val image = findViewById<ImageView>(R.id.backdrop)
        val uriStr = intent.getStringExtra(CONTACT_ID)
//        val uri = Uri.parse(uriStr)

        val myContactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, intent.getStringExtra(
            CONTACT_ID))
        val photoStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, myContactUri)
        val buf = BufferedInputStream(photoStream)
        val myBtmp = BitmapFactory.decodeStream(buf)
        image.setImageBitmap(myBtmp)
    }
}
