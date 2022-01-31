package com.example.myquotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.objectbox.Box;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesViewHolder> implements Filterable {

    private final List<Quote> mQuotesList;
    private List<Quote> mFilteredQuotesList;
    private final Context mContext;
    private Box<Quote> quotesBox;

    public QuotesAdapter (Context context, List<Quote> quotesList) {
        mContext = context;
        mQuotesList = quotesList;
        mFilteredQuotesList = quotesList;

        quotesBox = ObjectBox.get().boxFor(Quote.class);
    }

    @NonNull
    @Override
    public QuotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_quote, parent, false);
        QuotesViewHolder viewHolder = new QuotesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuotesViewHolder holder, int position) {
        holder.quoteEt.setText(new StringBuilder().append("\"").append(mFilteredQuotesList.get(position).getQuote()).append(".\"").toString());
        holder.authorEt.setText(mFilteredQuotesList.get(position).getAuthor());
        holder.dateEt.setText(mFilteredQuotesList.get(position).getDate());

            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.editQuoteEt.setText(holder.quoteEt.getText().toString());
                    holder.editQuoteEt.setVisibility(View.VISIBLE);
                    holder.doneEditQuote.setVisibility(View.VISIBLE);
                    holder.quoteEt.setVisibility(View.INVISIBLE);
                    holder.editBtn.setVisibility(View.INVISIBLE);

                }
            });

            holder.doneEditQuote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!holder.editQuoteEt.getText().toString().equals("")) {
                        String editedQuote = holder.editQuoteEt.getText().toString().trim();

                        holder.doneEditQuote.setVisibility(View.GONE);
                        holder.editQuoteEt.setVisibility(View.INVISIBLE);
                        holder.quoteEt.setVisibility(View.VISIBLE);

                        mFilteredQuotesList.get(holder.getAdapterPosition()).setQuote(editedQuote);
                        mFilteredQuotesList.get(holder.getAdapterPosition()).setDate(new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(new Date()));

                        Quote quote = new Quote();
                        quote.setQuote(mFilteredQuotesList.get(holder.getAdapterPosition()).getQuote());
                        quote.setAuthor(mFilteredQuotesList.get(holder.getAdapterPosition()).getAuthor());
                        quote.setId(mFilteredQuotesList.get(holder.getAdapterPosition()).getId());
                        quote.setDate(mFilteredQuotesList.get(holder.getAdapterPosition()).getDate());

                        quotesBox.put(quote);

                        notifyItemChanged(holder.getAdapterPosition());

                        holder.editBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, "Quote Edited", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.getAdapterPosition() > 0) {
                        quotesBox.remove(mQuotesList.get(holder.getAdapterPosition()).getId());

                        mFilteredQuotesList.remove(holder.getAdapterPosition());

                        notifyDataSetChanged();

                        Toast.makeText(mContext, "Quote Deleted.", Toast.LENGTH_SHORT).show();
                    } else {
                        quotesBox.removeAll();
                        mFilteredQuotesList.clear();
                        notifyDataSetChanged();
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return mFilteredQuotesList.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String textPattern = charSequence.toString().trim().toLowerCase();

                mFilteredQuotesList = new ArrayList<>();

                if (textPattern == null || textPattern.length() == 0) {
                    mFilteredQuotesList.addAll(mQuotesList);
                } else {

                    for (Quote quote : mQuotesList) {
                        if (quote.getQuote().toLowerCase().contains(textPattern) || quote.getAuthor().toLowerCase().contains(textPattern)) {
                            mFilteredQuotesList.add(quote);
                        }
                    }

                }

                FilterResults results = new FilterResults();
                results.values = mFilteredQuotesList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                mFilteredQuotesList = (List<Quote>) filterResults.values;
                notifyDataSetChanged(); // Notify recycler view adapter
            }
        };

        return filter;
    }
}

class QuotesViewHolder extends RecyclerView.ViewHolder {

    TextView quoteEt, authorEt, dateEt;
    EditText editQuoteEt;
    ImageButton editBtn, deleteBtn;
    CardView quotesContainer;
    Button doneEditQuote;

    public QuotesViewHolder(@NonNull View itemView) {
        super(itemView);

        quotesContainer = (CardView) itemView.getRootView();
        quoteEt = itemView.findViewById(R.id.quoteText);
        authorEt = itemView.findViewById(R.id.authorText);
        dateEt = itemView.findViewById(R.id.date);
        editBtn = itemView.findViewById(R.id.editQuote);
        deleteBtn = itemView.findViewById(R.id.deleteQuote);
        editQuoteEt = itemView.findViewById(R.id.quoteEtText);
        doneEditQuote = itemView.findViewById(R.id.doneEditQuote);

    }
}
