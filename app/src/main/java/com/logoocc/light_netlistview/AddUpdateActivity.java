package com.logoocc.light_netlistview;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.logoocc.light_netlistview.bean.Person;
import com.logoocc.light_netlistview.bean.Result;
import com.logoocc.light_netlistview.utils.HttpHelper;

/**
 * Created by samchen on 8/26/15.
 */
public class AddUpdateActivity extends Activity {

    private EditText etName;
    private EditText etAge;
    private EditText etPhone;
    private EditText etAddress;
    private Button btSaveUpdate;
    private Button btDel;
    private EditText etPhotoUrl;
    private boolean isAdd = true;
    private int id;
    private Person person;
    private HttpHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_update);

        etName = (EditText) findViewById(R.id.add_update_name);
        etAge = (EditText) findViewById(R.id.add_update_age);
        etPhone = (EditText) findViewById(R.id.add_update_phone);
        etAddress = (EditText) findViewById(R.id.add_update_address);
        etPhotoUrl = (EditText) findViewById(R.id.add_update_photo);

        helper = HttpHelper.getInstance(getApplicationContext());

        btSaveUpdate = (Button) findViewById(R.id.add_update_add);
        btDel = (Button) findViewById(R.id.add_update_delete);


        btSaveUpdate.setOnClickListener(clickLis);
        btDel.setOnClickListener(clickLis);

        person = new Person();

        isAdd = getIntent().getBooleanExtra("isAdd", true);
        person.setId(getIntent().getIntExtra("id", -1));

        if (isAdd) {
            btDel.setEnabled(false);
        } else {
            btSaveUpdate.setText("修改");
            loadPerson();
        }


    }

    private void toPerson() {
        person.setAddress(etAddress.getText().toString().trim());
        person.setName(etName.getText().toString().trim());
        person.setPhone(etPhone.getText().toString().trim());
        person.setPhotourl(etPhotoUrl.getText().toString().trim());



        String ageStr = etAge.getText().toString().trim();

        if (!TextUtils.isEmpty(ageStr) && ageStr.matches("^\\d+$")) {
            person.setAge(Integer.valueOf(ageStr));
        }
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toPerson();
            switch (v.getId()) {

                case R.id.add_update_add:
                    if (isAdd) {
                        toPerson();
                        if (isAdd) {
                            helper.addPerson(person, new Response.Listener<Result<Boolean>>() {
                                @Override
                                public void onResponse(Result<Boolean> booleanResult) {
                                    if (booleanResult != null) {

                                        Toast.makeText(AddUpdateActivity.this, "" + booleanResult.msg, Toast.LENGTH_SHORT).show();
                                        if (booleanResult.data) {
                                            setResult(50);
                                            finish();
                                        }
                                    }

                                }
                            });
                        }

                    } else {
                        helper.updatePerson(person, new Response.Listener<Result<Boolean>>() {
                            @Override
                            public void onResponse(Result<Boolean> booleanResult) {

                                if (booleanResult != null) {

                                    Toast.makeText(AddUpdateActivity.this, "" + booleanResult.msg, Toast.LENGTH_SHORT).show();
                                    if (booleanResult.data) {
                                        setResult(50);
                                        finish();
                                    }
                                }


                            }
                        });
                    }
                    break;

                case R.id.add_update_delete:

                    helper.delPerson(person.getId(), new Response.Listener<Result<Boolean>>() {
                        @Override
                        public void onResponse(Result<Boolean> booleanResult) {

                            if (booleanResult != null) {

                                Toast.makeText(AddUpdateActivity.this, "" + booleanResult.msg, Toast.LENGTH_SHORT).show();
                                if (booleanResult.data) {
                                    setResult(50);
                                    finish();
                                }
                            }


                        }
                    });

                    break;
            }

        }
    };

    private void loadPerson() {
        final String params = "{action:201,id:" + person.getId() + "}";
        helper.loadPerson(params, new Response.Listener<Result<Person>>() {
            @Override
            public void onResponse(Result<Person> personResult) {
                if (personResult != null) {

                    Toast.makeText(AddUpdateActivity.this, "" + personResult.msg, Toast.LENGTH_SHORT).show();
                    if (personResult.data != null) {
                        person = personResult.data;

                        etAddress.setText(person.getAddress());
                        etName.setText(person.getName());
                        etAge.setText("" + person.getId());
                        etPhone.setText(person.getPhone());

                        btSaveUpdate.setEnabled(true);

                    } else {
                        btSaveUpdate.setEnabled(false);
                    }
                }

            }
        });
    }


}

