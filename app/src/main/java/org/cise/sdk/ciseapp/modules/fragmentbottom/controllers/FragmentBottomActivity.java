package org.cise.sdk.ciseapp.modules.fragmentbottom.controllers;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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
        BottomFragment fragment = BottomFragment.newInstance();
        fragment.show(getSupportFragmentManager(), BottomFragment.TAG);
    }

    @OnClick(R.id.btn_show_async)
    public void onBtnShowLoaderClicked() {
        BottomLoaderFragment fragment = BottomLoaderFragment.newInstance();
        fragment.show(getSupportFragmentManager(), BottomLoaderFragment.TAG);
    }
}
