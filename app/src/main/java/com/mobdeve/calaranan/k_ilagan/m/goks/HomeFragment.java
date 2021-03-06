package com.mobdeve.calaranan.k_ilagan.m.goks;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

public class HomeFragment extends Fragment {
    public View view;
    public RecyclerView mainRv;
    public ArrayList<Book> bookList;
    public BottomNavigationView navBar;
    public RequestQueue reqQueue;
    public EditText searchEt;
    public ImageView searchBtn;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mainRv = view.findViewById(R.id.mainRv);
        searchEt = view.findViewById(R.id.searchFavTitleEt);
        searchBtn = view.findViewById(R.id.searchLibBtn);
        navBar = view.findViewById(R.id.navBar);
        bookList = new ArrayList<>();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().setBackgroundDrawableResource(R.drawable.bg);
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
        reqQueue = Volley.newRequestQueue(getActivity());
        reqQueue.getCache().clear();    // clear cache

        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=20";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

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

                        JSONObject accessInfo = itemsObj.getJSONObject("accessInfo");
                        JSONObject pdfObj = accessInfo.getJSONObject("pdf");
                        Boolean pdfAvailable = pdfObj.getBoolean("isAvailable");
                        String pdfLink = null;
                        if (pdfAvailable){
                            pdfLink = pdfObj.getString("acsTokenLink");
                        }

                        JSONObject saleInfo = itemsObj.getJSONObject("saleInfo");
                        String buyLink = null;
                        String saleability = null;
                        Boolean onSale = false;
                        saleability = saleInfo.optString("saleability");
                        if (saleability.equals("FOR_SALE") || saleability.equals("FREE") || saleability.equals("FOR_PREORDER")){
                            onSale = true;
                            buyLink = saleInfo.getString("buyLink");
                        }

                        ArrayList<String> authors = new ArrayList<>();

                        if (authorsList.length() != 0) {
                            for (j = 0; j < authorsList.length(); j++) {
                                authors.add(authorsList.optString(j));
                            }
                        }

                        if (pdfAvailable && onSale) {
                            Book books = new Book(id, cover, bookTitle, authors, bookDesc, bookPublisher, publishDate, previewLink, pdfLink, buyLink);
                            bookList.add(books);
                        }
                        else if(onSale){
                            Book books = new Book(id, cover, bookTitle, authors, bookDesc, bookPublisher, publishDate, previewLink, buyLink, onSale);
                            bookList.add(books);
                        }
                        else if (pdfAvailable){
                            Book books = new Book(id, cover, bookTitle, authors, bookDesc, bookPublisher, publishDate, previewLink, pdfLink);
                            bookList.add(books);
                        }
                        else {
                            Book books = new Book(id, cover, bookTitle, authors, bookDesc, bookPublisher, publishDate, previewLink);
                            bookList.add(books);
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        HomeAdapter adapter = new HomeAdapter(bookList, getActivity());
                        mainRv.setLayoutManager(layoutManager);
                        mainRv.setAdapter(adapter);
                    }
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