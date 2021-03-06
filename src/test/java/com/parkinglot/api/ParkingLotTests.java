package com.parkinglot.api;

import com.parkinglot.api.domain.Employee;
import com.parkinglot.api.domain.EmployeeRepository;
import com.parkinglot.api.domain.ParkingLot;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.models.ParkingLotResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import java.util.*;

import static com.parkinglot.api.WebTestUtil.getContentAsObject;
import static com.parkinglot.api.WebTestUtil.getTestingParkingBoy;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ParkingLotTests {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mvc;

    String getAccessToken() throws Exception {
        MvcResult signUpResult = mvc.perform(post("/employees/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"tester\",\"username\":\"tester\",\"password\":\"admin\",\"email\":\"mail@mail.com\",\"phone\":\"12945678\",\"authorities\":[{\"name\":\"ROLE_PARKING_CLERK\"}]}"))
            .andReturn();
        MvcResult loginResult = mvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"tester\", \"password\":\"admin\"}"))
            .andReturn();
        return loginResult.getResponse().getHeader("Authorization");
    }

    String getManagerAccessToken() throws Exception {
        mvc.perform(post("/employees/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"manager\",\"username\":\"manager\",\"password\":\"manager\",\"email\":\"manager@mail.com\",\"phone\":\"12332123\",\"authorities\":[{\"name\":\"ROLE_MANAGER\"}]}"))
                .andReturn();
        MvcResult loginResult = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"manager\", \"password\":\"manager\"}"))
                .andReturn();
        return loginResult.getResponse().getHeader("Authorization");
    }

    @Test
    public void should_get_all_parking_lots() throws Exception {
        // Given
        final ParkingLot parkingLot =parkingLotRepository.save(new ParkingLot("Testing parking lot", 10));
        parkingLotRepository.flush();
        String token = getAccessToken();

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .get("/parkinglots")
            .header("Authorization", token))
            .andReturn();

        // Then
        final ParkingLotResponse[] parkingLotResponses = getContentAsObject(result, ParkingLotResponse[].class);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(1, parkingLotResponses.length);
        assertEquals("Testing parking lot", parkingLotResponses[0].getParkingLotName());
        assertEquals(10, parkingLotResponses[0].getCapacity());
    }

    @Test
    public void should_parking_clerk_assign_parking_lot_and_return_403_forbidden() throws Exception {
        // Given
        Employee newParkingBoy =
                employeeRepository.save(getTestingParkingBoy("newBoy"));
        employeeRepository.flush();
        final ParkingLot testBoyParkingLot = parkingLotRepository.save(new ParkingLot("TestBoy parking lot", 10));
        final ParkingLot otherParkingLot = parkingLotRepository.save(new ParkingLot("Parking lot of other parking boy", 10));
        parkingLotRepository.flush();
        String token = getAccessToken();

        // When
        final MvcResult putResult = mvc.perform(MockMvcRequestBuilders
                .put("/parkinglots/" + testBoyParkingLot.getId() + "/employeeId/" + newParkingBoy.getId())
                .header("Authorization", token))
                .andReturn();

        assertEquals(403, putResult.getResponse().getStatus());
    }

    @Test
    public void should_assign_parking_lot_and_get_parking_available_parking_lots_by_employee_id() throws Exception {
        // Given
        Employee newParkingBoy =
            employeeRepository.save(getTestingParkingBoy("newBoy"));
        employeeRepository.flush();
        final ParkingLot testBoyParkingLot = parkingLotRepository.save(new ParkingLot("TestBoy parking lot", 10));
        final ParkingLot otherParkingLot = parkingLotRepository.save(new ParkingLot("Parking lot of other parking boy", 10));
        parkingLotRepository.flush();
        String token = getManagerAccessToken();

        // When
        final MvcResult putResult = mvc.perform(MockMvcRequestBuilders
            .put("/parkinglots/" + testBoyParkingLot.getId() + "/employeeId/" + newParkingBoy.getId())
            .header("Authorization", token))
            .andReturn();
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .get("/parkinglots?employeeId=" + newParkingBoy.getId())
            .header("Authorization", token))
            .andReturn();

        // Then
        final ParkingLotResponse[] parkingLotResponses = getContentAsObject(result, ParkingLotResponse[].class);
        assertEquals(201, putResult.getResponse().getStatus());
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(1, parkingLotResponses.length);
        assertEquals("TestBoy parking lot", parkingLotResponses[0].getParkingLotName());
        assertEquals(10, parkingLotResponses[0].getCapacity());
    }

    @Test
    public void should_post_parking_lot_to_DB() throws Exception {
        // Given
        String json = "{\"parkingLotName\" : \"TestPark123\", \"capacity\" : 10}";

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .post("/parkinglots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andReturn();

        // Then
        assertEquals(201, result.getResponse().getStatus());

        List<ParkingLot> parkingLots = parkingLotRepository.findAll();

        Optional<ParkingLot> actualParkingLot = parkingLotRepository.findById(1L);

        final ParkingLotResponse parkingLot = ParkingLotResponse.create(actualParkingLot.get());

        assertEquals(1, parkingLots.size());
        assertEquals("TestPark123", parkingLot.getParkingLotName());
        assertEquals(10, parkingLot.getCapacity());
    }
}
