package com.petclump.petclump.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.petclump.petclump.R;
import com.petclump.petclump.models.FreeSchedule;
import com.petclump.petclump.models.OwnerProfile;
import com.petclump.petclump.models.protocols.ProfileDownloader;
import com.petclump.petclump.models.protocols.ProfileUploader;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UserInfoEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {
    private static final String TAG = "EditUser";
    String day_array_string[], year_array_string[];
    private int year;
    EditText user_name_editText;
    Button  save_button, cancel_button;
    TextView match_range_value;
    SeekBar user_match_range_seekbar;
    Spinner user_dob_day, user_dob_month, user_dob_year, user_select_gender;
    Object dob_year, dob_month, dob_day, gender;
    Context c;
    ConstraintLayout constraintLayout;
    ConstraintSet constraintSet;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private OwnerProfile profile;
    private static final int gray_id = 2131165335;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(user == null){
            Log.d(TAG, "onCreate: User not logged in");
            finish();
        }
        setupUI();

    }


    private void setupUI(){
        setContentView(R.layout.activity_user_info_edit);
        c = getApplicationContext();

        constraintSet = new ConstraintSet();
        constraintLayout = findViewById(R.id.user_info_edit_layout);
        constraintSet.clone(constraintLayout);

        user_name_editText = findViewById(R.id.user_name_editText);
        match_range_value = findViewById(R.id.user_match_value);
        user_match_range_seekbar = findViewById(R.id.user_match_value_seekbar);

        user_dob_day = findViewById(R.id.user_dob_day);
        user_dob_day.setOnItemSelectedListener(this);
        user_dob_month = findViewById(R.id.user_dob_month);
        user_dob_month.setOnItemSelectedListener(this);
        user_dob_year = findViewById(R.id.user_dob_year);
        user_dob_year.setOnItemSelectedListener(this);
        user_select_gender = findViewById(R.id.user_select_gender);
        user_select_gender.setOnItemSelectedListener(this);

        save_button = findViewById(R.id.save_button);
        cancel_button = findViewById(R.id.cancel_button);

        year = 2003;
        day_array_string = new String[31];
        year_array_string = new String[75];
        for (int i = 0; i < 31; i++) {
            day_array_string[i] = String.valueOf(i + 1);
        }

        for (int i = 75; i > 0; i--) {
            year_array_string[75-i] = String.valueOf(year);
            year -= 1;
        }


        // get pet_num by extra, sent from UserInfoActivity
        // Bundle extras = getIntent().getExtras();
        // if(extras != null){
        //    pet_num = extras.getInt("pet_num");
        //}

        //edit_name_button.setOnClickListener(v ->nameChangeClick());
        save_button.setOnClickListener(v -> saveData());
        cancel_button.setOnClickListener(v -> cancelData());

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_month = ArrayAdapter.createFromResource(this,
                R.array.month_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapter_day = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, day_array_string);
        ArrayAdapter<String> adapter_year = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, year_array_string);
        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        user_dob_month.setAdapter(adapter_month);
        user_dob_day.setAdapter(adapter_day);
        user_dob_year.setAdapter(adapter_year);
        user_select_gender.setAdapter(adapter_gender);

        user_match_range_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                match_range_value.setText(progressToText(progress));
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        setFreeScheduleListener();
        profile = new OwnerProfile();
        profile.download(user.getUid(),()->{
            user_name_editText.setText(profile.getName());
            int index = getSpinnerPosition(user_select_gender, profile.getGender());
            user_select_gender.setSelection(index);
            Calendar gcBirthday = new GregorianCalendar();
            gcBirthday.setTime((Date)profile.getBirthday());
            index = getSpinnerPosition(user_dob_year, gcBirthday.get(Calendar.YEAR));
            user_dob_year.setSelection(index);

            index = gcBirthday.get(Calendar.MONTH);
            Log.d("MONTH:",String.valueOf(gcBirthday.get(Calendar.MONTH)));
            user_dob_month.setSelection(index);
            index = getSpinnerPosition(user_dob_day, gcBirthday.get(Calendar.DAY_OF_MONTH));
            user_dob_day.setSelection(index);
            String range = String.valueOf(profile.getDistancePerference());
            match_range_value.setText(stringToProgressText(range));
            user_match_range_seekbar.setProgress(stringToProgress(range));
            // setup free schedule
            FreeSchedule freeSchedule = profile.getFreeTime();
            //Toast.makeText(this, "freetime:"+freeSchedule, Toast.LENGTH_SHORT).show();
            for(int i=1; i<8; i++) {
                for(int j=1; j<4; j++) {
                    String imageID = "sch" + i + j;
                    int resID = getResources().getIdentifier(imageID, "id", getPackageName());
                    ImageView im = findViewById(resID);
                    if(!freeSchedule.isFree(i,j)){
                        im.setTag(R.drawable.schedule_gray);

                    }else{
                        im.setTag(R.drawable.schedule_green);
                        im.setImageResource(R.drawable.schedule_green);
                    }
                }
            }
        });
    }
    // use this method in case the user is first time log in
    private void setFreeScheduleListener(){
        for(int i=1; i<8; i++) {
            for(int j=1; j<4; j++) {
                String imageID = "sch" + i + j;
                int resID = getResources().getIdentifier(imageID, "id", getPackageName());
                ImageView im = findViewById(resID);
                im.setTag(R.drawable.schedule_gray);
                im.setOnClickListener(this);
            }
        }
    }

    private Integer getSpinnerPosition(Spinner spinner, Object item){
        return ((ArrayAdapter<String>) spinner.getAdapter()).getPosition(item.toString());
    }

    private String progressToText(int progress){
        switch (progress){
            case  0: return getResources().getString(R.string.Within_5_miles);
            case  1: return getResources().getString(R.string.Within_20_miles);
            case  2: return getResources().getString(R.string.Within_100_miles) ;
            default: return getResources().getString(R.string.No_preferred_range);
        }
    }

    private String stringToProgressText(String s){
        switch (s){
            case    "5": return getResources().getString(R.string.Within_5_miles);
            case   "20": return getResources().getString(R.string.Within_20_miles);
            case  "100": return getResources().getString(R.string.Within_100_miles) ;
            default:     return getResources().getString(R.string.No_preferred_range);
        }
    }

    private Integer stringToProgress(String s){
        switch (s){
            case    "5": return 0;
            case   "20": return 1;
            case  "100": return 2;
            default:     return 3;
        }
    }

    private Integer progressToMile(int progress){
        switch (progress){
            case  0: return 5;
            case  1: return 20;
            case  2: return 100;
            default: return 0;
        }
    }
    private boolean checkBirthday(){
        boolean valid = true;
        int month = getSpinnerPosition(user_dob_month, user_dob_month.getSelectedItem());
        int day = getSpinnerPosition(user_dob_day, user_dob_day.getSelectedItem()) + 1;
        if ((month==4||month==6||month==9||month==11)
                && (day==31)){
            Toast toast = Toast.makeText(getApplicationContext(), "you enter wrong birthday",Toast.LENGTH_LONG);
            toast.show();
            user_dob_day.setSelection(29);
            valid = false;

        }else if ((month==2)
                && (day==30||day==31)){
            Toast toast = Toast.makeText(getApplicationContext(), "you enter wrong birthday",Toast.LENGTH_LONG);
            toast.show();
            user_dob_day.setSelection(28);
            valid = false;
        }
        return valid;

    }
    private void cancelData() {
        finish();
    }

    private void saveData() {
        OwnerProfile profile = new OwnerProfile();
        profile.setGender(user_select_gender.getSelectedItem().toString());
        profile.setName(user_name_editText.getText().toString());
        profile.setDistancePerference(progressToMile(user_match_range_seekbar.getProgress()));
        GregorianCalendar birthday = new GregorianCalendar();
        birthday.set(
            Integer.parseInt(user_dob_year.getSelectedItem().toString()),
            getSpinnerPosition(user_dob_month, user_dob_month.getSelectedItem()),
            getSpinnerPosition(user_dob_day, user_dob_day.getSelectedItem()) + 1
        );


        Log.d("uploadBirthday:","year:"+user_dob_year.getSelectedItem().toString()
            +"month:"+(getSpinnerPosition(user_dob_month, user_dob_month.getSelectedItem()) + 1
        )+"\n");
        profile.setBirthday(birthday.getTime());
        // set up freeschedule
        String freetime = "";
        for(int i=1; i<8; i++){
            for(int j=1; j<4; j++){
                String imageID = "sch" + i + j;
                int resID = getResources().getIdentifier(imageID, "id", getPackageName());
                ImageView t = findViewById(resID);
                if((Integer)t.getTag() == gray_id)
                    freetime += "0";
                else
                    freetime += "1";
            }
        }
        Log.d("freetime:",freetime);
        profile.setFreeTime(freetime);
        profile.upload(user.getUid(),()->{
            Toast.makeText(this, "Upload successfully!", Toast.LENGTH_SHORT).show();
        });
        finish();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onClick(View v) {
        //Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
        ImageView i = findViewById(v.getId());
        if((Integer)i.getTag() == gray_id){
            i.setTag(R.drawable.schedule_green);
            i.setImageResource(R.drawable.schedule_green);
            Log.d("ClickView to green",String.valueOf((Integer)i.getTag()));
        }else{
            i.setTag(R.drawable.schedule_gray);
            i.setImageResource(R.drawable.schedule_gray);
            Log.d("ClickView to gray",String.valueOf((Integer)i.getTag()));
        }
    }
}