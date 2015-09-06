package com.hnttechs.www.reserva;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dell on 1/28/15.
 */
public class SendDataActivity extends ActionBarActivity implements View.OnClickListener{

    EditText txtpeople;
    EditText txtname,txtphone;
    Button btnsend;

    String name,people,reservedate,reservetime,restaurant_name,phone;
    final Context context = this;
    ProgressDialog dialog;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    static TextView txtDate;
    static TextView txtTime;
    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;
    private int mSelectedHour;
    private int mSelectedMinutes;

    private DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mSelectedDay = dayOfMonth;
            mSelectedMonth = monthOfYear;
            mSelectedYear = year;

            updateDateUI();
        }
    };

    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mSelectedHour = hourOfDay;
            mSelectedMinutes = minute;

            updateTimeUI();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_data);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        isInternetOn();

        Intent i = getIntent();
        restaurant_name = i.getStringExtra("restaurant_name");

        this.txtDate = (TextView) findViewById(R.id.Date);
        this.txtTime = (TextView) findViewById(R.id.Time);
        this.btnsend = (Button) findViewById(R.id.btnsend);
        this.txtDate.setOnClickListener(this);
        this.txtTime.setOnClickListener(this);
        this.btnsend.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        this.mSelectedYear = calendar.get(Calendar.YEAR);
        this.mSelectedMonth = calendar.get(Calendar.MONTH);
        this.mSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        this.mSelectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        this.mSelectedMinutes = calendar.get(Calendar.MINUTE);

        updateDateUI();
        updateTimeUI();

        txtpeople  = (EditText)findViewById(R.id.txtpeople);
        txtname    = (EditText)findViewById(R.id.txtname);
        txtphone    = (EditText)findViewById(R.id.txtphone);



        if(ListViewMain.btn_id.equals("Chinese Restaurant"))        { btnsend.setBackgroundColor(Color.parseColor("#8a188a"));  }
        else if (ListViewMain.btn_id.equals("Myanmar Restaurant"))  { btnsend.setBackgroundColor(Color.parseColor("#319517"));               }
        else if (ListViewMain.btn_id.equals("Japan Restaurant"))    { btnsend.setBackgroundColor(Color.parseColor("#85ca53")); }
        else if (ListViewMain.btn_id.equals("European Restaurant")) { btnsend.setBackgroundColor(Color.parseColor("#b71c46")); }
        else if (ListViewMain.btn_id.equals("Indian Restaurant"))   { btnsend.setBackgroundColor(Color.parseColor("#1eb0c1")); }
        else if (ListViewMain.btn_id.equals("Thailand Restaurant")) { btnsend.setBackgroundColor(Color.parseColor("#621946")); }


    }

    private void updateDateUI() {
        String month = ((mSelectedMonth+1) > 9) ? ""+(mSelectedMonth+1): "0"+(mSelectedMonth+1) ;
        String day = ((mSelectedDay) < 10) ? "0"+mSelectedDay: ""+mSelectedDay ;
        txtDate.setText(month+"/"+day+"/"+mSelectedYear);
    }

    private void updateTimeUI() {
        String hour = (mSelectedHour > 9) ? ""+mSelectedHour: "0"+mSelectedHour ;
        String minutes = (mSelectedMinutes > 9) ?""+mSelectedMinutes : "0"+mSelectedMinutes;
        txtTime.setText(hour+":"+minutes);
    }

    private DatePickerDialog showDatePickerDialog(int initialYear, int initialMonth, int initialDay, DatePickerDialog.OnDateSetListener listener) {
        DatePickerDialog dialog = new DatePickerDialog(this, listener, initialYear, initialMonth, initialDay);
        dialog.show();
        return dialog;
    }

    private TimePickerDialog showTimePickerDialog(int initialHour, int initialMinutes, boolean is24Hour, TimePickerDialog.OnTimeSetListener listener) {
        TimePickerDialog dialog = new TimePickerDialog(this, listener, initialHour, initialMinutes, is24Hour);
        dialog.show();
        return dialog;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Date:
                showDatePickerDialog(mSelectedYear, mSelectedMonth, mSelectedDay, mOnDateSetListener);
                break;
            case R.id.Time:
                showTimePickerDialog(mSelectedHour, mSelectedMinutes, true, mOnTimeSetListener);
                break;
            case R.id.btnsend:
                check_field_to_reserve ();
                break;
        }
    }


    private void check_field_to_reserve () {
        if (isInternetOn() == true) {
            name = txtname.getText().toString();
            people = txtpeople.getText().toString();
            phone = txtphone.getText().toString();
            reservedate = txtDate.getText().toString();
            reservetime = txtTime.getText().toString();

            if(name.isEmpty()==true)
            {
                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialog = alertDialogBuilder
                        .setMessage("Please Fill Your Name.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            txtname.setFocusableInTouchMode(true);
                            txtname.requestFocus();
                            }
                        }).setTitle("Warning").create();
                alertDialog.show();
            } else if(people.isEmpty()==true) {
                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialog = alertDialogBuilder
                        .setMessage("Please Fill No. of People.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            txtpeople.setFocusableInTouchMode(true);
                            txtpeople.requestFocus();
                            }
                        }).setTitle("Warning").create();
                alertDialog.show();
            } else if (reservedate.isEmpty()==true) {
                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialog = alertDialogBuilder
                        .setMessage("Please Fill Date.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            txtDate.setFocusableInTouchMode(true);
                            txtDate.requestFocus();
                            }
                        }).setTitle("Warning").create();
                alertDialog.show();
            } else if (reservetime.isEmpty()==true) {
                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialog = alertDialogBuilder
                        .setMessage("Please Fill Time.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            txtTime.setFocusableInTouchMode(true);
                            txtTime.requestFocus();
                            }
                        }).setTitle("Warning").create();
                alertDialog.show();
            } else if (phone.isEmpty()==true) {
                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialog = alertDialogBuilder
                        .setMessage("Please Fill Pbone No.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                txtTime.setFocusableInTouchMode(true);
                                txtTime.requestFocus();
                            }
                        }).setTitle("Warning").create();
                alertDialog.show();
            } else {
                sendPostRequest(name, people, reservedate, reservetime, phone, restaurant_name);
                dialog = ProgressDialog.show(SendDataActivity.this, "", "Sending...", true);
                dialog.show();
            }
        } else{
            alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialog = alertDialogBuilder
                    .setMessage("Please Switch Your Internet Connection and try again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).setTitle("Warning").create();
            alertDialog.show();
        }
    }

    private void sendPostRequest(String name, String number_of_guest, String res_date, String res_time, String phone, String restaurant_name) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {

                String paramName = params[0];
                String paramPeople = params[1];
                String paramReserveDate = params[2];
                String paramReserveTime = params[3];
                String paramPhone = params[4];
                String paramRestaurantName = params[5];

                System.out.println("*** doInBackground ** paramName " + paramName + " paramPeople :" + paramPeople + "paramReserveDate :" + paramReserveDate + "paramTime :" + paramReserveTime + "paramPhone :" + paramPhone + "paramRestaurant :" + paramRestaurantName);

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("https://secure-thicket-3242.herokuapp.com/hnt_save");

                BasicNameValuePair nameBasicNameValuePair = new BasicNameValuePair("customer_name", paramName);
                BasicNameValuePair peopleBasicNameValuePAir = new BasicNameValuePair("number_of_ppl", paramPeople);
                BasicNameValuePair reservedateBasicNameValuePAir = new BasicNameValuePair("reserve_date", paramReserveDate);
                BasicNameValuePair reservetimeBasicNameValuePAir = new BasicNameValuePair("reserve_time", paramReserveTime);
                BasicNameValuePair phoneBasicNameValuePAir = new BasicNameValuePair("phone", paramPhone);
                BasicNameValuePair restaurantnameBasicNameValuePAir = new BasicNameValuePair("restaurant_name", paramRestaurantName);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(nameBasicNameValuePair);
                nameValuePairList.add(peopleBasicNameValuePAir);
                nameValuePairList.add(reservedateBasicNameValuePAir);
                nameValuePairList.add(reservetimeBasicNameValuePAir);
                nameValuePairList.add(phoneBasicNameValuePAir);
                nameValuePairList.add(restaurantnameBasicNameValuePAir);

                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);

                    try {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;

                        while((bufferedStrChunk = bufferedReader.readLine()) != null){
                            stringBuilder.append(bufferedStrChunk);
                        }
                        return stringBuilder.toString();
                    } catch (ClientProtocolException cpe) {
                        System.out.println("First HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe) {
                        System.out.println("Second HttpResponse :" + ioe);
                        ioe.printStackTrace();
                    }
                } catch (UnsupportedEncodingException uee) {
                    System.out.println("because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                dialog.dismiss();

                Intent intent = new Intent(context, SuccessfullyReserve.class);
                context.startActivity(intent);
                finish();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, number_of_guest, res_date, res_time, phone, restaurant_name);
    }

    public final boolean isInternetOn() {
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED  ) {
            return false;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}

