package com.example.myquotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
@Deprecated
public class QuoteDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "QuotesDatabase.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "quotes";

    private static final String QUOTES_COL = "Quote";
    private static final String NUMBER_COL = "Number";
    private static final String AUTHOR_COL = "Author";
    private static final String DATE_COL = "Date";

    private ArrayList<Quote> mQuotesArrayList;
    Context mContext;


    public QuoteDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, ArrayList<Quote> quoteArrayList) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
        mQuotesArrayList = quoteArrayList;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ( "
                + NUMBER_COL + " INT PRIMARY KEY AUTOINCREMENT, "
                + QUOTES_COL + " TEXT, "
                + AUTHOR_COL + " TEXT, "
                + DATE_COL + " TEXT ); " ;

        sqLiteDatabase.execSQL(createTableQuery);
    }

    public void insertQuoteInDatabase(Quote quote) {

        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUOTES_COL, quote.getQuote());
        values.put(AUTHOR_COL, quote.getAuthor());
        values.put(DATE_COL, quote.getDate());

        database.insert(TABLE_NAME, null, values);
    }

    public void deleteQuoteFromDatabase() {
        SQLiteDatabase database = getWritableDatabase();
//        database.delete(TABLE_NAME, , );
    }

    public ArrayList<Quote> readQuotesFromDatabase() {

        SQLiteDatabase database = getReadableDatabase();

        String readQuery = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = database.rawQuery(readQuery, null);

        ArrayList<Quote> quoteArrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                quoteArrayList.add(new Quote(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)));
            } while (cursor.moveToNext());

            cursor.close();

        }
        return quoteArrayList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
