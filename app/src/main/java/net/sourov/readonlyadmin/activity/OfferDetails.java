package net.sourov.readonlyadmin.activity;


import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.sourov.readonlyadmin.R;
import net.sourov.readonlyadmin.Sourov;
import net.sourov.readonlyadmin.model.Offers;

import java.util.Objects;

public class OfferDetails extends AppCompatActivity {

    String offerID = "";
    Sourov sourov;
    Offers offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        sourov = new Sourov(OfferDetails.this);


        offerID = getIntent().getStringExtra("offerID");
        String appID = getIntent().getStringExtra("appID");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(appID).child("offers");
        reference.keepSynced(true);
        reference.child(offerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                offer = dataSnapshot.getValue(Offers.class);

                setData(offer);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findViewById(R.id.buyNowBtn).setOnClickListener(v -> sourov.confirmationPopUp(offer));
    }

    private void setData(Offers offers) {
        TextView validityText, priceText, codeText;

        validityText = findViewById(R.id.validityText);
        priceText = findViewById(R.id.priceText);
        codeText = findViewById(R.id.codeText);

        validityText.setText(offers.getValidity() + " Days");
        priceText.setText(offers.getPrice() + " Taka");
        codeText.setText(offers.getCode());

        getSupportActionBar().setTitle(offers.getTitle());

        TextView offerDetails = findViewById(R.id.offerDetails);
        offerDetails.setText(offers.getDetails());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}