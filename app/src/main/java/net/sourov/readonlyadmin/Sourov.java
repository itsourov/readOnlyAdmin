package net.sourov.readonlyadmin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import net.sourov.readonlyadmin.model.Offers;

public class Sourov {

    private Context context;

    public Sourov(Context context) {
        this.context = context;
    }

    public void confirmationPopUp(Offers offers) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View dialogView = layoutInflater.inflate(R.layout.confirm_buy, null);

        final AlertDialog pointDialog = new AlertDialog.Builder(context).create();
        pointDialog.setView(dialogView);
        pointDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pointDialog.show();

        TextView validityText,priceText,codeText;

        validityText = dialogView.findViewById(R.id.validityText);
        priceText = dialogView.findViewById(R.id.priceText);
        codeText = dialogView.findViewById(R.id.codeText);

        validityText.setText(offers.getValidity()+" Days");
        priceText.setText(offers.getPrice()+" Taka");
        codeText.setText(offers.getCode());

        dialogView.findViewById(R.id.cancelBtnOnPopup).setOnClickListener(v -> pointDialog.dismiss());
        dialogView.findViewById(R.id.confirmBtnOnPopup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = offers.getCode();
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                        "tel", phone, null));
                context.startActivity(phoneIntent);
            }
        });
    }
}
