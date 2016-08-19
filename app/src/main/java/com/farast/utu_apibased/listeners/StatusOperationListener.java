package com.farast.utu_apibased.listeners;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farast.utu_apibased.R;
import com.farast.utuapi.util.operations.Operation;
import com.farast.utuapi.util.operations.OperationListener;
import com.farast.utuapi.util.operations.OperationManager;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class StatusOperationListener implements OperationListener {

    private Activity parent;

    private RelativeLayout currentOperation;
    private TextView currentOperationText;
    private ProgressBar currentOperationProgress;

    public StatusOperationListener(Activity parent) {
        this.parent = parent;
        currentOperation = (RelativeLayout) parent.findViewById(R.id.currentOperation);
        currentOperationText = (TextView) parent.findViewById(R.id.currentOperationText);
        currentOperationProgress = (ProgressBar) parent.findViewById(R.id.currentOperationProgress);
    }

    @Override
    public void started(final Operation operation, OperationManager manager) {
        // TODO display all current running operations
        currentOperation.post(new Runnable() {
            @Override
            public void run() {
                currentOperation.setVisibility(View.VISIBLE);
                currentOperationText.setText(operation.getName().toLowerCase() + "...");
            }
        });
    }

    @Override
    public void ended(Operation operation, final OperationManager manager) {
        currentOperation.post(new Runnable() {
            @Override
            public void run() {
                if (!manager.isRunning())
                    currentOperation.setVisibility(View.GONE);
            }
        });
    }
}
