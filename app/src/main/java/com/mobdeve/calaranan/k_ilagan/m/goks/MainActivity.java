package com.mobdeve.calaranan.k_ilagan.m.goks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public ArrayList<Book> bookList;
    public RecyclerView mainRv;
    //public BookAdapter adapter;
    //public RecyclerView.LayoutManager layoutManager;
    public RequestQueue reqQueue;

    public EditText searchEt;
    public ImageView searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mainRv = findViewById(R.id.mainRv);
        this.searchEt = findViewById(R.id.searchEt);
        this.searchBtn = findViewById(R.id.searchBtn);

        bookList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchEt.getText().toString().isEmpty()) {
                    searchEt.setError("Please enter search query");
                    return;
                }
                searchResults(searchEt.getText().toString());
            }
        });
    }

    private void searchResults(String query){
        bookList = new ArrayList<>();
        reqQueue = Volley.newRequestQueue(MainActivity.this);
        reqQueue.getCache().clear();    // clear cache

        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=20";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest booksObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // extracting all json data.
                try {
                    JSONArray itemsList = response.getJSONArray("items");
                    int i, j;
                    for (i = 0; i < itemsList.length(); i++) {
                        JSONObject itemsObj = itemsList.getJSONObject(i);
                        String id = itemsObj.optString("id");
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");

                        String bookTitle = volumeObj.optString("title");
                        JSONArray authorsList = volumeObj.getJSONArray("authors");
                        String bookDesc = volumeObj.optString("description");
                        String bookPublisher = volumeObj.optString("publisher");
                        String publishDate = volumeObj.optString("publishedDate");

                        JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                        String cover = imageLinks.optString("thumbnail");
                        String previewLink = volumeObj.optString("previewLink");
                        String infoLink = volumeObj.optString("infoLink");

//                        JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
//                        String buyLink = saleInfoObj.optString("buyLink");

                        ArrayList<String> authors = new ArrayList<>();

                        if (authorsList.length() != 0) {
                            for (j = 0; j < authorsList.length(); j++) {
                                authors.add(authorsList.optString(j));
                            }
                        }
                        //Log.d("authors", String.valueOf(authors));
                        // after extracting all the data, save in Book class.
                        Book books = new Book(id, cover, bookTitle, authors, bookDesc, bookPublisher, publishDate, previewLink, infoLink);
                        bookList.add(books);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                        BookAdapter adapter = new BookAdapter(bookList, MainActivity.this);
                        mainRv.setLayoutManager(layoutManager);
                        mainRv.setAdapter(adapter);

//                        layoutManager = new LinearLayoutManager(MainActivity.this);
//                        adapter = new BookAdapter(bookList, this);
//                        mainRv.setLayoutManager(layoutManager);
//                        mainRv.setAdapter(adapter);
                    }
                    //recyclerViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    // displaying a toast message when we get any error from API
                    Toast.makeText(MainActivity.this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(booksObjReq);
    }

//    @Override
//    public void viewDetailsClick(int position) {
//        Book bList = bookList.get(position);
//
//        Intent i = new Intent(MainActivity.this, BookDetails.class);
//        i.putExtra("cover", bList.getBookCover());
//        i.putExtra("title", bList.getBookTitle());
//        i.putExtra("authors", bList.getAuthors());
//        i.putExtra("desc", bList.getBookDesc());
//        i.putExtra("publisher", bList.getBookPublisher());
//        i.putExtra("publishDate", bList.getPublishDate());
//
//        i.putExtra("preview", bList.getPreviewLink());
//        i.putExtra("info", bList.getInfoLink());
//        startActivity(i);
//    }
}