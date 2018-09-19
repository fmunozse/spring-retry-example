# spring-retry-example

Very basic example  to play with spring-retry to play with retriable.

The goal of test is:
* To have a REST endpoint that call a method in a @Service. This method represent a call to external system
* The system is set up to have a circuitBreaker at 2 tries (2 calls from REST)
* But for each call, the system try to retry 3 times
* In case that is not possible or is open the circuirbreaker, system call to recover method.
