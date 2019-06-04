package com.example.ruanjian.bookshelf.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.ruanjian.bookshelf.Entity.Book;
import com.example.ruanjian.bookshelf.Entity.Bookshelf;
import com.example.ruanjian.bookshelf.Entity.Label;
import com.example.ruanjian.bookshelf.R;
import com.example.ruanjian.bookshelf.Widget.BookAdapter;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Spinner spinner;
    private List<String> spinnerList = new ArrayList<>();
    private NavigationView navigationView;
    private CoordinatorLayout appBarMainLayout;
    private RecyclerView bookListView;
    private BookAdapter bookAdapter;
    //是否选中了书架
    private boolean isInBookshelf = false;
    //所有书籍列表
    private List<Book> bookList;
    //当前所选书架书籍列表
    private List<Book> curBookList;
    //当前显示的书籍列表
    private List<Book> tmpList;
    private List<Label> labels = new ArrayList<>();
    private List<Bookshelf> bookshelves = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBarMainLayout = (CoordinatorLayout)findViewById(R.id.appBarMain);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //初始化书籍列表view
        bookList = new ArrayList<>();
        bookListInit();
        tmpList = bookList;
        bookListView = (RecyclerView)findViewById(R.id.booklist_recycler_view);
        bookAdapter = new BookAdapter(tmpList);
        bookListView.setLayoutManager(new LinearLayoutManager(this));
        bookListView.setAdapter(bookAdapter);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionMenu fab = (FloatingActionMenu)findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //导航栏spinner初始化
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
        //左侧抽屉初始化
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //绘制左边抽屉列表
        Paint_Menu();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        if (id == 0) {
            // Handle the camera action
            //点击书籍
        } else if (id == 1) {
            //点击搜索
        } else if (id == 2) {
            //添加新标签
            //弹出dialog
            final EditText labelInput = new EditText(this);
            new AlertDialog.Builder(this).setTitle("请输入标签名")
                    .setView(labelInput)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //new一个label
                            if (labelInput.getText().length() != 0) {
                                Label label = new Label(labelInput.getText().toString());
                                labels.add(label);
                                navigationView.getMenu().removeGroup(0);
                                navigationView.getMenu().removeGroup(1);
                                navigationView.getMenu().removeGroup(2);
                                Paint_Menu();
                            }
                        }
                    }).setNegativeButton("取消", null).show();

        } else if (id == 3) {
            //设置
        } else if (id == 4) {
            //关于
        }
        else if (id >= 5 && id <= 1000) {
            //点击标签
            Label curLabel = labels.get(id-5);
            List<Book> tmp = null;
            if (isInBookshelf) {
                tmp = selectBookByLabel(curBookList, curLabel);
            }
            else {
                tmp = selectBookByLabel(bookList, curLabel);
            }
            refresh(tmp);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<Book> selectBookByLabel(List<Book> list, Label label) {
        List<Book> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<Label> subLabels = list.get(i).getLabels();
            if (subLabels.contains(label)) {
                newList.add(list.get(i));
                break;
            }
        }
        return newList;
    }

    class SpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            String selected = spinnerList.get(position);
            if (selected.equals("所有")) {
                tmpList = bookList;
                bookListView.setAdapter(new BookAdapter(tmpList));
                isInBookshelf = false;
            }
            else {
                isInBookshelf = true;
                List<Book> curList = new ArrayList<>();
                for (int i = 0; i < bookList.size(); i++) {
                    List<Bookshelf> tmplist = bookList.get(i).getBookShelfs();
                    for (Bookshelf bookshelf : tmplist) {
                        if (bookshelf.getTitle().equals(selected)) {
                            curList.add(bookList.get(i));
                            break;
                        }
                    }
                }
                curBookList = curList;
                refresh(curBookList);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    //测试用书籍数据初始化
    private void bookListInit() {
        spinnerList.add("所有");
        for (int i = 0; i < 9; i++) {
            Book book = new Book();
            book.setAuthor("阿瑟·克拉克");
            book.setCoverId(R.drawable.pic1);
            book.setTitle("2001：太空漫游"+i+"");
            book.setTranslator("郝明义");
            book.setPublisher("上海文艺出版社");
            book.setISBN("9787532170692");
            Label label = new Label("tag"+i+"");
            labels.add(label);
            book.addLabel(label);
            Bookshelf bookshelf = new Bookshelf("bookshelf"+i+"");
            spinnerList.add(bookshelf.getTitle());
            book.addBookshelf(bookshelf);
            bookList.add(book);
        }
    }

    //左侧抽屉绘制
    private void Paint_Menu() {
        navigationView.getMenu().add(0,0,0,"书籍").setIcon(R.drawable.books);
        navigationView.getMenu().add(0,1,0,"搜索").setIcon(R.drawable.search);
        for (int labelId = 0; labelId < labels.size(); labelId++) {
            navigationView.getMenu().add(1,labelId+5,0,labels.get(labelId).getTitle());
        }
        navigationView.getMenu().add(1,2,0,"添加新标签").setIcon(R.drawable.add);
        navigationView.getMenu().add(2,3,0,"设置").setIcon(R.drawable.ic_menu_manage);
        navigationView.getMenu().add(2,4,0,"关于").setIcon(R.drawable.about);
    }

    //添加书架
    private void AddBookshelf(String title) {
        Bookshelf bookshelf = new Bookshelf(title);
        bookshelves.add(bookshelf);
        spinnerList.add(title);
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
    }

    //刷新
    private void refresh(List<Book> list) {
        tmpList = list;
        bookListView.setAdapter(new BookAdapter(tmpList));
    }

}
