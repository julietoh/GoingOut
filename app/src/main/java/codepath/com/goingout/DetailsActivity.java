package codepath.com.goingout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;

import codepath.com.goingout.adapters.PostAdapter;
import codepath.com.goingout.models.Post;

public class DetailsActivity extends AppCompatActivity {

    //instance fields
    AsyncHttpClient client;


    // the list of posts
    ArrayList<Post> posts;
    // the recycler view
    RecyclerView rvPosts;
    //the adapter wired to the recycler view
    PostAdapter adapter;
    TextView tvDetailTitle;
    TextView tvLocation;
    TextView tvTime;
    FloatingActionButton fabUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Create a storage reference from our app
        FirebaseStorage storage = null;
        StorageReference storageRef = storage.getReference();

        // Create a child reference
        // imagesRef now points to "images"
        StorageReference imagesRef = storageRef.child("images");

        // Child references can also take paths
        // spaceRef now points to "images/space.jpg
        // imagesRef still points to "images"
        StorageReference spaceRef = storageRef.child("images/space.jpg");

        // Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child("mountains.jpg");

        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");




//        // TESTING - Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        // TESTING - Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d("fb", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("fb", "Failed to read value.", error.toException());
//            }
//        });
//
//        myRef.setValue("yoooo");


        storage = FirebaseStorage.getInstance();

        tvDetailTitle = (TextView) findViewById(R.id.tvDetailTitle);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        fabUpload = (FloatingActionButton) findViewById(R.id.fabUpload);

        // listen to add button click
        fabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                // builder.setTitle("Upload or Take a photo");
                builder.setPositiveButton("New Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // upload post
                    }

                });
                builder.setNeutralButton("Choose from library",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // upload and image
                            }
                        });
                builder.setNegativeButton("Take Photo or Video", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // take photo
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        String title = getIntent().getStringExtra("title");
        String time = getIntent().getStringExtra("time");
        String location = getIntent().getStringExtra("location");

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
