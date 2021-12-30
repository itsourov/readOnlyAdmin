package net.sourov.readonlyadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.sourov.readonlyadmin.AppList;
import net.sourov.readonlyadmin.R;
import net.sourov.readonlyadmin.activity.AppInfo;
import net.sourov.readonlyadmin.model.AppListModel;

import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppHolder> {

    Context context;
    List<AppListModel> appList;
    Activity activity ;

    public AppListAdapter(Context context, List<AppListModel> appList) {
        this.context = context;
        this.appList = appList;

        activity = (Activity) context;
    }

    @NonNull
    @Override
    public AppHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_app_item, parent,false);
        return new AppHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppHolder holder, int position) {
        AppListModel app = appList.get(position);

        holder.appName.setText(app.getAppName());
        Glide.with(context).load(app.getAppImage()).into(holder.appImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AppInfo.class);
            intent.putExtra("appID",app.getAppID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class AppHolder extends RecyclerView.ViewHolder {
        ImageView appImage;
        TextView appName;
        public AppHolder(@NonNull View itemView) {
            super(itemView);
            appImage = itemView.findViewById(R.id.appImage);
            appName = itemView.findViewById(R.id.appName);
        }
    }
}
