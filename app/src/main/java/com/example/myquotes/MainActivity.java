package com.example.myquotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    String quoteText, authorText, date;

    Box<Quote> quotesBox;

    List<Quote> quotesList = new ArrayList<>();
//    QuoteDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        dbHelper = new QuoteDBHelper(MainActivity.this, null, null, 1, quotesList);
        quotesBox = ObjectBox.get().boxFor(Quote.class);

        try {
//            showQuotes(dbHelper.readQuotesFromDatabase());
//            showQuotes(quotesBox.getAll());

            Toast.makeText(MainActivity.this, "Quote read successful ", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Failed to read the Quote", Toast.LENGTH_SHORT).show();
        }
        initializeViews();

        saveQuoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateInputs()) {

                    quoteText = quoteEt.getText().toString().trim();
                    authorText = authorEt.getText().toString().trim();

                    quoteEt.setText("");
                    authorEt.setText("");

                    Quote quote = new Quote();
                    quote.setQuote(quoteText);
                    quote.setAuthor(authorText);
                    quote.setDate(new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(new Date()));

                    try {
//                        dbHelper.insertQuoteInDatabase(quote);

                        quotesBox.put(quote);

                        Toast.makeText(MainActivity.this, "Quote successfully inserted into database", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Failed to insert the Quote", Toast.LENGTH_SHORT).show();
                    }
                    quotesList.add(quote);


                    showQuotes(quotesList);

                } else {
                    Snackbar.make(quoteEt, "Please write a Quote", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void showQuotes(List<Quote> quotesList) {
        if (quotesList.size() > 0) {
            QuotesAdapter adapter = new QuotesAdapter(MainActivity.this, quotesList);

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
    }

    private boolean validateInputs() {
        if (!quoteEt.getText().toString().trim().equals(""))
            return true;
        else
            return false;
    }
}