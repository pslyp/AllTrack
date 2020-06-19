package com.dev.alltrack;

import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dev.alltrack.adapters.StatusAdapter;
import com.dev.alltrack.models.*;
import com.dev.alltrack.services.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText urlEditText;
    private Button sendButton;
//    private TextView resTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstances();
    }

    private void initInstances() {
        urlEditText = findViewById(R.id.url_edit_text);
        sendButton = findViewById(R.id.send_button);
//        resTextView = findViewById(R.id.response_text_view);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = urlEditText.getText().toString().trim();

                Call<dataResponse> call = RetrofitRequest.getInstance().api().get(barcode);
                call.enqueue(new Callback<dataResponse>() {
                    @Override
                    public void onResponse(Call<dataResponse> call, Response<dataResponse> response) {
                        dataResponse body = response.body();
                        int code = body.getStatus();

                        if(code == 200) {
                            Toast.makeText(MainActivity.this, "200", Toast.LENGTH_SHORT).show();

                            Data data = body.getData();
                            if(data != null) {
                                Info info = data.getInfo();
                                List<Status> staList = data.getStatus();

                                RecyclerView staRecView = findViewById(R.id.status_recycler_view);

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

                                staRecView.setLayoutManager(layoutManager);

                                StatusAdapter adapter = new StatusAdapter(getApplicationContext(), staList);

                                staRecView.setAdapter(adapter);

                                Toast.makeText(MainActivity.this, body.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Shipping not found!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<dataResponse> call, Throwable t) {
                        Log.e("[ERROR]", t.toString());
                    }
                });
            }
        });
    }
}
