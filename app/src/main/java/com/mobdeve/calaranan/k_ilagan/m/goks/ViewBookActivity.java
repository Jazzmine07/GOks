package com.mobdeve.calaranan.k_ilagan.m.goks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewBookActivity extends AppCompatActivity {
    public ImageView bookCover;
    public TextView bookTitle, bookAuthor, bookPublisher, bookDate, bookDesc;
    //    public TextView publisherTV;
//    public TextView publishDateTV;
    public Button previewBtn, buyBtn;

    public String cover, title, description, publisher, publishedDate, previewLink, infoLink, buyLink;
    public ArrayList<String> authors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        this.bookCover = findViewById(R.id.bookCover);
        this.bookTitle = findViewById(R.id.bookTitle);
        this.bookAuthor = findViewById(R.id.bookAuthor);
        this.bookPublisher = findViewById(R.id.bookPublisher);
        this.bookDate = findViewById(R.id.bookDate);
        this.bookDesc = findViewById(R.id.bookDesc);
        this.previewBtn = findViewById(R.id.previewBtn);
        this.buyBtn = findViewById(R.id.infoBtn);

        Log.d("bookTitle details.java", String.valueOf(bookTitle));
        Intent intent = getIntent();
        cover = intent.getStringExtra("cover");
        title = intent.getStringExtra("title");
        authors = intent.getStringArrayListExtra("authors");
        String authorsList = authors.toString();
        authorsList = authorsList.substring(1, authorsList.length() - 1);   // removing [ ] from the author display
        description = intent.getStringExtra("desc");
        publisher = intent.getStringExtra("publisher");
        publishedDate = intent.getStringExtra("publishDate");
        previewLink = intent.getStringExtra("preview");
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

        this.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buyLink.isEmpty()) {
                    Toast.makeText(ViewBookActivity.this, "There is no buy link for this book!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri uri = Uri.parse(buyLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });
    }
}