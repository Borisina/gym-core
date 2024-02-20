@workload
Feature: Get workload

    Scenario: Get workload
        Given a user, who's already logined and got his jwt
        When the user tries to get workload by trainer's username "Michail.Kotov"
        Then response status should be 200
        And the user should get json workload info