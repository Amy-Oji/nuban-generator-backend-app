# NUBAN Generator Backend App

This is a backend application that generates Nigerian Uniform Bank Account Numbers (NUBAN) based on provided bank institution codes and serial numbers. It exposes an API endpoint for generating NUBANs.

## Features

- Generates NUBANs based on bank institution codes and serial numbers.
- Provides a RESTful API endpoint for NUBAN generation.
- Supports CORS configuration for cross-origin requests.
- Handles invalid bank codes and provides appropriate error messages.

## Technologies Used

- Java
- Spring Boot
- Maven
- Postgres

## Getting Started

To get started with the NUBAN Generator Backend App, follow these steps:

1. Clone the repository:

   ```shell
   git clone git@github.com:Amy-Oji/nuban-generator-backend-app.git
   ```
   
2. Set up the database:
- Install PostgreSQL on your machine if you haven't already.
- Create a new PostgreSQL database with the name for the NUBAN Generator Backend App.
- Update the database connection details in the `application.properties` file located in the `src/main/resources` directory accordingly.


3. Navigate to the project directory:

   ```shell
   cd nuban-generator-backend
   ```

4. Build the application:

   ```shell
   mvn clean install
   ```

5. Run the application:

   ```shell
   mvn spring-boot:run
   ```

6. The application will start running on `http://localhost:8091`.



## Testing the Application

You can test the NUBAN Generator Backend App using one of the following methods:

### 1. NUBAN Generator Frontend App

You can use the [NUBAN Generator Frontend App](https://github.com/Amy-Oji/nuban-generator-frontend-app) 
to interact with the backend application and test the NUBAN generation functionality. 
Follow the instructions in the README.md file of the NUBAN Generator Frontend App to set it up and run it. 

### 2. Postman

You can use Postman, a popular API testing tool, to send requests to the NUBAN Generator Backend App and test the NUBAN generation functionality. Follow these steps:

1. Open Postman and create a new request.
2. Set the request method to `POST`.
3. Enter the URL `http://localhost:8091/api/v1/nuban/generate`.
4. Set the request headers:
    - `Content-Type`: `application/json`
5. In the request body, provide a valid bank code (there is a [JSON file](https://github.com/Amy-Oji/nuban-generator-backend-app/blob/master/src/main/resources/bank_list.json) containing bank names and their CBN issued unique codes in this directory: src/main/resources/bank_list.json. Use bank codes from the file) 
6. and serial number(not more than 9 digits) in JSON format:

   ```json
   {
     "bankCode": "057",
     "serialNum": "123456789"
   }
   ```

7. Click the **Send** button to send the request.
8. If the bank code and serial number are valid, the response will contain a JSON object with the newly generated NUBAN and other detail:

   ```json
   {
    "bankCode": "057",
    "serialNum": "123456789",
    "generatedNuban": "1234567899",
    "dateTime": "2023-05-22T17:26:02.22312"
   }
   ```
   If there is an error, the response will contain an appropriate error message.

## Note
- The NUBAN is generated based the provided by the [CBN algorithm](https://www.cbn.gov.ng/OUT/2011/CIRCULARS/BSPD/NUBAN%20PROPOSALS%20V%200%204-%2003%2009%202010.PDF)
which can be summarized thus:

  #### CHECK DIGIT ALGORITHM
  The approved NUBAN format is  ABC-DEFGHIJKL-M

  - where ABC is the 3-digit bank code assigned by the CBN

  - DEFGHIJKL is the NUBAN Account serial number

  - M is the NUBAN Check Digit, required for account number
    validation

    __The Check Digit Algorithm:__
  
    - Step 1. Calculate A*3+B*7+C*3+D*3+E*7+F*3+G*3+H*7+I*3+J*3+K*7+L*3

    - Step 2. Calculate Modulo 10 of your result i.e. the remainder after dividing by 10

    - Step 3. Subtract your result from 10 to get the Check Digit

    - Step 4. If your result is 10, then use 0 as your check digit

  Following the above algorithm, this application uses the bank code and the serial number to find the check digit. The application then concatenates the check digit to the provided serial number and returns that as the new NUBAN.


- If the provided serial number is not up to 9 digits, the application pads the provided digits with zeros to the front of the  digits.
