package com.dev.alltrack.activities;

import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dev.alltrack.R;
import com.dev.alltrack.adapters.StatusAdapter;
import com.dev.alltrack.models.*;
import com.dev.alltrack.services.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private LinearLayout infoLayout;
    private RecyclerView staRecView;

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
    }

    private void initInstances() {
        searchEditText = findViewById(R.id.search_edit_text);
        searchButton = findViewById(R.id.send_button);
        infoLayout = findViewById(R.id.info_layout);
        staRecView = findViewById(R.id.status_recycler_view);

        searchButton.setOnClickListener(searchTrack);
    }

    private View.OnClickListener searchTrack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String barcode = searchEditText.getText().toString().trim();

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

                            TextView senderTextView = findViewById(R.id.sender_text_view);
                            TextView receiverTextView = findViewById(R.id.receiver_text_view);
                            senderTextView.setText(info.getSender());
                            receiverTextView.setText(info.getReceiver());
//                            infoLayout.setVisibility(View.VISIBLE);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            StatusAdapter adapter = new StatusAdapter(getApplicationContext(), staList);

                            staRecView.setLayoutManager(layoutManager);
                            staRecView.setAdapter(adapter);

                            Toast.makeText(MainActivity.this, body.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Shipping not found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<dataResponse> call, Throwable t) {
                    Log.e("searchtrack", t.toString());
                }
            });
        }
    };
}
