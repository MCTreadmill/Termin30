package com.example.androiddevelopment.actorapp.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Luke Skywalker on 27.11.2017.
 */

public class AboutDialog extends AlertDialog.Builder {

    public AboutDialog(Context context) {
        super(context);

        setTitle("About");
        setMessage("Luka Stojic");
        setCancelable(false);

        setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
    }


    public AlertDialog prepareDialog() {
        AlertDialog dialog = create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}

