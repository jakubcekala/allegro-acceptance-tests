Feature: GET a category by ID
  Scenario Outline: GET a category by ID
    Given User is authenticated
    When User call GET a category by ID with <idValue>
    Then User Receives category <categoryName>
    Examples:
      | idValue | categoryName |