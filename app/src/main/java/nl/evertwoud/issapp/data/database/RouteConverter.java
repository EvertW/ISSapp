package nl.evertwoud.issapp.data.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import androidx.room.TypeConverter;
import nl.evertwoud.issapp.data.models.Location;

public class RouteConverter {

        @TypeConverter
        public static List<Location> toList(String value) {
            Gson gson = new Gson();
            if (value == null) {
                return Collections.emptyList();
            }

            Type listType = new TypeToken<List<Location>>() {}.getType();

            return gson.fromJson(value, listType);
        }

        @TypeConverter
        public static String fromList(List<Location> someObjects) {
            Gson gson = new Gson();
            return gson.toJson(someObjects);
        }
}
