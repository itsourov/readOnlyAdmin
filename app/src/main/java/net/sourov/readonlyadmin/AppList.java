package net.sourov.readonlyadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.sourov.readonlyadmin.adapter.AppListAdapter;
import net.sourov.readonlyadmin.model.AppListModel;

import java.util.ArrayList;
import java.util.List;

public class AppList extends AppCompatActivity {

    RecyclerView recyclerView;
    List<AppListModel> appListModels;
    AppListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        recyclerView = findViewById(R.id.appListRV);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        displayApps();


    }

    private void displayApps() {
        appListModels =  new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                appListModels.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Log.d("TAG", "onDataChange: "+ds.getKey());
                    reference.child(ds.getKey()).child("Administration").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            AppListModel admin = snapshot.getValue(AppListModel.class);
                            admin.setAppID(ds.getKey());
                            appListModels.add(admin);
                            mAdapter = new AppListAdapter(AppList.this,appListModels);
                            recyclerView.setAdapter(mAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}