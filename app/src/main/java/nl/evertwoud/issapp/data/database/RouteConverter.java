package nl.evertwoud.issapp.data.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import androidx.room.TypeConverter;
import nl.evertwoud.issapp.data.models.Location;

//Converter to parse the data to json strings for storing using room
public class RouteConverter {

        //Convert json string to object list
        @TypeConverter
        public static List<Location> toList(String value) {
            Gson gson = new Gson();
            if (value == null) {
                return Collections.emptyList();
            }
            Type listType = new TypeToken<List<Location>>() {}.getType();
            return gson.fromJson(value, listType);
        }

        //Convert to json string
        @TypeConverter
        public static String fromList(List<Location> someObjects) {
            Gson gson = new Gson();
            return gson.toJson(someObjects);
        }
}
