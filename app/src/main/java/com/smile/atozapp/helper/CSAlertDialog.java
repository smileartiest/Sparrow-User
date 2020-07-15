package com.smile.atozapp.helper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.smile.atozapp.R;

public class CSAlertDialog {

    Dialog myDialog;
    Context mContext;

    public CSAlertDialog(Context mContext) {
        this.mContext = mContext;
        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.loading_dialog);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void ShowDialog(){
        myDialog.show();
    }

    public void CancelDialog(){
        myDialog.cancel();
    }
}
