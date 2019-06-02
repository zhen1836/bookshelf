package com.example.ruanjian.bookshelf.Widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruanjian.bookshelf.Entity.Book;
import com.example.ruanjian.bookshelf.R;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.VH> {
    Context context;
    private List<Book> bookList;

    public BookAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    class VH extends RecyclerView.ViewHolder {
        private ImageView cover;
        private TextView title;
        private TextView info;
        private TextView pubDate;
        VH(@NonNull View itemView) {
            super(itemView);
            cover = (ImageView)itemView.findViewById(R.id.bookPic);
            title = (TextView)itemView.findViewById(R.id.bookTitle);
            info = (TextView)itemView.findViewById(R.id.bookInfo);
            pubDate = (TextView)itemView.findViewById(R.id.pubDate);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item_view, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH viewHolder, int position) {
        Book book = bookList.get(position);
        viewHolder.cover.setImageResource(book.getCoverId());
        viewHolder.title.setText(book.getTitle());
        final String info = book.getAuthor()+" 著, "+book.getPublisher();
        viewHolder.info.setText(info);
        viewHolder.pubDate.setText(book.getPubdate());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
