
package com.safe.eldershield.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.safe.eldershield.R
import com.safe.eldershield.core.ScanItem
import com.safe.eldershield.core.Store

class ReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        val list = findViewById<RecyclerView>(R.id.list)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = ScanAdapter(Store.results.toList())
    }

    class ScanAdapter(private val items: List<ScanItem>) :
        RecyclerView.Adapter<ScanAdapter.VH>() {

        class VH(v: View) : RecyclerView.ViewHolder(v) {
            val title: TextView = v.findViewById(android.R.id.text1)
            val subtitle: TextView = v.findViewById(android.R.id.text2)
            val spam: Button = v.findViewById(android.R.id.button1)
            val notSpam: Button = v.findViewById(android.R.id.button2)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(
                android.R.layout.simple_list_item_2, parent, false
            )
            // add buttons programmatically
            val container = View.inflate(parent.context, android.R.layout.simple_list_item_2, null)
            val root = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_2, parent, false) as ViewGroup
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_feedback, parent, false)
            return VH(view)
        }

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            val it = items[position]
            holder.title.text = "[${it.type}] ${it.preview}"
            holder.subtitle.text = "From: ${it.from ?: "unknown"}   Score: ${it.score}"
            holder.spam.setOnClickListener { _ -> it.userLabel = "spam" }
            holder.notSpam.setOnClickListener { _ -> it.userLabel = "not_spam" }
        }
    }
}
