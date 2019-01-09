package nl.evertwoud.issapp.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import androidx.room.Entity;

@Entity
public class Location implements Serializable {

    @SerializedName("message")
    String message;

    @SerializedName("timestamp")
    Long timestamp;

    @SerializedName("iss_position")
    ISSPosition position;


    public String getMessage() {
        return message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public ISSPosition getPosition() {
        return position;
    }
}
