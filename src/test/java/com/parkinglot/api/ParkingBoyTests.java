package com.parkinglot.api;

import com.parkinglot.api.domain.Employee;
import com.parkinglot.api.domain.EmployeeRepository;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.models.EmployeeDetailResponse;
import com.parkinglot.api.models.EmployeeResponse;
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
import static com.parkinglot.api.domain.EmployeeStatus.FROZEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ParkingBoyTests {
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
            .content("{\"name\":\"tester\",\"username\":\"tester\",\"password\":\"admin\",\"email\":\"mail@mail.com\",\"phone\":\"12945678\",\"authorities\":[{\"name\":\"ROLE_ADMIN\"}]}"))
            .andReturn();
        MvcResult loginResult = mvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"tester\", \"password\":\"admin\"}"))
            .andReturn();
        return loginResult.getResponse().getHeader("Authorization");
    }

    @Test
    public void should_get_parking_boys() throws Exception {
        // Given
        Employee newParkingBoy =
            employeeRepository.save(getTestingParkingBoy("newBoy"));

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .get("/parkingboys"))
            .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());

        final EmployeeResponse[] parkingBoys = getContentAsObject(result, EmployeeResponse[].class);

        assertEquals(1, parkingBoys.length);
        assertEquals("newBoy", parkingBoys[0].getName());
    }

    @Test
    public void should_throws_exception_when_employee_name_lenth_too_long() {
        //Given A parking boy with employeeID too long
        final String employeeName =
            "IdThatISTooLonggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg";
        //When save into repository should throw exception
        AssertHelper.assertThrows(Exception.class, () -> {
            employeeRepository.save(getTestingParkingBoy(employeeName));
            employeeRepository.flush();
        });
    }

    @Test
    public void should_post_parking_boy_to_repo() throws Exception {
        // Given
        String json =
            "{\"name\":\"newBoy\",\"username\":\"newBoy\",\"password\":\"admin\",\"email\":\"NEWBOY@mail.com\",\"phone\":\"15151515\",\"authorities\":[{\"name\":\"ROLE_PARKING_CLERK\"}]}";

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .post("/parkingboys")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header("Authorization", getAccessToken()))
            .andReturn();

        // Then
        assertEquals(201, result.getResponse().getStatus());

        Optional<Employee> actualBoy = employeeRepository.findByName("newBoy");
        assertTrue(actualBoy.isPresent());
    }

    @Test
    public void should_find_employee_by_username() throws Exception {
        // Given
        String username = "newboy";
        Employee employee = getTestingParkingBoy(username);
        employeeRepository.save(employee);
        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .get("/parkingboys?username="+username)
            .header("Authorization", getAccessToken()))
            .andReturn();

        // Then
        final EmployeeResponse parkingBoys = getContentAsObject(result, EmployeeResponse.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(parkingBoys.getUsername(),username );
    }

    @Test
    public void should_update_the_employee_status_to_frozen() throws Exception {
        // Given
        String username = "newboy";
        Employee newEmployee = employeeRepository.save(getTestingParkingBoy(username));
        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .put("/parkingboys/"+newEmployee.getId()+"/status/"+FROZEN.name())
            .header("Authorization", getAccessToken()))
            .andReturn();

        // Then
        Optional<Employee> updatedStatusEmployee = employeeRepository.findById(newEmployee.getId());

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(updatedStatusEmployee.get().getStatus(),FROZEN.name());
    }


    @Test
    public void should_find_employee_by_employee_id() throws Exception {
        // Given
        Employee newboy = employeeRepository.save(getTestingParkingBoy("newboy"));
        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .get("/employees/"+newboy.getId())
            .header("Authorization", getAccessToken()))
            .andReturn();

        // Then
        final EmployeeResponse parkingBoy = getContentAsObject(result, EmployeeResponse.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(parkingBoy.getEmployeeId(),newboy.getId() );
    }

    @Test
    public void should_find_employee_by_contains_some_keyword() throws Exception {
        // Given
        String keyword = "keyword";
        Employee newboy = employeeRepository.save(getTestingParkingBoy("the name have "+keyword));
        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .get("/employees/search?q="+keyword)
            .header("Authorization", getAccessToken()))
            .andReturn();

        // Then
        final EmployeeDetailResponse[] parkingBoy = getContentAsObject(result, EmployeeDetailResponse[].class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(parkingBoy[0].getName(),newboy.getName() );
    }
}