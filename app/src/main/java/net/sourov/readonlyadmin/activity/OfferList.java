package net.sourov.readonlyadmin.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.sourov.readonlyadmin.R;
import net.sourov.readonlyadmin.Sourov;
import net.sourov.readonlyadmin.model.Offers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OfferList extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Offers> offersList;
    Sourov sourov;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);

      

        sourov = new Sourov(OfferList.this);

        recyclerView = findViewById(R.id.rvOfferList);
        recyclerView.setHasFixedSize(true);

        offersList = new ArrayList<>();

        String appID = getIntent().getStringExtra("appID");
        if (appID != null) {
            getData(appID);
        }






    }




    private void getData(String appID) {
        
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(appID).child("offers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                offersList.clear();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        try {
                            Offers offers = ds.getValue(Offers.class);
                            offersList.add(offers);
                            OfferAdapter offerAdapter = new OfferAdapter();
                            offerAdapter.appid = appID;
                            recyclerView.setAdapter(offerAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferHolder> {


        public String appid;

        @NonNull
        @Override
        public OfferAdapter.OfferHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(OfferList.this).inflate(R.layout.single_offer_item, parent, false);
            return new OfferHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OfferAdapter.OfferHolder holder, int position) {
            Offers offers = offersList.get(position);
            holder.offerTitle.setText(offers.getTitle());
            holder.offerDetails.setText(offers.getDetails());
            holder.priceText.setText("Tk."+offers.getPrice());

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(OfferList.this, OfferDetails.class);
                intent.putExtra("offerID", offers.getOfferID());
                intent.putExtra("appID", appid);
                startActivity(intent);
            });

            holder.buyNowBtnOnList.setOnClickListener(v -> {

                sourov.confirmationPopUp(offers);
            });
            holder.editBtn.setOnClickListener(v -> {
                Intent intent = new Intent(OfferList.this,AddOrEditOffer.class);
                intent.putExtra("appID",appid);
                intent.putExtra("offerID",offers.getOfferID());
                startActivity(intent);
            });

        }




        @Override
        public int getItemCount() {
            return offersList.size();
        }

        public class OfferHolder extends RecyclerView.ViewHolder {
            TextView offerTitle, offerDetails,priceText;
            Button buyNowBtnOnList,editBtn;

            public OfferHolder(@NonNull View itemView) {
                super(itemView);

                offerTitle = itemView.findViewById(R.id.offerTitle);
                priceText = itemView.findViewById(R.id.priceText);
                offerDetails = itemView.findViewById(R.id.offerDetails);
                buyNowBtnOnList = itemView.findViewById(R.id.buyNowBtnOnList);
                editBtn = itemView.findViewById(R.id.editBtn);
            }
        }
    }
}