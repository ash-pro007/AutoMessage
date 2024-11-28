package com.example.automessageone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ScheduleActivity extends AppCompatActivity {

    ImageButton btnCreateScheduleMessage;
    EditText edtMessageForSchedule;
    EditText edtNoOfMessage;

    boolean isBtnCreateScheduleMessageVisible = false;
    boolean isEditCreateScheduleMessageVisible = true;
    String enteredMessage;
    int noOfMessage;
    private View dialogView;

    TimePicker timePicker;
    DatePicker datePicker;
    String timeToSendScheduleMessage;
    String dateToSendScheduleMessage;
    String revDateToSendScheduleMessage;

    Button btnChooseDateOfScheduleMessage;

    boolean isDateForScheduleSelected = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        btnCreateScheduleMessage = findViewById(R.id.btnCreateScheduleMessageButton);
        btnCreateScheduleMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this,R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(ScheduleActivity.this).inflate(R.layout.popup_schedule_message, viewGroup, false);
                Button buttonOk=dialogView.findViewById(R.id.buttonOkSchedule);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();


                // Making edt create message visible
                Button btnUseTemplate = dialogView.findViewById(R.id.btnUseTemplateSchedule);
                Button btnCreateMessage =  dialogView.findViewById(R.id.btnCreateMessage);
                edtMessageForSchedule =  dialogView.findViewById(R.id.edtMessageForSchedule);

                // TODO Drop down spinner


                Spinner spinnerTemplate = (Spinner) dialogView.findViewById(R.id.spinner);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ScheduleActivity.this,
                        R.array.templates, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerTemplate.setAdapter(adapter);


                edtNoOfMessage = dialogView.findViewById(R.id.edtShowNumberOfSchedule);

                btnCreateMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isBtnCreateScheduleMessageVisible = false;
                        isEditCreateScheduleMessageVisible = true;
                        edtMessageForSchedule.setVisibility(View.VISIBLE);

                        edtNoOfMessage.setVisibility(View.VISIBLE);


                        btnCreateMessage.setVisibility(View.GONE);
                        spinnerTemplate.setVisibility(View.GONE);
                        btnUseTemplate.setVisibility(View.VISIBLE);

                        // Getting the data from user

                        enteredMessage =  edtMessageForSchedule.getText().toString();
                        noOfMessage = Integer.parseInt(edtNoOfMessage.getText().toString());

                    }
                });

                btnUseTemplate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnUseTemplate.setVisibility(View.GONE);
                        btnCreateMessage.setVisibility(View.VISIBLE);
                        edtMessageForSchedule.setVisibility(View.GONE);


                        spinnerTemplate.setVisibility(View.VISIBLE);




                        spinnerTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {



                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                                // checking that if night mode is on

                                //  ((TextView) spinnerTemplate.getChildAt(0)).setTextColor(R.color.white);



                                String[] arr = getResources().getStringArray(R.array.templates);

                                enteredMessage = arr[i];

                                Toast.makeText(ScheduleActivity.this, "You selected: "  + enteredMessage
                                        , Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }

                });



                // TODO: Time

                timePicker = dialogView.findViewById(R.id.timePickerScheduleMessage);




                // TODO: Date


                btnChooseDateOfScheduleMessage = dialogView.findViewById(R.id.btnChooseDateOfScheduleMessage);
                btnChooseDateOfScheduleMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this,R.style.CustomAlertDialog);
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(ScheduleActivity.this).inflate(R.layout.pop_up_date_picker_for_schedule_message, viewGroup, false);
                        Button buttonOk=dialogView.findViewById(R.id.buttonOkDateSelected);
                        builder.setView(dialogView);
                        final AlertDialog alertDialog = builder.create();


                        Button button = dialogView.findViewById(R.id.buttonOkDateSelected);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                datePicker = dialogView.findViewById(R.id.datePickerScheduleMessage);
                                dateToSendScheduleMessage = "";

                                if(datePicker.getDayOfMonth() < 10) {
                                    dateToSendScheduleMessage += "0" + datePicker.getDayOfMonth() + "-";
                                } else {
                                    dateToSendScheduleMessage += datePicker.getDayOfMonth() + "-";
                                }
                                if(datePicker.getMonth() + 1 < 10) {
                                    dateToSendScheduleMessage += "0" + (datePicker.getMonth() + 1) + "-";
                                } else {
                                    dateToSendScheduleMessage += datePicker.getMonth() + 1 + "-";
                                }

                                dateToSendScheduleMessage += datePicker.getYear();

                                // TODO : rev date

                                revDateToSendScheduleMessage = datePicker.getYear() + "-";

                                if(datePicker.getMonth() + 1 < 10) {
                                    revDateToSendScheduleMessage += "0" + (datePicker.getMonth() + 1) + "-";
                                } else {
                                    revDateToSendScheduleMessage += datePicker.getMonth() + 1 + "-";
                                }

                                if(datePicker.getDayOfMonth() < 10) {
                                    revDateToSendScheduleMessage += "0" + datePicker.getDayOfMonth();
                                } else {
                                    revDateToSendScheduleMessage += datePicker.getDayOfMonth() ;
                                }




                                Toast.makeText(ScheduleActivity.this, "Date Picked", Toast.LENGTH_SHORT).show();

                                isDateForScheduleSelected = true;


                                btnChooseDateOfScheduleMessage.setText("Date Selected: " + dateToSendScheduleMessage);

                                alertDialog.cancel();

                            }
                        });


                        alertDialog.show();
                    }
                });

                datePicker = findViewById(R.id.datePickerScheduleMessage);





                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Setting the Entered message to the autoMessageActivity's card of create message

                        // CardView cardCreateMessage = findViewById(R.id.cardMessage);


                        if(edtMessageForSchedule.getText().toString().length() == 0 && enteredMessage == "") {
                            Toast.makeText(ScheduleActivity.this, "Please enter a message to send"
                                    , Toast.LENGTH_SHORT).show();
                        }

                        else if(!isDateForScheduleSelected) {
                            Toast.makeText(ScheduleActivity.this, "Please select a date to send the message"
                                    , Toast.LENGTH_SHORT).show();
                        }
//                        else if(noOfSilentRings < 1) {
//                            Toast.makeText(AutoMessageActivity.this, "Please enter a valid number to silent ring"
//                                    , Toast.LENGTH_SHORT).show();
//                        }

                        else {

                            if(edtMessageForSchedule.getText().toString().length() != 0) {
                                enteredMessage = edtMessageForSchedule.getText().toString();
                            }

                            String inputForRingSilent = edtNoOfMessage.getText().toString();

                            // TODO ==> Date



                            int hour, min;

                            hour = timePicker.getHour();
                            min = timePicker.getMinute();


                            timeToSendScheduleMessage = "";

                            // TODO - making time in format : HH:MM:SS

                            if(hour < 10) {
                                timeToSendScheduleMessage += "0" + hour + ":";
                            } else {
                                timeToSendScheduleMessage +=  hour + ":";
                            }
                            if(min < 10) {
                                timeToSendScheduleMessage += "0" + min + ":";
                            } else {
                                timeToSendScheduleMessage += min + ":";
                            }

                            timeToSendScheduleMessage += "00";

                            Log.i("time70", timeToSendScheduleMessage);

                            // TODO: Validating the date and time

                            if(!isFutureDateTime(revDateToSendScheduleMessage + " " + timeToSendScheduleMessage )) {
                                Log.i("dateAndTime", revDateToSendScheduleMessage + " " + timeToSendScheduleMessage);
                                Toast.makeText(ScheduleActivity.this, "Please select date or time in future!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                alertDialog.dismiss();
                            }

                        }




                    }
                });


                alertDialog.show();

            }
        });


    }

    public static boolean isFutureDateTime(String dateTimeString) {
        // Specify the date and time format expected from the user
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            // Parse the provided date and time string
            Date dateTime = sdf.parse(dateTimeString);

            // Get the current date and time
            Date currentDateTime = new Date();

            // Compare the provided date and time with the current date and time
            return dateTime.after(currentDateTime);

        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
            return false;
        }
    }
}