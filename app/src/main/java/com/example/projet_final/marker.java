package com.example.projet_final;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class marker  implements ClusterItem {
    private String title;
    private LatLng position;
    private String snippet;
    private String userID;
    private int icon;

    public marker(String title, LatLng position, String snippet, String userID, int icon) {
        this.title = title;
        this.position = position;
        this.snippet = snippet;
        this.userID = userID;
        this.icon = icon;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return snippet;
    }
}
