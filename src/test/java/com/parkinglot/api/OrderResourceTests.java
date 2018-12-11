package com.parkinglot.api;

import com.parkinglot.api.domain.Employee;
import com.parkinglot.api.domain.EmployeeRepository;
import com.parkinglot.api.domain.Order;
import com.parkinglot.api.domain.OrderRepository;
import com.parkinglot.api.domain.ParkingLot;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.domain.RoleName;
import com.parkinglot.api.models.OrderResponse;
import com.parkinglot.api.models.OrderTicketResponse;
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
    private EmployeeRepository employeeRepository;
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
    public void should_get_orderTicket_based_on_order() throws Exception {
        // Given
        Order newOrder = orderRepository.save(new Order("Car1"));
        orderRepository.flush();

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .get("/orders/" + newOrder.getId()))
            .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());
        final OrderTicketResponse orderTicketResponse = getContentAsObject(result, OrderTicketResponse.class);

        assertEquals("Car1", orderTicketResponse.getVehicleNumber());
    }

    @Test
    public void should_create_new_order() throws Exception {
        // Given
        String vehicleNumber = "CarToAdd";

        // When
        final MvcResult createOrderResult = mvc.perform(MockMvcRequestBuilders
            .post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"vehicleNumber\":\"" + vehicleNumber + "\"}"))
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

    @Test
    public void should_update_status_from_pending_to_parking_and_assign_the_parking_boy_to_order() throws Exception {
        // Given
        Order newOrder = orderRepository.save(new Order("car"));
        Employee newParkingBoy = employeeRepository.save(new Employee("boy", RoleName.ROLE_PARKING_CLERK));

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .put("/orders/" + newOrder.getId() + "/employeeId/" + newParkingBoy.getId()))
            .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());
        final OrderResponse orderResponse = getContentAsObject(result, OrderResponse.class);

        assertEquals("car", orderResponse.getVehicleNumber());
        assertEquals("parking", orderResponse.getOrderStatus());
        assertEquals(newParkingBoy.getId(), (long)orderResponse.getEmployeeId());
    }

    @Test
    public void should_update_status_from_parking_to_parked_and_assign_the_parking_lot_id_to_order() throws Exception {
        // Given
        Order newOrder = orderRepository.save(new Order("car"));
        Employee newParkingBoy = employeeRepository.save(new Employee("boy",RoleName.ROLE_PARKING_CLERK));
        ParkingLot newParkingLot = parkingLotRepository.save(new ParkingLot("lot", 10));

        // When
        final MvcResult putEmployeeIdResult = mvc.perform(MockMvcRequestBuilders
            .put("/orders/" + newOrder.getId() + "/employeeId/" + newParkingBoy.getId()))
            .andReturn();

        final MvcResult putParkingLotIdResult = mvc.perform(MockMvcRequestBuilders
            .put("/orders/" + newOrder.getId() + "/parkingLotId/" + newParkingLot.getId()))
            .andReturn();

        // Then
        assertEquals(200, putEmployeeIdResult.getResponse().getStatus());
        assertEquals(200, putParkingLotIdResult.getResponse().getStatus());


        final OrderResponse orderResponse = getContentAsObject(putParkingLotIdResult, OrderResponse.class);

        assertEquals("car", orderResponse.getVehicleNumber());
        assertEquals("parked", orderResponse.getOrderStatus());
        assertEquals(newParkingBoy.getId(), (long)orderResponse.getEmployeeId());
        assertEquals(newParkingLot.getId(), (long)orderResponse.getParkingLotId());
    }

    @Test
    public void should_update_status_from_parked_to_parking() throws Exception {
        // Given
        Order newOrder = orderRepository.save(new Order("car"));
        Employee newParkingBoy = employeeRepository.save(new Employee("boy",RoleName.ROLE_PARKING_CLERK));
        ParkingLot newParkingLot = parkingLotRepository.save(new ParkingLot("lot", 10));

        // When
        final MvcResult putEmployeeIdResult = mvc.perform(MockMvcRequestBuilders
            .put("/orders/" + newOrder.getId() + "/employeeId/" + newParkingBoy.getId()))
            .andReturn();
        final MvcResult putParkingLotIdResult = mvc.perform(MockMvcRequestBuilders
            .put("/orders/" + newOrder.getId() + "/parkingLotId/" + newParkingLot.getId()))
            .andReturn();
        final MvcResult customerCreateFetchRequestResult = mvc.perform(MockMvcRequestBuilders
            .patch("/orders/" + newOrder.getId()))
            .andReturn();


        // Then
        assertEquals(200, putEmployeeIdResult.getResponse().getStatus());
        assertEquals(200, putParkingLotIdResult.getResponse().getStatus());
        assertEquals(200, customerCreateFetchRequestResult.getResponse().getStatus());

        final OrderResponse orderResponse = getContentAsObject(customerCreateFetchRequestResult, OrderResponse.class);

        assertEquals("car", orderResponse.getVehicleNumber());
        assertEquals("fetching", orderResponse.getOrderStatus());
    }

    @Test
    public void should_update_status_from_fetching_to_fetched() throws Exception {
        // Given
        Order newOrder = orderRepository.save(new Order("car"));
        Employee newParkingBoy = employeeRepository.save(new Employee("boy",RoleName.ROLE_PARKING_CLERK));
        ParkingLot newParkingLot = parkingLotRepository.save(new ParkingLot("lot", 10));

        // When
        final MvcResult putEmployeeIdResult = mvc.perform(MockMvcRequestBuilders
            .put("/orders/" + newOrder.getId() + "/employeeId/" + newParkingBoy.getId()))
            .andReturn();
        final MvcResult putParkingLotIdResult = mvc.perform(MockMvcRequestBuilders
            .put("/orders/" + newOrder.getId() + "/parkingLotId/" + newParkingLot.getId()))
            .andReturn();
        final MvcResult customerCreateFetchRequestResult = mvc.perform(MockMvcRequestBuilders
            .patch("/orders/" + newOrder.getId()))
            .andReturn();
        final MvcResult parkingBoyFinishFetchResult = mvc.perform(MockMvcRequestBuilders
            .delete("/orders/" + newOrder.getId()))
            .andReturn();


        // Then
        assertEquals(200, putEmployeeIdResult.getResponse().getStatus());
        assertEquals(200, putParkingLotIdResult.getResponse().getStatus());
        assertEquals(200, customerCreateFetchRequestResult.getResponse().getStatus());
        assertEquals(200, parkingBoyFinishFetchResult.getResponse().getStatus());

        final OrderResponse orderResponse = getContentAsObject(parkingBoyFinishFetchResult, OrderResponse.class);

        assertEquals("car", orderResponse.getVehicleNumber());
        assertEquals("fetched", orderResponse.getOrderStatus());
    }

}
