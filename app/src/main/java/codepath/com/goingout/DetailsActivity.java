package codepath.com.goingout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
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
import android.widget.RatingBar;
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
import io.kickflip.sdk.Kickflip;
import io.kickflip.sdk.api.KickflipCallback;
import io.kickflip.sdk.api.json.Response;
import io.kickflip.sdk.api.json.Stream;
import io.kickflip.sdk.av.BroadcastListener;
import io.kickflip.sdk.av.SessionConfig;
import io.kickflip.sdk.exception.KickflipException;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Uri streamUrl;

    private boolean mKickflipReady = false;

    private BroadcastListener mBroadcastListener = new BroadcastListener() {
        @Override
        public void onBroadcastStart() {
            Log.i(TAG, "onBroadcastStart");
        }

        @Override
        public void onBroadcastLive(Stream stream) {
            Log.i(TAG, "onBroadcastLive @ " + stream.getKickflipUrl());
            streamUrl = Uri.parse(stream.getStreamUrl());
            addImagePost(streamUrl, LIVE_REQUEST_CODE, null);

        }

        @Override
        public void onBroadcastStop() {
            Log.i(TAG, "onBroadcastStop");
        }

        @Override
        public void onBroadcastError(KickflipException error) {
            Log.i(TAG, "onBroadcastError " + error.getMessage());
        }
    };

    private String mRecordingOutputPath = new File(Environment.getExternalStorageDirectory(), "MySampleApp/index.m3u8").getAbsolutePath();

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
    TextView tvPrice;
    TextView tvTime;
    ImageView ivPicture;
    VideoView vvVideo;
    FloatingActionButton fabUpload;
    Toolbar detailsToolbar;
    ImageView ivBackground;
    RatingBar ratingBar;

    private static final int VIDEO_REQUEST_CODE = 20;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int TEXT_REQUEST_CODE = 3;
    private static final int LIVE_REQUEST_CODE = 10;


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
        tvPrice = (TextView) findViewById(R.id.tvPrice);
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
        int rating = getIntent().getIntExtra("rating", 0);
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
        setSupportActionBar(detailsToolbar);

        tvDetailTitle.setText(title);
        tvTime.setText(time);
        tvLocation.setText(location);

        if (price != null) {
            tvPrice.setText(price);
        }


        detailsToolbar.setTitle(title);
        detailsToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ivBackground = (ImageView) findViewById(R.id.ivBackground);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        if (rating > 0) {
            ratingBar.setRating(rating);
        }




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
        collapsingToolbarLayout.setTitle(title);
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
                builder.setNeutralButton("Go Live",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // upload an image

                                if (mKickflipReady) {
                                    startBroadcastingActivity();
                                } else {
                                    new AlertDialog.Builder(context)
                                            .setTitle(getString(R.string.dialog_title_not_ready))
                                            .setMessage(getString(R.string.dialog_msg_not_ready))
                                            .setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                }
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

        postAdapter.notifyItemRangeChanged(0, postAdapter.getItemCount());
        rvPosts.getLayoutManager().scrollToPosition(0);

        Kickflip.setup(this, "F?u=7QKTkeO2RrrPuAhYb-5swB9r2!be1-O_vGip",
                "73jn;@dWkEXts54N:Z@n;:T_MmUwCxv3Y01PFhjnz!rvQF38GMEaFLV-HtMxZhOrOGk9l@F9.5ZLqpnWzVy@Q5UotLfysiiS;PeJVxi45FRls@@JNmciHR:aO@ABjFQG",
                new KickflipCallback() {
                    @Override
                    public void onSuccess(Response response) {
                        mKickflipReady = true;
                    }

                    @Override
                    public void onError(KickflipException error) {

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
        }


    }


    public void addImagePost(Uri uri, final int requestCode, String body) {
        // creates a unique string inside Posts and gets the key
        String id = databasePosts.push().getKey();
        Post post;
        if (requestCode == TEXT_REQUEST_CODE) {
            // creates a new post with only text
            post = new Post(id, currentUser.getFirstName()+" "+currentUser.getLastName(), getTimeStamp(), body, -1 * new Date().getTime());
        } else if (requestCode == LIVE_REQUEST_CODE) {
            post = new Post(id, currentUser.getFirstName()+" "+currentUser.getLastName(), getTimeStamp(), null, uri.toString(), "arbitrary", -1 * new Date().getTime(), true);

        } else if (requestCode == CAMERA_REQUEST_CODE) {
            // creates a new post with image
            post = new Post(id, currentUser.getFirstName()+" "+currentUser.getLastName(), getTimeStamp(), null, uri.toString(), 1, -1 * new Date().getTime());
        } else {
            // Create a new post with video
            post = new Post(id, currentUser.getFirstName()+" "+currentUser.getLastName(), getTimeStamp(), null, uri.toString(), "arbitrary", -1 * new Date().getTime(), false);
        }
        databasePosts.child(id).setValue(post);
        posts.add(0, post);
//        postAdapter.notifyItemInserted(0);
        postAdapter.notifyItemRangeChanged(0, postAdapter.getItemCount());
        rvPosts.getLayoutManager().scrollToPosition(0);

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

    private void startBroadcastingActivity() {
        configureNewBroadcast();
        Kickflip.startBroadcastActivity(this, mBroadcastListener);
    }

    private void configureNewBroadcast() {
        // Should reset mRecordingOutputPath between recordings
        SessionConfig config = Util.create720pSessionConfig(mRecordingOutputPath);
        //SessionConfig config = Util.create420pSessionConfig(mRecordingOutputPath);
        Kickflip.setSessionConfig(config);
    }

}