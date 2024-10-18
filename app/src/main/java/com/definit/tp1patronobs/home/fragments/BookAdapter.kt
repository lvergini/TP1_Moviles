package com.definit.tp1patronobs.home.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.definit.tp1patronobs.databinding.ItemBookBinding
import com.definit.tp1patronobs.models.Book
import com.bumptech.glide.Glide

class BookAdapter (private val books:List<Book>): RecyclerView.Adapter<BookAdapter.BookViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding=ItemBookBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book=books[position]
        holder.bind(book)
    }

    override fun getItemCount(): Int =books.size

    class BookViewHolder(private val binding: ItemBookBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(book:Book){
            binding.tvBookTitle.text=book.title
            binding.tvBookAuthor.text=book.author
            binding.tvBookGenre.text=book.genre

            Glide.with(binding.root.context).load(book.coverUrl).into(binding.ivBookCover)

        }

    }


}
