@workload
Feature: Get workload

    Scenario: Get workload
        Given a user, who's already logined and got his jwt
        And a trainerWorkload (trainerUsername "Michail.Kotov", duration 30, year 2020, month "MARCH"), that is saved in the other microservice
        When the user tries to get workload by trainer's username "Michail.Kotov"
        Then response status should be 200
        And the user should get json workload info