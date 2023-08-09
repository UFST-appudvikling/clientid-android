# ClientId Android 1.0.0

Small client id library. The library generates a set of headers that can be accessed either as a simple map
or with the provided OkHttp interceptor.

The library utilizes autostart to initialize and generates a ClientId on first run. The ClientId is stored in sharef preferences.

This describes release 1.0.0, for earlier versions of the documentation, consult the relevant release branches.

See https://confluence.ccta.dk/display/UOA/API+Request+headers for more information.

## Gradle dependency
```
implementation("com.github.UFST-appudvikling:clientid-android:$VERSION")
```
Where $VERSION is a release, tag or branch snap. Check [jitpack.io](https://jitpack.io)
for all available options.

# Usage
If you use OkHttp3, you can use the provided interceptor to add the headers to your requests.

```kotlin
val client = OkHttpClient.Builder()
    .addInterceptor(ClientIdInterceptor())
    .build()
```

If don't use OkHttp3, you can use the ClientId object to get the headers as a map.

```kotlin
val headers = ClientId.getHeaders()
// manually set the headers in your HTTP requests
```
