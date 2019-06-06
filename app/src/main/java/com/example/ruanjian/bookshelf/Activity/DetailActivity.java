package com.example.ruanjian.bookshelf.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ruanjian.bookshelf.Entity.Book;
import com.example.ruanjian.bookshelf.Entity.Label;
import com.example.ruanjian.bookshelf.R;
import com.klinker.android.sliding.SlidingActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailActivity extends SlidingActivity {
    private Book book_clicked;
    private ArrayList<Label> labels;
    private ArrayList<Label> bookshelfs;
    private StringBuilder setlabel= new StringBuilder();

    private static int REQUEST_CODE=1;
    public final static int RESULT_CODE = 1;

    @Override
    public void init(Bundle savedInstanceState) {
        book_clicked = (Book) getIntent().getSerializableExtra("book");
        labels = (ArrayList<Label>)getIntent().getSerializableExtra("labels") ;
        bookshelfs = (ArrayList<Label>)getIntent().getSerializableExtra("shelfs") ;
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
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("book",book_clicked);
                        bundle.putSerializable("labels",(Serializable) labels);
                        bundle.putSerializable("shelfs",(Serializable) bookshelfs);
                        //intent.putExtra("book",book_clicked);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }
        );
        setdetailInfo();
    }

    void setdetailInfo(){
        ImageView img = (ImageView) findViewById(R.id.book_img);
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
        TextView source = (TextView) findViewById(R.id.Info_source);

        if (book_clicked.getCoverId() != 0){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),book_clicked.getCoverId());
            img.setImageBitmap(changeBitmapSize(bitmap));
        }

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
            statetext.setText("阅读状态未设置");;

        if(book_clicked.getBookShelfs().size() != 0)
            shelftext.setText(book_clicked.getBookShelfs().get(0).getTitle());
        else
            shelf.setVisibility(View.GONE);

        if(book_clicked.getNotes() != null)
            notetext.setText(book_clicked.getNotes());
        else
            note.setVisibility(View.GONE);

        if(book_clicked.getLabels().size() != 0){
            setlabel.append(book_clicked.getLabels().get(0).getTitle());
            for(int i = 1; i < book_clicked.getLabels().size(); i++){
                setlabel.append("，"+book_clicked.getLabels().get(i).getTitle());
            }
            labeltext.setText(setlabel.toString());
        }
        else
            label.setVisibility(View.GONE);

        if(book_clicked.getSourceWeb() != null) {
            webtext.setText(book_clicked.getSourceWeb());
            //source.setText("信息（来源：" + book_clicked.getSourceWeb().substring(14,24) + ")");
        }
        else {
            web.setVisibility(View.GONE);
            source.setText("信息（来源：Manually)");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //添加日记
        if (requestCode == REQUEST_CODE) {
            if (resultCode == EditActivity.RESULT_CODE) {
                setResult(RESULT_CODE, data);
                finish();

            }

        }
    }

    private Bitmap changeBitmapSize(Bitmap bad_bitmap) {
        Bitmap bitmap = bad_bitmap;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //设置想要的大小
        int newWidth=270;
        int newHeight=370;

        //计算压缩的比率
        float scaleWidth=((float)newWidth)/width;
        float scaleHeight=((float)newHeight)/height;

        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);

        //获取新的bitmap
        bitmap=Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        bitmap.getWidth();
        bitmap.getHeight();

        return bitmap;
    }


}
