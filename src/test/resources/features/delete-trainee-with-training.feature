@Endpoint1
@queueTest
Feature: Delete trainee with training

    Scenario: Delete trainee with a scheduled training (training date is after now )
        Given a trainee with username "Ivan.Petrov"
        And  a trainer with username "Genadiy.Palkin"
        And  trainee's training with the trainer, that is scheduled for next year
        And  a user, who's already logined and got his jwt
        When the user tries to delete trainee by username
        Then response status should be 200
        And the trainee and the training should be deleted from the database
        And a message should be added to the queue with action status "DELETE"

    Scenario: Delete trainee with a already passed training (training date is before now )
        Given a trainee with username "Ivan.Petrov"
        And  a trainer with username "Genadiy.Palkin"
        And  trainee's training with the trainer, that is passed last year
        And a user, who's already logined and got his jwt
        When the user tries to delete trainee by username
        Then response status should be 200
        And the trainee and the training should be deleted from the database
        And a message shouldn't be added to the queue