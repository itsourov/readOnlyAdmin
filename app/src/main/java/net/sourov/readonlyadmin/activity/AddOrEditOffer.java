package net.sourov.readonlyadmin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.sourov.readonlyadmin.R;
import net.sourov.readonlyadmin.model.Offers;

import java.util.HashMap;
import java.util.UUID;

public class AddOrEditOffer extends AppCompatActivity {

    EditText offerTitle,offerDetails,offerPrice,offerValidity,offerCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_offer);

        String offerID = getIntent().getStringExtra("offerID");
        String appID = getIntent().getStringExtra("appID");

        Log.d("TAG", "onCreate: "+getIntent().getExtras());
        if (appID!=null){
            doThings(offerID,appID);
        }

        offerTitle = findViewById(R.id.offerTitle);
        offerDetails = findViewById(R.id.offerDetails);
        offerPrice = findViewById(R.id.offerPrice);
        offerValidity = findViewById(R.id.offerValidity);
        offerCode = findViewById(R.id.offerCode);

    }

    private void doThings(String offerID, String appID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(appID).child("offers");

        if (offerID==null){


        }else {

            reference.child(offerID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                   try {
                       Offers offers = snapshot.getValue(Offers.class);
                       assert offers != null;
                       offerTitle.setText(offers.getTitle());
                       offerDetails.setText(offers.getDetails());
                       offerPrice.setText(String.valueOf(offers.getPrice()));
                       offerValidity.setText(String.valueOf(offers.getValidity()));
                       offerCode.setText(offers.getCode());
                   } catch (Exception e) {
                       e.printStackTrace();
                   }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Button delete = findViewById(R.id.delete);
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(v -> reference.child(offerID).removeValue().addOnSuccessListener(unused -> finish()));



        }
        findViewById(R.id.submit).setOnClickListener(v -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("title", offerTitle.getText().toString().trim());
            hashMap.put("details", offerDetails.getText().toString().trim());
            hashMap.put("code", offerCode.getText().toString().trim());
            hashMap.put("validity",Integer.parseInt(offerValidity.getText().toString().trim()) );
            hashMap.put("price", Integer.parseInt(offerPrice.getText().toString().trim()));

            if (offerID !=null){
                reference.child(offerID).updateChildren(hashMap).addOnSuccessListener(unused -> Toast.makeText(AddOrEditOffer.this, "Submitted", Toast.LENGTH_SHORT).show());
            }else {
                String UniqueID = UUID.randomUUID().toString().replaceAll("-", "");
                hashMap.put("offerID",UniqueID);
                reference.child(UniqueID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();

                    }
                });

            }
        });

    }
}