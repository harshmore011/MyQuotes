package com.example.myquotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesViewHolder> {

    private final List<Quote> mQuotesList;
    private final Context mContext;

    public QuotesAdapter (Context context, List<Quote> quotesList) {
        mContext = context;
        mQuotesList = quotesList;
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
                    Toast.makeText(mContext, "Edit Clicked.", Toast.LENGTH_SHORT).show();

                }
            });

            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.quotesContainer.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Quote Deleted.", Toast.LENGTH_SHORT).show();
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
    ImageButton editBtn, deleteBtn;
    CardView quotesContainer;

    public QuotesViewHolder(@NonNull View itemView) {
        super(itemView);

        quotesContainer = (CardView) itemView.getRootView();
        quoteEt = itemView.findViewById(R.id.quoteText);
        authorEt = itemView.findViewById(R.id.authorText);
        dateEt = itemView.findViewById(R.id.date);
        editBtn = itemView.findViewById(R.id.editQuote);
        deleteBtn = itemView.findViewById(R.id.deleteQuote);

    }
}
