package com.example.testfirestore

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class AdapterFirestore(options: FirestoreRecyclerOptions<Quote>) : FirestoreRecyclerAdapter<Quote, AdapterFirestore.QuoteViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        // Inflate layout item_quote (assume you have an XML layout file named item_quote.xml)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false)
        return QuoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int, model: Quote) {
        // Bind the Quote object to the ViewHolder
        holder.quoteTextView.text = model.quote
        holder.authorTextView.text = model.nama

        holder.bind(model, snapshots.getSnapshot(position).id)
    }

    inner class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quoteTextView: TextView = itemView.findViewById(R.id.textQuote)
        val authorTextView: TextView = itemView.findViewById(R.id.textNama)

        fun bind(quote: Quote,documentId: String) {
            quoteTextView.text = quote.quote
            authorTextView.text = quote.nama

            // Set onClickListener for the itemView
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("DOCUMENT_ID", documentId)
                itemView.context.startActivity(intent)
            }
        }
    }
}
