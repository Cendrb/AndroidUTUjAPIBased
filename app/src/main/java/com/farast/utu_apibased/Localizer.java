package com.farast.utu_apibased;

import android.content.res.Resources;

import com.farast.utuapi.data.common.UtuType;
import com.farast.utuapi.util.operations.ItemRelatedOperation;
import com.farast.utuapi.util.operations.LoggingInOperation;
import com.farast.utuapi.util.operations.Operation;
import com.farast.utuapi.util.operations.PredataOperation;
import com.farast.utuapi.util.operations.TimetablesDataOperation;
import com.farast.utuapi.util.operations.UtuDataOperation;

/**
 * Created by cendr on 07/02/2017.
 */

public class Localizer {

    private final Resources mResources;

    public Localizer(Resources resources) {
        mResources = resources;
    }

    public String localizeOperation(Operation operation) {
        if (operation instanceof ItemRelatedOperation) {
            ItemRelatedOperation itemRelatedOperation = (ItemRelatedOperation) operation;
            switch (itemRelatedOperation.getOperationType()) {
                case CREATE:
                    return mResources.getString(R.string.operation_submitting_new_of_type_x, localizeUtuItemType(itemRelatedOperation.getItemType()));
                case UPDATE:
                    return mResources.getString(R.string.operation_updating_x_of_type_x, itemRelatedOperation.getItemName(), localizeUtuItemType(itemRelatedOperation.getItemType()));
                case DELETE:
                    return mResources.getString(R.string.operation_deleting_x_of_type_x, itemRelatedOperation.getItemName(), localizeUtuItemType(itemRelatedOperation.getItemType()));
            }
        } else if (operation instanceof LoggingInOperation) {
            return mResources.getString(R.string.operation_logging_in);
        } else if (operation instanceof PredataOperation) {
            return mResources.getString(R.string.operation_downloading_predata);
        } else if (operation instanceof TimetablesDataOperation) {
            return mResources.getString(R.string.operation_downloading_timetables);
        } else if (operation instanceof UtuDataOperation) {
            return mResources.getString(R.string.operation_downloading_utu_data);
        }

        throw new IllegalArgumentException("Operation " + operation.getClass().getSimpleName() + " is not translatable at the moment");
    }

    public String localizeUtuItemType(UtuType utuType) {
        switch (utuType) {
            case ARTICLE:
                return mResources.getString(R.string.item_article);
            case EVENT:
                return mResources.getString(R.string.item_event);
            case EXAM:
                return mResources.getString(R.string.item_exam);
            case TASK:
                return mResources.getString(R.string.item_task);
            default:
                throw new IllegalArgumentException("UtuType " + utuType.toString() + " is not translatable at the moment");
        }
    }
}
