package com.example.ruanjian.bookshelf.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruanjian.bookshelf.Entity.Book;
import com.example.ruanjian.bookshelf.Entity.Bookshelf;
import com.example.ruanjian.bookshelf.Entity.Label;
import com.example.ruanjian.bookshelf.R;
import com.example.ruanjian.bookshelf.Widget.BytesBitmap;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class EditActivity extends AppCompatActivity {
    private Book book_clicked;
    private ArrayList<Label> labels;
    private ArrayList<String> labelsTitle = new ArrayList<>();
    private ArrayList<String> All_state = new ArrayList<String>(Arrays.asList("阅读状态未设置", "未读", "阅读中", "已读"));
    private ArrayList<Bookshelf> All_shelf = new ArrayList<>();
    private ArrayList<String> All_shelf_title = new ArrayList<>();

    private StringBuilder setlabel= new StringBuilder();

    private ArrayAdapter<String> state_adapter;
    private ArrayAdapter<String> shelf_adapter;


    //private int img;
    //private LinearLayout title = (LinearLayout) findViewById(R.id.LL_title);
    private ImageButton imgButton;
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

    private TextView source;
    private Spinner state;
    private Spinner shelf;
    private EditText notes;
    private EditText label;
    private EditText websourse;

    public final static int RESULT_CODE = 1;

    private final int CAMERA_RESULT_CODE=500;
    private final int CROP_RESULT_CODE=501;
    private final int ALBUM_RESULT_CODE=502;

    private String imag_file;
    private File file;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_main);
        permission();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


        toolbar_init();

        book_clicked = (Book) getIntent().getSerializableExtra("book");
        labels = (ArrayList<Label>)getIntent().getSerializableExtra("labels") ;
        All_shelf = (ArrayList<Bookshelf>)getIntent().getSerializableExtra("shelfs") ;
        Init();




        imgButton = (ImageButton)findViewById(R.id.book_img);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setTitle("更换封面");
                builder.setItems(new String[]{"拍摄新图片","从相册选择图片"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                openSysCamera();
                                break;
                            case 1:
                                openSysAlbum();
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });

        for(Label i : labels){
            labelsTitle.add(i.getTitle());
        }

        for(Bookshelf i : All_shelf){
            All_shelf_title.add(i.getTitle());
        }
        All_shelf_title.add("默认");
        All_shelf_title.add("添加新书架");

        state_init();
        shelf_init();
        label_init();

    }



    private void permission() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.WRITE_APN_SETTINGS,
                    Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(EditActivity.this, mPermissionList, 123);
        }
    }

    private void toolbar_init() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.edit_toolbar);

        toolbar.setTitle("编辑书籍详情");//标题
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back);//设置Navigation 图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(EditActivity.this);
            @Override
            public void onClick(View v) {
                dialog.setTitle("书架名称");

                dialog.setMessage("我们不会保存此书，书籍信息会被丢弃。");
                dialog.setPositiveButton("舍弃",new sureClick());
                dialog.setNegativeButton("取消",new cancelClick());
                dialog.create();
                dialog.show();
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

    private void Init(){
        imgButton = (ImageButton)findViewById(R.id.book_img);
        titletext = (EditText)findViewById(R.id.et_title);
        authortext = (EditText)findViewById(R.id.et_author);
        translator = (LinearLayout) findViewById(R.id.LL_translator);
        translatortext = (EditText)findViewById(R.id.et_translator);
        publishertext = (EditText)findViewById(R.id.et_publisher);
        pubdate_yeartext = (EditText)findViewById(R.id.et_pubdate_year);
        pubdate_monthtext = (EditText)findViewById(R.id.et_pubdate_month);
        ISBNtext = (EditText)findViewById(R.id.et_ISBN);

        source = (TextView)findViewById(R.id.edit_detail);
        state = (Spinner)findViewById(R.id.state_spinner);
        shelf = (Spinner)findViewById(R.id.shelf_spinner);
        notes = (EditText)findViewById(R.id.et_note);
        label = (EditText)findViewById(R.id.et_label);
        websourse = (EditText)findViewById(R.id.et_web);


        if (book_clicked.getCoverId() != null){
            Bitmap bitmap = BytesBitmap.getBitmap(book_clicked.getCoverId());
            imgButton.setImageBitmap(bitmap);
        }

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

        if(book_clicked.getLabels().size() != 0) {
            setlabel.append(book_clicked.getLabels().get(0).getTitle());
            for(int i = 1; i < book_clicked.getLabels().size(); i++){
                setlabel.append("，"+book_clicked.getLabels().get(i).getTitle());
            }
            label.setText(setlabel.toString());
        }

        if(book_clicked.getSourceWeb() != null) {
            websourse.setText(book_clicked.getSourceWeb());
            source.setText("信息（来源：" + book_clicked.getSourceWeb().substring(14,24) + ")");
        }
        else
            source.setText("信息（来源：Manually)");
    }

    private void state_init(){
        state = (Spinner)findViewById(R.id.state_spinner);
        state_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, All_state);
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(state_adapter);
    }

    private void shelf_init(){
        shelf = (Spinner)findViewById(R.id.shelf_spinner);
        shelf_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, All_shelf_title);
        shelf_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shelf.setAdapter(shelf_adapter);
        shelf.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selected = All_shelf_title.get(position);
                if (selected.equals("添加新书架")) {
                    Toast.makeText(EditActivity.this, selected, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditActivity.this);

                    dialog.setTitle("书架名称");
                    final EditText input = new EditText(EditActivity.this);
                    input.setHint("输入新的书架的名称");
                    dialog.setView(input,80,40,100,40);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(@NonNull DialogInterface dialog, @NonNull int which) {
                            if(input.getText() != null){
                                All_shelf_title.remove("添加新书架");
                                All_shelf_title.add(input.getText().toString());
                                All_shelf_title.add("添加新书架");
                                Bookshelf newbs = new Bookshelf(input.getText().toString());
                                newbs.add(book_clicked);
                                // book_clicked.setBookShelfs();
                                All_shelf.add(newbs);
                                dialog.cancel();
                                shelf_init();

                            }
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(@NonNull DialogInterface dialog, @NonNull int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
                else {
                    //All_shelf.get(position).add(book_clicked);
                    // book_clicked.setBookShelfs();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void label_init() {
//        label.setFocusable(false);
//        label.setFocusableInTouchMode(false);
        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label_multi();

            }
        });
    }

    private void label_multi(){
        final ArrayList<Integer> yourChoices = new ArrayList<>();
        final ArrayList<Label> new_labels = new ArrayList<>();
        final String[] items = (String[]) labelsTitle.toArray(new String[labelsTitle.size()]);
        // 设置默认选中的选项，全为false默认均未选中
        final boolean initChoiceSets[] = new boolean[labelsTitle.size()];
        int j = 0;
        Arrays.fill(initChoiceSets, false);
        for(Label i : book_clicked.getLabels()){
            for(;j<labelsTitle.size();j++){
                if(i.getTitle().equals(labelsTitle.get(j))){
                    initChoiceSets[j] = true;
                    yourChoices.add(j);
                    break;
                }
            }
        }
        final AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(EditActivity.this);
        multiChoiceDialog.setTitle("选择标签");
        multiChoiceDialog.setMultiChoiceItems(items, initChoiceSets,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            yourChoices.add(which);
                        } else {
                            yourChoices.remove((Object)which);
                        }
                        Collections.sort(yourChoices);
                    }

                });
        multiChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int size = yourChoices.size();
                        StringBuilder str = new StringBuilder();
                        if(size > 0){
                            str.append(items[yourChoices.get(0)]);
                            new_labels.add(labels.get(yourChoices.get(0)));
                            for (int i = 1; i < size; i++) {
                                str.append("," + items[yourChoices.get(i)]);
                                new_labels.add(labels.get(yourChoices.get(i)));
                            }
                        }

                        dialog.cancel();
                        label.setText(str.toString());
                        book_clicked.setLabels(new_labels);
                    }
                });

        multiChoiceDialog.setNeutralButton("添加标签",
                new DialogInterface.OnClickListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final EditText labelInput = new EditText(EditActivity.this);
                        labelInput.setHint("输入新的标签的名称");
                        new AlertDialog.Builder(EditActivity.this).setTitle("请输入标签名")
                                .setView(labelInput,80,40,100,40)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //new一个label
                                        if (labelInput.getText().length() != 0) {
                                            Label label = new Label(labelInput.getText().toString());
                                            labels.add(label);
                                            labelsTitle.add(label.getTitle());
                                            label_multi();
                                        }
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        label_multi();
                                    }
                                }).show();

                    }
                });
        multiChoiceDialog.create();
        multiChoiceDialog.setCancelable(false);
        multiChoiceDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return  true;
    }

    class sureClick implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent();
            intent.setClass(EditActivity.this,MainActivity.class);
            startActivity(intent);
        }

    }

    class cancelClick implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    private void save() {
        //Toast.makeText(EditActivity.this, "Save !", Toast.LENGTH_SHORT).show();
        Bitmap bitmap2 = ((BitmapDrawable)imgButton.getDrawable()).getBitmap();
        //book_clicked.setCoverId(new MyBitmap(BytesBitmap.getBytes(bitmap)));
        book_clicked.setCoverId(BytesBitmap.getBytes(bitmap2));
        if(titletext.getText().length() == 0){
            Toast.makeText(EditActivity.this, "标题不能为空！", Toast.LENGTH_SHORT).show();
            return ;
        }
        if(ISBNtext.getText().toString().length() != 10 && ISBNtext.getText().toString().length() != 13){
            Toast.makeText(EditActivity.this, "ISBN码应该为10或13位数字！", Toast.LENGTH_SHORT).show();
            return ;
        }
        if (titletext.getText().length() != 0)
            book_clicked.setTitle(titletext.getText().toString());
        if (authortext.getText().length() != 0)
            book_clicked.setAuthor(authortext.getText().toString());
        else
            book_clicked.setAuthor(null);
        if (translatortext.getText().length() != 0)
            book_clicked.setTranslator(translatortext.getText().toString());
        else
            book_clicked.setTranslator(null);
        if (publishertext.getText().length() != 0)
            book_clicked.setPublisher(publishertext.getText().toString());
        else
            book_clicked.setPublisher(null);
        if(pubdate_yeartext.getText().length() != 0 && pubdate_monthtext.getText().length() != 0)
            book_clicked.setPubdate(pubdate_yeartext.getText().toString()+"-"+pubdate_monthtext.getText().toString());
        else
            book_clicked.setPubdate(null);
        if (ISBNtext.getText().length() != 0)
            book_clicked.setISBN(ISBNtext.getText().toString());
        else
            book_clicked.setISBN(null);
        book_clicked.setState(state.getSelectedItem().toString());
        //book_clicked.setBookShelfs(shelf.getSelectedItem().toString());
        if (notes.getText().length() != 0)
            book_clicked.setNotes(notes.getText().toString());
        else
            book_clicked.setNotes(null);
        if (websourse.getText().length() != 0)
            book_clicked.setSourceWeb(websourse.getText().toString());
        else
            book_clicked.setSourceWeb(null);


        Intent intent = new Intent();
        intent.setClass(EditActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("book",book_clicked);
        bundle.putSerializable("labels",(Serializable) labels);
        bundle.putSerializable("shelfs",(Serializable) All_shelf);
        //intent.putExtra("book",book_clicked);
        intent.putExtras(bundle);
        setResult(RESULT_CODE, intent);
        finish();

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

    //打开相机
    public void openSysCamera(){
        if(!ISBNtext.getText().toString().equals("")){
            imag_file=ISBNtext.getText().toString();
        }
        else {
            imag_file=String.valueOf(new Random().nextInt(1000))+String.valueOf(new Random().nextInt(1000));
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
                new File(Environment.getExternalStorageDirectory(), imag_file)));
        startActivityForResult(cameraIntent, CAMERA_RESULT_CODE);
    }

    //返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_RESULT_CODE:
                File tempFile = new File(Environment.getExternalStorageDirectory(), imag_file);
                cropPic(Uri.fromFile(tempFile));
                break;
            case CROP_RESULT_CODE:
                // 裁剪时,这样设置 cropIntent.putExtra("return-data", true); 处理方案如下
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap bitmap = bundle.getParcelable("data");
                        imgButton.setImageBitmap(bitmap);
                        // 把裁剪后的图片保存至本地 返回路径
                        //String urlpath = FileUtilcll.saveFile(this, "crop.jpg", bitmap);
                        //L.e("裁剪图片地址->" + urlpath);
                    }
                }

                // 裁剪时,这样设置 cropIntent.putExtra("return-data", false); 处理方案如下
                //  try {
                //      ivHead.setImageBitmap(BitmapFactory.decodeStream(
                // getActivity().getContentResolver().openInputStream(imageUri)));
                //                } catch (FileNotFoundException e) {
                //                    e.printStackTrace();
                //                }
                break;

            case ALBUM_RESULT_CODE:
                // 相册
                cropPic(data.getData());
                break;

        }

    }


    /**
     * 裁剪图片
     *
     * @param data
     */
    private void cropPic(Uri data) {
        if (data == null) {
            return;
        }
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(data, "image/*");

        //需要加上这两句话 ： uri 权限
        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // 开启裁剪：打开的Intent所显示的View可裁剪
        cropIntent.putExtra("crop", "true");
        // 裁剪宽高比
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1.5);
        // 裁剪输出大小
        cropIntent.putExtra("outputX", 270);
        cropIntent.putExtra("outputY", 370);
        cropIntent.putExtra("scale", true);
        /**
         * return-data
         * 这个属性决定我们在 onActivityResult 中接收到的是什么数据，
         * 如果设置为true 那么data将会返回一个bitmap
         * 如果设置为false，则会将图片保存到本地并将对应的uri返回，当然这个uri得有我们自己设定。
         * 系统裁剪完成后将会将裁剪完成的图片保存在我们所这设定这个uri地址上。我们只需要在裁剪完成后直接调用该uri来设置图片，就可以了。
         */
        cropIntent.putExtra("return-data", true);
        // 当 return-data 为 false 的时候需要设置这句
        //        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        // 图片输出格式
        //        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 头像识别 会启动系统的拍照时人脸识别
        //        cropIntent.putExtra("noFaceDetection", true);
        startActivityForResult(cropIntent, CROP_RESULT_CODE);
    }

    /**
     * 打开系统相册
     */
    private void openSysAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, ALBUM_RESULT_CODE);
    }


    private void useCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/test/" + System.currentTimeMillis() + ".jpg");
        file.getParentFile().mkdirs();
        //改变Uri com.xykj.customview.fileprovider注意和xml中的一致
        Uri uri = FileProvider.getUriForFile(this, "com.gjp.activity.teste.fileprovider", file);
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA_RESULT_CODE);
    }

    private void getPhoto(){
        //在这里跳转到手机系统相册里面
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, ALBUM_RESULT_CODE);
    }


//    private Bitmap changeBitmapSize(Bitmap bad_bitmap) {
//        Bitmap bitmap = bad_bitmap;
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        //设置想要的大小
//        int newWidth=270;
//        int newHeight=370;
//
//        //计算压缩的比率
//        float scaleWidth=((float)newWidth)/width;
//        float scaleHeight=((float)newHeight)/height;
//
//        //获取想要缩放的matrix
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth,scaleHeight);
//
//        //获取新的bitmap
//        bitmap=Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
//        bitmap.getWidth();
//        bitmap.getHeight();
//
//        return bitmap;
//    }


}
