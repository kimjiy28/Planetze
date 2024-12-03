package com.example.planetze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EcoHubFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_eco_hub, container, false);

        // Set up RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.resourceRecyclerView);
        ResourceRepository resourceRepository = new ResourceRepository(requireContext());
        ResourceAdapter adapter = new ResourceAdapter(requireContext(), resourceRepository.getResources());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return root;
    }
}




