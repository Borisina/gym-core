Feature: Logout

  Scenario: user logout
    Given a user, who's already logined and got his jwt
    When the user tries to logout
    Then response status should be 200
    And jwt cookie should be expired