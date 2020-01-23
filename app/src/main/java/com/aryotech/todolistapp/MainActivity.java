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
                return false;
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
    //Buat method loadPreferences
    private void loadDataFromPreferences(){
        SharedPreferences todosPref = getSharedPreferences("todosPref",MODE_PRIVATE);
        // cek dalam SP ada data atau tidak
        if (todosPref.getAll().size() > 0){
            // masukan semua data di SP ke array list data
            for (int i = 0; i < todosPref.getAll().size(); i++){
                String key = String.valueOf(i);
                String item = todosPref.getString(key, null);
                data.add(item);
            }
        }
    }
}

