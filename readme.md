## Guide to use back-end api

Root api: https://parking-lot-backend.herokuapp.com/

### `https://parking-lot-backend.herokuapp.com/parkinglots`

#### Create a new parking lot:
1. Given: a parking lot with capacity
2. When: POST to `/parkinglots` with body:
```$json
{
    "parkingLotName": "name",
	"capacity" : 10
}
```  
3. Then: should return 201 created

#### Get all parking lot by employeeId:
1. When: GET from `/parkinglots?employeeId={employeeId}`
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

#### Assign parking lot to a parking boy:
1. When: PUT from `/parkinglots/{parkingLotId}`
2. Then: should return 200 with body:

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

#### Get orders by status (pending / parking / fetched ...):
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

(for parkingboy)
#### Grab order:
1. Given:  employeeId, orderid of order with status "pending"
2. When: PUT to `/orders/{orderId}/employeeId/{employeeId}`
3. Then: update the order status to "parking" and set a employeeId to order
---------------

(for parkingboy)
#### Finish parking the car:
1. Given:  parkingLotId, orderid of order with status "parking"
2. When: PUT to `/orders/{orderId}/parkingLotId/{parkingLotId}`
3. Then: update the order status to "parked" and set a parkingLotId to order
---------------

(for customer)
#### Request fetching car:
1. Given: orderid of order with status "parked"
2. When: PATCH to `/orders/{orderId}`
3. Then: update the order status to "fetching"
---------------

(for parkingboy)
#### Finish fetching car:
1. Given: orderid of order with status "parked"
2. When: DELETE to `/orders/{orderId}`
3. Then: update the order status to "fetched"
---------------

## Guide to deploy to prod
Replace the deploy text in .travis.yml with below
deploy:
  provider: heroku
  api_key: 02ad66a3-fa5a-4f4a-a0c2-1542413b1246
  app: parking-lot-backend-prod
  on:
    branches:
        only:
            - prod
