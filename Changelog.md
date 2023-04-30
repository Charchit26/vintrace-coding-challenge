## Bug Fixes

- Fix all tests in `BreakdownControllerTest`
  - The map used in the controller does not group the items based on the category as required. 
  - So, we need to first group the ComponentDTO items based on the categories. 
  - Then we use the map obtained to stream on the entry set and map each group into the breakdown response object using
  the key and value of the map. This returns us the list of the breakdown response as required.


## Code refactors

- Created and moved DTOs and response entity to their own files, to make things more reusable, readable and concise
- Created `BreakdownService` to separate out business logic from the controller level validation and sending back the response
- Use `private` methods for service and DTOs (where required) to add more security and encapsulation
- Break down the service methods according to individual responsibilities to avoid redundant code
- Break down the controller class loading logic to a private function for re-use and reduce duplicate code. Also added a
test case to verify its working as expected
- Use `switch` instead of multiple `if-else` in `BreakdownController`
- Removed the check for `year/variety etc` being present in a list before throwing `IllegalArgumentException` as it's not
required, provided that a request is made to a specific controller endpoint. Addition of a component attribute won't 
have any effect on this piece of code.
- Using more verbose variable naming like `response` instead of `r` and `wineDto` instead of `w`
