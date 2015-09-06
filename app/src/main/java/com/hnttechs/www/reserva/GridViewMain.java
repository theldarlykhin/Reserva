package com.hnttechs.www.reserva;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * Created by dell on 7/15/15.
 */
public class GridViewMain extends ActionBarActivity {

    GridView gridView;
    static final String[ ] GRID_DATA = new String[] {
            "Chinese Restaurant" ,
            "Myanmar Restaurant",
            "Japan Restaurant" ,
            "European Restaurant" ,
            "Indian Restaurant" ,
            "Thailand Restaurant"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.grid_view_android_example );

        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(new GridViewAdapter(this, GRID_DATA));
        gridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(getBaseContext(),ListViewMain.class);
                intent.putExtra("btn_id",GRID_DATA[position].toString());
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about_us) {
            Intent i = new Intent(getBaseContext(),About_Us.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
