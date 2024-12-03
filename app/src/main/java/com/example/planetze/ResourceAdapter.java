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
    private final List<Resource> resources;

    public ResourceAdapter(Context context, List<Resource> resources) {
        this.context = context;
        this.resources = resources;
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resource_item, parent, false);
        return new ResourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResourceViewHolder holder, int position) {
        Resource resource = resources.get(position);

        holder.title.setText(resource.getTitle());
        holder.description.setText(resource.getDescription());

        Glide.with(context)
                .load(resource.getImage()) // `resource.getImage()` is the URL from JSON
                .error(R.drawable.error_image)
                .into(holder.image);

        holder.image.setOnClickListener(v -> {
            String url = resource.getUrl();
            if (url != null && !url.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    public static class ResourceViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView image;

        public ResourceViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.resourceTitle);
            description = itemView.findViewById(R.id.resourceDescription);
            image = itemView.findViewById(R.id.resourceImage);
        }
    }
}


