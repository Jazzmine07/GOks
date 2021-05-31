package com.mobdeve.calaranan.k_ilagan.m.goks;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class BooklistFragment extends Fragment {
    public View view;
    public RecyclerView wantRv;
    public BooklistAdapter adapter;
    public ArrayList<Book> bookList;
    public ArrayList<String> idList;
    public ArrayList<String> searchIdList;
    public String bookID;
    public Book book;
    public EditText searchTitleEt;
    public FrameLayout searchList;
    public BottomNavigationView navBar;
    public RequestQueue reqQueue;
    public DatabaseBooklist db;

    public BooklistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_booklist, container, false);
        wantRv = view.findViewById(R.id.wantRv);
        searchTitleEt = view.findViewById(R.id.searchFavTitleEt);
        searchList = view.findViewById(R.id.searchList);
        navBar = view.findViewById(R.id.navBar);
        db = new DatabaseBooklist(getActivity());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallBack);
        itemTouchHelper.attachToRecyclerView(wantRv);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.searchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookList = new ArrayList<>();
                searchIdList = new ArrayList<>();
                if (searchTitleEt.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter book title!", Toast.LENGTH_SHORT).show();
                }
                searchBook(searchTitleEt.getText().toString());
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
        getToRead(); // get booklist from database
        for(int i = 0; i < idList.size(); i++){
            getBooks(idList.get(i));
        }
    }

    public void getToRead(){
        Cursor cursor = db.getToRead();
        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(), "No book/s found in booklist!", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                idList.add(cursor.getString(0));    // getting id list from db
            }
        }
    }

    public void searchBook(String title){
        Cursor cursor = db.searchBook(title);
        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(), "No books in booklist!", Toast.LENGTH_SHORT).show();
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

                    ArrayList<String> authors = new ArrayList<>();

                    if (authorsList.length() != 0) {
                        for (int j = 0; j < authorsList.length(); j++) {
                            authors.add(authorsList.optString(j));
                        }
                    }

                    if (pdfAvailable){
                        Book books = new Book(id, cover, bookTitle, authors, bookDesc, bookPublisher, publishDate, previewLink, pdfLink);
                        bookList.add(books);
                    }
                    else {
                        Book books = new Book(id, cover, bookTitle, authors, bookDesc, bookPublisher, publishDate, previewLink);
                        bookList.add(books);
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    adapter = new BooklistAdapter(bookList, getActivity());
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

    ItemTouchHelper.SimpleCallback simpleCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            bookID = idList.get(position);
            book = bookList.get(position);
            bookList.remove(position);
            idList.remove(position);
            adapter.notifyItemRemoved(position);
            db.removeBook(bookID);

            Snackbar.make(wantRv, book.getBookTitle() + " removed from booklist", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
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