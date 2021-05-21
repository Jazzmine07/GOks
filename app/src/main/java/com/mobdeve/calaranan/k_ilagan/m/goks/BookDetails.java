package com.mobdeve.calaranan.k_ilagan.m.goks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookDetails extends AppCompatActivity {
    public ImageView bookCover;
    public TextView bookTitle;
    public TextView bookAuthor;
    public TextView bookDesc;
//    public TextView publisherTV;
//    public TextView publishDateTV;
    public Button readBtn, buyBtn;

    public String cover;
    public String title;
    public ArrayList<String> authors;
    public String description;
    public String publisher;
    public String publishedDate;
    public String readLink;
    public String infoLink;
    public String buyLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.bookCover = findViewById(R.id.bookCover);
        this.bookTitle = findViewById(R.id.bookTitle);
        this.bookAuthor = findViewById(R.id.bookAuthor);
        this.bookDesc = findViewById(R.id.bookDesc);
        this.readBtn = findViewById(R.id.previewBtn);
        this.buyBtn = findViewById(R.id.infoBtn);

        Intent intent = getIntent();
        cover = intent.getStringExtra("cover");
        title = intent.getStringExtra("title");
        authors = intent.getStringArrayListExtra("authors");
        description = intent.getStringExtra("desc");
        publisher = intent.getStringExtra("publisher");
        publishedDate = intent.getStringExtra("publishDate");
        readLink = intent.getStringExtra("preview");
        buyLink = intent.getStringExtra("buy");

        // setting data to views
        Picasso.get().load(cover).into(bookCover);
        bookTitle.setText(title);
        bookAuthor.setText(authors.toString());
        bookDesc.setText(description);
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readLink.isEmpty()) {
                    // below toast message is displayed when preview link is not present.
                    Toast.makeText(BookDetails.this, "There is no preview for this book!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri uri = Uri.parse(readLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        this.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyLink.isEmpty()) {
                    Toast.makeText(BookDetails.this, "There is no buy link for this book!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri uri = Uri.parse(buyLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });
    }
}
