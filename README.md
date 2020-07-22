# Scythe
Ktor service for adding and resetting minutes. 

## Installation and build
```
git clone https://github.com/Slenkis/scythe.git
cd scythe/
chmod +x gradlew
./gradlew shadowJar
```

## Usage
### Run service
`java -jar build/libs/scythe.jar`

url: `localhost:8080`  
username: `admin`  
password: `12345`

### Run service with a custom config
`java -jar build/libs/scythe.jar -config="/path/to/cofig"`

## References
* [Jackson](https://ktor.io/servers/features/content-negotiation/jackson.html)
* [FreeMarker](https://ktor.io/servers/features/templates/freemarker.html)
* [Basic authentication](https://ktor.io/servers/features/authentication/basic.html)
* [Auto Reload](https://ktor.io/servers/autoreload.html)
* [Heroku deploy](https://ktor.io/servers/deploy/hosting/heroku.html) ([Procfile](./Procfile))
* [Fat JAR](https://ktor.io/servers/deploy/packing/fatjar.html) ([ShadowJar](./build/libs))

## Endpoints
`/` - GET: return HTML page for control minutes  
`/temp` - GET: return JSON with total minutes. Example: `{ minutes: 32 }`  
`/temp` - PUT: adds minutes from JSON body to counter; return `200` or `406` or `415` code  
`/temp` - POST: **reset** counter to 0 minutes; return `200` code

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.  
Please make sure to update tests as appropriate.

## License
[MIT](LICENSE)