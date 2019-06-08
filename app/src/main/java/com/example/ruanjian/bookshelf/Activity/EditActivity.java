package com.example.ruanjian.bookshelf.Activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ruanjian.bookshelf.Entity.Book;
import com.example.ruanjian.bookshelf.R;

import java.util.ArrayList;
import java.util.Arrays;

public class EditActivity extends AppCompatActivity {
    private Book book_clicked;
    private ArrayList<String> All_state = new ArrayList<String>(Arrays.asList("阅读状态未设置", "未读", "阅读中", "已读"));

    private ArrayAdapter<String> state_adapter;
    //private int img;
    //private LinearLayout title = (LinearLayout) findViewById(R.id.LL_title);
    private EditText titletext;
    //private LinearLayout author = (LinearLayout) findViewById(R.id.LL_author);
    private EditText authortext;
    private LinearLayout translator;
    private EditText translatortext;
    //private LinearLayout publisher = (LinearLayout) findViewById(R.id.LL_publisher);
    private EditText publishertext;
    //private LinearLayout pubdate = (LinearLayout) findViewById(R.id.LL_pubdate);
    private EditText pubdate_yeartext;
    private EditText pubdate_monthtext;
    //private LinearLayout ISBN = (LinearLayout) findViewById(R.id.LL_ISBN);
    private EditText ISBNtext;

    private Spinner state;
    private Spinner shelf;
    private EditText notes;
    private EditText label;
    private EditText websourse;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.edit_toolbar);

        toolbar.setTitle("编辑书籍详情");//标题
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));


        toolbar.setNavigationIcon(R.drawable.back);//设置Navigation 图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditActivity.this, "back !", Toast.LENGTH_SHORT).show();
            }
        });
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        //        toolbar.inflateMenu(R.menu.edit_menu);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.edit_save:
//                        save();
//                        break;
//                }
//                return true;
//            }
//        });

        book_clicked = (Book) getIntent().getSerializableExtra("book");

        state = (Spinner)findViewById(R.id.state_spinner);
        state_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, All_state);
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        state.setAdapter(state_adapter);
//        state.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int pos, long id) {
//
//                String[] languages = getResources().getStringArray(R.array.languages);
//                Toast.makeText(EditActivity.this, "你点击的是:"+languages[pos], Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Another interface callback
//            }
//        });


        Init();
    }

    private void Init(){
        //ImageButton img = (ImageButton)findViewById(R.id.book_img);
        titletext = (EditText)findViewById(R.id.et_title);
        authortext = (EditText)findViewById(R.id.et_author);
        translator = (LinearLayout) findViewById(R.id.LL_translator);
        translatortext = (EditText)findViewById(R.id.et_translator);
        publishertext = (EditText)findViewById(R.id.et_publisher);
        pubdate_yeartext = (EditText)findViewById(R.id.et_pubdate_year);
        pubdate_monthtext = (EditText)findViewById(R.id.et_pubdate_month);
        ISBNtext = (EditText)findViewById(R.id.et_ISBN);

        state = (Spinner)findViewById(R.id.state_spinner);
        shelf = (Spinner)findViewById(R.id.shelf_spinner);
        notes = (EditText)findViewById(R.id.et_note);
        label = (EditText)findViewById(R.id.et_label);
        websourse = (EditText)findViewById(R.id.et_web);


        if(book_clicked.getTitle() != null)
            titletext.setText(book_clicked.getTitle());

        if(book_clicked.getAuthor() != null)
            authortext.setText(book_clicked.getAuthor());

        if(book_clicked.getTranslator() != null)
            translatortext.setText(book_clicked.getTranslator());
        else
            translator.setVisibility(View.GONE);

        if(book_clicked.getPublisher() != null)
            publishertext.setText(book_clicked.getPublisher());

        if(book_clicked.getPubdate() != null) {
            pubdate_yeartext.setText(book_clicked.getPubdate().substring(0,4));
            pubdate_monthtext.setText(book_clicked.getPubdate().substring(6));
        }

        if(book_clicked.getISBN() != null)
            ISBNtext.setText(book_clicked.getISBN());

//        if(book_clicked.getState() != null)
//            state.setText(book_clicked.getState());
//        else
//            state.setVisibility(View.GONE);
//
//        if(book_clicked.getBelongBookShelf() != null)
//            shelftext.setText(book_clicked.getBelongBookShelf());
//        else
//            shelf.setVisibility(View.GONE);

        if(book_clicked.getNotes() != null)
            notes.setText(book_clicked.getNotes());

        if(book_clicked.getTag() != null)
            label.setText(book_clicked.getTag());

        if(book_clicked.getSourceWeb() != null)
            websourse.setText(book_clicked.getSourceWeb());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return  true;
    }


    private void save() {
        Toast.makeText(EditActivity.this, "Save !", Toast.LENGTH_SHORT).show();
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.edit_save:
                    save();
                    break;
                default:
                    break;
            }
            return true;
        }
    };
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        return super.onOptionsItemSelected(item);
//    }
}
