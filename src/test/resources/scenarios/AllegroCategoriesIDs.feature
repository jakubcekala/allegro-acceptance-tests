Feature: GET IDs of Allegro categories

 Scenario: Get IDs of Allegro categories - user is authenticated
    Given User is authenticated
    When User call GET IDs of Allegro categories
    Then Receive all following categories with ids:
     | Dom i Ogród        | 5                                     |
     | Dziecko            | 11763                                 |
     | Elektronika        | 42540aec-367a-4e5e-b411-17c09b08e41f  |
     | Firma i usługi     | 4bd97d96-f0ff-46cb-a52c-2992bd972bb1  |
     | Kolekcje i sztuka  | a408e75a-cede-4587-8526-54e9be600d9f  |
     | Kultura i rozrywka | 38d588fd-7e9c-4c42-a4ae-6831775eca45  |
     | Moda               | ea5b98dd-4b6f-4bd0-8c80-22c2629132d0  |
     | Motoryzacja        | 3                                     |
     | Nieruchomości      | 20782                                 |
     | Sport i turystyka  | 3919                                  |
     | Supermarket        | 258832                                |
     | Uroda              | 1429                                  |
     | Zdrowie            | 121882                                |
   #Define here names and ids of Allegro categories

  Scenario: Get IDs of Allegro categories - user is not authenticated
    Given User is not authenticated
    When User call GET IDs of Allegro categories
    Then Receives unauthorized error

  Scenario Outline: Get IDs of Allegro categories - correct parent ID param
    Given User is authenticated
    When User call GET IDs of Allegro categories with <parentID> param
    Then Receive <amount> of categories
    Examples:
      | parentID | amount |
      | 1        | 5      |
      | 2        | 23     |
      | 3        | 12     |
    #Define here parentID and amount od categories in response

  Scenario Outline: Get IDs of Allegro categories - incorrect parent ID param
    Given User is authenticated
    When User call GET IDs of Allegro categories with <parentID> param
    Then Receive error that category with the given ID does not exist
    Examples:
      | parentID |
      | 1111111  |
      | 2222222  |
      | 3333333  |
    #Define here incorrect values of parent IDs for categories