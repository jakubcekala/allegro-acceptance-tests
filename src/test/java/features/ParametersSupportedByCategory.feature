Feature: GET parameters supported by category
  Scenario Outline: GET parameters supported by category
    Given User is authenticated
    When User call GET parameters supported by a category with ID value <idValue>
    Then Receives <parametersAmount> parameters
    And There are following categories: <categories>
    Examples:
      | idValue | parametersAmount | categories |