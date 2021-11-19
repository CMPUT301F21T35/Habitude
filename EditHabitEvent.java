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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class EditHabitEvent extends DialogFragment {
    private String habitSrc;
    private Event event;
    private EditText eventName;
    private EditText eventComment;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private CheckBox eventFinished;
    private OnFragmentInteractionListener listener;

    private static final int RESULT_LOAD_IMAGE = 1;
    private ImageView photoView;
    private Button loadButton;
    private Button takeButton;

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

        photoView = view.findViewById(R.id.photograph);
        loadButton = view.findViewById(R.id.loadPhotoButton);
        takeButton = view.findViewById(R.id.takePhotoButton);

        //Request for camera runtime permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        takeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        // Set layout views
        eventName.setText(event.getEventName());
        eventComment.setText(event.getEventComment());
        Date date = new Date();
        Date time = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(event.getEventDate());
            time = new SimpleDateFormat("hh : mm").parse(event.getEventTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        datePicker.updateDate(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        timePicker.setHour(time.getHours());
        timePicker.setMinute(time.getMinutes());
        eventFinished.setChecked(event.getEventFinished());

        // Create the edit fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = eventName.getText().toString();
                        String comment = eventComment.getText().toString();
                        String year = Integer.toString(datePicker.getYear());
                        String month = Integer.toString(datePicker.getMonth());
                        String day = Integer.toString(datePicker.getDayOfMonth());
                        String eventDate = year + "-" + month + "-" + day;
                        String eventTime = timePicker.getHour() + " " + ":" + " " + timePicker.getMinute();
                        Boolean finished = eventFinished.isChecked();
                        //listener.onOkPressed(new Event(name, comment,eventDate,eventTime,finished));
                    }
                }).create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            photoView.setImageBitmap(bitmap);
        }
        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            photoView.setImageURI(selectedImage);
        }
    }
}