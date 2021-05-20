package com.mobdeve.calaranan.k_ilagan.m.goks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
    public RecyclerView.Adapter recyclerViewAdapter;
    public RecyclerView.LayoutManager layoutManager;
    public RequestQueue reqQueue;

    public EditText searchEt;
    public ImageButton searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mainRv = findViewById(R.id.mainRv);
        this.searchEt = findViewById(R.id.searchEt);
        this.searchBtn = findViewById(R.id.searchBtn);
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

        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
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
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");

                        String bookTitle = volumeObj.optString("title");
                        JSONArray authorsList = volumeObj.getJSONArray("authors");
                        Log.d("authorsList", String.valueOf(authorsList));
                        String bookDesc = volumeObj.optString("description");
                        String bookPublisher = volumeObj.optString("publisher");
                        String publishDate = volumeObj.optString("publishedDate");

                        JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                        String cover = imageLinks.optString("thumbnail");
                        Log.d("Cover", cover);
                        String previewLink = volumeObj.optString("previewLink");
                        String infoLink = volumeObj.optString("infoLink");

//                        JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
//                        String buyLink = saleInfoObj.optString("buyLink");

                        ArrayList<String> authors = new ArrayList<>();

                        if (authorsList.length() != 0) {
                            for (j = 0; j < authorsList.length(); j++) {
                                authors.add(authorsList.optString(i));
                            }
                        }
                        // after extracting all the data, save in Book class.
                        Book books = new Book(cover, bookTitle, authors, bookDesc, bookPublisher, publishDate, previewLink, infoLink);
                        bookList.add(books);
                        layoutManager = new LinearLayoutManager(MainActivity.this);
                        recyclerViewAdapter = new BookAdapter(bookList, MainActivity.this);
                        mainRv.setLayoutManager(layoutManager);
                        mainRv.setAdapter(recyclerViewAdapter);
                    }
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
//    public void bookOnClick(String title, ArrayList<String> authors, String publisher, String publishDate, String cover) {
//        Intent i = new Intent(MainActivity.this, BookDetails.class);
//        i.putExtra("title", title);
//        i.putExtra("authors", authors);
//        i.putExtra("publisher", publisher);
//        i.putExtra("publishDate", publishDate);
//        i.putExtra("cover", cover);
//        Log.d("authors", String.valueOf(authors));
//        startActivity(i);
//    }
}