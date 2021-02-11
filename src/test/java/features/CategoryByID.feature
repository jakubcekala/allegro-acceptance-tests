Feature: GET a category by ID

  Scenario Outline: GET a category by ID - user is authenticated
    Given User is authenticated
    When User call GET a category by ID with <idValue>
    Then User Receives category <categoryName>
    Examples:
      | idValue | categoryName |
      | 1       | Muzyka       |
      | 2       | Komputery    |
    #Define here IDs of categories and names

  Scenario Outline: GET a category by ID - user is not authenticated
    Given User is not authenticated
    When User call GET a category by ID with <idValue>
    Then Receives unauthorized error
    Examples:
      | idValue |
      | 1       |
      | 2       |

  Scenario Outline: Get a category by ID - incorrect id
    Given User is authenticated
    When User call GET a category by ID with <idValue>
    Then Receive error that category with the given ID does not exist
    Examples:
      | idValue |
      | 1111111 |
      | 2222222 |
      | 3333333 |