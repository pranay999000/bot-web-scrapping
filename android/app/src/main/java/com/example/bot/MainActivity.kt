package com.example.bot

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bot.adapters.NoticeAdapter
import com.example.bot.databinding.ActivityMainBinding
import com.example.bot.models.Notice
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var adapter: NoticeAdapter
    private var list: ArrayList<Notice> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = NoticeAdapter(this, list)
        binding.mainRecycler.layoutManager = linearLayoutManager
        binding.mainRecycler.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()

        binding.progressCircular.isVisible = true

        val noticeListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()

                for (dataSnapshot in snapshot.children) {
                    list.add(Notice(
                        dataSnapshot.child("text").value.toString(),
                        dataSnapshot.child("link").value.toString()
                    ))
                }

                val comparator = TextComparator()
                list.sortWith(comparator)

                binding.progressCircular.isVisible = false
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        if (list.isEmpty()) {
            databaseReference.child("notice").addValueEventListener(noticeListener)
        } else {
            binding.progressCircular.isVisible = false
            adapter.notifyDataSetChanged()
        }
    }
}

class TextComparator: Comparator<Notice> {
    override fun compare(p0: Notice?, p1: Notice?): Int {
        if (p0 == null || p1 == null) return 0;
        return p1.link.compareTo(p0.link)
    }
}