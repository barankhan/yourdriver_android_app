package live.yourdriver.driver.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import live.yourdriver.driver.Activity.MainActivity;
import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Model.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import live.yourdriver.driver.R;;
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
    User current_user;
    public DriverDataUpdateFragmentStep1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_driver_data_update_step_1, container, false);


        current_user = MainActivity.appPreference.getUserObject(getContext(),getActivity());
        etDriverName = root.findViewById(R.id.et_driver_name);
        etDriverFather = root.findViewById(R.id.et_driver_father);
        etDriverCNIC = root.findViewById(R.id.et_driver_cnic);

        etDriverName.setText(current_user.getName());

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




                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContext().getContentResolver().query(selectedImageURI, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String mediaPath = cursor.getString(columnIndex);
                // Set the Image in ImageView for Previewing the Media
                cursor.close();


                v.setTag(R.id.media_path, mediaPath);

            }
        }

    }

    private void driverRegisterStep1(){

        final String name = etDriverName.getText().toString().trim();
        final String father = etDriverFather.getText().toString().trim();
        final String cnic = etDriverCNIC.getText().toString().trim();



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

            Utils.showProgressBarSpinner(getContext());
            final Uri cnic_front_uri = (Uri) imDriverCNICFront.getTag(R.id.image_uri);
            final Uri cnic_rear_uri = (Uri) imDriverCNICRear.getTag(R.id.image_uri);
            final Uri picture_uri = (Uri) imDriverPic.getTag(R.id.image_uri);


            File cnicFront = new File(Utils.getRealPathFromURI(getContext(), cnic_front_uri));
            File cnicRear = new File(Utils.getRealPathFromURI(getContext(), cnic_rear_uri));
            File picture = new File(Utils.getRealPathFromURI(getContext(), picture_uri));
//
//            File cnicFront = new File(imDriverCNICFront.getTag(R.id.media_path).toString());
//            File cnicRear = new File(imDriverCNICRear.getTag(R.id.media_path).toString());
//            File picture = new File(imDriverPic.getTag(R.id.media_path).toString());

            // Parsing any Media type file
            RequestBody cnicFrontRequestBody = RequestBody.create(MediaType.parse("image/*"), cnicFront);
            RequestBody cnicRearRequestBody = RequestBody.create(MediaType.parse("image/*"), cnicRear);
            RequestBody pictureRequestBody = RequestBody.create(MediaType.parse("image/*"), picture);


            RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody fatherBody = RequestBody.create(MediaType.parse("text/plain"), father);
            RequestBody cnicBody = RequestBody.create(MediaType.parse("text/plain"), cnic);

            RequestBody mobileBody = RequestBody.create(MediaType.parse("text/plain"), current_user.getMobile());



            MultipartBody.Part cnicFrontToUpload = MultipartBody.Part.createFormData("cnic_front", "cnic_front_" + cnicFront.getName(), cnicFrontRequestBody);
            MultipartBody.Part cnicRearToUpload = MultipartBody.Part.createFormData("cnic_rear", "cnic_rear_" + cnicRear.getName(), cnicRearRequestBody);
            MultipartBody.Part pictureToUpload = MultipartBody.Part.createFormData("picture", "pic_" + picture.getName(), pictureRequestBody);

            MultipartBody.Part licenceToUpload ;
            if (!imDriverLicence.getTag(R.id.image_uri).equals("@")) {

                final Uri licence_uri = (Uri) imDriverLicence.getTag(R.id.image_uri);
                File licence = new File(Utils.getRealPathFromURI(getContext(), licence_uri));

//                File licence = new File(imDriverLicence.getTag(R.id.media_path).toString());
                RequestBody licenceRequestBody = RequestBody.create(MediaType.parse("image/*"), licence);

                licenceToUpload = MultipartBody.Part.createFormData("licence", "licence" + licence.getName(), licenceRequestBody);
            }else{
                licenceToUpload =  MultipartBody.Part.createFormData("licence", "licence" + cnicFront.getName(), cnicFrontRequestBody);
            }




            Call<User> userCall = MainActivity.serviceApi.doDriverRegistrationStep1(pictureToUpload, licenceToUpload, cnicFrontToUpload, cnicRearToUpload, cnicBody, nameBody, fatherBody, mobileBody);
            userCall.enqueue(new Callback<User>() {




                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Utils.dismissProgressBarSpinner();
                    if(response.body().getResponse().equals("step1_completed")){
                        MainActivity.appPreference.setUserObject(response.body());
                        FragmentManager fragmentManager = getFragmentManager();
                        Utils.showAlertBox(getActivity(),"Please enter your vehicle details now.");
                        DriverDataUpdateFragmentStep2 step2 = new DriverDataUpdateFragmentStep2();
                        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, step2).commit();
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
                    Utils.showAlertBox(getActivity(),"Something went wrong Please try again later! :(");
//                    Log.e("error",t.toString());
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
