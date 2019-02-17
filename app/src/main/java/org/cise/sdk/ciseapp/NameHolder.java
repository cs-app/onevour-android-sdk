package org.cise.sdk.ciseapp;

import android.view.View;
import android.widget.TextView;

import org.cise.core.utilities.ui.adapter.recyclerview.GenericHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NameHolder extends GenericHolder<String> {

    @BindView(R.id.name)
    TextView name;

    public NameHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    protected void onBindViewHolder(String o) {
        name.setText(o);
    }
}
