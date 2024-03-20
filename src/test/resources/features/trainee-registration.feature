Feature: Trainee Registration

  Scenario: a new trainee successful registration
    Given a trainee with firstName "Ivan", lastName "Sidorov" and bearerToken "myBearerToken" for registration
    When the trainee tries to register
    Then response status should be 201
    And the trainee should be saved in the database

  Scenario: a new trainee failed registration (no bearerToken)
    Given a trainee with firstName "Ivan", lastName "Sidorov" and bearerToken "" for registration
    When the trainee tries to register
    Then response status should be 403
    And the trainee shouldn't be saved in the database

  Scenario: a new trainee failed registration (invalid firstName)
    Given a trainee with firstName "Ivan1", lastName "Sidorov" and bearerToken "myBearerToken" for registration
    When the trainee tries to register
    Then response status should be 400
    And the trainee shouldn't be saved in the database

  Scenario: a new trainee failed registration (invalid secondName)
    Given a trainee with firstName "Ivan1", lastName "Sidorov" and bearerToken "myBearerToken" for registration
    When the trainee tries to register
    Then response status should be 400
    And the trainee shouldn't be saved in the database