package com.farast.utu_apibased.util;

import android.content.Intent;

import com.farast.utu_apibased.exceptions.ItemIdNotSuppliedException;
import com.farast.utu_apibased.exceptions.ItemTypeNotSuppliedException;
import com.farast.utuapi.data.common.UtuType;

/**
 * Created by cendr on 09/02/2017.
 */

public class ItemUtil {
    public static final String ITEM_ID = "item_id";
    public static final String ITEM_TYPE = "item_type";

    public static int getItemIdFromIntent(Intent intent) {
        if (intent == null)
            throw new ItemIdNotSuppliedException("Intent is null");
        int itemId = intent.getIntExtra(ItemUtil.ITEM_ID, -1);
        if (itemId == -1)
            throw new ItemIdNotSuppliedException("Item id is not stored in this Intent");
        return itemId;
    }

    public static UtuType getItemTypeFromIntent(Intent intent) {
        if (intent == null)
            throw new ItemTypeNotSuppliedException("Intent is null");
        int itemTypeId = intent.getIntExtra(ItemUtil.ITEM_TYPE, -1);
        if (itemTypeId == -1)
            throw new ItemTypeNotSuppliedException("Item type is not stored in this Intent");
        try {
            return UtuType.values()[itemTypeId];
        } catch (IndexOutOfBoundsException e) {
            throw new ItemTypeNotSuppliedException("Item type is out of valid range (not in enum UtuType)");
        }
    }
}
