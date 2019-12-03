package com.baran.driver.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Extras.Utils;
import com.baran.driver.Model.DriverServerResponse;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import static android.app.Activity.RESULT_OK;


public class DriverDataUpdateFragmentStep1 extends Fragment{


    private TextView tvDriverPic,tvDriverLicence,tvDriverCNICFront,tvDriverCNICRare;
    private ImageView imDriverPic,imDriverLicence,imDriverCNICFront, imDriverCNICRear;
    private EditText etDriverName,etDriverFather,etDriverCNIC;
    private Button submitButton;
    public final int PICK_DRIVER_IMAGE = 1;
    public final int PICK_DRIVER_LICENCE = 2;
    public final int PICK_DRIVER_CNIC_FRONT = 3;
    public final int PICK_DRIVER_CNIC_REAR = 4;
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 60;
    public DriverDataUpdateFragmentStep1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_driver_data_update_step_1, container, false);

        etDriverName = root.findViewById(R.id.et_driver_name);
        etDriverFather = root.findViewById(R.id.et_driver_father);
        etDriverCNIC = root.findViewById(R.id.et_driver_cnic);



        tvDriverPic = root.findViewById(R.id.tv_driver_pic);
        imDriverPic = root.findViewById(R.id.im_driver_pic);
        imDriverPic.setTag(R.id.image_uri,"@");


        tvDriverLicence = root.findViewById(R.id.tv_driver_licence);
        imDriverLicence = root.findViewById(R.id.im_driver_licence);
        imDriverLicence.setTag(R.id.image_uri,"@");

        tvDriverCNICFront = root.findViewById(R.id.tv_driver_cnic_front);
        imDriverCNICFront = root.findViewById(R.id.im_driver_cnic_front);
        imDriverCNICFront.setTag(R.id.image_uri,"@");


        tvDriverCNICRare = root.findViewById(R.id.tv_driver_cnic_rare);
        imDriverCNICRear = root.findViewById(R.id.im_driver_cnic_rare);
        imDriverCNICRear.setTag(R.id.image_uri,"@");

        submitButton = root.findViewById(R.id.btn_save_driver_data_step1);

        tvDriverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(v,PICK_DRIVER_IMAGE);
            }
        });
        imDriverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(v,PICK_DRIVER_IMAGE);
            }
        });

        tvDriverLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(v,PICK_DRIVER_LICENCE);
            }
        });
        imDriverLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(v,PICK_DRIVER_LICENCE);
            }
        });


        tvDriverCNICFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(v,PICK_DRIVER_CNIC_FRONT);
            }
        });
        imDriverCNICFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(v,PICK_DRIVER_CNIC_FRONT);
            }
        });


        tvDriverCNICRare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(v, PICK_DRIVER_CNIC_REAR);
            }
        });
        imDriverCNICRear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(v, PICK_DRIVER_CNIC_REAR);
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverRegisterStep1();
            }
        });

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }



        return root;
    }


    private void selectPicture(View v,int RequestCode){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        CharSequence title = "";
        switch (RequestCode){
            case PICK_DRIVER_IMAGE:
                title = "Please select your Picture";
                break;
            case PICK_DRIVER_CNIC_FRONT:
                title = "Please select CNIC Front Image";
                break;
            case PICK_DRIVER_CNIC_REAR:
                title = "Please select CNIC Rear Image";
                break;
            case PICK_DRIVER_LICENCE:
                title = "Please select Licence Image";
                break;
        }
        startActivityForResult(Intent.createChooser(intent,title), RequestCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK) {
            ImageView v = null;
            switch (requestCode) {
                case PICK_DRIVER_IMAGE:
                    v = imDriverPic;
                    break;
                case PICK_DRIVER_LICENCE:
                    v = imDriverLicence;
                    break;
                case PICK_DRIVER_CNIC_FRONT:
                    v = imDriverCNICFront;
                    break;
                case PICK_DRIVER_CNIC_REAR:
                    v = imDriverCNICRear;
                    break;
            }

            if (v != null) {
                Uri selectedImageURI = data.getData();
                v.setTag(R.id.image_uri, selectedImageURI);
                Picasso.get().load(selectedImageURI).noPlaceholder().centerCrop()
                        .fit().into(v);


                File cnicFront = new File(selectedImageURI.getPath());

                v.setTag(R.id.media_path, selectedImageURI.getPath());


            }
        }

    }

    private void driverRegisterStep1(){
        final String name = etDriverName.getText().toString();
        final String father = etDriverFather.getText().toString();
        final String cnic = etDriverCNIC.getText().toString();



        if(imDriverPic.getTag(R.id.image_uri).equals("@")){
            MainActivity.appPreference.showToast("Please select your Picture");
        }else if(imDriverCNICRear.getTag(R.id.image_uri).equals("@")){
            MainActivity.appPreference.showToast("Please select your CNIC Rear Image");
        }else if(imDriverCNICFront.getTag(R.id.image_uri).equals("@")){
            MainActivity.appPreference.showToast("Please select your CNIC Front Image");
        }else if (TextUtils.isEmpty(name)){
            MainActivity.appPreference.showToast("Your name is required.");
        }else if (TextUtils.isEmpty(father)){
            MainActivity.appPreference.showToast("Your Mobile Number is required.");
        } else if (TextUtils.isEmpty(cnic)){
            MainActivity.appPreference.showToast("Your email is required.");
        } else if (cnic.length() != 13){
            MainActivity.appPreference.showToast("Please Enter Correct CNIC");
        }

//        else if ( picture_uri.toString()){
//            MainActivity.appPreference.showToast("Please add your picture");
//        }else if (cnic_front_uri == "@"){
//            MainActivity.appPreference.showToast("Please add your CNIC Front Image");
//        }else if (cnic_rear_uri == "@"){
//            MainActivity.appPreference.showToast("Please add your CNIC Rear Image");
//        }

        else {

            final Uri cnic_front_uri = (Uri) imDriverCNICFront.getTag(R.id.image_uri);
            final Uri cnic_rear_uri = (Uri) imDriverCNICRear.getTag(R.id.image_uri);
            final Uri picture_uri = (Uri) imDriverPic.getTag(R.id.image_uri);


            File cnicFront = new File(Utils.getRealPathFromURI(getContext(), cnic_front_uri));
            File cnicRear = new File(Utils.getRealPathFromURI(getContext(), cnic_rear_uri));
            File picture = new File(Utils.getRealPathFromURI(getContext(), picture_uri));


            // Parsing any Media type file
            RequestBody cnicFrontRequestBody = RequestBody.create(MediaType.parse("image/*"), cnicFront);
            RequestBody cnicRearRequestBody = RequestBody.create(MediaType.parse("image/*"), cnicRear);
            RequestBody pictureRequestBody = RequestBody.create(MediaType.parse("image/*"), picture);


            MultipartBody.Part cnicFrontToUpload = MultipartBody.Part.createFormData("cnic_front", "cnic_front_" + cnicFront.getName(), cnicFrontRequestBody);
            MultipartBody.Part cnicRearToUpload = MultipartBody.Part.createFormData("cnic_rear", "cnic_rear_" + cnicRear.getName(), cnicRearRequestBody);
            MultipartBody.Part pictureToUpload = MultipartBody.Part.createFormData("picture", "pic_" + picture.getName(), pictureRequestBody);

            MultipartBody.Part licenceToUpload = MultipartBody.Part.createFormData("licence", "licence" + cnicFront.getName(), cnicFrontRequestBody);
            if (!imDriverLicence.getTag(R.id.image_uri).equals("@")) {
                final Uri licence_uri = (Uri) imDriverLicence.getTag(R.id.image_uri);
                File licence = new File(Utils.getRealPathFromURI(getContext(), licence_uri));
                RequestBody licenceRequestBody = RequestBody.create(MediaType.parse("image/*"), licence);
                licenceToUpload = MultipartBody.Part.createFormData("licence", "licence" + licence.getName(), licenceRequestBody);
            }


            User u = MainActivity.appPreference.getUserObject();

            Call<DriverServerResponse> userCall = MainActivity.serviceApi.doDriverRegistrationStep1(pictureToUpload, licenceToUpload, cnicFrontToUpload, cnicRearToUpload, cnic, name, father, "03336126632");
            userCall.enqueue(new Callback<DriverServerResponse>() {
                @Override
                public void onResponse(Call<DriverServerResponse> call, Response<DriverServerResponse> response) {
                    Log.e("fucked", "i'm ok");
                }

                @Override
                public void onFailure(Call<DriverServerResponse> call, Throwable t) {
                    Log.e("fuckeding", t.toString());
                }
            });
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


}
