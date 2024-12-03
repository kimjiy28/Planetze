package com.example.planetze;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder> {

    private final Context context;
    private final List<Resource> resources;

    public ResourceAdapter(Context context, List<Resource> resources) {
        this.context = context;
        this.resources = resources;
    }

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
    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    public static class ResourceViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public ResourceViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.resourceTitle);
            description = itemView.findViewById(R.id.resourceDescription);
        }
    }
}


