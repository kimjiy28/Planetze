package com.example.planetze;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ResourceRepository {

    private final Context context;

    public ResourceRepository(Context context) {
        this.context = context;
    }

    public List<Resource> getResources() {
        List<Resource> resources = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("resources.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("resources");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String type = item.getString("type");
                String title = item.getString("title");
                String description = item.getString("description");
                String url = item.getString("url");
                String image = item.getString("image");

                resources.add(new Resource(type, title, description, url, image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resources;
    }
}


