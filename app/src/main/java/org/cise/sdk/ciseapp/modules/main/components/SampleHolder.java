package org.cise.sdk.ciseapp.modules.main.components;

import android.view.View;
import android.widget.TextView;

import org.cise.core.utilities.commons.ValueOf;
import org.cise.core.utilities.ui.adapter.recyclerview.HolderGeneric;
import org.cise.sdk.ciseapp.R;
import org.cise.sdk.ciseapp.modules.main.models.Sample;
import org.cise.sdk.ciseapp.modules.main.models.SampleMV;

public class SampleHolder extends HolderGeneric<SampleMV> implements View.OnClickListener {

    int index;

    Sample o;

    TextView name;

    public SampleHolder(View view) {
        super(view);
        name = view.findViewById(R.id.name);
        view.setOnClickListener(this);
    }

    @Override
    protected void onBindViewHolder(SampleMV model, int position) {
        this.o = model.getModel();
        this.index = position;
        name.setText(o.getSample());
    }

    @Override
    public void onClick(View v) {
        Listener listener = getListener(Listener.class);
        if (ValueOf.isNull(listener)) return;
        listener.onSelectedHolder(index, o);
    }

    public interface Listener extends HolderGeneric.Listener {

        void onSelectedHolder(int index, Sample o);
    }

}
