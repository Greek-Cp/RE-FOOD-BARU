package com.nicomot.re_food.util;

import android.app.ActionBar;

public class UtilityHidden {

    static void hiddenActionBar(ActionBar actionBar){
        if(actionBar != null){
            actionBar.hide();
        }
    }
}
