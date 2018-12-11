package com.parkinglot.api;

import com.parkinglot.api.domain.Order;
import com.parkinglot.api.domain.OrderRepository;
import com.parkinglot.api.domain.ParkingBoyRepository;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.models.OrderResponse;
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

import static com.parkinglot.api.WebTestUtil.getContentAsObject;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderResourceTests {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ParkingBoyRepository parkingBoyRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mvc;

    @Test
    public void should_get_all_pending_orders() throws Exception {
        // Given
        orderRepository.save(new Order("Car1"));
        orderRepository.flush();
        String expectStatus = "pending";

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .get("/orders?status=" + expectStatus))
            .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());
        final OrderResponse[] orderResponses = getContentAsObject(result, OrderResponse[].class);

        assertEquals(1, orderResponses.length);
        assertEquals("Car1", orderResponses[0].getVehicleNumber());
    }

    @Test
    public void should_create_new_order() throws Exception {
        // Given
        String vehicleNumber = "CarToAdd";

        // When
        final MvcResult createOrderResult = mvc.perform(MockMvcRequestBuilders
            .post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"vehicleNumber\":\""+vehicleNumber+"\"}"))
            .andReturn();

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .get("/orders"))
            .andReturn();

        // Then
        assertEquals(201, createOrderResult.getResponse().getStatus());
        assertEquals(200, result.getResponse().getStatus());
        final OrderResponse[] orderResponses = getContentAsObject(result, OrderResponse[].class);

        assertEquals(1, orderResponses.length);
        assertEquals(vehicleNumber, orderResponses[0].getVehicleNumber());
    }
}
