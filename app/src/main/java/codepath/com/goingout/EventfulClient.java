package codepath.com.goingout;

/**
 * Created by joh on 7/18/17.
 */

public class EventfulClient {

    //public static final BaseApi REST_API_INSTANCE = EventfulApi.instance(); // Change this
    public static final String REST_URL = "http://api.eventful.com/rest/"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "a036d39b5258419788ce";       // Change this
    public static final String REST_CONSUMER_SECRET = "61ac112466870eeaafbb"; // Change this

    // Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
    public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

    // See https://developer.chrome.com/multidevice/android/intents
    public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";



}
