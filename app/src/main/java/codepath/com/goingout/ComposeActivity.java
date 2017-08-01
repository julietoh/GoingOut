package codepath.com.goingout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ComposeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        final EditText ETpost = (EditText)findViewById(R.id.ETpost);
        Button BTNpost = (Button)findViewById(R.id.BTNpost);
        BTNpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("body", ETpost.getText().toString());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }});


    }

    public void onSubmit(View v) {

    }
}
