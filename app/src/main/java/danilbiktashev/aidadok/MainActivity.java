package danilbiktashev.aidadok;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<symp> products = new ArrayList<symp>();
    SympAdapter boxAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("СХЕМЫ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setOnItemClickListener(new ItemList());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        boxAdapter = new SympAdapter(this, products);
        // создаем адаптер
        getSymp("ОБЩИЕ");
    }

    class ItemList implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent myIntent = new Intent(MainActivity.this, ChooseSymptomActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("a",(Serializable)products);
            myIntent.putExtra("question", args);
            myIntent.putExtra("charapterID", String.valueOf(position+1));
            MainActivity.this.startActivity(myIntent);
        }
    }

//secret commit

    public void colorOfMenuItems() {

    }

    public void getSymp(String shemeNameChild) {
        products.clear();
        boxAdapter.notifyDataSetChanged();
        myRef.child(shemeNameChild).addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                products.clear();
                boxAdapter.notifyDataSetChanged();
                for (DataSnapshot child : snapshot.getChildren()) {
                    products.add(new symp(child));
                    Collections.sort(products, new SympComporator());
                }

                boxAdapter.objects = products;
                // настраиваем список
                ListView lvMain = (ListView) findViewById(R.id.lvMain);
                lvMain.setAdapter(boxAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            getSymp("ОБЩИЕ");
        } else if (id == R.id.nav_slideshow) {
            getSymp("МУЖСКИЕ");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
