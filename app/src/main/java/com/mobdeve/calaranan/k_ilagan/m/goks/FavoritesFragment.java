package com.mobdeve.calaranan.k_ilagan.m.goks;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    public View view;
    public RecyclerView favRv;
    public FavoriteAdapter adapter;
    public ArrayList<Book> bookList;
    public ArrayList<String> idList;
    public ArrayList<String> searchIdList;
    public String bookID;
    public Book book;
    public EditText searchFavTitleEt;
    public FrameLayout searchFavorite;
    public BottomNavigationView navBar;
    public RequestQueue reqQueue;
    public DatabaseFavorites db;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favorites, container, false);
        favRv = view.findViewById(R.id.favRv);
        searchFavTitleEt = view.findViewById(R.id.searchFavTitleEt);
        searchFavorite = view.findViewById(R.id.searchFavorite);
        navBar = view.findViewById(R.id.navBar);
        db = new DatabaseFavorites(getActivity());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallBack);
        itemTouchHelper.attachToRecyclerView(favRv);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.searchFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookList = new ArrayList<>();
                searchIdList = new ArrayList<>();
                if (searchFavTitleEt.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter book title!", Toast.LENGTH_SHORT).show();
                }
                searchBook(searchFavTitleEt.getText().toString());
                for(int i = 0; i < searchIdList.size(); i++){
                    getBooks(searchIdList.get(i));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        bookList = new ArrayList<>();
        idList = new ArrayList<>();
        getFavorites(); // get favorites from database
        for(int i = 0; i < idList.size(); i++){
            getBooks(idList.get(i));
        }
    }

    public void getFavorites(){
        Cursor cursor = db.getFavoriteBooks();
        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(), "No favorite books!", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                idList.add(cursor.getString(0));    // getting id list from db
            }
        }
    }

    public void searchBook(String title){
        Cursor cursor = db.searchBook(title);
        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(), "No books in favorites!", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                searchIdList.add(cursor.getString(0));    // getting id list from db based on search title
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

                    JSONObject volumeObj = response.getJSONObject("volumeInfo");
                    String bookTitle = volumeObj.optString("title");
                    JSONArray authorsList = volumeObj.getJSONArray("authors");
                    String bookDesc = volumeObj.optString("description");
                    String bookPublisher = volumeObj.optString("publisher");
                    String publishDate = volumeObj.optString("publishedDate");

                    JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                    String cover = imageLinks.optString("thumbnail");
                    String previewLink = volumeObj.optString("previewLink");

                    JSONObject accessInfo = response.getJSONObject("accessInfo");
                    JSONObject pdfObj = accessInfo.getJSONObject("pdf");
                    Boolean pdfAvailable = pdfObj.getBoolean("isAvailable");
                    String pdfLink = null;
                    if (pdfAvailable){
                        pdfLink = pdfObj.getString("acsTokenLink");
                    }

                    JSONObject saleInfo = response.getJSONObject("saleInfo");
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
                        for (int j = 0; j < authorsList.length(); j++) {
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
                    adapter = new FavoriteAdapter(bookList, getActivity());
                    favRv.setLayoutManager(layoutManager);
                    favRv.setAdapter(adapter);
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

    ItemTouchHelper.SimpleCallback simpleCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            bookID = idList.get(position);  // string
            book = bookList.get(position);  // class BOOK
            Log.d("book on swipe", String.valueOf(book));
            bookList.remove(position);
            idList.remove(position);
            Log.d("position", String.valueOf(position));
            adapter.notifyItemRemoved(position);
            db.removeBook(bookID);

            Snackbar.make(favRv, book.getBookTitle() + " removed from favorites", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookList.add(position, book);
                    idList.add(position, bookID);
                    adapter.notifyItemInserted(position);
                    db.undoRemove(bookID, book.getBookTitle());
                }
            }).show();
        }
    };

}