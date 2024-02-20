Feature: Login

  Scenario: user successful login
    Given a user with username "first.last" and password "password", who's already registered
    When the user tries to login
    Then response status should be 200
    And the user should get a jwt cookie