package codepath.com.goingout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.parceler.Parcels;

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
        //btnAdd = (Button) findViewById(R.id.btnAdd);

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
            uri = data.getData();
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
