package com.example.planetze;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ResourceManager {
    private Context context;

    public ResourceManager(Context context) {
        this.context = context;
    }

    public List<Resource> getResources() {
        List<Resource> resourceList = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("resources.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject obj = new JSONObject(json);
            JSONArray resourcesArray = obj.getJSONArray("resources");

            for (int i = 0; i < resourcesArray.length(); i++) {
                JSONObject resourceObj = resourcesArray.getJSONObject(i);
                Resource resource = new Resource(
                        resourceObj.getString("type"),
                        resourceObj.getString("title"),
                        resourceObj.getString("description"),
                        resourceObj.getString("url"),
                        resourceObj.getString("image")
                );
                resourceList.add(resource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceList;
    }
}

