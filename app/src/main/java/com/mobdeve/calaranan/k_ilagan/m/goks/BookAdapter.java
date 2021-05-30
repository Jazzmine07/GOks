package com.mobdeve.calaranan.k_ilagan.m.goks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    public ArrayList<Book> bookList;
    public Context activity;

    public BookAdapter(ArrayList<Book> bList, Context context){   // getting bookList to the adapter (constructor)
        this.bookList = bList;
        this.activity = context;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout bookLayout;
        public TextView bookTitle, bookAuthor, bookPublisher, publishDate;
        public ImageView bookCover;

        public BookViewHolder(View itemView) {
            super(itemView);
            bookLayout = itemView.findViewById(R.id.bookLayout);
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

        String bookURL;
        bookURL = book.getBookCover().replace("http:", "https:");
        Picasso.get().load(bookURL).resize(80, 100).into(holder.bookCover); // loading image from URL

        holder.bookTitle.setText(book.getBookTitle());
        String authors = book.getAuthors().toString();
        authors = authors.substring(1, authors.length() - 1);   // removing [ ] from the author display
        holder.bookAuthor.setText(authors);
        holder.bookPublisher.setText(book.getBookPublisher());
        holder.publishDate.setText(book.getPublishDate());

        holder.bookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ViewBookActivity.class);
                i.putExtra("id", book.getBookID());
                i.putExtra("cover", bookURL);
                i.putExtra("title", book.getBookTitle());
                i.putExtra("authors", book.getAuthors());
                i.putExtra("desc", book.getBookDesc());
                i.putExtra("publisher", book.getBookPublisher());
                i.putExtra("publishDate", book.getPublishDate());
                i.putExtra("preview", book.getPreviewLink());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

}
