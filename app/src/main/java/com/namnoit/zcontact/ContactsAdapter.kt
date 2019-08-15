package com.namnoit.zcontact

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_contact_row.view.*


const val CONTACT_ID = "com.example.myfirstapp.id"
const val PHONE_NUMBER = "com.example.myfirstapp.number"
const val CONTACT_NAME = "com.example.myfirstapp.name"
const val CONTACT_EMAIL = "com.example.myfirstapp.email"


class ContactsAdapter(
    private var context: Context,
    private var contacts: ArrayList<ContactModel>
) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {
    private val colorSet = arrayListOf(
        Color.parseColor("#ffcdd2"),
        Color.parseColor("#90caf9"),
        Color.parseColor("#a5d6a7"),
        Color.parseColor("#f8bbd0"),
        Color.parseColor("#b39ddb"),
        Color.parseColor("#80deea"),
        Color.parseColor("#e1bee7"),
        Color.parseColor("#9fa8da"),
        Color.parseColor("#ffab91")
    )
    private var expandPosition = RecyclerView.NO_POSITION


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View = layoutInflater.inflate(R.layout.layout_contact_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.textName.text = contacts[position].name
        holder.itemView.phone_expand.text = contacts[position].phone
        if (contacts[position].avatarURI != null)
            holder.itemView.avatar.setImageURI(Uri.parse(contacts[position].avatarURI))
        else
            holder.itemView.avatar.setImageBitmap(
                textAsBitmap(
                    contacts[position].name[0].toString(),
                    80f,
                    pickColor(position)
                )
            )
        if (position == expandPosition)
            holder.itemView.item_expand.visibility = View.VISIBLE
        else
            holder.itemView.item_expand.visibility = View.GONE

        holder.itemView.setOnClickListener {
//            Toast.makeText(context,contacts[position].email,Toast.LENGTH_SHORT).show()
            if (position == expandPosition) {
                notifyItemChanged(position)
                expandPosition = RecyclerView.NO_POSITION
            }
            else {
                notifyItemChanged(expandPosition)
                expandPosition = position
                notifyItemChanged(expandPosition)
            }
        }
        holder.itemView.call_expand.setOnClickListener {
            val callUri = Uri.parse("tel:" + contacts[position].phone)
            val intent = Intent(Intent.ACTION_DIAL, callUri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
        holder.itemView.message_expand.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("smsto:" + Uri.encode(contacts[position].phone))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
        holder.itemView.detail_expand.setOnClickListener {
            val intent = Intent(context,DetailActivity::class.java).apply {
                putExtra(CONTACT_ID,contacts[position].contactID)
                putExtra(CONTACT_NAME,contacts[position].name)
                putExtra(PHONE_NUMBER,contacts[position].phone)
                putExtra(CONTACT_EMAIL,contacts[position].email)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }


    private fun textAsBitmap(text: String, textSize: Float, backgroundColor: Int): Bitmap {
        val paint = Paint(ANTI_ALIAS_FLAG)
        paint.textSize = textSize
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.LEFT
        val baseline = -paint.ascent() // ascent() is negative
        var width = (paint.measureText(text) + 40.0f).toInt() // round
        var height = (baseline + paint.descent() + 40.0f).toInt()

        val trueWidth = width
        if (width > height) height = width else width = height
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(image)
        val paint2 = Paint()
        paint2.color = backgroundColor
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint2)
        canvas.drawText(text, (width / 2 - trueWidth / 2).toFloat() + 20, baseline + 20, paint)
        return image
    }

    private fun pickColor(i: Int): Int {
        return colorSet[i % colorSet.size]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}