package nl.evertwoud.issapp.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ISSPosition implements Serializable {

    @SerializedName("longitude")
    String longitude;

    @SerializedName("latitude")
    String latitiude;

    public String getLongitude() {
        return longitude;
    }

    public String getLatitiude() {
        return latitiude;
    }
}