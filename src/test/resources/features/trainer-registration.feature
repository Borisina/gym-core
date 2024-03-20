Feature: Trainer Registration

  Scenario: a new trainer successful registration
    Given a trainer with firstName "Genadiy", lastName "Palkin", specialization "TYPE_1" and bearerToken "myBearerToken" for registration
    When the trainer tries to register
    Then response status should be 201
    And the trainer should be saved in the database

  Scenario: a new trainer failed registration (no bearerToken)
    Given a trainer with firstName "Genadiy", lastName "Palkin", specialization "TYPE_1" and bearerToken "" for registration
    When the trainer tries to register
    Then response status should be 403
    And the trainer shouldn't be saved in the database

  Scenario: a new trainer failed registration (invalid firstName)
    Given a trainer with firstName "Genadiy1", lastName "Palkin", specialization "TYPE_1" and bearerToken "myBearerToken" for registration
    When the trainer tries to register
    Then response status should be 400
    And the trainer shouldn't be saved in the database

  Scenario: a new trainer failed registration (invalid secondName)
    Given a trainer with firstName "Genadiy", lastName "Palkin1", specialization "TYPE_1" and bearerToken "myBearerToken" for registration
    When the trainer tries to register
    Then response status should be 400
    And the trainer shouldn't be saved in the database