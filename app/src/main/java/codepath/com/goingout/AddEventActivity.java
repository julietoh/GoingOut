package codepath.com.goingout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import codepath.com.goingout.models.Event;

public class AddEventActivity extends AppCompatActivity {
    ImageButton backButton;
    Button btnAdd;
    EditText etTitle;
    EditText etDate;
    EditText etTime;
    EditText etLocation;
    Button btnUploadPic;
    Context context;
    Uri uri;
    Event event;
    StorageReference storage;

    //Bitmap bitmap = null;
    private final int GALLERY_REQUEST = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        this.context = this;
        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
            }
        });
        etTitle = (EditText) findViewById(R.id.etTitle);
        etDate = (EditText) findViewById(R.id.etDate);
        etTime = (EditText) findViewById(R.id.etTime);
        etLocation = (EditText) findViewById(R.id.etLocation);
        btnUploadPic = (Button) findViewById(R.id.btnUploadPic);
        storage = FirebaseStorage.getInstance().getReference();

        btnUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GALLERY_REQUEST);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            //Uri downloadUrl = null;
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // generate random UUID
            UUID randomId = new UUID(3045, 7102);
            randomId = randomId.randomUUID();

            StorageReference fileRef = storage.child("Events/").child(randomId.toString());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] imageData = baos.toByteArray();
            UploadTask uploadTask = fileRef.putBytes(imageData);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    uri = downloadUrl;
                    //sendMsg("" + downloadUrl, 2);
                    //Log.d("downloadUrl-->", "" + downloadUrl);
                }
            });
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
        }
    }

//    private void uploadFile(Bitmap bitmap) {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReferenceFromUrl("Your url for storage");
//        StorageReference mountainImagesRef = storageRef.child("images/" + chat_id + Utils.getCurrentTimeStamp() + ".jpg");
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
//        byte[] data = baos.toByteArray();
//        UploadTask uploadTask = mountainImagesRef.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                sendMsg("" + downloadUrl, 2);
//                Log.d("downloadUrl-->", "" + downloadUrl);
//            }
//        });
//
//    }
        public void makeEvent(View view) {
            Event event = new Event();
            event.setTitle(etTitle.getText().toString());
            event.setDate(etDate.getText().toString()+" "+etTime.getText().toString());
            event.setLocation(etLocation.getText().toString());
            event.setImage(uri.toString());

        Intent intent = new Intent();
        intent.putExtra("event", Parcels.wrap(event));
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_up);

    }

}
