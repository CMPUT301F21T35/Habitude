package com.cmput301f21t35.habitude;


import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class EditHabitEvent extends DialogFragment {
    private String habitSrc;
    private Event event;
    private EditText eventName;
    private EditText eventComment;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private CheckBox eventFinished;
    private OnFragmentInteractionListener listener;
    private String geolocation;
    private String photoString;

    private Button loadButton, takeButton;
    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private final int TAKE_IMAGE_REQUEST = 100;
    FirebaseStorage storage;
    StorageReference storageReference;

    /**
     * Public interface to create edit fragment
     * @param habitSrc habit name
     * @param event habit event class
     */
    public EditHabitEvent(String habitSrc, Event event) {
        this.habitSrc = habitSrc;
        this.event = event;
    }

    public interface OnFragmentInteractionListener {
        void onOkPressed(Event newEvent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Create the event dialog
     * @param savedInstanceState Bundle
     * @return Dialog
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Get layout views
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_habit_event, null);
        eventName = view.findViewById(R.id.edit_event_name_editText);
        eventComment = view.findViewById(R.id.edit_event_comment_editText);
        datePicker = view.findViewById(R.id.edit_event_date);
        timePicker = view.findViewById(R.id.edit_event_time);
        eventFinished = view.findViewById(R.id.edit_event_finished);

        loadButton = view.findViewById(R.id.loadPhotoButton);
        takeButton= view.findViewById(R.id.takePhotoButton);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        geolocation = event.getEventGeolocation();
        View geolocationButton = view.findViewById(R.id.geolocation_button);
        imageView = view.findViewById(R.id.edit_photograph);
        geolocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.getInstance(), MapsActivity.class);
                startActivityForResult(intent,1); //Modernize?
            }
        });

        //Request for camera runtime permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                selectImage();

            }
        });

        takeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        // Set layout views
        eventName.setText(event.getEventName());
        eventComment.setText(event.getEventComment());
        Date date = new Date();
        Date time = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getEventDate());
            time = new SimpleDateFormat("hh:mm").parse(event.getEventTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        datePicker.updateDate(localDate.getYear(), localDate.getMonthValue()-1, localDate.getDayOfMonth());
        timePicker.setHour(time.getHours());
        timePicker.setMinute(time.getMinutes());
        eventFinished.setChecked(event.getEventFinished());
        photoString = event.getEventPhoto();
        try {
            URL photoUrl = new URL(photoString);
            Bitmap bmp = BitmapFactory.decodeStream(photoUrl.openConnection().getInputStream());
            imageView.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the edit fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    String name = eventName.getText().toString();
                    String comment = eventComment.getText().toString();
                    String month;
                    if (datePicker.getMonth() < 10) {
                        month = "0" + (datePicker.getMonth() + 1);
                    } else {
                        month = Integer.toString(datePicker.getMonth()+1);
                    }
                    String day;
                    if (datePicker.getDayOfMonth() < 10) {
                        day = "0" + datePicker.getDayOfMonth();
                    } else {
                        day = Integer.toString(datePicker.getDayOfMonth());
                    }
                    String year = Integer.toString(datePicker.getYear());
                    String eventDate = year + "-" + month + "-" + day;
                    String min;
                    if (timePicker.getMinute() < 10) {
                        min = "0" + timePicker.getMinute();
                    } else {
                        min = String.valueOf(timePicker.getMinute());
                    }
                    String eventTime = timePicker.getHour() + ":" + min;
                    Boolean finished = eventFinished.isChecked();
                    listener.onOkPressed(new Event(name, comment,eventDate,eventTime,finished,geolocation, photoString));
                }).create();
    }

    private void selectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_IMAGE_REQUEST);
        //startActivityForResult(Intent.createChooser(intent, "Take Image from here..."), TAKE_IMAGE_REQUEST);
    }
    /*
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getActivity().getApplicationContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && requestCode == RESULT_OK) {
            try {
                super.onActivityResult(requestCode, resultCode, data);
                geolocation = data.getStringExtra("keyName");
                //Log.v("Tagalog",geolocation);
            } catch (Exception ignored) {
            }
        }
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getActivity().getApplicationContext().getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            UploadTask uploadTask = ref.putFile(filePath);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue the task to get a download url
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        photoString = downloadUri.toString();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
        else if (requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK){
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getActivity().getApplicationContext().getContentResolver(), photo, "Title", null);
                Uri filePath1 = Uri.parse(path);
                imageView.setImageBitmap(photo);
                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                UploadTask uploadTask = ref.putFile(filePath1);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            photoString = downloadUri.toString();
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
            }catch (Exception e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

}