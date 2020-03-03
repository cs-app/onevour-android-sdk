package org.cise.sdk.ciseapp.modules.main.components;

import android.view.View;
import android.widget.TextView;

import org.cise.core.utilities.commons.ValueUtils;
import org.cise.core.utilities.ui.adapter.recyclerview.GenericHolder;
import org.cise.sdk.ciseapp.R;
import org.cise.sdk.ciseapp.modules.main.models.Sample;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SampleHolder extends GenericHolder<Sample> implements View.OnClickListener {

    int index;

    Sample o;

//    @BindView(R.id.name) // cause use rx binding
    TextView name;

    public SampleHolder(View view) {
        super(view);
//        ButterKnife.bind(this, view);
        name = view.findViewById(R.id.name);
        view.setOnClickListener(this);
    }

    @Override
    protected void onBindViewHolder(Sample o, int position) {
        this.o = o;
        this.index = position;
        name.setText(o.getSample());
    }

    @Override
    public void onClick(View v) {
        if (ValueUtils.nonNull(getListener())) {
            getListener().onSelectedHolder(index, o);
        }
    }

    public interface Listener extends GenericHolder.Listener<Sample> {

    }

}
