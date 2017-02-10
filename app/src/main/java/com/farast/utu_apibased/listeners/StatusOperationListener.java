package com.farast.utu_apibased.listeners;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.Localizer;
import com.farast.utu_apibased.R;
import com.farast.utuapi.util.operations.ItemRelatedOperation;
import com.farast.utuapi.util.operations.Operation;
import com.farast.utuapi.util.operations.OperationListener;
import com.farast.utuapi.util.operations.OperationManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class StatusOperationListener implements OperationListener {

    private Activity mParent;

    private Operation mCurrentOperation = null;
    private View mCurrentOperationView;
    private TextView mCurrentOperationText;
    private ProgressBar mCurrentOperationProgress;
    private Timer mTimer;

    private Localizer mLocalizer;

    public StatusOperationListener(final Activity parent) {
        this.mParent = parent;
        mLocalizer = new Localizer(parent.getResources());
        mCurrentOperationView = parent.findViewById(R.id.currentOperation);
        mCurrentOperationText = (TextView) parent.findViewById(R.id.current_operation_text);
        mCurrentOperationProgress = (ProgressBar) parent.findViewById(R.id.current_operation_progress);

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mCurrentOperationView.post(new Runnable() {
                    @Override
                    public void run() {
                        showLastUpdatedAt();
                    }
                });
            }
        }, 1000, 60000);
    }

    @Override
    public void started(final Operation operation, OperationManager manager) {
        // TODO display all current running operations
        mCurrentOperation = operation;
        final String operationText = mLocalizer.localizeOperation(operation) + "...";
        mCurrentOperationView.post(new Runnable() {
            @Override
            public void run() {
                mCurrentOperationProgress.setVisibility(View.VISIBLE);
                mCurrentOperationText.setText(operationText);
                if (operation instanceof ItemRelatedOperation) {
                    Toast.makeText(mParent, operationText, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void ended(Operation operation, final OperationManager manager) {
        mCurrentOperation = null;
        mCurrentOperationView.post(new Runnable() {
            @Override
            public void run() {
                if (!manager.isRunning()) {
                    mCurrentOperationProgress.setVisibility(View.GONE);
                    showLastUpdatedAt();
                }
            }
        });
    }

    private void showLastUpdatedAt() {
        if (mCurrentOperation == null && Bullshit.dataLoader.isLoaded()) {
            mCurrentOperationText.setText(mParent.getString(R.string.last_updated_at_x, Bullshit.prettyTime.format(Bullshit.dataLoader.getLastDataUpdate())));
        }
    }
}
