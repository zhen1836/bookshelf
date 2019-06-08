package com.example.ruanjian.bookshelf.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ruanjian.bookshelf.Entity.Book;
import com.example.ruanjian.bookshelf.R;

import com.klinker.android.sliding.SlidingActivity;

public class DetailActivity extends SlidingActivity {
    private Book book_clicked;

    @Override
    public void init(Bundle savedInstanceState) {
        book_clicked = (Book) getIntent().getSerializableExtra("book");
        //String s1 = getIntent().getStringExtra("title");
        setTitle(book_clicked.getTitle());
        setPrimaryColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );
        setContent(R.layout.activity_detail);
        setHeaderContent(R.layout.activity_detail_header);
        setFab(ContextCompat.getColor(this, R.color.colorAccent), R.drawable.ic_edit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(DetailActivity.this, EditActivity.class);
                        //Bundle bundle = new Bundle();
                        intent.putExtra("book",book_clicked);
                        startActivity(intent);
                    }
                }
        );
        setdetailInfo();
    }

    void setdetailInfo(){
        RelativeLayout author = (RelativeLayout) findViewById(R.id.author_item);
        TextView authortext = (TextView) findViewById(R.id.author_text);
        RelativeLayout translator = (RelativeLayout) findViewById(R.id.translator_item);
        TextView translatortext = (TextView) findViewById(R.id.translator_text);
        RelativeLayout publisher = (RelativeLayout) findViewById(R.id.publisher_item);
        TextView publishertext = (TextView) findViewById(R.id.publisher_text);
        RelativeLayout time = (RelativeLayout) findViewById(R.id.time_item);
        TextView timetext = (TextView) findViewById(R.id.time_text);
        RelativeLayout ISBN = (RelativeLayout) findViewById(R.id.ISBN_item);
        TextView ISBNtext = (TextView) findViewById(R.id.ISBN_text);
        RelativeLayout state = (RelativeLayout) findViewById(R.id.state_item);
        TextView statetext = (TextView) findViewById(R.id.state_text);
        RelativeLayout shelf = (RelativeLayout) findViewById(R.id.shelf_item);
        TextView shelftext = (TextView) findViewById(R.id.shelf_text);
        RelativeLayout note = (RelativeLayout) findViewById(R.id.note_item);
        TextView notetext = (TextView) findViewById(R.id.note_text);
        RelativeLayout label = (RelativeLayout) findViewById(R.id.label_item);
        TextView labeltext = (TextView) findViewById(R.id.label_text);
        RelativeLayout web = (RelativeLayout) findViewById(R.id.web_item);
        TextView webtext = (TextView) findViewById(R.id.web_text);

        if(book_clicked.getAuthor() != null)
            authortext.setText(book_clicked.getAuthor());
        else
            author.setVisibility(View.GONE);

        if(book_clicked.getTranslator() != null)
            translatortext.setText(book_clicked.getTranslator());
        else
            translator.setVisibility(View.GONE);

        if(book_clicked.getPublisher() != null)
            publishertext.setText(book_clicked.getPublisher());
        else
            publisher.setVisibility(View.GONE);

        if(book_clicked.getPubdate() != null)
            timetext.setText(book_clicked.getPubdate());
        else
            time.setVisibility(View.GONE);

        if(book_clicked.getISBN() != null)
            ISBNtext.setText(book_clicked.getISBN());
        else
            ISBN.setVisibility(View.GONE);

        if(book_clicked.getState() != null)
            statetext.setText(book_clicked.getState());
        else
            state.setVisibility(View.GONE);

        if(book_clicked.getBelongBookShelf() != null)
            shelftext.setText(book_clicked.getBelongBookShelf());
        else
            shelf.setVisibility(View.GONE);

        if(book_clicked.getNotes() != null)
            notetext.setText(book_clicked.getNotes());
        else
            note.setVisibility(View.GONE);

        if(book_clicked.getTag() != null)
            labeltext.setText(book_clicked.getTag());
        else
            label.setVisibility(View.GONE);

        if(book_clicked.getSourceWeb() != null)
            webtext.setText(book_clicked.getSourceWeb());
        else
            web.setVisibility(View.GONE);

    }


}
