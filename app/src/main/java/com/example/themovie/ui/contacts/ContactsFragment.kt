package com.example.themovie.ui.contacts

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themovie.databinding.FragmentContactsBinding
import com.example.themovie.model.Contact
import java.util.*
import kotlin.collections.HashMap


class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val contactsData: MutableList<Contact> = mutableListOf()
    private val filteredContactData: MutableList<Contact> = mutableListOf()
    private val adapter = ContactsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
        textInputEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) = setFilter(editable.toString())
        })
    }

    private fun setFilter(textFilter: String) {
        filteredContactData.clear()
        contactsData.forEach {
            if (it.name != null && it.name.toLowerCase(Locale.ROOT).contains(textFilter.toLowerCase(
                    Locale.ROOT))
            )
                filteredContactData.add(Contact(it.name, it.phoneNumber))
        }
        adapter.setContactsData(filteredContactData, textFilter)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray,
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getContacts()
                } else {
                    context?.let {
                        AlertDialog.Builder(it)
                            .setTitle("Доступ к контактам")
                            .setMessage("Объяснение")
                            .setNegativeButton("Закрыть") { dialog, _ -> dialog.dismiss() }
                            .create()
                            .show()
                    }
                }
                return
            }
        }
    }

    private fun checkPermissions() {
        context?.let {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) -> {
                    getContacts()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun getContacts() {
        context?.let { context ->
            val projection = arrayOf(CommonDataKinds.Phone.DISPLAY_NAME,
                CommonDataKinds.Phone.NUMBER)
            val cursorWithContacts: Cursor? = context.contentResolver.query(
                CommonDataKinds.Phone.CONTENT_URI,
                projection,
                null,
                null,
                null
            )

            val contacts: HashMap<String, Contact> = HashMap()
            cursorWithContacts?.let { cursor ->
                while (cursor.moveToNext()) {
                    val name: String = cursor.getString(0)
                    val phone: String = cursor.getString(1)
                    if (contacts[name] == null) contacts[name] = Contact(name, mutableListOf())
                    contacts[name]?.phoneNumber?.add(phone)
                }
            }
            cursorWithContacts?.close()
            for (data in contacts) contactsData.add(Contact(data.key, data.value.phoneNumber))
            contactsData.sortedBy { it.name }
            initRecyclerView(contactsData)
        }
    }

    private fun initRecyclerView(contactsData: MutableList<Contact>) = with(binding) {
        val recyclerView: RecyclerView = recyclerContacts
        recyclerView.setHasFixedSize(true)
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).let {
            recyclerView.layoutManager = it
        }
        recyclerView.adapter = adapter
        adapter.setContactsData(contactsData, null)
        adapter.setOnItemClickListener(object : ContactsAdapter.OnContactClickListener {
            override fun onItemClick(view: View?, position: Int) {
                startActivity(
                    Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:${filteredContactData[position].phoneNumber[0]}"))
                )
            }
        })
    }

    private fun requestPermission() =
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val REQUEST_CODE = 42

        @JvmStatic
        fun newInstance() = ContactsFragment()
    }
}