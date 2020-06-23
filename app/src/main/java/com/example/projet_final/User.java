package com.example.projet_final;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class User {
    private Bitmap bitmap;
    private String stat,
            birthday,
            phone,
            sex,
            user_name,
            email="no email"
            ;
    private boolean Builder;
    private boolean House_painter;
    private boolean Moving;
    private boolean air_conditioner;
    private boolean electrician;
    private boolean gardening;
    private boolean housework;
    private boolean plumber;
    private double Latitude,Longitude;
    private String icone="";
    private int rate;

    public Uri getUri(){
        final CountDownLatch done=new CountDownLatch(1);
        final Uri[] s = new Uri[1];
        StorageReference storageReference= FirebaseStorage.getInstance().getReference()
                .child("default").child("default_men_img.png");
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()) {
                    s[0] = task.getResult();
                    done.countDown();
                    //return s;
                }else{
                    done.countDown();
                }
            }
        });
        /*storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                s[0]=uri;
                done.countDown();
            }
        });*/
        /*try {
            done.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return s[0];
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isBuilder() {
        return Builder;
    }

    public void setBuilder(boolean builder) {
        Builder = builder;
    }

    public boolean isPainter() {
        return House_painter;
    }

    public void setPainter(boolean painter) {
        this.House_painter = House_painter;
    }

    public boolean isMoving() {
        return Moving;
    }

    public void setMoving(boolean moving) {
        Moving = moving;
    }

    public boolean isAir_conditioner() {
        return air_conditioner;
    }

    public void setAir_conditioner(boolean air_conditioner) {
        this.air_conditioner = air_conditioner;
    }

    public boolean isElectrician() {
        return electrician;
    }

    public void setElectrician(boolean electrician) {
        this.electrician = electrician;
    }

    public boolean isGardening() {
        return gardening;
    }

    public void setGardening(boolean gardening) {
        this.gardening = gardening;
    }

    public boolean isHousework() {
        return housework;
    }

    public void setHousework(boolean housework) {
        this.housework = housework;
    }

    public boolean isPlumber() {
        return plumber;
    }

    public void setPlumber(boolean plumber) {
        this.plumber = plumber;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public boolean isHouse_painter() {
        return House_painter;
    }

    public void setHouse_painter(boolean house_painter) {
        House_painter = house_painter;
    }

    public HashMap<String,Boolean> jabs(){
        HashMap<String,Boolean> Hm=new HashMap<String, Boolean>();
        Hm.put("no_filter",true);
        Hm.put("annuler le filtre",true);
        Hm.put("Builder",Builder);
        Hm.put("constructeur",Builder);
        Hm.put("House_painter",House_painter);
        Hm.put("Peintre en bâtiment",House_painter);
        Hm.put("Moving",Moving);
        Hm.put("Déplacement",Moving);
        Hm.put("air_conditioner",air_conditioner);
        Hm.put("climatisation",air_conditioner);
        Hm.put("electrician",electrician);
        Hm.put("électricien",electrician);
        Hm.put("gardening",gardening);
        Hm.put("jardinage",gardening);
        Hm.put("housework",housework);
        Hm.put("travaux ménagers",housework);
        Hm.put("plumber",plumber);
        Hm.put("plombier",plumber);
        return Hm;
    }

    public Bitmap getIconeBitmap() {
        //StorageReference storageReference= FirebaseStorage.getInstance().getReferenceFromUrl(icone);
        final File file[] = new File[1];
        final CountDownLatch done=new CountDownLatch(1);
        StorageReference storageReference= FirebaseStorage.getInstance().getReference()
                .child("default").child("default_men_img.png");

        try {
            file[0]=File.createTempFile("image","png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        storageReference.getFile(file[0]).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.i("bitmap","secc");
                    bitmap= BitmapFactory.decodeFile(file[0].getAbsolutePath());
                    done.countDown();
                }
            });
        //bitmap= BitmapFactory.decodeFile(file[0].getAbsolutePath());
        try {
            done.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public String getJobsString(){
        StringBuffer jobs=new StringBuffer();
        switch (MapsActivity.lang){

            case "fr":

                if (Builder){
                    jobs.append("constructeur-");
                }
                if (House_painter){
                    jobs.append("Peintre en bâtiment-");
                }
                if (Moving){
                    jobs.append("Déplacement-");
                }
                if (air_conditioner){
                    jobs.append("climatisation-");
                }
                if (electrician){
                    jobs.append("électricien-");
                }
                if (gardening){
                    jobs.append("jardinage-");
                }
                if (housework){
                    jobs.append("travaux ménagers-");
                }
                if (plumber){
                    jobs.append("plombier-");
                }


                break;
            case "en":

                if (Builder){
                    jobs.append("Builder-");
                }
                if (House_painter){
                    jobs.append("House_painter-");
                }
                if (Moving){
                    jobs.append("Moving-");
                }
                if (air_conditioner){
                    jobs.append("air_conditioner-");
                }
                if (electrician){
                    jobs.append("electrician-");
                }
                if (gardening){
                    jobs.append("gardening-");
                }
                if (housework){
                    jobs.append("housework-");
                }
                if (plumber){
                    jobs.append("plumber-");
                }

                break;
        }

        jobs.deleteCharAt(jobs.length()-1);
        return jobs.toString();
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}


