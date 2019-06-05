# android-playground
Exploration of the latest Android technologies, patterns and libraries.

**Note** - Purely early experimental stages

This project is my personal playground for trying out Android concepts and technologies.

This is an experiment to use the latest techniques and libraries with;
- Kotlin + coroutines
- MVVM architecture + Repository pattern
- DI with Koin
- Jetpack navigation
- Retrofit with suspending functions
- Latest Android testing 

## Testing
The `test` folder contains all the application unit tests, which test the various non-UI components in isolation.
Here I use the following libraries;
- AndroidX Test
- Android Architecture Test Utils
- JUnit
- Mockito
- AssertJ
- Koin Test Utils
- Coroutine Test Utils
- LiveData Test Utils

The `androidTest` folder contains instrumentation tests. The fragments are tested using the `FragmentScenario` library, allowing
us to test the fragments in isolation from our activity. In addition, there are tests to ensure the navigation works correctly between
the fragments.

Koin is used to override dependencies for test scenarios. The instrumentation tests operate from UI to the API service layer which is mocked.
This means the view models, repository, etc are part of the tests, which means we do proper integration testing via these.

Due to the use coroutines, dealing with asynchronous code in tests is easy. We can effectively make the code run synchronously via;
- Overriding the main dispatcher to use Unconfined
- Injecting dispatchers / coroutine scopes where required and using Unconfined for tests
