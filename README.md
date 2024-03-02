[![Prehistoric Planning Poker](poker-logo.png 'Prehistoric Planning Poker logo')](https://poker.developmentanddinosaurs.co.uk/)

# Prehistoric Planning Poker

A dinosaur themed planning poker application for agile refinement ceremonies.

Head to [https://poker.developmentanddinosaurs.co.uk/](https://poker.developmentanddinosaurs.co.uk/) to see it in action, or build it yourself! 

## Self Hosting

Prehistoric Planning Poker is simple to self-host. The entire application is distilled into a single executable jar file that can be run anywhere with a JVM. 

Simply build the application from source using the included Gradle wrapper, and then run the resulting jar file. 

**Build from source**
```shell
$ ./gradlew jar
```

**Run jar**
```shell
$ java -jar build/libs/planning-poker-jvm-<version>.jar
```

The application will spin up on ports 80 and 443 on localhost, so you'll be able to access the application after running the jar by navigating to https://localhost. 

## Hosting on AWS

You can also host the application on AWS mostly out of the box. 

All infrastructure required is defined in the [infra](/infra) directory. Most resources are deployed using the CDK, though some are references to infrastructure created manually. 

All infrastructure fits within AWS free-tier limits, so you can try it out for free. 

## Hosting Elsewhere

You can host the application anywhere with a JVM, so shared hosting is probably out of the picture, but you should be able to find something suitable in Google Cloud, Microsoft Azure, Oracle Cloud, or a VPS. 

There are no explicit instructions for this - but it will be very similar to self-hosting. Just on someone else's server. 

## Contributing 

Contributions are welcome but not expected. The ethos of this project is that contributions should be a combination of the following:

- Fun
- Useful

All contributions should be written in Kotlin where possible - especially where doing something else would be smarter. 
