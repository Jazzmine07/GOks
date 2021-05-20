package com.mobdeve.calaranan.k_ilagan.m.goks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    public ArrayList<Book> bookList;
    Context activity;

    public BookAdapter(ArrayList<Book> bList, Context context){   // getting bookList to the adapter (constructor)
        bookList = bList;
        activity = context;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView bookTitle, bookAuthor, bookPublisher, publishDate;
        public ImageView bookCover;

        public BookViewHolder(View itemView) { // book desc
            super(itemView);
            bookCover = itemView.findViewById(R.id.picture);
            bookTitle = itemView.findViewById(R.id.title);
            bookAuthor = itemView.findViewById(R.id.author);
            bookPublisher = itemView.findViewById(R.id.publisher);
            publishDate = itemView.findViewById(R.id.date);

        }
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_layout, parent, false);
        BookViewHolder bookViewHolder = new BookViewHolder(view);
        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        Log.d("thumbnnail adapter", book.getBookCover());
        Picasso.get().load(book.getBookCover()).into(holder.bookCover); // loading image from URL
        holder.bookTitle.setText(book.getBookTitle());
        holder.bookAuthor.setText(book.getAuthors().toString());
        holder.bookPublisher.setText(book.getBookPublisher());
        holder.publishDate.setText(book.getPublishDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, BookDetails.class);
                i.putExtra("cover", book.getBookCover());
                i.putExtra("title", book.getBookTitle());
                i.putExtra("authors", book.getAuthors());
                i.putExtra("desc", book.getBookDesc());
                i.putExtra("publisher", book.getBookPublisher());
                i.putExtra("publishDate", book.getPublishDate());

                i.putExtra("preview", book.getPreviewLink());
                //i.putExtra("info", book.getInfoLink());
                i.putExtra("buy", book.getBuyLink());

                Log.d("authors", String.valueOf(book.getAuthors()));
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
