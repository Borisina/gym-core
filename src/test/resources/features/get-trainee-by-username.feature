Feature: Get trainee by username

  Scenario: get trainee by username
    Given a trainee with username "Ivan.Sidorov", who's already in the database
    And   a user, who's already logined and got his jwt
    When the user tries to get trainee info by username
    Then response status should be 200
    And the user should get json info about trainee