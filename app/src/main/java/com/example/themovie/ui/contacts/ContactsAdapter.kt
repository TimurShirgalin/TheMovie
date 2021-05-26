package com.example.themovie.ui.contacts

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.themovie.R
import com.example.themovie.model.Contact
import java.util.*


class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    private var contactsData: MutableList<Contact> = mutableListOf()
    private lateinit var itemClickListener: OnContactClickListener
    private var filterLetters: String? = null

    fun setContactsData(data: MutableList<Contact>, filterLettersData: String?) {
        contactsData = data
        filterLetters = filterLettersData
        notifyDataSetChanged()
    }

    interface OnContactClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    fun setOnItemClickListener(clickListener: OnContactClickListener) {
        itemClickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_contacts_card, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactsAdapter.ViewHolder, position: Int) =
        holder.bind(contactsData[position], itemClickListener)

    override fun getItemCount(): Int = contactsData.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contactName = itemView.findViewById<TextView>(R.id.contact_name)
        private val contactPhoneNumber = itemView.findViewById<TextView>(R.id.contact_phone_number)

        fun bind(contactData: Contact, listener: OnContactClickListener) {
            val searchLength = filterLetters?.length
            SpannableString(contactData.name).apply {
                if (filterLetters != null) {
                    this.forEachIndexed { index, _ ->
                        if (index + searchLength!! <= this.length &&
                            contactData.name!!.toLowerCase(Locale.ROOT)
                                .substring(index, index + searchLength)
                                .contains(filterLetters.toString().toLowerCase(Locale.ROOT))
                        ) {
                            setSpan(
                                ForegroundColorSpan(Color.GREEN),
                                index,
                                index + searchLength,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                    }
                }
                contactName.text = this
            }
            contactData.phoneNumber[0].let {
                if (it == null) contactPhoneNumber.text = "нет данных"
                else contactPhoneNumber.text = it
            }
            itemView.setOnClickListener { listener.onItemClick(itemView, adapterPosition) }
        }
    }
}