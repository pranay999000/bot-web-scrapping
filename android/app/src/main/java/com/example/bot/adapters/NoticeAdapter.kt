package com.example.bot.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bot.PdfActivity
import com.example.bot.databinding.ItemNoticeBinding
import com.example.bot.models.Notice

class NoticeAdapter(private  var context: Context, private var list: ArrayList<Notice>):
    RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

        inner class ViewHolder(val binding: ItemNoticeBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.text.text = text
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PdfActivity::class.java)
            intent.putExtra("link", list[position].link)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}