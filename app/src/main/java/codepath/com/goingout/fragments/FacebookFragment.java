//package codepath.com.goingout.fragments;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.facebook.CallbackManager;
//import com.facebook.FacebookAuthorizationException;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//import com.facebook.share.Sharer;
//
//import codepath.com.goingout.R;
//
//import static com.facebook.FacebookSdk.getApplicationContext;
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link FacebookFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link FacebookFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class FacebookFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    private LoginButton loginButton;
//    private OnFragmentInteractionListener mListener;
//    private CallbackManager callbackManager;
//
//    public FacebookFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment FacebookFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static FacebookFragment newInstance(String param1, String param2) {
//        FacebookFragment fragment = new FacebookFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View v = inflater.inflate(R.layout.fragment_facebook, container, false);
//
//        loginButton = (LoginButton) v.findViewById(R.id.loginButton);
//        loginButton.setFragment(this);
//
//        private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
//            @Override
//            public void onCancel() {
//                Log.d("FacebookFragment", "Canceled");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d("FacebookFragment", String.format("Error: %s", error.toString()));
//                String title = getString(R.string.error);
//                String alertMessage = error.getMessage();
//                showResult(title, alertMessage);
//            }
//
//            @Override
//            public void onSuccess(Sharer.Result result) {
//                Log.d("FacebookFragment", "Success!");
//                if (result.getPostId() != null) {
//                    String title = getString(R.string.success);
//                    String id = result.getPostId();
//                    String alertMessage = getString(R.string.successfully_posted_post, id);
//                    showResult(title, alertMessage);
//                }
//            }
//
//            private void showResult(String title, String alertMessage) {
//                new AlertDialog.Builder(getActivity())
//                        .setTitle(title)
//                        .setMessage(alertMessage)
//                        .setPositiveButton(R.string.ok, null)
//                        .show();
//            }
//        };
//
//        callbackManager = CallbackManager.Factory.create();
//        // Callback registration
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Toast toast = Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_SHORT);
//                postingEnabled = true;
//                postPhotoButton.setVisibility(View.VISIBLE);
//                postStatusUpdateButton.setVisibility(View.VISIBLE);
//                getUserInterests.setVisibility(View.VISIBLE);
//
//                toast.show();
//                handlePendingAction();
//                updateUI();
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//                if (pendingAction != PendingAction.NONE) {
//                    showAlert();
//                    pendingAction = PendingAction.NONE;
//                }
//                updateUI();
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                if (pendingAction != PendingAction.NONE
//                        && exception instanceof FacebookAuthorizationException) {
//                    showAlert();
//                    pendingAction = PendingAction.NONE;
//                }
//                updateUI();
//
//            }
//
//            private void showAlert() {
//                new AlertDialog.Builder(getActivity())
//                        .setTitle(R.string.cancelled)
//                        .setMessage(R.string.permission_not_granted)
//                        .setPositiveButton(R.string.ok, null)
//                        .show();
//            }
//
//        });
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//                        LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile"));
//            if(!postingEnabled) {
//
//                postingEnabled = true;
//                postPhotoButton.setVisibility(View.VISIBLE);
//                postStatusUpdateButton.setVisibility(View.VISIBLE);
//                getUserInterests.setVisibility(View.VISIBLE);
//            }else{
//
//                postingEnabled = false;
//                postPhotoButton.setVisibility(View.GONE);
//                postStatusUpdateButton.setVisibility(View.GONE);
//                getUserInterests.setVisibility(View.GONE);
//
//            }
//        });
//                return v;
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//}
