<h1>Pension Management Portal</h1>
<p>Pension Management Portal is a project designed to disburse the pension to former employees of an organization.

The project uses Java, Springboot, JPA, hibernate, h2 (in-memory database) among a few other dependencies for backend while angular handles the frontend. Further, in backend, microservice architecture has been used for processing of data. Let's have a high level overview of the project.</p>

![Project Architecture](https://user-images.githubusercontent.com/49477641/185588264-54a78a64-ca76-4683-9b1b-8d885574ae66.png)

<p>We will walk through the following - Pension Management Portal - backend, Pension Management Portal - frontend and Cloud Native approach used in project. Let's have a closer look into all these, one-by-one.
<ul>
<li><b>Pension Management Portal - backend</b> - This is where we handle all the logic for processing of data via microservices. There is one admin account responsible for processing pension of former employees. Three microservices that are used:
  <ul>
  <br>
    <li><b>Authorization Microservice</b> - Microservice is responsible for authentication and authorization of requests made to the endpoints exposed by other microservices. A Authorization header along with JWT Token is added to all the requests forwarded to peer microservices and any response is returned only after validation of the request via the token shared. The password for user is stored in an encrypted format in the in-memory database.
    </li> 
    <br>
    <li><b>Pensioner Detail Microservice</b> - Microservice takes Aadhaar number of the employee as a path variable while calling the endpoint exposed with GET, and fetches the details from a csv file having details of employees. Any invalid aadhaar number passed as path variable returns Error 404 - Not Found. Microservice connects with the Authorization Microservice to pass the Authorization header with token as request header during the call for validation of the request and returns a response after validation.
  </li>
  <br>
  <li><b>Process Pension Microservice</b> - This microservice is responsible for processing the pension for employees. It triggers Pensioner Detail Microservice, recieves the response from it and passes values from the response in its request body and gets its own response back. Further, it adds both the pensioner input (pensioner detail) and pension amount to the in-memory database. This microservice also makes use of Authorization Microservice for validation of requests.
  </li>
  <br>
  </ul>
  </li>
  Apart from the above mentioned microservices we have two more components to help facilitate better communication between these microservices. 
  <ul>
  <br>
  <li><b>Discovery Client</b> - Project makes use of Netflix Eureka Server for service registry to help facilitate the communication between microservices. 
  </li>
  <br>
  <li><b>Api Gateway</b> - Project also has a api gateway responsible for handling all the incoming requests and forwarding them to the right microservice.
  </li>
  <br>
  </ul>
  <li><b>Pension Management Portal - frontend</b> - For frontend, project makes use of angular, and all the components, services among others have been created and used for rendering the data that we recieve from backend. Validations for most of the cases - token expiration, services up/down, invalid credentials or invalid input among others have been added and work as expected.
  <br><br>
Normal Flow of the app wil look something like this:


![Pension Managemnet Portal Application Flowchart](https://user-images.githubusercontent.com/49477641/185571013-a21eec47-4b04-4d01-a964-ab846e022d0a.png)
  </ul>
  Microservices can be deployed on Amazon Web Services (AWS) Fargate. An appspec.yaml and a buildspec.yaml file is present in each of the microservices facilitating CI/CD using AWS CodePipeline - fetching data from source, creating container images and pushing it to Elastic Container Registry (ECR) and deploying them to AWS Fargate.
  </p>
