## Guide to use back-end api

Root api: https://parking-lot-backend.herokuapp.com/

### `https://parking-lot-backend.herokuapp.com/parkingboys`

#### Create a new parking boy:
1. Given: a parking boy name
2. When: POST to `parkingboys` with body:
```$json
{
	"name" : "TestBoy123"
}
```  
3. Then: should return 201 created

#### Get all parking boy:
1. When: GET from `/parkingboys`
2. Then: should return 200 with body:
```$json
[
    {
        "employeeId": 33,
        "name": "TestBoy123"
    },
    {
        "employeeId": 34,
        "name": "TestBoy456"
    }...
]
```

#### Park a car:
1. Given: a parking order with vehicle number
2. When: PUT to `parkingboys/{employeeId}/parkinglots/{parkingLotId}/orders/{orderId}`
3. Then: update the order status to "parking" and set a parking_lot_id to order
---------------


#### fetch a car:
1. Given: a parking order with vehicle number
2. When: DELETE to `parkingboys/{employeeId}/parkinglots/{parkingLotId}/orders/{orderId}`
3. Then: update the order status to "fetched"
---------------
### `https://parking-lot-backend.herokuapp.com/parkinglots`

#### Create a new parking lot:
1. Given: a parking lot with capacity
2. When: POST to `parkinglots` with body:
```$json
{
	"capacity" : 10
}
```  
3. Then: should return 201 created

#### Get all parking lot:
1. When: GET from `/parkinglots`
2. Then: should return 200 with body:
```$json
[
    {
        "parkingLotId": 35,
        "capacity": 10,
        "reservedSpace": 0
    },
    {
        "parkingLotId": 36,
        "capacity": 5,
        "reservedSpace": 0
    }...
]
```  
----------
### `https://parking-lot-backend.herokuapp.com/orders`

#### Create a new order:
1. Given: a parking order with vehicle number
2. When: POST to `orders` with body:
```$json
{
	"vehicleNumber" : "test123"
}
```  
3. Then: should return 201 created and save a new order with orderStatus="pending"

#### Get all orders:
1. When: GET from `/orders`
2. Then: should return 200 with body:
```$json
[
    {
        "vehicleNumber": "test123",
        "orderID": 1
        "status": "pending"
    },
    {
        "vehicleNumber": "test999",
        "orderID": 2
        "status": "parking"
    }...
]
```

#### Get orders by status (pending / parking / fetched / cancel):
1. When: GET from `/orders?status=pending`
2. Then: should return 200 with body:
```$json
[
    {
        "vehicleNumber": "test123",
        "orderID": 1
        "status": "pending"
    }...
]
```  
