package com.parkinglot.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkinglot.api.domain.Employee;
import com.parkinglot.api.user.RoleName;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.*;

class WebTestUtil {
    public static <T> T toObject(String jsonContent, Class<T> clazz) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonContent, clazz);
    }

    public static <T> T getContentAsObject(MvcResult result, Class<T> clazz) throws Exception {
        return toObject(result.getResponse().getContentAsString(), clazz);
    }

    public static Employee getTestingParkingBoy(String name){
        return new Employee(name, "newBoy", "boy@mail", "password",
            Arrays.asList(RoleName.ROLE_PARKING_CLERK));
    }
}
