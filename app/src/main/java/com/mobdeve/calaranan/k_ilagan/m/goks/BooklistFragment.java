package com.mobdeve.calaranan.k_ilagan.m.goks;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BooklistFragment extends Fragment {
    public View view;
    public RecyclerView wantRv;
    public ArrayList<Book> bookList;
    public ArrayList<String> idList;
    public BottomNavigationView navBar;
    public RequestQueue reqQueue;
    public DatabaseToRead db;

    public BooklistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_booklist, container, false);
        wantRv = view.findViewById(R.id.wantRv);
        navBar = view.findViewById(R.id.navBar);
        db = new DatabaseToRead(getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        bookList = new ArrayList<>();
        idList = new ArrayList<>();
        getToRead(); // get booklist from database
        for(int i = 0; i < idList.size(); i++){
            getBooks(idList.get(i));
        }
    }

    public void getToRead(){
        Cursor cursor = db.getToRead();
        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(), "No favorite books!", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                idList.add(cursor.getString(0));    // getting id list from db
            }
        }
    }

    private void getBooks(String volumeID){
        reqQueue = Volley.newRequestQueue(getActivity());
        reqQueue.getCache().clear();    // clear cache

        String url = "https://www.googleapis.com/books/v1/volumes/" + volumeID;    // get book details based on volume id
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest booksObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // extracting all json data.
                try {
                    String id = response.getString("id");
                    Log.d("volume id", id);
                    //String id = volumeID.optString("id");
                    JSONObject volumeObj = response.getJSONObject("volumeInfo");
                    String bookTitle = volumeObj.optString("title");
                    JSONArray authorsList = volumeObj.getJSONArray("authors");
                    String bookPublisher = volumeObj.optString("publisher");
                    String publishDate = volumeObj.optString("publishedDate");

                    JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                    String cover = imageLinks.optString("thumbnail");

                    ArrayList<String> authors = new ArrayList<>();
                    if (authorsList.length() != 0) {
                        for (int j = 0; j < authorsList.length(); j++) {
                            authors.add(authorsList.optString(j));
                        }
                    }

                    Book books = new Book(id, cover, bookTitle, authors, bookPublisher, publishDate);
                    bookList.add(books);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    ToReadAdapter adapter = new ToReadAdapter(bookList);
                    wantRv.setLayoutManager(layoutManager);
                    wantRv.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // displaying a toast message when we get any error from API
                    Toast.makeText(getActivity(), "No Data Found" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(booksObjReq);
    }
}