# spring-hoverfly

Example of testing with hoverfly

ServiceA returns greeting "Hello {name}" at http://localhost:8081/
But it gets the name from ServiceB endpoint at http://localhost:8082/name

ServiceA has an IntegrationTestWithHoverfly.java which starts ServiceA on random port.
It uses Hoverfly to mock calls to serviceB
This is just a dummy learning example.

Of course, in this dummy project we can have a mocked version of 
com.example.serviceA.ServiceBClient instead.
See IntegrationTestWithMocks.java



