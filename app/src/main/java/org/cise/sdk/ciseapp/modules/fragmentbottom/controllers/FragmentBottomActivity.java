package org.cise.sdk.ciseapp.modules.fragmentbottom.controllers;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.cise.core.utilities.http.ApiRequest;
import org.cise.core.utilities.http.HttpError;
import org.cise.core.utilities.http.HttpResponse;
import org.cise.sdk.ciseapp.R;
import org.cise.sdk.ciseapp.modules.adapter.components.AdapterSampleData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentBottomActivity extends AppCompatActivity {

    @BindView(R.id.btn_show)
    Button btnShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_bottom);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_show)
    public void onBtnShowClicked() {
        ApiRequest.post("https://api.digitalrecordcard.com/index.php/api_v1/login", new String(""), new HttpResponse.Listener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d("API-TEST", response);
            }

            @Override
            public void onError(HttpError httpError) {
                Log.e("API-TEST", httpError.getMessage());
            }
        });
    }

    @OnClick(R.id.btn_show_async)
    public void onBtnShowLoaderClicked() {
        BottomLoaderFragment fragment = BottomLoaderFragment.newInstance();
        fragment.show(getSupportFragmentManager(), BottomLoaderFragment.TAG);
    }
}
