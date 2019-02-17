package org.cise.sdk.ciseapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.cise.core.utilities.dummy.DummyHelper;
import org.cise.core.utilities.helper.UIHelper;
import org.cise.core.utilities.http.APIHitter;
import org.cise.core.utilities.http.Response;
import org.cise.core.utilities.http.Error;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MA";

    @BindView(R.id.rv_sample)
    RecyclerView rvSample;

    private NameAdapter adapter = new NameAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        UIHelper.initRecyclerView(rvSample, adapter);
        adapter.addMore(DummyHelper.initListString());
        User user = new User("uadmin1", "sphere154");
        APIHitter.post("http://rundiri.com/sphere_warehouse/index.php/sync/api_login", user,
                new Response.Listener<ResponseGeneric<User>>() {

                    @Override
                    public void onSuccess(ResponseGeneric<User> response) {
                        Log.d(TAG, "OK " + String.valueOf(response.getResponse().getUsername()));
                    }

                    @Override
                    public void onError(Error error) {
                        Log.d(TAG, "NOK " + String.valueOf(error.getMessage()));
                    }

                });
    }

}
