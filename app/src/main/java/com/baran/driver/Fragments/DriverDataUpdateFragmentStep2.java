package com.baran.driver.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Extras.Utils;
import com.baran.driver.Fragments.passenger.home.HomeFragment;
import com.baran.driver.Model.DriverServerResponse;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import static android.app.Activity.RESULT_OK;


public class DriverDataUpdateFragmentStep2 extends Fragment implements View.OnClickListener {
   EditText etRegAlphabets,etRegYear,etRegNo;
   Spinner spVehicleType;
   ImageView imVehicleFront,imVehicleRear,imVehicleRegistration,imVehicleRoute;
   TextView tvVehicleFront,tvVehicleRear,tvVehicleRegistration,tvVehicleRoute;
   Button btnSaveStep2;
    User current_user;

    public final int PICK_VEHICLE_ROUTE = 1;
    public final int PICK_VEHICLE_REGISTRATION = 2;
    public final int PICK_VEHICLE_FRONT = 3;
    public final int PICK_VEHICLE_REAR = 4;

    public DriverDataUpdateFragmentStep2() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root  = inflater.inflate(R.layout.fragment_driver_data_update_step_2, container, false);

        current_user = MainActivity.appPreference.getUserObject(getContext(),getActivity());
        etRegAlphabets = root.findViewById(R.id.et_vehicle_reg_alphabet);
        etRegYear = root.findViewById(R.id.et_vehicle_reg_year);
        etRegNo = root.findViewById(R.id.et_vehicle_reg_number);

        spVehicleType = root.findViewById(R.id.sp_driver_type);


        imVehicleFront = root.findViewById(R.id.im_vehicle_front);
        imVehicleRear = root.findViewById(R.id.im_vehicle_rear);
        imVehicleRegistration = root.findViewById(R.id.im_vehicle_registration);
        imVehicleRoute = root.findViewById(R.id.im_vehicle_route);

        imVehicleFront.setTag(R.id.image_uri,"@");
        imVehicleRear.setTag(R.id.image_uri,"@");
        imVehicleRegistration.setTag(R.id.image_uri,"@");
        imVehicleRoute.setTag(R.id.image_uri,"@");

        imVehicleFront.setOnClickListener(this);
        imVehicleRear.setOnClickListener(this);
        imVehicleRegistration.setOnClickListener(this);
        imVehicleRoute.setOnClickListener(this);


        tvVehicleFront = root.findViewById(R.id.tv_vehicle_front);
        tvVehicleRear = root.findViewById(R.id.tv_vehicle_rear);
        tvVehicleRegistration = root.findViewById(R.id.tv_vehicle_registration);
        tvVehicleRoute = root.findViewById(R.id.tv_vehicle_route);


        tvVehicleFront.setOnClickListener(this);
        tvVehicleRear.setOnClickListener(this);
        tvVehicleRegistration.setOnClickListener(this);
        tvVehicleRoute.setOnClickListener(this);

        btnSaveStep2 = root.findViewById(R.id.btn_save_driver_data_step2);
        btnSaveStep2.setOnClickListener(this);

        return root;
    }


    @Override
    public void onClick(View v) {
        int RequestCode=0;
        CharSequence title="";
        switch (v.getId()){
            case R.id.im_vehicle_front:
            case R.id.tv_vehicle_front:
                title = "Please select your Vehicle Front Image";
                RequestCode = PICK_VEHICLE_FRONT;
                break;
            case R.id.im_vehicle_rear:
            case R.id.tv_vehicle_rear:
                title = "Please select your Vehicle Front Rear Image";
                RequestCode = PICK_VEHICLE_REAR;
                break;
            case R.id.im_vehicle_registration:
            case R.id.tv_vehicle_registration:
                title = "Please select your Vehicle Registration Book Image";
                RequestCode = PICK_VEHICLE_REGISTRATION;
                break;
            case R.id.im_vehicle_route:
            case R.id.tv_vehicle_route:
                RequestCode = PICK_VEHICLE_ROUTE;
                title = "Please select your Vehicle Route Image";
                break;

            case R.id.btn_save_driver_data_step2:
                driverRegisterStep2();
                break;
        }


        if(RequestCode>0) {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, title), RequestCode);
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ImageView v = null;
            switch (requestCode) {
                case PICK_VEHICLE_FRONT:
                    v = imVehicleFront;
                    break;
                case PICK_VEHICLE_REAR:
                    v = imVehicleRear;
                    break;
                case PICK_VEHICLE_REGISTRATION:
                    v = imVehicleRegistration;
                    break;
                case PICK_VEHICLE_ROUTE:
                    v = imVehicleRoute;
                    break;

            }

            if (v != null) {
                Uri selectedImageURI = data.getData();
                v.setTag(R.id.image_uri, selectedImageURI);
                Picasso.get().load(selectedImageURI).noPlaceholder().centerCrop()
                        .fit().into(v);
                v.setTag(R.id.media_path, selectedImageURI.getPath());
            }

        }
    }


    private void driverRegisterStep2(){
        final String regAlphabet = etRegAlphabets.getText().toString();
        final String regYear = etRegYear.getText().toString();
        final String regNumber = etRegNo.getText().toString();



        if(imVehicleFront.getTag(R.id.image_uri).equals("@")){
            MainActivity.appPreference.showToast("Please select your Vehicle Front Image");
        }else if(imVehicleRear.getTag(R.id.image_uri).equals("@")){
            MainActivity.appPreference.showToast("Please select your Vehicle Rear Image");
        }else if(imVehicleRegistration.getTag(R.id.image_uri).equals("@")){
            MainActivity.appPreference.showToast("Please select your Registration Book Image");
        }else if (TextUtils.isEmpty(regAlphabet)){
            MainActivity.appPreference.showToast("Vehicle Registration Numbr is required");
        }else if (TextUtils.isEmpty(regYear)){
            MainActivity.appPreference.showToast("Vehicle Registration Numbr is required");
        } else if (TextUtils.isEmpty(regNumber)) {
            MainActivity.appPreference.showToast("Vehicle Registration Numbr is required");
        }
        else {
            Utils.showProgressBarSpinner(getContext());

            final Uri vehicle_front_uri = (Uri) imVehicleFront.getTag(R.id.image_uri);
            final Uri vehicle_rear_uri = (Uri) imVehicleRear.getTag(R.id.image_uri);
            final Uri registration_uri = (Uri) imVehicleRegistration.getTag(R.id.image_uri);

            File vehicleFront = new File(Utils.getRealPathFromURI(getContext(), vehicle_front_uri));
            File vehicleRear = new File(Utils.getRealPathFromURI(getContext(), vehicle_rear_uri));
            File registration = new File(Utils.getRealPathFromURI(getContext(), registration_uri));


//            File vehicleFront = new File(imVehicleFront.getTag(R.id.media_path).toString());
//            File vehicleRear = new File(imVehicleRear.getTag(R.id.media_path).toString());
//            File registration = new File(imVehicleRegistration.getTag(R.id.media_path).toString());





            String vehicleType = spVehicleType.getSelectedItem().toString();






            // Parsing any Media type file
            RequestBody vehicleFrontRequestBody = RequestBody.create(MediaType.parse("image/*"), vehicleFront);
            RequestBody vehicleRearRequestBody = RequestBody.create(MediaType.parse("image/*"), vehicleRear);
            RequestBody registrationRequestBody = RequestBody.create(MediaType.parse("image/*"), registration);

            RequestBody regAlphabetBody = RequestBody.create(MediaType.parse("text/plain"), regAlphabet);
            RequestBody regYearBody = RequestBody.create(MediaType.parse("text/plain"), regYear);
            RequestBody regNumberBody = RequestBody.create(MediaType.parse("text/plain"), regNumber);
            RequestBody mobileBody = RequestBody.create(MediaType.parse("text/plain"), current_user.getMobile());
            RequestBody vehicleTypeBody = RequestBody.create(MediaType.parse("text/plain"), vehicleType);

            MultipartBody.Part vehicleFrontToUpload = MultipartBody.Part.createFormData("vehicle_front", "vehicle_front_" + vehicleFront.getName(), vehicleFrontRequestBody);
            MultipartBody.Part vehicleRearToUpload = MultipartBody.Part.createFormData("vehicle_rear", "vehicle_rear_" + vehicleRear.getName(), vehicleRearRequestBody);
            MultipartBody.Part registrationToUpload = MultipartBody.Part.createFormData("registration", "registration_" + registration.getName(), registrationRequestBody);

            MultipartBody.Part routeToUpload = MultipartBody.Part.createFormData("route", "route_" + vehicleFront.getName(), vehicleFrontRequestBody);
            if (!imVehicleRoute.getTag(R.id.image_uri).equals("@")) {

                final Uri licence_uri = (Uri) imVehicleRoute.getTag(R.id.image_uri);
                File licence = new File(Utils.getRealPathFromURI(getContext(), licence_uri));
//
//                File licence = new File(imVehicleRoute.getTag(R.id.media_path).toString());



                RequestBody licenceRequestBody = RequestBody.create(MediaType.parse("image/*"), licence);
                routeToUpload = MultipartBody.Part.createFormData("route", "route_" + licence.getName(), licenceRequestBody);
            }

            Call<User> userCall = MainActivity.serviceApi.doDriverRegistrationStep2(vehicleFrontToUpload,
                    vehicleRearToUpload,registrationToUpload,routeToUpload,regAlphabetBody,regYearBody,regNumberBody,mobileBody,vehicleTypeBody);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Utils.dismissProgressBarSpinner();
                    if(response.body().getResponse().equals("step2_completed")){
                        MainActivity.appPreference.setUserObject(response.body());
                        Utils.showAlertBox(getActivity(),"Thanks! your account will be activated shortly!");
                        FragmentManager fragmentManager = getFragmentManager();
                        HomeFragment home = new HomeFragment();
                        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, home).commit();
                    }else if(response.body().getResponse().equals("error_uploading")){
                        Utils.showAlertBox(getActivity(),"Sorry We are not able to upload your data.");
                    }else if(response.body().getResponse().equals("required_fields_missing")){
                        Utils.showAlertBox(getActivity(),"All Fields are required.");
                    }else{
                        Utils.showAlertBox(getActivity(),"Something went wrong Please try again later! :(");
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Utils.dismissProgressBarSpinner();
                    Log.e("Error",t.toString());
                    Utils.showAlertBox(getActivity(),"Something went wrong Please try again later! :(");
                }
            });
        }
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
}
