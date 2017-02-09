package com.farast.utu_apibased;

import android.content.Intent;

/**
 * Created by cendr on 09/02/2017.
 */

public class ItemUtil {
    public static int getItemIdFromIntent(Intent intent) {
        if (intent == null)
            throw new ItemIdNotSuppliedException("Intent is null");
        int itemId = intent.getIntExtra("item_id", -1);
        if (itemId == -1)
            throw new ItemIdNotSuppliedException("Item id is not stored in this Intent");
        return itemId;
    }
}
