Feature: Add training

    Scenario: Add training
        Given a training data with traineeUsername "Q.Q", trainerUsername "R.R", duration 20, date "2025-10-10" and type "TYPE_1"
        And   a user, who's already logined and got his jwt
        When the user tries to add training
        Then response status should be 201
        And the training should be saved in the database
        And a message should be added to the queue