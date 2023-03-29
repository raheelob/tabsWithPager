# Android Coding Challenge Raheel Masood

#The Brief
A simple app recipe collection with a Listing screen on discover tabs.
Initially the user Tab is empty, once the user favorites a recipe that would be displayed in the favorite tab.

#Architecture and design.
Single Activity Architecture using:
1) MVVM + Clean
2) DI Using HILT.
3) Coroutine with flow and Channels.
4) 2 way dataBinding.
5) Unit test of repository, view model and room DB tests are included.
6) Navigation graph.
7) Error Handling, in case of no internet or error from network. Once the internet resumes, user can hit the request again.
8) Room Database in case there is no internet. (Module is added)

#General Logic for the codeSteps...Views will call a view model.
View model is injected with a use case.
Use case makes a call to a repository.
API for collection is hit and then we compare the remote list with the favorite list, If an id is present in the favorite list,
we then update the model for the remote as already favorite.
In case there are no favorites, we won't do any changes to the remote data.
Repository returns the data to the view model.
View model returns the response to the view i.e fragment.
Local DB is updated every time a new record is fetched based on the comparison with the favorite list.
A shared view model is used between MainActivity, Collection Fragment and Favorite Fragment.

#Test:
#Unit Tests are included in the test folder:
FetchCollectionListTest: This will use mock responses.
MainViewModelTest: Checking all the Channel's events using mocks.

#AndroidTest:
EntityRoomDBTest: Simple CRUD operations are tested in this folder.
To run these tests a device is required, since no Activity is attached, It will run quickly.

### Scenarios ###
#Collections List
Given the recipe collection card
When the user taps the favorite button
Then collection should be added to the favorite list
And the favorite button icon should indicate that the collection is a favorite

Given a recipe collection card for a favorite collection
When the user taps the favorite button
Then collection should be removed from the favorite list
And the favorite button icon should indicate that this collection is not a favorite

#Favorites List
Given the user views their favorites
Then a list of favorite collections should be presented

Given the recipe collection card
When the user taps the favorite button in the favorite list
Then the recipe collection should be removed from the favorite list

## Instructions
- Provide a write-up in the description touching on:
- What you changed and why ?
- Use cases were added to call repo as it adds a layer on top of repo.
- repo becomes the interface, and then repo implementation classes are added. In future if there is a change, then it will
- not be tightly coupled.
- 2 way data binding for the views. Clicks are handled from the view, and values are set through the binding adapter.
- Channels are used instead of MutableStateFlows as it takes a default value through the constructor and emits it immediately when someone starts collecting, so I felt that I should use channels with their events.

- What things gave you problems:
- Implementing local DB was a bit challenging for my favorite, as there was no API for favorite tab unlike Collections in discover tab,
- so handling was required for it.

- Other thoughts, where you would like to take this given more time, etc.
- After reviewing my code, I found issue listed below, and it's fix.

Issue - 2 Different tables were maintained for collection and favorites. This means there was no relation between favorites and collection tables, and more db queries.

Fix:
1-Remote as CollectionDTO
2-Local as CollectionEntity.

A mapping between 1 and 2 is drawn. A boolean is maintained for favorite.
To get all favorite, a simple query of where to match where favorite is true will return you the favorite list.
Local list will already have the favorite item, so we can check it every time we fetch a list from the server.