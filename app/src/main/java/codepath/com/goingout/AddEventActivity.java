package codepath.com.goingout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import org.parceler.Parcels;

import codepath.com.goingout.models.Event;

public class AddEventActivity extends AppCompatActivity {
    ImageButton backButton;
    EditText etTitle;
    EditText etDate;
    EditText etTime;
    EditText etLocation;
    Context context;

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

    }

    public void makeEvent(View view) {
        Event event = new Event();
        event.setTitle(etTitle.getText().toString());
        event.setDate(etDate.getText().toString()+" "+etTime.getText().toString());
        event.setLocation(etLocation.getText().toString());

        Intent intent = new Intent();
        intent.putExtra("event", Parcels.wrap(event));
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
    }
}
