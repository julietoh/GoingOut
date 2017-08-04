package codepath.com.goingout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import org.parceler.Parcels;

import java.io.InputStream;

import codepath.com.goingout.models.User;

public class MainActivity extends AppCompatActivity {
    String name;
    String surname;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle inBundle = getIntent().getExtras();
        name = inBundle.get("name").toString();
        surname = inBundle.get("surname").toString();
        String imageUrl = inBundle.get("imageUrl").toString();
        currentUser = Parcels.unwrap(getIntent().getParcelableExtra("current_user"));

        TextView nameView = (TextView)findViewById(R.id.nameAndSurname);
        nameView.setText("" + currentUser.getFirstName() + " " + currentUser.getLastName() + "!");

        // logout function
        Button logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
        new MainActivity.DownloadImage((ImageView)findViewById(R.id.profileImage)).execute(imageUrl);
    }



    public void toPreferences(View view) {
        Intent intent = new Intent(this, PreferenceActivity.class);
        intent.putExtra("current_user", Parcels.wrap(currentUser));
        this.startActivity(intent);
    }


    // get profile image URL
    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
