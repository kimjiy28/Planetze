package com.example.planetze;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder> {
    private final Context context;
    private final List<Resource> resourceList;

    public ResourceAdapter(Context context, List<Resource> resourceList) {
        this.context = context;
        this.resourceList = resourceList;
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.resource_item, parent, false);
        return new ResourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceViewHolder holder, int position) {
        Resource resource = resourceList.get(position);
        holder.title.setText(resource.getTitle());
        holder.description.setText(resource.getDescription());
        Glide.with(context).load(resource.getImage()).into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resource.getUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return resourceList.size();
    }

    public static class ResourceViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView image;

        public ResourceViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.resourceTitle);
            description = itemView.findViewById(R.id.resourceDescription);
            image = itemView.findViewById(R.id.resourceImage);
        }
    }
}
