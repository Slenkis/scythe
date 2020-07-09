### What is it?
Minute counter (increment and reset)

### How to run:
#### Change [config](resources/application.conf) to your before run!

**Gradle:**  
`./gradlew run`

**ShadowJar:**  
`java -jar ./build/libs/scythe.jar`

### Project Info:
* Written on Kotlin, FreeMarker, JS (for HTML)
* Work on default 8080 port (change in [config](resources/application.conf))
* SQL Database **required** for counter (change DB settings in [config](resources/application.conf))
* Auto Reload (watch)
* Ready for Heroku deploy ([Procfile](./Procfile) and [ShadowJar](./build/libs))

### Endpoints:
`/` - GET: return HTML page for control minutes  
`/temp` - GET: return JSON with total minutes: `{ minutes: 32 }`  
`/temp` - PUT: with JSON body: `{ minutes: 45 }` **adds** 45 minutes to counter; return 200 or 406 or 415 code  
`/temp` - POST: **reset** counter to 0 minutes; return 200 code


#### If you know how to add authorization for this project, please send the `Pull Request`