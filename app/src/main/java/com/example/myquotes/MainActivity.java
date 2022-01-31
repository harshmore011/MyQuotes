package com.example.myquotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.objectbox.Box;

public class MainActivity extends AppCompatActivity {

    EditText quoteEt, authorEt;
    Button saveQuoteBtn;
    RecyclerView recyclerView;
    String quoteText, authorText;
    TextView addQuoteLayout;
    ImageButton arrowBtnAddQ, arrowBtnMyQ;

    Box<Quote> quotesBox;

    List<Quote> quotesList = new ArrayList<>();
    QuotesAdapter adapter;

    private final String TAG = "MainActivity";
    CardView saveQuoteLayout;
    private boolean isSearching = false;
//    QuoteDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        addQuoteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (saveQuoteLayout.getVisibility() == View.GONE) {
                    saveQuoteLayout.setVisibility(View.VISIBLE);
                    arrowBtnAddQ.setImageResource(R.drawable.ic_arrow_up_24);
                } else {
                    saveQuoteLayout.setVisibility(View.GONE);
                    arrowBtnAddQ.setImageResource(R.drawable.ic_arrow_down_24);
                }

            }
        });

        findViewById(R.id.textView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (recyclerView.getVisibility() == View.GONE) {
                    recyclerView.setVisibility(View.VISIBLE);
                    arrowBtnMyQ.setImageResource(R.drawable.ic_arrow_up_24);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    arrowBtnMyQ.setImageResource(R.drawable.ic_arrow_down_24);
                }

            }
        });

//        dbHelper = new QuoteDBHelper(MainActivity.this, null, null, 1, quotesList);
        quotesBox = ObjectBox.get().boxFor(Quote.class);

//        try {
//            showQuotes(dbHelper.readQuotesFromDatabase());
        quotesList = quotesBox.getAll();
            showQuotes();

            Toast.makeText(MainActivity.this, "Quote read successful \n" + quotesBox.count() +
                    " Quotes found", Toast.LENGTH_SHORT).show();

//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(MainActivity.this, "Failed to read the Quote", Toast.LENGTH_SHORT).show();
//        }

        saveQuoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateInputs()) {

                    quoteText = quoteEt.getText().toString().trim();
                    authorText = authorEt.getText().toString().trim();

                    quoteEt.setText("");
                    authorEt.setText("");

                     Quote quote = insertQuoteIntoDatabase(quoteText, authorText);

                    quotesList.add(quote);

                    showQuotes();

                } else {
                    Snackbar.make(quoteEt, "Please write a Quote", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    private Quote insertQuoteIntoDatabase(String quoteText, String authorText) {

        Quote quote = new Quote();
        quote.setQuote(quoteText);
        quote.setAuthor(authorText);
        quote.setDate(new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(new Date()));

        try {
//                        dbHelper.insertQuoteInDatabase(quote);

            quotesBox.put(quote);
            Log.d(TAG, "Quote inserted into database with id " + quote.getId());
            Toast.makeText(MainActivity.this, "Quote successfully inserted into database", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Failed to insert the Quote", Toast.LENGTH_SHORT).show();
        }
        return quote;
    }

    private void showQuotes() {
        if (quotesList != null || quotesList.size() > 0) {
            adapter = new QuotesAdapter(MainActivity.this, quotesList);

            if (adapter.getItemCount() > 0) {

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                findViewById(R.id.textView5).setVisibility(View.GONE);
                findViewById(R.id.textView4).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.textView5).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }
        } else {
            findViewById(R.id.textView5).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void initializeViews() {
        quoteEt = findViewById(R.id.quoteText);
        authorEt = findViewById(R.id.authorText);
        saveQuoteBtn = findViewById(R.id.saveQuoteBtn);
        recyclerView = findViewById(R.id.recyclerView);
        addQuoteLayout = findViewById(R.id.addQuoteLayout);
        arrowBtnAddQ = findViewById(R.id.arrowBtn);
        arrowBtnMyQ = findViewById(R.id.arrowBtnMyQuotes);
        saveQuoteLayout = findViewById(R.id.saveQuoteLayout);

    }

    private boolean validateInputs() {
        if (!quoteEt.getText().toString().trim().equals(""))
            return true;
        else
            return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem =  menu.findItem(R.id.searchQuotes);

        if (!searchItem.isActionViewExpanded())
            searchItem.expandActionView();

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                isSearching = true;
                adapter.getFilter().filter(query);
                isSearching  = false;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                isSearching = true;
                adapter.getFilter().filter(newText);
                isSearching = false;
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.deleteAllQuotes) {
            quotesBox.removeAll();
            quotesList.clear();

            Log.d(TAG, "All quotes deleted ");
            Toast.makeText(MainActivity.this, "All quotes are deleted", Toast.LENGTH_SHORT).show();

            showQuotes();
        }

        return super.onOptionsItemSelected(item);
    }
}