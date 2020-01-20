package com.hazyaz.ctup.menu_item;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hazyaz.ctup.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class Setting_activity extends AppCompatActivity {

    private static final int GALLERY_PCIK = 1;
    private StorageReference mStorageRef;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private Button change_status;
    private CircleImageView mImageView;
    private TextView mDisplayName;
    private TextView mStatus;
    private Button mChangeImage;
    private String imageM;
    private static String imageDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mDisplayName = (TextView) findViewById(R.id.Display_name);
        mStatus = (TextView) findViewById(R.id.status_button);
        mImageView = (CircleImageView) findViewById(R.id.setting_image);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        final String mCurrentUserUid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUserUid);
        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
               imageM = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                //       String thumbnaill_img = dataSnapshot.child("thumb_image").getValue().toString();


                mDisplayName.setText(name);
                mStatus.setText(status);

                //only try to ask for url if the url is not default
                if (!imageM.equals("default")) {

                    Picasso.get().load(imageM).networkPolicy(NetworkPolicy.OFFLINE)
                            .into(mImageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(imageM).placeholder(R.drawable.ic_launcher_background).into(mImageView);
                                }
                            });



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//this intent starts image chooser from the gallery
        mChangeImage = (Button) findViewById(R.id.Image_button);
        mChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PCIK);
            }
        });
    }


    //after selecting the image from the gallert the data is sent to te result method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PCIK && resultCode == RESULT_OK) {
            //if the request code is ok then we take the data uri of the image
            Uri imageUri = data.getData();
            //cropping the image taken from gallery to 1:1 ratio
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
//if the crop is done carefully then it is send below
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//taking the cropped image uri into result uri
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                String currentUserId = mCurrentUser.getUid();

                Uri resultUri = result.getUri();

                File thumbFilePath = new File(resultUri.getPath());

                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxHeight(200)
                        .setMaxWidth(200)
                        .setQuality(75)
                        .compressToBitmap(thumbFilePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();
                // getting the file path of the storage in firebase storage into to file path
                final StorageReference filePath = mStorageRef.child("profile_image").child(currentUserId + ".jpg");

                final StorageReference thumbFile1 = mStorageRef.child("profile_image").child("thumbnails").child(currentUserId + ".jpg");


                filePath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                        //getting the downlaod url
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull final Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downUri = task.getResult();
                            imageDownload = downUri.toString();


                            final UploadTask uploadTask = thumbFile1.putBytes(thumb_byte);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()) {
                                                throw task.getException();
                                            }

                                            // Continue with the task to get the download URL
                                            return thumbFile1.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri downloadUri = task.getResult();
                                                String ThumbUrl = downloadUri.toString();

                                                Map ImgData = new HashMap();
                                                ImgData.put("image", imageDownload);
                                                ImgData.put("thumb_image", ThumbUrl);

                                                mUserDatabase.updateChildren(ImgData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        if (aVoid != null) {
                                                            Toast.makeText(getApplicationContext(), "picupdates in datavbase", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Error in thumbnail", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });


                                            } else {


                                            }
                                        }
                                    });

                                }
                            });


                        }
                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }
}