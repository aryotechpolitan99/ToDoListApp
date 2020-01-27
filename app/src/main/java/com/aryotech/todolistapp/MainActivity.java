package com.aryotech.todolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fabAdd;
    ListView listView;
    EditText edtTodo;

    //1. Siapkan Data
    ArrayList<String> data = new ArrayList<String>();

    //3. Buat Adapter untuk List View
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //1. Siapkan data
        //createTodos();

        //9.2 Panggil method loadPreferences() agar data dari SP dimasukan ke arraylist saat pertama di panggil
        loadDataFromPreferences();

        //2. Buat List View
        listView = findViewById(R.id.lv_list); // define list view

        // 3. Buat Adapter dan masukkan parameter yg dibutuhkan. (context, layout_content,tv,data)
        //      parameter data diambil dari langkah 1.
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.content_list,R.id.tv_todo,data);

        // 4. Set Adapter kepada List View
        listView.setAdapter(arrayAdapter);

        // 5. Define FAB dan buat onClickListener nya.
        fabAdd = findViewById(R.id.btn_fab);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //6. Method di bawah ini dibuat sendiri di bawah
                onClickFabAdd();
            }
        });

        //7.1 Buat onItemLongclickListener di list view unutk hapus data
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // panggil method deleteItem()
                deleteItem(position);

                //10.2 Panggil method deleteFromSP untuk menghapus data dari Shared Preferences
                deleteFromSP(position); // Sampai sini akan terjadi error karena key d SP tidak berurutan
                return false;
            }
        });

        // 12.4 Buat OnItemClickListener dan panggil method showDialogEdit()
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Panggil method showDialogEdit()
                showDialogEdit(position);
            }
        });
    }

    //1. siapkan data
    private void createTodos(){
        data.add("eat");
        data.add("pray");
        data.add("sport");
        data.add("break");
    }
    //6. Buat Method ketika FAB Add di click untuk menambahkan data
    private void onClickFabAdd(){
        //Cara pertama tambah edit text ke dialog
        //EditText edtTodo = new EditText(this);

        //Cara dua tambah edit text ke dialog
        //proses ini disebut dengan inflate layout
        View view = View.inflate(this,R.layout.dialog_add_view, null);

        //EditText ini dideklarisikan di atas di dalam class
        edtTodo = view.findViewById(R.id.edt_todo);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Add your list");
        dialog.setView(view);
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //8.2 Hitung size dari arraylist data untuk dijadikan calon key unutk SP
                int newKey = data.size();

                String item = edtTodo.getText().toString();
                data.add(item); // tambah data ke object arraylist data
                arrayAdapter.notifyDataSetChanged(); // merefresh list view

                //8.3 Tambahkan data ke SP
                // panggil method addToSP() untuk menyimpan data ke SP
                addToSP(newKey,item);

                Toast.makeText(getApplicationContext(),String.valueOf(newKey),Toast.LENGTH_LONG).show();
            }
        });
        dialog.setNegativeButton("Batal", null);
        dialog.create();
        dialog.show();
    }

    // 7.2 buat method delete item untuk menghapus data dari array list dan mengupdate listviewdan mengupdate listview
    private void deleteItem(int position){// beri paramater position untuk mewadahi position dari listview

       // kontanta untuk menampung data position yang di passing dari item onitemlongclicklistener
        final int index = position;

        /// buat alert dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Anda yakin menghapus item ?");
        dialog.setPositiveButton("ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // hapus item dari array list data berdasarkan index/position dari item di list view
                data.remove(index); // index didapat position paramater

                //11.2 Panggil method reGenerateAndSortSP()
                //reGenerateAndSortSP();
                reGenerateAndSortSP();


                // suruh adapter unutk notify ke listview kalau data telah berubah, lalu merefresh listview
                arrayAdapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("Tidak", null);
        dialog.create().show();
    }

    // 8.1 Buat method untuk input data ke Shared Preferences
    private void addToSP(int key, String item){
        // buat key untuk SP diambil dari size terakhir array list data
        String newKey = String.valueOf(key);
        SharedPreferences todosPref = getSharedPreferences("todosPref",MODE_PRIVATE);
        SharedPreferences.Editor todosPrefEditor = todosPref.edit();
        // simpan ke SP dengan key dari size terakhir array list
        todosPrefEditor.putString(newKey,item);
        todosPrefEditor.apply();
        // atau todosPrefEditor.commit();

    }

    //9.1 Load data dari SP
    //Buat method loadDataFromPrefrences
    private void loadDataFromPreferences(){
        SharedPreferences todosPref = getSharedPreferences("todosPref",MODE_PRIVATE);
        // cek dalam SP ada data atau tidak
        if (todosPref.getAll().size() > 0){ //2
            // masukan semua data di SP ke array list data
            for (int i = 0; i < todosPref.getAll().size(); i++){ // i < 2
                String key = String.valueOf(i); //i = 1
                String item = todosPref.getString(key, null);
                data.add(item);
            }
        }
    }

    //10.1 Menghapus data dari Shared Preferences
    // Buat method hapus data dari shared preferences
    private void deleteFromSP(int position){
        String key = String.valueOf(position);
        SharedPreferences todosPref = getSharedPreferences("todosPref",MODE_PRIVATE);
        SharedPreferences.Editor todosPrefEditor = todosPref.edit();
        todosPrefEditor.remove(key);
        todosPrefEditor.apply();
    }


    //11.1 Fix Error di langkah 10 untuk mengurutkan kembali key dan value di dalam Shared Preference
    private void reGenerateAndSortSP(){
        SharedPreferences todosPref = getSharedPreferences("todosPref",MODE_PRIVATE);
        SharedPreferences.Editor todosPrefEditor = todosPref.edit();
        // hapus semua data di Shared Preference
        todosPrefEditor.clear();
        todosPrefEditor.apply();

        // isi ulang Shared Preference dengan data dari array list yang sudah otomatis terurut
        for(int i = 0; i < data.size();i++){
            String key = String.valueOf(i);
            todosPrefEditor.putString(key,data.get(i));
        }
        /*int i = 0;
        for (String item: data) {
            String key = String.valueOf(i);
            todosPrefEditor.putString(key,item);
            i++;
        }*/
        todosPrefEditor.apply();

    }

    // 12.1 Membuat fitur Edit Item
    //  Buat method untuk menampilkan AlertDialog data yang hendak diedit

    private void showDialogEdit(final int position){

        View view = View.inflate(this,R.layout.dialog_add_view, null);

        //EditText ini dideklarisikan di atas di dalam class
        edtTodo = view.findViewById(R.id.edt_todo);

        //EditText diisi dengan data dari list view yang dipilih berdasarkan parameter position
        edtTodo.setText(arrayAdapter.getItem(position)); //diambil dari adapter list view
        //edtTodo.setText(data.get(position)); //diambil dari array list : alternatif dari cara diatas ini.

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Coba ganti mau ngapain jadinya ?");
        dialog.setView(view);
        dialog.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //12.3 Panggil method editItem() di bawah yang telah dibuat pada langkah 12.2
                editItem(position,edtTodo.getText().toString());
            }
        });
        dialog.setNegativeButton("Batal",null);
        dialog.create();
        dialog.show();
    }

    // 12.2 Buat method untuk mengubah item dengan parameter postition dan text item baru.
    private void editItem(int position, String newItem){
        //set data di array dengan value baru berdasarkan index/position
        data.set(position, newItem);

        //jangan lupa Shared Preferences di generate ulang
        reGenerateAndSortSP();

        //refresh list view
        arrayAdapter.notifyDataSetChanged();
    }
}

