package codepath.com.goingout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.loopj.android.http.AsyncHttpClient;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import codepath.com.goingout.adapters.PostAdapter;
import codepath.com.goingout.models.Post;

public class DetailsActivity extends AppCompatActivity {

    public final String APP_TAG = "GoingOutApp";
    public String photoFileName = "photo.jpg";
    //instance fields
    AsyncHttpClient client;
    // the list of posts
    ArrayList<Post> posts;
    // the recycler view
    RecyclerView rvPosts;
    //the adapter wired to the recycler view
    PostAdapter postAdapter;
    TextView tvDetailTitle;
    TextView tvLocation;
    TextView tvTime;
    ImageView ivPicture;
    FloatingActionButton fabUpload;
    Uri takenPhotoUri;
    private final int REQUEST_CODE = 20;


    private static final int CAMERA_REQUEST_CODE = 1;

    private StorageReference storage;
    private ProgressDialog mProgress;
    DatabaseReference databasePosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvDetailTitle = (TextView) findViewById(R.id.tvDetailTitle);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        fabUpload = (FloatingActionButton) findViewById(R.id.fabUpload);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        // set to root data tree
        storage = FirebaseStorage.getInstance().getReference();
        databasePosts = FirebaseDatabase.getInstance().getReference("posts");

        mProgress = new ProgressDialog(this);

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
        postAdapter = new PostAdapter(posts);

        //getPosts();

        //resolve the recycler view and connect a layout manager and the adapter
        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(postAdapter);

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
                        Intent i = new Intent(DetailsActivity.this, ComposeActivity.class);
                        // plain text option
                        i.putExtra("option", 1);
                        //startActivityforResult(i, REQUEST_CODE);
                        finish();
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
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName));
                        // ensure there's a camera activity to handle the intent
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            // Start the image capture intent to take photo
                            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        Query chronological = databasePosts.orderByChild("order");
        chronological.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);

                    posts.add(post);
                }

                PostAdapter adapter = new PostAdapter(posts);
                rvPosts.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // MOVE THIS ?
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            takenPhotoUri = getPhotoFileUri(photoFileName);

            // by this point the camera photo is on disk
            Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());

            mProgress.setMessage("Uploading Image ...");
            mProgress.show();

            // ivPicture.setImageBitmap(takenImage);

        } else { // Result was a failure
            Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }


            // file path to store image TODO: Change this!
            // generate a unique Id
            UUID randomId = new UUID(3045, 7102);
            randomId = randomId.randomUUID();

            // place in custom location inside storage to avoid overwrite
            //StorageReference photosRef = storage.child("Photos/" + takenPhotoUri.getLastPathSegment());
            StorageReference fileRef =
                    storage.child("Photos/")
                    .child(randomId.toString());

            // upload process
            fileRef.putFile(takenPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();
                    Toast.makeText(DetailsActivity.this, "Uploaded", Toast.LENGTH_LONG).show();

                    @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();
                    addImagePost(downloadUri);
                    //Picasso.with(DetailsActivity.this).load(downloadUri).fit().centerCrop().into(ivPicture);
                }
            });

        }

    public void addImagePost(Uri uri) {
        // creates a unique string inside Posts and gets the key
        String id = databasePosts.push().getKey();

        // creates a new post
        Post post = new Post(id, "Juliet Oh", getTimeStamp(), null, uri.toString(), 1, -1 * new Date().getTime());

        // add new post to view
        posts.add(0, post);
        postAdapter.notifyItemInserted(0);
        rvPosts.scrollToPosition(0);

        databasePosts.child(id).setValue(post);
        Toast.makeText(this, "added to database", Toast.LENGTH_LONG).show();

    }
    public String getTimeStamp() {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String date = sdf.format(new Date());

        // converting from military time to standard time
        int hour = Integer.parseInt(date.substring(11, 13));
        String time;
        if (hour > 12) {
            // pm
            hour = (hour - 12);
            time = hour + date.substring(13, 16) + "pm";
        } else if (hour == 0) {
            // am
             hour = 12;
             time = hour + date.substring(13, 16) + "am";
        } else if (hour == 12) {
            // pm
            time = hour + date.substring(13, 16) + "pm";
        } else {
            // am
            time = hour + date.substring(13, 16) + "am";
        }
        return time;
    }


    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

            // wrap File object into a content provider
            // required for API >= 24
            // See https://guides.codepath.com/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            return FileProvider.getUriForFile(DetailsActivity.this, "com.codepath.fileprovider", file);
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public void toReviews(View view) {
        Intent intent = new Intent(DetailsActivity.this, ReviewActivity.class);
        startActivity(intent);
    }

}
