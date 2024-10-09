package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.example.Modals.Todos;
import org.example.Modals.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class FanCodeValidator {
    private static final String userApi = "http://jsonplaceholder.typicode.com/users";
    private static final String todossApi = "http://jsonplaceholder.typicode.com/todos";
    HashMap<Integer, Double> hashMap = new HashMap<>();

    public void validateUser() throws JsonProcessingException {
        Response response = given()
                .when().get(userApi)
                .then().assertThat().statusCode(200).extract().response();
        List<User> userList = new ObjectMapper().readValue(response.asString(), new TypeReference<List<User>>() {
        });

        for (User user : userList) {
            filterUserOnPercentage(user);
        }

    }

    private void filterUserOnPercentage(User user) throws JsonProcessingException {
        if (isFanCodeCityUser(user)){
        Response response = given().queryParam("userId", user.getId())
                .when().get(todossApi).then().assertThat().statusCode(200).extract().response();
        List<Todos> list = new ObjectMapper().readValue(response.asString(), new TypeReference<List<Todos>>() {
        });
        int completedTodos = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCompleted()) completedTodos++;

        }
        double completetedPercentage = ((double) completedTodos / list.size() * 100);


        if (completetedPercentage > 50) {
            hashMap.put(user.getId(), completetedPercentage);
        }
    }
    }

    public boolean printUserData() {
        if (hashMap.isEmpty()) return false;
        System.out.println("Users whose is a part of fancode city and having todos completed more than 50%");
        for (Map.Entry<Integer, Double> map : hashMap.entrySet()) {
            System.out.println("Userid => " + map.getKey() + " " + "with " + map.getValue()+"%");
        }
        return true;
    }


    private boolean isFanCodeCityUser(User user) {

        try {
            double lat = Double.parseDouble(user.getAddress().getGeo().getLat());
            double lng = Double.parseDouble(user.getAddress().getGeo().getLng());
            return lat >= -40 && lat <= 5 && lng >= 5 && lng <= 100;
        } catch (NumberFormatException e) {
            // Handle invalid geo data gracefully
            return false;
        }

    }
}
