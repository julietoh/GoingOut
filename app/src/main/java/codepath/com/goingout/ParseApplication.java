package codepath.com.goingout;

import android.app.Application;

import com.parse.Parse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by joh on 7/21/17.
 */

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Use for troubleshooting - remove line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Use for monitoring Parse OkHttp trafic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myAppId") // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .clientBuilder(builder)
                .server("https://goingoutapp.herokuapp.com/parse/").build());

        // test creation of object
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
    }
}
