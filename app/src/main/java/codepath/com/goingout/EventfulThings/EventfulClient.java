//package codepath.com.goingout;
//
//import android.content.Context;
//
//import com.codepath.oauth.OAuthBaseClient;
//import com.github.scribejava.core.builder.api.BaseApi;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
//
///**
// * Created by joh on 7/18/17.
// */
//
//public class EventfulClient extends OAuthBaseClient {
//    public static final BaseApi REST_API_INSTANCE = EventfulApi.instance();
//    public static final String REST_URL = "http://api.eventful.com/json";
//    public static final String REST_CONSUMER_KEY = "bd01112765338e0fd66a";
//    public static final String REST_CONSUMER_SECRET = "d8f56a26370ea1151279";
//
//    public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";
//
//    public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";
//
//
//    public EventfulClient(Context context){
//        super(context, REST_API_INSTANCE,
//                REST_URL,
//                REST_CONSUMER_KEY,
//                REST_CONSUMER_SECRET,
//                String.format(
//                        REST_CALLBACK_URL_TEMPLATE,context.getString(R.string.intent_host),
//                        context.getString(R.string.intent_scheme),
//                        context.getPackageName(), FALLBACK_URL));
//    }
//
//    public void getEvents(AsyncHttpResponseHandler handler) {
//        String apiUrl = getApiUrl("/events/search");
//        RequestParams params = new RequestParams();
//        params.put("location", "San Francisco");
//        params.put("page_size", 25);
//        params.put("keywords", "art");
//        client.get(apiUrl,params,handler);
//    }
//}
