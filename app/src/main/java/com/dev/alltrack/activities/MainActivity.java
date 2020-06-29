package com.dev.alltrack.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dev.alltrack.PreferenceManager;
import com.dev.alltrack.R;
import com.dev.alltrack.adapters.PackageRecyAdap;
import com.dev.alltrack.models.*;
import com.dev.alltrack.models.Package;
import com.dev.alltrack.services.RetrofitRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private LinearLayout loadingLayout;
    private RecyclerView packageRecView;
    private FloatingActionButton fab;
    private TextView backgroundText;

    private boolean isReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstances();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Call<String> call = RetrofitRequest.getInstance().api().activate();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 200) {
                    isReady = true;
                    Toast.makeText(MainActivity.this, "Api Active", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        setPackageList();
    }

    private void initInstances() {
        backgroundText = findViewById(R.id.background_text_view);
        loadingLayout = findViewById(R.id.loading_layout);
        packageRecView = findViewById(R.id.package_recycler_view);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(showPackageDialogCreate);
    }

    private void setPackageList() {
        PreferenceManager manager = new PreferenceManager.Builder(getApplicationContext())
                .name("PACKAGE")
                .mode(MODE_PRIVATE)
                .build();

        List<Package> packList = new ArrayList<>();
        Map<String, ?> stringMap = manager.getAll();
        for(Map.Entry<String, ?> entry : stringMap.entrySet()) {
            Log.e("wwww", entry.getKey() + ":" + entry.getValue());

            Data data = new Gson().fromJson(entry.getValue().toString(), Data.class);

            String lastStatus = data.getStatus().get(0).getCode();
            String code = data.getInfo().getNo();
            String company = data.getInfo().getCompany();
            String lastStatusDesc = data.getStatus().get(0).getDetail();

            packList.add(new Package(lastStatus, code, company, lastStatusDesc));
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        PackageRecyAdap adapter = new PackageRecyAdap(getApplicationContext(), packList);

        adapter.notifyDataSetChanged();

        packageRecView.setLayoutManager(layoutManager);
        packageRecView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PackageRecyAdap.OnItemClickListener() {
            @Override
            public void onItemClick(String code) {
                Toast.makeText(MainActivity.this, code, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, PackageStatusActivity.class);
                intent.putExtra("PACKAGE_CODE", code);
                startActivity(intent);
            }
        });

        if(adapter.getItemCount() > 0)
            backgroundText.setVisibility(View.GONE);
        else
            backgroundText.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener showPackageDialogCreate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View view = inflater.inflate(R.layout.create_track_package_layout, null);

            final TextView codeText = view.findViewById(R.id.code_package_edit_text);
            final TextView nameText = view.findViewById(R.id.name_package_edit_text);
            Button negativeButton = view.findViewById(R.id.negative_button);
            Button positiveButton = view.findViewById(R.id.positive_button);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setView(view)
                    .setTitle("Create Track");

            final AlertDialog dialog = builder.create();
            dialog.show();

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String barcode = codeText.getText().toString().trim();
                    String name = nameText.getText().toString().trim();

                    loadingLayout.setVisibility(View.VISIBLE);

//                    Intent intent = new Intent(MainActivity.this, PackageStatusActivity.class);
//                    intent.putExtra("PACKAGE_CODE", barcode);
//                    intent.putExtra("PACKAGE_NAME", name);
//                    startActivity(intent);
//                    finish();

                    searchPackageStatus(barcode);

                    dialog.cancel();
                }
            });
        }
    };

    private void searchPackageStatus(@NonNull final String packCode) {
        loadingLayout.setVisibility(View.VISIBLE);

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

                        Gson gson = new Gson();
                        PreferenceManager packPrefM = new PreferenceManager.Builder(getApplicationContext())
                                .name("PACKAGE")
                                .mode(MODE_PRIVATE)
                                .build();

                        packPrefM.remove(packCode);
                        if(packPrefM.putString(packCode, gson.toJson(data))) {
                            Intent intent = new Intent(MainActivity.this, PackageStatusActivity.class);
                            intent.putExtra("PACKAGE_CODE", packCode);
                            intent.putExtra("PACKAGE_NAME", "");
                            startActivity(intent);

                            loadingLayout.setVisibility(View.GONE);
                        }
                    } else {
                        loadingLayout.setVisibility(View.GONE);
                    }
                } else if(code == 204) {
                    loadingLayout.setVisibility(View.GONE);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setMessage("ไม่พบพัสดุ")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<dataResponse> call, Throwable t) {
                Log.e("searchtrack", t.toString());

                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
