package com.mobdeve.calaranan.k_ilagan.m.goks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewBookActivity extends AppCompatActivity {
    public ImageView bookCover, back, saveBook;
    public TextView bookTitle, bookAuthor, bookPublisher, bookDate, bookDesc;
    public Button previewBtn, toReadBtn, downloadBtn, buyBtn;

    public String bookID, cover, title, description, publisher, publishedDate, previewLink, pdfLink, buyLink;
    public ArrayList<String> authors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        this.saveBook = findViewById(R.id.saveBook);
        this.back = findViewById(R.id.back);
        this.bookCover = findViewById(R.id.bookCover);
        this.bookTitle = findViewById(R.id.bookTitle);
        this.bookAuthor = findViewById(R.id.bookAuthor);
        this.bookPublisher = findViewById(R.id.bookPublisher);
        this.bookDate = findViewById(R.id.bookDate);
        this.bookDesc = findViewById(R.id.bookDesc);
        this.previewBtn = findViewById(R.id.previewBtn);
        this.toReadBtn = findViewById(R.id.toReadBtn);
        this.downloadBtn = findViewById(R.id.downloadBtn);
        this.buyBtn = findViewById(R.id.buyBtn);

        Intent intent = getIntent();
        bookID = intent.getStringExtra("id");
        cover = intent.getStringExtra("cover");
        title = intent.getStringExtra("title");
        authors = intent.getStringArrayListExtra("authors");
        String authorsList = authors.toString();
        authorsList = authorsList.substring(1, authorsList.length() - 1);   // removing [ ] from the author display
        description = intent.getStringExtra("desc");
        publisher = intent.getStringExtra("publisher");
        publishedDate = intent.getStringExtra("publishDate");
        previewLink = intent.getStringExtra("preview");
        pdfLink = intent.getStringExtra("pdf");
        buyLink = intent.getStringExtra("buy");

        // setting data to views
        Picasso.get().load(cover).resize(350, 530).into(bookCover);
        bookTitle.setText(title);
        bookAuthor.setText(authorsList);
        bookPublisher.setText(publisher);
        bookDate.setText(publishedDate);
        bookDesc.setText(description);
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewBookActivity.super.onBackPressed();
            }
        });

        this.saveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseFavorites db = new DatabaseFavorites(ViewBookActivity.this);
                db.saveBook(bookID, title);
            }
        });

        this.previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previewLink.isEmpty()) {
                    // below toast message is displayed when preview link is not present.
                    Toast.makeText(ViewBookActivity.this, "There is no preview for this book!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri uri = Uri.parse(previewLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        this.toReadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseBooklist db = new DatabaseBooklist(ViewBookActivity.this);
                db.AddToRead(bookID, title);
            }
        });

        this.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdfLink == null) {
                    // below toast message is displayed when preview link is not present.
                    Toast.makeText(ViewBookActivity.this, "There is no ACS token for this book!", Toast.LENGTH_LONG).show();
                }
                else if(pdfLink != null) {
                    Uri uri = Uri.parse(pdfLink);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(i);
                }
            }
        });

        this.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyLink == null || buyLink.equals("NOT_FOR_SALE")) {
                    // below toast message is displayed when preview link is not present.
                    Toast.makeText(ViewBookActivity.this, "Book is not available for sale!", Toast.LENGTH_LONG).show();
                }
                else if(buyLink != null) {
                    Uri uri = Uri.parse(buyLink);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(i);
                }
            }
        });
    }
}