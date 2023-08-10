package com.onevour.sdk.impl.modules.form.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.onevour.core.base.BaseActivity;
import com.onevour.sdk.impl.R;
import com.onevour.sdk.impl.applications.configuration.AppDatabase;
import com.onevour.sdk.impl.databinding.ActivityFormDatabaseBinding;
import com.onevour.sdk.impl.modules.adapter.controllers.AdapterSampleActivity;
import com.onevour.sdk.impl.modules.chat.ChatActivity;
import com.onevour.sdk.impl.modules.formscroll.controllers.FormScrollActivity;
import com.onevour.sdk.impl.modules.fragment.controllers.FragmentActivity;
import com.onevour.sdk.impl.modules.fragmentbottom.controllers.FragmentBottomActivity;
import com.onevour.sdk.impl.modules.fragmentbottomnavigation.controllers.FragmentBottomNavigationActivity;
import com.onevour.sdk.impl.modules.main.components.SampleAdapter;
import com.onevour.sdk.impl.modules.main.models.SampleMV;
import com.onevour.sdk.impl.modules.mvvm.views.MVVMActivity;
import com.onevour.sdk.impl.repositories.database.entity.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

// https://medium.com/mindorks/using-room-database-android-jetpack-675a89a0e942
// https://developer.android.com/training/data-storage/room
public class FormDatabaseActivity extends BaseActivity {

    private SampleAdapter adapter = new SampleAdapter();

    ActivityFormDatabaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormDatabaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init(binding.rvUser, adapter);
        //
        AppDatabase db = AppDatabase.getInstance(this);
        db.userRepository().insertAll(new User("Zuliadin", "Zuliadin"));
        //
        List<SampleMV> samples = new ArrayList<>();
        List<User> users = db.userRepository().getAll();
        for (User o : users) samples.add(new SampleMV(o.getFirstName()));
        //
        adapter.setValue(samples);
        Toast.makeText(FormDatabaseActivity.this, "success", Toast.LENGTH_LONG).show();

    }
}