<h1>Pension Management Portal</h1>
<p>Pension Management Portal is a project designed to disburse the pension to former employees of an organization.

The project uses Java, Springboot, JPA, hibernate among a few other dependencies for backend while Angular handles the frontend. Further, in backend, microservice architecture has been used for processing of data. Also, project has compatibility with docker for containerization.</p>

<h3>Pension Management Portal - Components</h3>

<p>Project will have three components - PMP - backend, PMP - frontend, PMP - DevOps. Let's have a closer look into all these, one-by-one.
<ul>
<li><b>PMP - backend</b> - This is where we handle all the logic for processing of data via microservices. Three microservices that are used:
  <ul>
    <li><b>Authorization Microservice</b>- Microservice is responsible for authentication and authorization of requests made to the endpoints exposed by other microservices. A Authorizatyion header along with JWT Token is added to all the requests forwarded to peer microservices and any response is returned only after validation of the request via the token shared.
    </li>
  </ul>
  </li>
  </ul>
  </p>
