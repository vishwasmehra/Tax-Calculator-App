package index1.developer.acadview.com.taxcalculator;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GSTActivity extends AppCompatActivity {

    DatabaseHelper db;
    AlertDialog.Builder b;
    AlertDialog a;
    ListView list1;
    EditText Text;
    TextView tx1;
    ArrayList<String> itemslist;
    ArrayList<String> taxlist;
    static int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gst);

        list1 = (ListView) findViewById(R.id.listview);
        tx1=(TextView)findViewById(R.id.textview2);
        b = new AlertDialog.Builder(this);
        fetchall();

        list1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemslist));
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                Text = new EditText(GSTActivity.this);
                b.setTitle("Enter Amount");
                b.setMessage("Amount of " + itemslist.get(i) + "\t");
                b.setView(Text);
                b.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                                calculate();

                    }
                });
                b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        calculate();

                    }
                });
                a = b.create();
                a.show();
            }
        });
    }

    public void calculate() {
        int am = 0;
        float t;
        String val = list1.getItemAtPosition(position).toString();;
        int total = 0, tax = 0;
        if (!TextUtils.isEmpty(Text.getText().toString())) {
            am = Integer.parseInt(Text.getText().toString());
            if (val.equals(itemslist.get(position))) {
                t=Float.parseFloat(taxlist.get(position));
                tax = (int) ((am * t) / 100);
                total = am + tax;
                tx1.setText("Amount of "+val+" ="+am+"\n"+
                            "Tax% ="+t+"\n"+
                            "GST Calculated =" + tax + "\n" +
                            "Total Amount (Inclusion of GST) =" + total + "\t");
            }
        } else {
            tx1.setText("Amount of "+val+" ="+am+"\n"+
                            "Tax =" + tax + "\n" +
                        "Total amount =" + total);
        }
    }

    public void fetchall() {
        db = new DatabaseHelper(this);
        try {

            db.createDatabase();
            db.openDataBase();

        } catch (Exception e) {
            e.printStackTrace();
        }
        int i, j;
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM gst", null);
        i=cursor.getColumnIndex("items");
        j=cursor.getColumnIndex("Tax%");
        itemslist=new ArrayList<>();
        taxlist=new ArrayList<>();
        while(cursor.moveToNext())
        {
            String it=cursor.getString(i);
            String tax=cursor.getString(j);
            itemslist.add(it);
            taxlist.add(tax);
        }
        cursor.close();
    }
}

