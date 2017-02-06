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

    private Activity mParent;

    private RelativeLayout mCurrentOperation;
    private TextView mCurrentOperationText;
    private ProgressBar mCurrentOperationProgress;

    public StatusOperationListener(Activity parent) {
        this.mParent = parent;
        mCurrentOperation = (RelativeLayout) parent.findViewById(R.id.currentOperation);
        mCurrentOperationText = (TextView) parent.findViewById(R.id.currentOperationText);
        mCurrentOperationProgress = (ProgressBar) parent.findViewById(R.id.currentOperationProgress);
    }

    @Override
    public void started(final Operation operation, OperationManager manager) {
        // TODO display all current running operations
        mCurrentOperation.post(new Runnable() {
            @Override
            public void run() {
                mCurrentOperation.setVisibility(View.VISIBLE);
                mCurrentOperationText.setText(operation.getName().toLowerCase() + "...");
            }
        });
    }

    @Override
    public void ended(Operation operation, final OperationManager manager) {
        mCurrentOperation.post(new Runnable() {
            @Override
            public void run() {
                if (!manager.isRunning())
                    mCurrentOperation.setVisibility(View.GONE);
            }
        });
    }
}
