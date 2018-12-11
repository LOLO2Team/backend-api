package com.parkinglot.api;

import com.parkinglot.api.domain.ParkingBoy;
import com.parkinglot.api.domain.ParkingBoyRepository;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.models.ParkingBoyResponse;
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
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ParkingBoyTests {
    @Autowired
    private ParkingBoyRepository parkingBoyRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mvc;

    @Test
    public void should_get_parking_boys() throws Exception {
        // Given
        final ParkingBoy boy = parkingBoyRepository.save(new ParkingBoy("boy"));

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/parkingboys"))
                .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());

        final ParkingBoyResponse[] parkingBoys = getContentAsObject(result, ParkingBoyResponse[].class);

        assertEquals(1, parkingBoys.length);
        assertEquals("boy", parkingBoys[0].getName());
    }

    @Test
    public void should_throws_exception_when_employeeID_lenth_too_long() {
        //Given A parking boy with employeeID too long
        final String employeeID = "IdThatISTooLonggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg";
        ParkingBoy parkingBoy = new ParkingBoy(employeeID);
        //When save into repository should throw exception
        AssertHelper.assertThrows(Exception.class, () -> {
            parkingBoyRepository.save(parkingBoy);
            parkingBoyRepository.flush();
        });
    }

    @Test
    public void should_post_append_parking_boy_to_repo() throws Exception {
        // Given
        String json = "{\"name\" : \"test123\"}";

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/parkingboys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andReturn();

        // Then
        assertEquals(201, result.getResponse().getStatus());

        List<ParkingBoy> parkingBoys = parkingBoyRepository.findAll();

        // Should not use getOne() because it only get a proxy object from cache
        // Should use findOne() instead

        Optional<ParkingBoy> actualBoy = parkingBoyRepository.findById(1L);

        final ParkingBoyResponse parkingBoy = ParkingBoyResponse.create(actualBoy.get());

        assertEquals(1, parkingBoys.size());
        assertEquals("test123", parkingBoy.getName());
    }

//    @Test
//    public void should_post_return_404() throws Exception {
//        // Given
//        String json = "{\"WrongName\" : \"test123\"}";
//
//        // When
//        final MvcResult result = mvc.perform(MockMvcRequestBuilders
//                .post("/parkingboys")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andReturn();
//
//        // Then
//        AssertHelper.assertThrows(Exception.class, () -> {
//            parkingBoyRepository.save(parkingBoy);
//            parkingBoyRepository.flush();
//        });
//    }
}