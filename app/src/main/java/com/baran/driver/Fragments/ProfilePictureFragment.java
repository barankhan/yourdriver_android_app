package com.baran.driver.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Activity.Passenger;
import com.baran.driver.Constants.Constant;
import com.baran.driver.Extras.Utils;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.baran.driver.Services.RetrofitClient;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import static android.app.Activity.RESULT_OK;


public class ProfilePictureFragment extends Fragment implements View.OnClickListener{

    private ImageView imProfilePicture;

    private User currentUser;
    private Button btnUpdatePicture;
    ImageView imPassengerIcon;

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
        currentUser = MainActivity.appPreference.getUserObject(getContext(),getActivity());

        imPassengerIcon =  Passenger.headerView.findViewById(R.id.im_passenger_image);


        if(currentUser.getPicture()!="") {
            Passenger.picasso.get().load(Constant.baseUrl.UPLOADS_URL+currentUser.getPicture()).noPlaceholder().fit().centerCrop()
                    .into(imProfilePicture);
        }

        return root;
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



                Call<User> userCall = MainActivity.serviceApi.updateProfilePicture(profilePictureToUpload, mobileBody);
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Utils.dismissProgressBarSpinner();
                        if(response.isSuccessful()){

                            if(response.body().getResponse().equals("uploaded")){
                                MainActivity.appPreference.setUserObject(response.body());
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
                        Log.e("Error_Uploading",t.toString());
                    }
                });




            }

        }

    }

}