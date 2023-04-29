## Bug Fixes

- Fix all tests in `BreakdownControllerTest`
  - The map used in the controller does not group the items based on the category as required. 
  - So, we need to first group the ComponentDTO items based on the categories. 
  - Then we use the map obtained to stream on the entry set and map each group into the breakdown response object using
  the key and value of the map. This returns us the list of the breakdown response as required.


## Code refactors

- Created and moved DTOs and response entity to their own files, to make things more readable and concise
- Use `switch` instead of multiple `if-else` in `BreakdownController`