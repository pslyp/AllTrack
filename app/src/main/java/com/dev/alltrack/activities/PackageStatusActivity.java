package com.dev.alltrack.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dev.alltrack.PreferenceManager;
import com.dev.alltrack.R;
import com.dev.alltrack.adapters.StatusRecyAdap;
import com.dev.alltrack.models.Data;
import com.dev.alltrack.models.Info;
import com.dev.alltrack.models.Package;
import com.dev.alltrack.models.Status;
import com.dev.alltrack.models.dataResponse;
import com.dev.alltrack.services.RetrofitRequest;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PackageStatusActivity extends AppCompatActivity {

    private RecyclerView staRecView;

    private LinearLayout loadLayout;

    private TextView noDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_status);

        initInstances();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle bundle = getIntent().getExtras();
        String barcode = bundle.getString("PACKAGE_CODE", " ");
        String name = bundle.getString("PACKAGE_NAME", " ");

        Log.e("sdfsdf", barcode);

        PreferenceManager packPrefM = new PreferenceManager.Builder(getApplicationContext())
                .name("PACKAGE")
                .mode(MODE_PRIVATE)
                .build();

        String result = packPrefM.getString(barcode, "");
        if(!result.equals("")) {
            List<Status> staList = new Gson().fromJson(result, Data.class).getStatus();

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            StatusRecyAdap adapter = new StatusRecyAdap(getApplicationContext(), staList);

            staRecView.setLayoutManager(layoutManager);
            staRecView.setAdapter(adapter);

            loadLayout.setVisibility(View.GONE);
            staRecView.setVisibility(View.VISIBLE);

            noDataText.setVisibility(View.GONE);
        } else {
//            searchPackageStatus();
        }
    }

    private void initInstances() {
        loadLayout = findViewById(R.id.load_layout);
        staRecView = findViewById(R.id.status_recycler_view);

        noDataText = findViewById(R.id.no_data_text_view);
    }

    private View.OnClickListener searchTrack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            String barcode = searchEditText.getText().toString().trim();

            Bundle bundle = getIntent().getExtras();
            String barcode = bundle.getString("PACKAGE_CODE", " ");
            String name = bundle.getString("PACKAGE_NAME", " ");

            final TextView noDataText = findViewById(R.id.no_data_text_view);
            noDataText.setVisibility(View.GONE);
            staRecView.setVisibility(View.GONE);
            loadLayout.setVisibility(View.VISIBLE);

            Call<dataResponse> call = RetrofitRequest.getInstance().api().get(barcode);
            call.enqueue(new Callback<dataResponse>() {
                @Override
                public void onResponse(Call<dataResponse> call, Response<dataResponse> response) {
                    dataResponse body = response.body();
                    assert body != null;
                    int code = body.getStatus();

                    if(code == 200) {

                        Data data = body.getData();
                        if(data != null) {

                            Info info = data.getInfo();
                            List<Status> staList = data.getStatus();

//                            TextView senderTextView = findViewById(R.id.sender_text_view);
//                            TextView receiverTextView = findViewById(R.id.receiver_text_view);
//                            senderTextView.setText(info.getSender());
//                            receiverTextView.setText(info.getReceiver());

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            StatusRecyAdap adapter = new StatusRecyAdap(getApplicationContext(), staList);

                            staRecView.setLayoutManager(layoutManager);
                            staRecView.setAdapter(adapter);

                            loadLayout.setVisibility(View.GONE);
                            staRecView.setVisibility(View.VISIBLE);
                        } else {
                            loadLayout.setVisibility(View.GONE);
                            noDataText.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<dataResponse> call, Throwable t) {
                    Log.e("searchtrack", t.toString());

                    Toast.makeText(PackageStatusActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private void searchPackageStatus() {
        Bundle bundle = getIntent().getExtras();
        final String packCode = bundle.getString("PACKAGE_CODE", " ");
        String packName = bundle.getString("PACKAGE_NAME", " ");

//        noDataText = findViewById(R.id.no_data_text_view);
        noDataText.setVisibility(View.GONE);
        staRecView.setVisibility(View.GONE);
        loadLayout.setVisibility(View.VISIBLE);

        Call<dataResponse> call = RetrofitRequest.getInstance().api().get(packCode);
        call.enqueue(new Callback<dataResponse>() {
            @Override
            public void onResponse(Call<dataResponse> call, Response<dataResponse> response) {
                dataResponse body = response.body();
                assert body != null;
                int code = body.getStatus();

                if(code == 200) {

                    Data data = body.getData();
                    if(data != null) {

                        Info info = data.getInfo();
                        List<Status> staList = data.getStatus();

//                            TextView senderTextView = findViewById(R.id.sender_text_view);
//                            TextView receiverTextView = findViewById(R.id.receiver_text_view);
//                            senderTextView.setText(info.getSender());
//                            receiverTextView.setText(info.getReceiver());

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        StatusRecyAdap adapter = new StatusRecyAdap(getApplicationContext(), staList);

                        staRecView.setLayoutManager(layoutManager);
                        staRecView.setAdapter(adapter);

                        loadLayout.setVisibility(View.GONE);
                        staRecView.setVisibility(View.VISIBLE);

                        Gson gson = new Gson();
                        PreferenceManager packPrefM = new PreferenceManager.Builder(getApplicationContext())
                                .name("PACKAGE")
                                .mode(MODE_PRIVATE)
                                .build();

//                        Package aPackage = new Package(packCode, info.getCompany(), staList.get(0).getDetail());
//                        String json = gson.toJson(aPackage);
                        String json = gson.toJson(data);
                        packPrefM.remove(packCode);
                        packPrefM.putString(packCode, json);
                    } else {
                        loadLayout.setVisibility(View.GONE);
                        noDataText.setVisibility(View.VISIBLE);
                    }
                } else if(code == 204) {
                    loadLayout.setVisibility(View.GONE);

                    AlertDialog.Builder builder = new AlertDialog.Builder(PackageStatusActivity.this)
                            .setMessage("ไม่พบพัสดุ")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(PackageStatusActivity.this, MainActivity.class));
                                    finish();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<dataResponse> call, Throwable t) {
                Log.e("searchtrack", t.toString());

                Toast.makeText(PackageStatusActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
