## Guide to use back-end api

### `/parkingboys`

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
---------------
### `/parkinglots`

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
### `/orders`

#### Create a new order:
1. Given: a parking order with vehicle number
2. When: POST to `oders` with body:
```$json
{
	"vehicleNumber" : "test123"
}
```  
3. Then: should return 201 created

#### Get all orders with 'pending' status:
1. When: GET from `/parkinglots`
2. Then: should return 200 with body:
```$json
[
    {
        "vehicleNumber": "test123",
        "orderID": 1
    },
    {
        "vehicleNumber": "test456",
        "orderID": 2
    }...
]
```  
