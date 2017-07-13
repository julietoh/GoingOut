package codepath.com.goingout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import codepath.com.goingout.adapters.FilterAdapter;
import codepath.com.goingout.models.Type;

public class PreferenceActivity extends AppCompatActivity {
    private RecyclerView rvFilters;
    private FilterAdapter adapter;
    private List<Type> types;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        // Bind recycler view to adapter
        rvFilters = (RecyclerView) findViewById(R.id.rvFilters);

        // allows for optimizations
        rvFilters.setHasFixedSize(true);

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(PreferenceActivity.this, 1);

        // Unlike ListView, you have to explicitly give a LayoutManager to the RecyclerView to position items on the screen.
        // There are three LayoutManager provided at the moment: GridLayoutManager, StaggeredGridLayoutManager and LinearLayoutManager.
        rvFilters.setLayoutManager(layout);

        // get data
        types = Type.getTypes();

        // Create an adapter
        adapter = new FilterAdapter(PreferenceActivity.this, types);

        // Bind adapter to list
        rvFilters.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}