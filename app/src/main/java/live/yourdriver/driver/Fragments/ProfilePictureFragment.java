package live.yourdriver.driver.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Model.User;
import live.yourdriver.driver.Services.RetrofitClient;
import live.yourdriver.driver.Services.ServiceApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import live.yourdriver.driver.Activity.Passenger;
import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.Utils;

import live.yourdriver.driver.R;;
import com.squareup.picasso.Picasso;

import java.io.File;

import static android.app.Activity.RESULT_OK;


public class ProfilePictureFragment extends Fragment implements View.OnClickListener{

    private ImageView imProfilePicture;

    private User currentUser;
    private Button btnUpdatePicture;
    ImageView imPassengerIcon;
    public static AppPreference appPreference;
    public static ServiceApi serviceApi;

    public ProfilePictureFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_profile_picture, container, false);
        imProfilePicture = root.findViewById(R.id.im_profile_pic);
        btnUpdatePicture = root.findViewById(R.id.btn_profile_picture);
        btnUpdatePicture.setOnClickListener(this);
        imProfilePicture.setOnClickListener(this);
        appPreference = new AppPreference(getContext());
        serviceApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_USERS_API).create(ServiceApi.class);

        currentUser = appPreference.getUserObject(getContext(),getActivity());

        imPassengerIcon =  Passenger.headerView.findViewById(R.id.im_passenger_image);


        if(currentUser.getPicture()!="") {
            Passenger.picasso.get().load(Constant.baseUrl.UPLOADS_URL+currentUser.getPicture()).noPlaceholder().fit().centerCrop()
                    .into(imProfilePicture);
        }

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        String[] PERMISSIONS = {
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
        };

        if (!Utils.hasPermissions(getContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_profile_picture:
            case R.id.im_profile_pic:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                String title = "Please select your Picture";
                int RequestCode = 150;
                startActivityForResult(Intent.createChooser(intent,title), RequestCode);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK) {
            ImageView v = null;
            if(requestCode==150){
                Uri selectedImageURI = data.getData();
                Picasso.get().load(selectedImageURI).noPlaceholder().centerCrop()
                        .fit().into(imProfilePicture);
                Utils.showProgressBarSpinner(getContext());

                File profilePicture = new File(Utils.getRealPathFromURI(getContext(), selectedImageURI));

                RequestBody profilePictureRequestBody = RequestBody.create(MediaType.parse("image/*"), profilePicture);
                RequestBody mobileBody = RequestBody.create(MediaType.parse("text/plain"), currentUser.getMobile());
                MultipartBody.Part profilePictureToUpload = MultipartBody.Part.createFormData("picture", "profile_pic_" + profilePicture.getName(), profilePictureRequestBody);



                Call<User> userCall = serviceApi.updateProfilePicture(profilePictureToUpload, mobileBody);
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Utils.dismissProgressBarSpinner();
                        if(response.isSuccessful()){

                            if(response.body().getResponse().equals("uploaded")){
                                appPreference.setUserObject(response.body());
                                currentUser = response.body();
                                Utils.showAlertBox(getActivity(),"Picture Updated Successfully");
                                if(currentUser.getPicture()!="") {
                                    Passenger.picasso.get().load(Constant.baseUrl.UPLOADS_URL+currentUser.getPicture()).noPlaceholder().fit().centerCrop()
                                            .into(imPassengerIcon);
                                }
                            }else{

                            }



                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Utils.dismissProgressBarSpinner();
                        Utils.showAlertBox(getActivity(),"Unable to connect to server.");
//                        Log.e("Error_Uploading",t.toString());
                    }
                });




            }

        }

    }

}
