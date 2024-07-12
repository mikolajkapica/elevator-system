# Elevator System

This project implements an elevator system using Scala and Akka HTTP, providing an API for managing and simulating elevator operations within a building.

## Overview
The API exposes several endpoints to interact with the elevator system. It utilizes Akka HTTP for handling HTTP requests and Akka actors for managing the state and behavior of elevators. Below are the key endpoints and their functionalities:

#
1. **Pickup Route (`POST /elevator/pickup`)**: Adds a new elevator pickup request.
2. **Update Route (`PATCH /elevator/update`)**: Updates elevator status with current and target floors.
3. **Status Route (`GET /elevator/status`)**: Retrieves current elevator statuses.
4. **Floors Route (`GET /elevator/floors`)**: Retrieves floor waiting counts.
5. **Step Route (`POST /elevator/step`)**: Advances the elevator simulation.
6. **Simulate New Pickup Requests Route (`POST /elevator/simulate`)**: Simulates new pickup requests.

## Finding the Best Elevator Cab Algorithm
The findBestCab function determines which elevator cab should respond to a pickup request based on several criteria:

There are two algorithms provided:
- FIFO (First In, First Out) (default, just the first cab that is empty is selected)
- Priority

Priority Algorithm:

1. Distance Score:
The absolute distance between the cab's current floor and the request floor.

2. Direction Score:
- If the cab is idle or moving in the same direction as the request and hasn't passed the request floor, the score is 0.
- If the cab is moving in the opposite direction or has passed the request floor, it calculates the distance to the next drop request that could change the cab's direction, accounting for extra time.
 
3. Requests Size Score:
- Adds a penalty based on the number of pending drop requests, scaled by a factor of 0.5.

4. The function maps these scores to each cab, sums them, and selects the cab with the lowest score as the best choice for the new pickup request.

## How to Run with Docker

Follow these steps to run the project using Docker:

### Prerequisites

Make sure you have Docker installed on your machine. You can download and install Docker from [here](https://www.docker.com/get-started).

### Step-by-Step Instructions

1. **Clone the repository:**

```console
$ git clone https://github.com/mikolajkapica/elevator-system.git
$ cd elevator-system
```
   
Build the Docker image:

   
```console
$ docker build -t elevator-system .
```

This command builds a Docker image named elevator-system based on the Dockerfile in the project directory.

Run the Docker container:

```console
$ docker run -it elevator-system
```
This command starts the application inside a Docker container. The -it flag ensures an interactive terminal session.
You can also use docker compose to run the application.

## License
This project is licensed under the GPL-3.0 License. See the LICENSE file for more details.

## Contact
For any questions or feedback, please reach out to us at mikolajkapica7@gmail.com

