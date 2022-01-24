package com.example.myquotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.objectbox.Box;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesViewHolder> {

    private final List<Quote> mQuotesList;
    private final Context mContext;
    private Box<Quote> quotesBox;

    public QuotesAdapter (Context context, List<Quote> quotesList) {
        mContext = context;
        mQuotesList = quotesList;

        quotesBox = ObjectBox.getInstance().boxFor(Quote.class);
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
        holder.quoteEt.setText(new StringBuilder().append("\"").append(mQuotesList.get(position).getQuote()).append(".\"").toString());
        holder.authorEt.setText(mQuotesList.get(position).getAuthor());
        holder.dateEt.setText(mQuotesList.get(position).getDate());

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

                        mQuotesList.get(holder.getAdapterPosition()).setQuote(editedQuote);
                        mQuotesList.get(holder.getAdapterPosition()).setDate(new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(new Date()));

                        Quote quote = new Quote();
                        quote.setQuote(mQuotesList.get(holder.getAdapterPosition()).getQuote());
                        quote.setAuthor(mQuotesList.get(holder.getAdapterPosition()).getAuthor());
                        quote.setId(mQuotesList.get(holder.getAdapterPosition()).getId());
                        quote.setDate(mQuotesList.get(holder.getAdapterPosition()).getDate());

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

                        mQuotesList.remove(holder.getAdapterPosition());

                        notifyDataSetChanged();

                        Toast.makeText(mContext, "Quote Deleted.", Toast.LENGTH_SHORT).show();
                    } else {
                        quotesBox.removeAll();
                        mQuotesList.clear();
                        notifyDataSetChanged();
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return mQuotesList.size();
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
