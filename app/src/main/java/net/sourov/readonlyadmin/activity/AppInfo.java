package net.sourov.readonlyadmin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.sourov.readonlyadmin.R;
import net.sourov.readonlyadmin.model.AppListModel;

import java.util.HashMap;

public class AppInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        String appID = getIntent().getStringExtra("appID");


        if (appID != null) {
            getData(appID);
        }


    }

    private void getData(String appID) {
        EditText appName = findViewById(R.id.appName);
        EditText tagLine = findViewById(R.id.tagLine);
        EditText imageLink = findViewById(R.id.imageLink);
        ImageView imagePreview = findViewById(R.id.imagePreview);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(appID).child("Administration");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AppListModel app = snapshot.getValue(AppListModel.class);
                assert app != null;
                appName.setText(app.getAppName());
                tagLine.setText(app.getTagLine());
                imageLink.setText(app.getAppImage());

                Glide.with(AppInfo.this).load(app.getAppImage()).into(imagePreview);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("appName", appName.getText().toString().trim());
                hashMap.put("appImage", imageLink.getText().toString().trim());
                hashMap.put("tagLine", tagLine.getText().toString().trim());

                reference.updateChildren(hashMap);


            }
        });
        findViewById(R.id.seeOffers).setOnClickListener(v -> {
            Intent intent = new Intent(AppInfo.this,OfferList.class);
            intent.putExtra("appID",appID);
            startActivity(intent);
        });
        findViewById(R.id.addOffer).setOnClickListener(v -> {
            Intent intent = new Intent(AppInfo.this,AddOrEditOffer.class);
            intent.putExtra("appID",appID);
            startActivity(intent);
        });

    }
}