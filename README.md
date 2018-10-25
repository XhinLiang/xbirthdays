## xbirthdays

Manage you friends birthday -- [xbirthdays](https://xbsd.xhinliang.com)

### Angular 5+ Frontend with SpringBoot 2.0 (Java) Backend
Application to demonstrate various parts of a service oriented RESTful application. 

### Technology Stack
Component         | Technology
---               | ---
Frontend          | [Angular 5](https://github.com/angular/angular)
Backend (REST)    | [SpringBoot 2](https://projects.spring.io/spring-boot) (Java)
Security          | Token Based (Spring Security and [JWT](https://github.com/auth0/java-jwt) )
REST Documentation| [Swagger UI / SpringFox](https://github.com/springfox/springfox)
REST Spec         | [Open API Standard](https://www.openapis.org/) 
In Memory DB      | H2 
Production        | MySQL
Persistence       | Spring Data JPA
Client Build Tools| [angular-cli](https://github.com/angular/angular-cli), Webpack, npm
Server Build Tools| Maven

## Folder Structure
```bash
PROJECT_FOLDER
│  pom.xml           
└──[src]      
│  └──[main]      
│     └──[java]      
│     └──[resources]
│        │  application.properties # contains springboot cofigurations
│        │  application-dev.properties # contains dev profile springboot cofigurations
│        │  application-prod.properties # contains prod profile springboot cofigurations
│        └──[public]    # keep all html,css etc, resources that needs to be exposed to user without security
│
└──[target]              # Java build files, auto-created after running java build: mvn install
│  └──[classes]
│     └──[public]
│     └──[webui]         # webui folder is created by (maven/gradle) which copies webui/dist folder 
│                        # the application.properties file list webui as a resource folder that means files can be accesses http://localhost/<files_inside_webui> 
│
└──[webui]
   │  package.json     
   │  angular-cli.json   # ng build configurations)
   └──[node_modules]
   └──[src]              # frontend source files
   └──[dist]             # frontend build files, auto-created after running angular build: ng -build
```

## Prerequisites
Ensure you have this installed before proceeding further
- Java 8
- Maven 3.3.9+
- Node 6.0 or above,  
- npm 5 or above,   
- Angular-cli 1.6.3

## About
This is an RESTful implementation of an order processing app based on Northward database schema from Microsoft.
The goal of the project is to 
- Highlight techniques of making and securing a REST full app using [SpringBoot](https://projects.spring.io/spring-boot)
- How to consume an RESTful service and make an HTML5 based Single Page App using [Angular 4+](https://github.com/angular/angular)

### Features of the Project
* Backend
  * Token Based Security (using Spring security)
  * API documentation and Live Try-out links with Swagger 
  * In Memory DB with H2 
  * Using JPA and JDBC template to talk to relational database
  * How to request and respond for paginated data 

* Frontend
  * Organizing Components, Services, Directives, Pages etc in an Angular App
  * How to chain RxJS Observables (by making sequential AJAX request- its different that how you do with promises)
  * Techniques to Lazy load Data (Infinite Scroll)
  * Techniques to load large data set in a data-table but still keeping DOM footprint less
  * Routing and guarding pages that needs authentication
  * Basic visualization

* Build
  * How to build all in one app that includes (database, sample data, RESTful API, Auto generated API Docs, frontend and security)
  * Portable app, Ideal for dockers, cloud hosting.

### Build Frontend (optional step)
Code for frontend is already compiled and saved under the ```webui/dist``` 
when building the backend app (using maven) it will pickup the code from ```webui/dist```. However if you modified the frontend code and want your changes to get reflected then you must build the frontend 
```bash
# Navigate to PROJECT_FOLDER/webui (should contain package.json )
npm install
# build the project (this will put the files under dist folder)
ng build --prod --aot=true
```

### Build Backend (SpringBoot Java)
```bash
# Maven Build : Navigate to the root folder where pom.xml is present 
mvn clean install

#OR

# Gradle Build : Navigate to the root folder where build.gradle is present 
gradle build
```

### Start the API and WebUI server
```bash
# Start the server (9119)
# port and other configurations for API servere is in [./src/main/resources/application.properties](/src/main/resources/application.properties) file

# If you build with maven jar location will be 
java -jar ./target/app-1.0.0.jar

# If you build with gradle jar location will be 
java -jar ./build/libs/app-1.0.0.jar
```

### Accessing Application
Component         | URL                                      | Credentials
---               | ---                                      | ---
Frontend          |  http://localhost:9119                   | `demo/demo`
H2 Database       |  http://localhost:9119/h2-console        |  Driver:`org.h2.Driver` <br/> JDBC URL:`jdbc:h2:mem:demo` <br/> User Name:`sa`
Swagger (API Ref) |  http://localhost:9119/swagger-ui.html   | 
Swagger Spec      |  http://localhost:9119/api-docs          |

## LICENSE
```
   Copyright 2018 XhinLiang

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
