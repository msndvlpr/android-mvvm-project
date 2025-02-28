# Android MVVM Project

### API Details
* API endpoint URL: https://gateway.kleinanzeigen.de/coding-challenge/api/v1/ads/{ad_id}
* Ad ID to request: 1118635128
* Authorization: Basic Authentication
* Credentials:
  * Username: candidate
  * Password: LnpwL7HoZjTL

### Additional Info
* To generate the real image url replace the {imageId} placeholder with the string 1 or 57 accordingly for preview or full size.

Example:
`https://gateway.kleinanzeigen.de/coding-challenge/img/LukAAOSwZEFhSgCC_{imageId}.jpeg`
should be converted to
`https://gateway.kleinanzeigen.de/coding-challenge/img/LukAAOSwZEFhSgCC_1.jpeg` for preview
and
`https://gateway.kleinanzeigen.de/coding-challenge/img/LukAAOSwZEFhSgCC_57.jpeg` for full size image.

### Sample Json Output

**Run this curl command to get the sample data:**

```
curl --basic -u candidate:LnpwL7HoZjTL --user-agent "some-user-agent" https://gateway.kleinanzeigen.de/coding-challenge/api/v1/ads/1118635128
```

### Architecture and Design Pattern
The MVVM design pattern has been used for the implementations and I have tried to stick to the most of Google's best practices and also Separation Of Concerns- 
principle to have a cleaner, maintainable, scalable and testable code. 
* MODEL (data package): this layer represents the MODEL layer of MVVM in which the repositories and data source are implemented, I have created only one repository 
  and one data-source regarding the project requirements.
* Dependency Injection (di package): Dagger Hilt has been used for DI which is the recommended library for this purpose by Google, and accordingly one module named `AdsModule`
  has been implemented for providing the injections for `NetworkDataSource` and `RealEstateRepository`
* View Model (ui package): it is as an intermediary layer between the MODEL layer and the VIEW and is responsible for business logics related to UI and data presentation, so I 
  tried to decouple all logics from UI layer and moved all non-UI methods to the VIEW MODEL. I also did it for the navigation stuff to keep the view as clean as possible.
* VIEW (ui package): this layer should be kept responsible only for UI related stuffs and not any business logic. I have used StateFlow and Immutable Pattern to make the data 
  available and accessible from VIEW MODEL to this layer, so private objects are filled with data in VIEW MODEL and then VIEW just observes for every data change to populate it.
  VIEW MODEL is injected to the fragment to make the methods and objects accessible in the fragment.
* State Handling: I used a separate ViewState sealed class for the state handling purpose, so initially the state is Loading and a Shimmer will be displayed until the date is 
  fetched completely and state is changed to Failure or Success, then depending on the case the corresponding data or error screen will be displayed. A retry action is also 
  added in the UI to re-fetch the data from repository in case of having connectivity issue.
* Repository Layer (repository package): this layer will get the data from any required local or network data-sources and will emit data in a flow to be accessible in View Models, so regarding the
  Immutable Access Patter I have created two state flows, one is mutable and private for setting the data only inside the repository and another state flow is public and mutable
  for collecting it in the View Models. As I already explained the same pattern/approach is also implemented for the data flow from View Model to the View. 
* Data Source Layer (datasource package): there is only one data-source implemented in this project for network call and fetching data from backend, Ktor is used as a powerful library for this purpose.
* Other Libraries: other utilised libraries for testing, mocking, navigation, serialization etc. are also listed in the module level build.gradle file and the references added to the 
  Gradle Version Catalog's .toml file.


### Nice To Have Features/Architectural Improvements
In case there will be a project feature extension or improvement phase, following topics can be considered in my opinion:
* Jetpack Compose: adhering to the latest Google's UI development framework which actually I needed a more time to do my development with
* Domain Layer: in case of having a more complex logic it is recommended to include this extra layer for our different use-cases, but I ignored it now 
  as recommended by Google to pass up for small projects with not complex business logic
* More Unit/UI Tests: because of the time constraints not enough tests have been implemented which are very important and assumed as a Tech Debt now, 
  ao they must be considered in the project improvement
* Multi Language: as the content of backend data and also the design was only in German, it can be an extra feature to include other languages like English etc.
* Dark Theme: as there was no design for the dark theme it was not implemented, but as most of the apps now a days support this feature it would have been nice
  to have it.
* Main Page: from the UX perspective and also for a better screen flow, it would have been better to first show the list of all ads to the user in the first- 
  fragment, and then user could click on any item to see the details afterwards
* App Bar and Bottom Nav Bar: again for a better user experience and giving a better accessibility to the user, it would be have been nice to have these 
  two Material Design elements
* Picture Pinch: to have the possibility to zoom in/out the picture for a better user experience to get more knowledge about the advertisement