package codepath.com.goingout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import codepath.com.goingout.adapters.FilterAdapter;
import codepath.com.goingout.models.Preference;

public class PreferenceActivity extends AppCompatActivity {
    private RecyclerView rvFilters;
    private FilterAdapter adapter;
    private List<Preference> preferences;
    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        // Bind recycler view to adapter
        rvFilters = (RecyclerView) findViewById(R.id.rvFilters);
        // allows for optimizations
        rvFilters.setHasFixedSize(true);

        // Define 2 column grid layout
        final GridLayoutManager layout = new GridLayoutManager(PreferenceActivity.this, 3);

        // Unlike ListView, you have to explicitly give a LayoutManager to the RecyclerView to position items on the screen.
        // There are three LayoutManager provided at the moment: GridLayoutManager, StaggeredGridLayoutManager and LinearLayoutManager.
        rvFilters.setLayoutManager(layout);

        // get data
        preferences = Preference.getTypes();

        // Create an adapter
        adapter = new FilterAdapter(PreferenceActivity.this, preferences);

        // Bind adapter to list
        rvFilters.setAdapter(adapter);

//        if (adapter.filter.size()>=1){
//            floatingActionButton.setClickable(true);
//        }else{
//            floatingActionButton.setBackgroundColor(Color.BLACK);
//            floatingActionButton.setClickable(true);
//
//        }

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

    public void toFeed(View view) {
        ArrayList<String> filter = adapter.getAdapter();
        Intent intent = new Intent(PreferenceActivity.this, EventListActivity.class);
        intent.putStringArrayListExtra("preferences", filter);
        startActivity(intent);
    }
}