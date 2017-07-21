package codepath.com.goingout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;

import codepath.com.goingout.adapters.PostAdapter;
import codepath.com.goingout.models.Post;

public class DetailsActivity extends AppCompatActivity {

    //instance fields
    AsyncHttpClient client;


    // the list of currenly playing movies
    ArrayList<Post> posts;
    // the recylcler view
    RecyclerView rvPosts;
    //the adapter wired to the recycler view
    PostAdapter adapter;

    TextView tvDetailTitle;
    TextView tvLocation;
    TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvDetailTitle = (TextView) findViewById(R.id.tvDetailTitle);
        tvTime = (TextView) findViewById(R.id.tvTime) ;
        tvLocation = (TextView) findViewById(R.id.tvLocation);

        String title = getIntent().getStringExtra("title");
        String time = getIntent().getStringExtra("time");
        String location =  getIntent().getStringExtra("location");

        tvDetailTitle.setText(title);
        tvTime.setText(time);
        tvLocation.setText(location);


        //initialize the client
        client = new AsyncHttpClient();
        //initialize the list of movies
        posts = new ArrayList<>();
        //initialize the adapter -- movies array list cannot be reinitialized after this point
        adapter = new PostAdapter(posts);

        getPosts();

        //resolve the recycler view and connect a layout manager and the adapter
        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(adapter);


    }

    public void toReviews(View view) {
        Intent intent = new Intent(DetailsActivity.this, ReviewActivity.class);
        startActivity(intent);
    }

    public void getPosts() {
        for (int i = 10; i>1; i-=3)
        {
            posts.add(new Post("Amadou",i+"PM", "Wow this club is awesome"));
            posts.add(new Post("Juliet",(i-1)+"PM", "Nah, man this sucks lol"));
            posts.add(new Post("Rafael",(i-2)+"PM", "We have so many better clubs in panama! What is going on here omg"));
        }
    }
}
