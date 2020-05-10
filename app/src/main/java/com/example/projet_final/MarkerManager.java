package com.example.projet_final;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class MarkerManager extends DefaultClusterRenderer<marker> {
    private IconGenerator iconGenerator;
    private ImageView imageView;
    private int markerH;
    private int markerW;


    public MarkerManager(Context context, GoogleMap map, ClusterManager<marker> clusterManager, IconGenerator iconGenerator, ImageView imageView, int imageH, int imageW) {
        super(context, map, clusterManager);
        this.iconGenerator = iconGenerator;
        this.imageView = imageView;
        this.markerH = imageH;
        this.markerW = imageW;

        iconGenerator=new IconGenerator(context.getApplicationContext());
        imageView=new ImageView(context.getApplicationContext());
        //markerH=(int) context.getResources().getDimension(700011);
        //markerW=(int) context.getResources().getDimension(700011);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(512,512));
        int padding=300;
        imageView.setPadding(padding,padding,padding,padding);
        iconGenerator.setContentView(imageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull marker item, @NonNull MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        //imageView.setImageResource(item.getIco);
    }

}
