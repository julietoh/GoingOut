package codepath.com.goingout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import codepath.com.goingout.adapters.PostAdapter;
import codepath.com.goingout.models.Post;
import codepath.com.goingout.models.User;

public class DetailsActivity extends AppCompatActivity {

    public final String APP_TAG = "GoingOutApp";
    public String photoFileName = "photo.jpg";
    public String textFileName = "text.txt";
    public String videoFileName = "video.mp4";


    //instance fields
    Context context;
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
    VideoView vvVideo;
    FloatingActionButton fabUpload;
    Toolbar detailsToolbar;
    ImageView ivBackground;

    private static final int VIDEO_REQUEST_CODE = 20;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int TEXT_REQUEST_CODE = 3;

    private StorageReference storage;
    private ProgressDialog mProgress;
    DatabaseReference databasePosts;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvDetailTitle = (TextView) findViewById(R.id.tvDetailTitle);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        fabUpload = (FloatingActionButton) findViewById(R.id.fabUpload);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        vvVideo = (VideoView) findViewById(R.id.vvVideo);
        // set to root data tree
        storage = FirebaseStorage.getInstance().getReference();
        databasePosts = FirebaseDatabase.getInstance().getReference("posts");

        mProgress = new ProgressDialog(this);

        final String title = getIntent().getStringExtra("title");
        String time = getIntent().getStringExtra("time");
        String location = getIntent().getStringExtra("location");
        String price = getIntent().getStringExtra("price");
        String image_url = getIntent().getStringExtra("image_url");
        currentUser = Parcels.unwrap(getIntent().getParcelableExtra("current_user"));


        //initialize the client
        client = new AsyncHttpClient();
        //initialize the list of movies
        posts = new ArrayList<>();
        //initialize the adapter -- movies array list cannot be reinitialized after this point
        postAdapter = new PostAdapter(posts);


        //resolve the recycler view and connect a layout manager and the adapter
        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(postAdapter);
        //rvPosts.getLayoutManager().scrollToPosition(0);


        detailsToolbar = (Toolbar) findViewById(R.id.detailsToolbar);

        tvDetailTitle.setText(title);
        tvTime.setText(time);
        tvLocation.setText(location);

        detailsToolbar.setTitle("Event: "+title);
        ivBackground = (ImageView) findViewById(R.id.ivBackground);




        if (image_url != null) {
            String GoogleUriString = image_url;
            Uri GoogleUri = Uri.parse(GoogleUriString);


            Picasso.with(this).load(GoogleUri).into(ivBackground);
        } else {
            String GoogleUriString = "https://maps.googleapis.com/maps/api/plac/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyB4MzgjTcqzPIL_6v468qvhyLCbPzeTKlo";
            Uri GoogleUri = Uri.parse(GoogleUriString);
            Picasso.with(this).load(GoogleUri).into(ivBackground);
        }

        rvPosts.getLayoutManager().scrollToPosition(0);


        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        AppBarLayout app_bar_layout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    detailsToolbar.setTitle(title);
                    isShow = true;
                } else if(isShow) {
                    detailsToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });


        // listen to add button click
        fabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setPositiveButton("New Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // upload post
                        Intent i = new Intent(DetailsActivity.this, ComposeActivity.class);
                        // plain text option
                        i.putExtra("mode", 2); // pass arbitrary data to launched activity
                        startActivityForResult(i, TEXT_REQUEST_CODE);



                    }

                });
                builder.setNeutralButton("Choose from library",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // upload an image
                            }
                        });
                builder.setNegativeButton("Take Photo or Video", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // create second alert dialog to allow user to choose video or image
                        dialog.dismiss();
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(DetailsActivity.this);
                        builder2.setPositiveButton("Photo", new DialogInterface.OnClickListener() {
                            // launch image capture
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName, CAMERA_REQUEST_CODE));
                                        // ensure there's a camera activity to handle the intent
                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                            // Start the image capture intent to take photo
                                            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                                        }
                                    }
                                });
                        builder2.setNegativeButton("Video", new DialogInterface.OnClickListener() {
                            // launch video capture
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                    takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(videoFileName, VIDEO_REQUEST_CODE));
                                    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(takeVideoIntent, VIDEO_REQUEST_CODE);
                                    }
                            }
                        });
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        // increase width of buttons and center
                        final Button positiveButton = dialog2.getButton(AlertDialog.BUTTON_POSITIVE);
                        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                        positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;;
                        positiveButtonLL.gravity = Gravity.CENTER;
                        positiveButton.setLayoutParams(positiveButtonLL);
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
                //posts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    posts.add(post);
                }
                //PostAdapter adapter = new PostAdapter(posts);
                //rvPosts.setAdapter(adapter);

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // MOVE THIS ?
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        postAdapter.clear();
        StorageReference fileRef = null;
        Uri takenMediaUri = null;

        // file path to store image
        // generate a unique Id
        UUID randomId = new UUID(3045, 7102);
        randomId = randomId.randomUUID();

        // plain text post
        if (requestCode == TEXT_REQUEST_CODE && resultCode == RESULT_OK) {
            // Extract the data returned from the child Activity.
            //addImagePost(null, requestCode, returnValue);
            // store into firebase
            //takenMediaUri = getPhotoFileUri(textFileName, TEXT_REQUEST_CODE);
            //fileRef = storage.child("Text/")
                    //.child(randomId.toString());
            addImagePost(null, requestCode, data.getStringExtra("body"));
            // video post
        } else {
            if (requestCode == VIDEO_REQUEST_CODE && resultCode == RESULT_OK) {
                takenMediaUri = data.getData();
                fileRef = storage.child("Videos/")
                        .child(randomId.toString());
                // image post
            } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                takenMediaUri = getPhotoFileUri(photoFileName, CAMERA_REQUEST_CODE);
                //takenMediaUri = data.getData();

                fileRef = storage.child("Photos/")
                        .child(randomId.toString());
                // Result was a failure
            } else {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }

            mProgress.setMessage("Uploading ...");
            mProgress.show();
            // upload process
            fileRef.putFile(takenMediaUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();
                    Toast.makeText(DetailsActivity.this, "Uploaded", Toast.LENGTH_LONG).show();

                    @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();
                    if (requestCode == TEXT_REQUEST_CODE) {
                        String returnValue = data.getStringExtra("body");
                        addImagePost(downloadUri, requestCode, returnValue);
                    } else {
                        addImagePost(downloadUri, requestCode, null);
                    }
                }

            });


            rvPosts.getLayoutManager().scrollToPosition(0);

        }


    }


    public void addImagePost(Uri uri, final int requestCode, String body) {
        // creates a unique string inside Posts and gets the key
        String id = databasePosts.push().getKey();
        Post post;
        if (requestCode == TEXT_REQUEST_CODE) {
            // creates a new post with only text
            post = new Post(id, currentUser.getFirstName()+" "+currentUser.getLastName(), getTimeStamp(), body, -1 * new Date().getTime());
        }
        else if (requestCode == CAMERA_REQUEST_CODE) {
            // creates a new post with image
            post = new Post(id, currentUser.getFirstName()+" "+currentUser.getLastName(), getTimeStamp(), null, uri.toString(), 1, -1 * new Date().getTime());
        } else {
            // Create a new post with video
            post = new Post(id, currentUser.getFirstName()+" "+currentUser.getLastName(), getTimeStamp(), null, uri.toString(), "arbitrary", -1 * new Date().getTime());
        }
        databasePosts.child(id).setValue(post);
        posts.add(0, post);
        postAdapter.notifyItemInserted(0);
        rvPosts.scrollToPosition(0);

        // add new post to view


//        rvPosts.getLayoutManager().scrollToPosition(0);
        //rvPosts.getLayoutManager().scrollToPosition(0);

        //Toast.makeText(this, "added to database", Toast.LENGTH_LONG).show();

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
    public Uri getPhotoFileUri(String fileName, int requestCode) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir;
            // create image file
            if (requestCode == CAMERA_REQUEST_CODE) {
                mediaStorageDir = new File(
                        getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
            } else if (requestCode == VIDEO_REQUEST_CODE){
                // create video file
                mediaStorageDir = new File(
                        getExternalFilesDir(Environment.DIRECTORY_MOVIES), APP_TAG);
            } else {
                mediaStorageDir = new File(
                        getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), APP_TAG);
            }
            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

            // wrap File object into a content provider
            // required for API >= 24

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