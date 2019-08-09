package com.namnoit.zcontact

import android.content.Context
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


class ContactsAdapter(
    private var context: Context,
    private var contacts: ArrayList<ContactModel>
) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {
    private val colorSet = arrayListOf(
        Color.parseColor("#ffcdd2"),
        Color.parseColor("#f8bbd0"),
        Color.parseColor("#e1bee7"),
        Color.parseColor("#b39ddb"),
        Color.parseColor("#9fa8da"),
        Color.parseColor("#90caf9"),
        Color.parseColor("#80deea"),
        Color.parseColor("#a5d6a7"),
        Color.parseColor("#ffab91")
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View = layoutInflater.inflate(R.layout.layout_contact_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.textName.text = contacts[position].name
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

        holder.itemView.setOnClickListener {
            holder.itemView.phone_expand.text = contacts[position].phone
            holder.itemView.item_expand.visibility =
                if (holder.itemView.item_expand.visibility == View.GONE) View.VISIBLE else View.GONE
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    private fun createImage(width: Int, height: Int, color: Int, name: String): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint2 = Paint()
        paint2.color = color
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint2)
        val paint = Paint()
        paint.color = Color.WHITE
        paint.textSize = 72f
        paint.textScaleX = 1f
        canvas.drawText(name, 75f - 25f, 75f + 20f, paint)
        return bitmap


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