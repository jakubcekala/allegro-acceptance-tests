Feature: GET parameters supported by category

  Scenario Outline: GET parameters supported by category - user is authenticated
    Given User is authenticated
    When User calls GET parameters supported by a category with ID value <idValue>
    Then Receives <parametersAmount> parameters
    And There are following categories: <categories>
    Examples:
      | idValue | parametersAmount | categories                                          |
      | 1       | 3                | Stan, Waga produktu z opakowaniem jednostkowym, EAN |
      | 2       | 3                | Stan, Waga produktu z opakowaniem jednostkowym, EAN |
      | 3       | 3                | Stan, Waga produktu z opakowaniem jednostkowym, EAN |
      | 4       | 3                | Stan, Waga produktu z opakowaniem jednostkowym, EAN |
    #Define here id of categories and amount of parameters

  Scenario Outline: GET parameters supported by category - user is not authenticated
    Given User is not authenticated
    When User calls GET parameters supported by a category with ID value <idValue>
    Then Receives unauthorized error
    Examples:
      | idValue |
      | 1       |

  Scenario Outline: GET parameters supported by category - incorrect ID
    Given User is authenticated
    When User calls GET parameters supported by a category with ID value <idValue>
    Then Receives error that category with the given ID does not exist
    Examples:
      | idValue |
      | 1111111 |
      | 2222222 |
      | 3333333 |