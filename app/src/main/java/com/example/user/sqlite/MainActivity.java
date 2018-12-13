package com.example.user.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText ed_book, ed_price;
    private Button btn_query, btn_insert, btn_update, btn_delete;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();

    private SQLiteDatabase dbrw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_book = findViewById(R.id.ed_book);
        ed_price = findViewById(R.id.ed_price);
        btn_query = findViewById(R.id.btn_query);
        btn_insert = findViewById(R.id.btn_insert);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        dbrw = new MySQLiteOpenHelper(this).getWritableDatabase();


        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c;
                if(ed_book.length()<1)
                    c = dbrw .rawQuery( "SELECT * FROM myTable" ,null );
                else
                    c = dbrw .rawQuery( "SELECT * FROM myTable WHERE book LIKE '" + ed_book .getText().toString() + "'" ,null );
                c.moveToFirst();
                items.clear();
                Toast.makeText(MainActivity.this,"Total of " + c.getCount() + " data",Toast.LENGTH_SHORT).show();
                for(int i = 0; i < c.getCount(); i++) {
                    items.add("Title: " + c.getString(0) + "\t\t\t\t Price: " + c.getString(1));
                    c.moveToNext();
                }
                adapter.notifyDataSetChanged();

                c.close();
            }
        });

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_book.length()<1 || ed_price.length()<1)
                    Toast.makeText(MainActivity.this,"Don't ", Toast.LENGTH_SHORT).show();
    else{
                    try{
                        dbrw.execSQL("INSERT INTO myTable(book, price) VALUES(?,?)", new Object[]{ed_book.getText().toString(),ed_price.getText().toString()});
                        Toast.makeText(MainActivity.this,"Insert title " + ed_book.getText().toString() + " Price" + ed_price.getText().toString(),Toast.LENGTH_SHORT).show();

                        ed_book.setText("");
                        ed_price.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"Insert Failure: " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_book.length()<1 || ed_price.length()<1)
                    Toast.makeText(MainActivity.this,"Don't", Toast.LENGTH_SHORT).show();
                else{
                    try{
                        dbrw.execSQL("UPdATE myTable SET price = " + ed_price.getText().toString() + " WHERE book LIKE '" + ed_book.getText().toString() + "'");
                        Toast.makeText(MainActivity.this,"Modify title "+ ed_book.getText().toString()+" Price"+ed_price.getText().toString(),Toast.LENGTH_SHORT).show();

                        ed_book.setText("");
                        ed_price.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"Modify failure: "+ e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_book.length()<1)
                    Toast.makeText(MainActivity.this,"Don't",Toast.LENGTH_SHORT).show();
                else{
                    try{
                        dbrw.execSQL("DELETE FROM myTable WHERE book LIKE '" +ed_book.getText().toString()+"'");
                        Toast.makeText(MainActivity.this,"Delete title " +ed_book.getText().toString(),Toast.LENGTH_SHORT).show();

                        ed_book.setText("");
                        ed_price.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"Delete failure: " + e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dbrw.close();
    }
}




