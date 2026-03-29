package org.example.demo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/hello-new")
public class HelloNew {
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, omaaaaaaaaaaaaaar!";
    }
}