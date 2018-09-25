# spring-security-user-registration

## Project development notes ##
The README.md of the project will be in a form of __log of notes__ (see [this chapter](#development-log)), made to remember some important features and to avoid and fix errors met on the project progress.
The log will progress from commit to commit.

## How to run the Application ##
The simplest way is to run the command in the project's home:
```text
$ mvn spring-boot:run
```
Otherwise, you can first build a __jar__ file by
```text
$ mvn clean package
```
command, which builds target/spring-security-registration-1.0-SNAPSHOT.jar, and then you can run this jar:
```text
java -jar target/spring-security-registration-1.0-SNAPSHOT.jar

```
Both the ways make Spring to start and to run the application:
```text
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.4.RELEASE)

```
After the server starts, you can enter in the browser's address bar __http://localhost:8761/login__ and the applicaion should show its first __login page__. It will start on __server.port=8761__.

## Application's landscape ##
The application is developed on __Java__, it's web pages are developed on __HTML__ with tiny inclusions of __CSS__ and __Javascript__ fragments. 
For the HTML pages CDN __Bootstrap__ v.4.1.3 stylesheets and __Thymeleaf-4__ templates are used. 

### Environement ###
An environment, used for the development, includes:
* Ubuntu 18.04.1 LTS
* java version "1.8.0_181"
* Apache Maven 3.5.2
* mysql  Ver 8.0.12 for Linux on x86_64 (MySQL Community Server - GPL)
* Google Chrome Version 68.0.3440.106 (Official Build) (64-bit)
* FireFox Quantrum 62.0 (64-bit)

### Spring Boot Maven Project ### 
Maven pom.xml refers to Spring Boot parent project version 2.0.4.RELEAS:
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.4.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```
And then it uses following org.springframework.boot dependencies: __spring-boot-starter-web__, __spring-boot-starter-data-jpa__, __spring-boot-starter-security__. 

This project uses [Thymeleaf](https://www.thymeleaf.org/) as a HTML pages template engine and includes __spring-boot-starter-thymeleaf__ in the dependencies.

Also the project dependencies include mysql:mysql-connector-java:5.1.46 dependency.


## The Database configuration and creation ##
To run the code you should have MySQL server installed. In order to install it please refer, for example, to [How To Install MySQL on Ubuntu 14.04](https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-14-04) and [B.5.3.2 How to Reset the Root Password](https://dev.mysql.com/doc/refman/5.7/en/resetting-permissions.html). After the server is installed you need to create a user to connect to the database:
```sql
mysql -u root -p 
> CREATE USER 'user1'@'localhost' IDENTIFIED BY 'user123';
> GRANT ALL PRIVILEGES ON *.* TO 'user1'@'localhost';
> FLUSH PRIVILEGES;
``` 
__/resources/persistence.properties__ file contains properties which allow to create and to connect to the database with name of __'lizard_users_db'__: 
```
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/lizard_users_db?createDatabaseIfNotExist=true
jdbc.user=user1
jdbc.pass=user123
init-db=false
################### Hibernate Configuration ##########################
hibernate.dialect=com.lizard.buzzard.persistence.CustomMySQLDialect
hibernate.show_sql=false
hibernate.hbm2ddl.auto=update

hibernate.id.new_generator_mappings = false
```
The database __'lizard_users_db'__ created (spring.jpa.hibernate.ddl-auto=update property) if it not yet exists and __'user1'__ has access to the database (see [81. Database Initialization](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html) and [Spring boot ddl auto generator](https://stackoverflow.com/questions/21113154/spring-boot-ddl-auto-generator)). 
The property spring.jpa.properties.hibernate.dialect refers to CustomMySQLDialect class.
```java
public class CustomMySQLDialect extends MySQL57Dialect {
    @Override
    public boolean dropConstraints() {
        return false;
    }
}
``` 
It's important to have this class corresponds to the dialect of the database you work with (see [SQL Dialects in Hibernate](https://www.javatpoint.com/dialects-in-hibernate)). As on the computer where the development was done 

# Development Log #

## --Commit-1-- ##

### Thymleaf templates ###
__Thymleaf__ will be used as html pages template engine and for internationalization support. To use __thymeleaf__ you need
* to include dependency in pom.xml
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```
* to define __th:__ namespace on *.html pages as follows:
```
<!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
```
An example see in templates/login.html

### JpaRepository ###
There is a base repository (BaseRepository) which extends JpaRepository.
 * Spring Data JPA contains some built-in Repository implemented some common functions to work with database: findOne, findAll, save, etc.
 * should be annotated win @NoRepositoryBean, otherwise gives an BeanCreationException (Error creating bean with name 'baseRepository')
 
### MVC Configuration ###
If you want to take complete control of Spring MVC, you can add your own @Configuration annotated with @EnableWebMvc should be set ([27. Developing Web Applications](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html)).
```
public class MvcConfig implements WebMvcConfigurer
``` 
Do not use the src/main/webapp directory if your application is packaged as a jar.
Although this directory is a common standard, it works only with war packaging, and it is silently ignored by most build tools if you generate a jar ([Chapter: 27.1.5 Static Content](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html)).
```
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry)
```
Next bean helped me to show messages in different languages depending on language choosed: 
```
@Bean
public MessageSource messageSource()
```
It's a strategy interface for resolving messages, with support for the parameterization and internationalization of such messages:
* [Internationalization in Spring](https://www.logicbig.com/tutorials/spring-framework/spring-core/message-sources.html)
* [Interface MessageSource](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/MessageSource.html)

### PersistenceJPAConfig ###
The  source for @Bean's definition used in Persistence JPA Repository
* Path to BaseRepository, UserRepository, RoleRepository
```
@EnableJpaRepositories(basePackages = "com.lizard.buzzard.persistence.dao")
```
* Works with persistence.properties from ./resource/ folder
```
@PropertySource({ "classpath:persistence.properties" })
```
* Model's (database) entities (Role, User etc.). Used in JpaTransactionManager
```
@Bean
public LocalContainerEntityManagerFactoryBean entityManagerFactory()
```
* Jdbc datasource object, used for LocalContainerEntityManagerFactoryBean
```
@Bean
public  DataSource dataSource()
```
* Transaction Manager for Jpa model entities, use LocalContainerEntityManagerFactoryBean
```
@Bean
public JpaTransactionManager transactionManager()
```
* Get properties from persistence.properties, used in @Bean public LocalContainerEntityManagerFactoryBean entityManagerFactory()
```
protected Properties additionalProperties()
```
### MySQLDialect ###
Should be set for correct working with MySql version currently installed. Otherwise gives an error.
It works together with __persistence.properties__'s __hibernate.dialect__=CustomMySQLDialect
```
public class CustomMySQLDialect extends MySQL57Dialect
```

## --Commit-2-- ##
Commit-2 just fixes some inaccuracies in description of Commit-1.

## --Commit-3-- ##
Internationalization. Pay attention at the link between 'lang_choosed' in html form and in LocaleChangeInterceptor, it's the same.

## --Commit-4-- ##
Commit-4: an initial _fake_ __registration.html__ addad; Apps moved to highest package to be reached from any test class; attention to two ways of tests: integration and local

## --Commit-5-- ##
The registration form shows three fields, internationalized messages added in resource files. Remember @Controller vs. @RestController difference
* __@Controller__ vs. __@RestController__ difference

    NOTE: [Spring rest controller not returning html](https://stackoverflow.com/questions/43263667/spring-rest-controller-not-returning-html/43371228).

    __RestController__ annotation returns the json from the method, not HTML or JSP. It is the combination of __@Controller__ and __@ResponseBody__ in one. The main purpose of __@RestController__ is to create __RESTful__ web services. For returning html or jsp, simply annotated the controller class with __@Controller__.

## --Commit-6-- ##
A preparation for registration of a new user: 
- ApplicationListener<ContextRefreshedEvent> to initialize DB entities; 
- utility methods in BaseRepository for finding objects; 
- new RolesPrivilege @Entity joined with Role @Entity; 
- User @Entity extended by email field; 
- first not working draft of UserService; 
- separation of @Entity and DTO for User, ViewFormUser introduced for server-side fields check

### ApplicationListener<ContextRefreshedEvent> ###
 * ContextRefreshedEvent. This event is published when the ApplicationContext is either initialized or refreshed ([Event Handling in Spring](https://www.tutorialspoint.com/spring/event_handling_in_spring.htm)).
 * We use this to create and populate DB tables

### DTO User class ViewFormUser ###
Entity is class mapped to table. Dto is class mapped to "view" layer mostly. What needed to store is entity & which needed to 'show' on web page is DTO ([JAVA: Difference between Entity and DTO](https://stackoverflow.com/questions/39397147/java-difference-between-entity-and-dto)).
 
## --Commit-7-- ##
New fields and buttons on registration form were added

## --Commit-8-- ##
The commit works with the simple Hibernate Validator-JSR 380 and Thymeleaf template engine for internationalizing HTML forms' fields names and error messaging. __Neither BindingResult nor plain target object for bean name ‘mybean’ available as request attribute.__ error resolved.

### Thymeleaf Java library ###
__Thymeleaf__ is a Java library. It is an __XML/XHTML/HTML5 template engine__ able to apply a set of transformations to template files in order to display data and/or text produced by your applications.
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```
### JSR 380 specification, the validation-api dependency ###
JSR 380 specification, the validation-api dependency
* [java bean validation 2.0 vs hibernate validator](https://stackoverflow.com/questions/49606316/java-bean-validation-2-0-vs-hibernate-validator)
* For html forms validation we use Thymeleaf views - see: <artifactId>spring-boot-starter-thymeleaf</artifactId>

Hibernate Validator requires an implementation of the Unified Expression Language (JSR 341) for evaluating dynamic expressions in constraint violation messages (see Section 4.1, “Default message interpolation”).
When your application runs in a Java EE container such as JBoss AS, an EL implementation is already provided by the container. In a Java SE environment, however, you have to add an implementation as dependency to your POM file.
 ([Hibernate Validator 6.0.13.Final - JSR 380 Reference Implementation: Reference Guide](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/))
 
__ViewFormUser__ uses Hibernate Validator annotations.

### Exception: 'Neither BindingResult nor plain target object for bean name ‘mybean’ available as request attribute' ###
If you are trying to bind a __Spring MVC form__ to a __bean__, then you might have come across the below exception.
To solve this, you have to add a __@ModelAttribute annotation__ before your bean argument in the controller action with the __POST__ request method.

Here is __viewFormUser__ object of the form:
```html
<form action="#" th:action="@{/registration}" th:object="${viewFormUser}" method="post">
```
In the controller we first have to initialize it by adding an __attribute__ with the same name (__viewFormUser__) to the __Model__ by 'empty' bean's (DTO) object. 
```
@RequestMapping(value = "/registration", method = RequestMethod.GET)
public String showRegistrationForm(ViewFormUser viewFormUser, Model model) {
    model.addAttribute("viewFormUser", new ViewFormUser());
    return "registration";
}
```
Then in the __RequestMethod.POST__ method we have to to add a __@ModelAttribute("viewFormUser")__ annotation before bean argument:
```
@RequestMapping(value = "/registration", method = RequestMethod.POST)
public String checkPersonInfo(@Valid @ModelAttribute("viewFormUser") ViewFormUser viewFormUser, BindingResult bindingResult) {...}
```
See also:
 * [Neither BindingResult nor plain target object available as request attribute](https://codingexplained.com/coding/java/spring-framework/neither-bindingresult-nor-plain-target-object-available-as-request-attribute).
 * [Neither BindingResult nor plain target object for bean name available as request attribute [duplicate]](https://stackoverflow.com/questions/8781558/neither-bindingresult-nor-plain-target-object-for-bean-name-available-as-request)
 * [Spring MVC - Form Validation with JSR-349 Bean Validation](https://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/spring-mvc-form-validation.html)

### @Valid annotation before the parameter in controller's method ###
__@Valid__ before __ViewFormUser viewFormUser__ means that __JSR-303__ bean validation with the Hibernate Validator implementation is used. If you do not, then you can simply remove the annotations from the bean class as well as the @Valid annotation in the arguments of the controller action.
In order to provide validation in the __@Controller__ apply __@Valid__ annotation. This is __JSR-349__ specific annotation.
The annotation __@Valid__ used on instance or parameter marks it to be validated by the framework. For __@Valid__ annotation see 
[Hibernate Validator 6.0.12.Final - JSR 380 Reference Implementation: Reference Guide](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#preface)

### No mapping found for HTTP request with URI in DispatcherServlet with name 'dispatcherServlet' ###
If __o.s.web.servlet.PageNotFound : No mapping found for HTTP request with URI in DispatcherServlet with name 'dispatcherServlet'__ message appears in the Spring log then

* [Why does Spring MVC respond with a 404 and report “No mapping found for HTTP request with URI […] in DispatcherServlet”?](https://stackoverflow.com/questions/41577234/why-does-spring-mvc-respond-with-a-404-and-report-no-mapping-found-for-http-req/42785538)
* [Annotation Configuration Replacement for mvc:resources - Spring](https://stackoverflow.com/questions/14861720/annotation-configuration-replacement-for-mvcresources-spring)

## --Commit-9-- ##
The login form got new fields with Hibernate Validator-JSR 380 and test Thymeleaf messaging.

## --Commit-10-- ##
Spring MVC Custom Validation of User's DTO Email field: on basis of ConstraintValidator interface implementation, Hibernate Validator-JSR 380, Thymeleaf for internationalized error messaging.

Added dependency for e-mail sending (see [Guide to Spring Email](https://www.baeldung.com/spring-email) how to send emails from a Spring Boot application):
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```
### Spring MVC Custom Validation ###
An examlpe of custom validation annotation is the __EmailConstraintValidator__ interface: 
```
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailConstraintValidatorImpl.class)
public @interface EmailConstraintValidator {
    String message() default "{constraint.validator.user.registration.email}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```
Normally error message keys are being searched in the file called __ValidationMessages.properties__ that should be available on the application class path (see [Custom validation annotation in Spring](http://dolszewski.com/spring/custom-validation-annotation-in-spring/)), but in our case it was sufficient to define message key in Thymeleaf's __messages_xx.properties__.
Also see [Spring MVC Custom Validation](https://www.baeldung.com/spring-mvc-custom-validator).

## --Commit-11-- ##
The commit supports Spring MVC Custom Validation of User's DTO __Email__ field. __ConstraintValidator__ interface was implemented by __EmailConstraintValidatorImpl__ class, it uses __Hibernate Validator-JSR 380__, __Thymeleaf__ for internationalized __email__ field error message on registration form.

## --Commit-12-- ##
The commit supports Spring MVC Custom Validation of User's DTO __password__ field. __ConstraintValidator__ interface was implementated, Hibernate Validator-JSR 380, Thymeleaf are used for internationalized error messages.
On how to implement the password validation see [Registration – Password Strength and Rules](https://www.baeldung.com/registration-password-strength-and-rules).

Include this dependency in your pom.xml:
```
<dependency>
    <groupId>org.passay</groupId>
    <artifactId>passay</artifactId>
    <version>${passay.version}</version>
</dependency>
```

For implementation of __public void initialize(PasswordConstraintValidator constraintAnnotation)__ method of __PasswordConstraintValidatorImpl__ I recommend to refer to [Custom Password Constraint Validator Annotation Example](https://memorynotfound.com/custom-password-constraint-validator-annotation/).
And for implementation of __public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext)__ method refer to [passay/src/test/java/org/passay/CharacterCharacteristicsRuleTest.java](https://github.com/vt-middleware/passay/blob/master/src/test/java/org/passay/CharacterCharacteristicsRuleTest.java).

## --Commit-13-- ##
The commit supports Spring MVC Custom Validation of User's DTO 'password' field: passay.org library's custom Message Resolver for different languages, depending on __'lang_choosed'__ login.html HttpRequest parameter.

In order to understand how it works, please refers to [Guide to Internationalization in Spring Boot](https://www.baeldung.com/spring-boot-internationalization)

Pay attention to next pair of methods of the __MvcConfig__ class which implements __WebMvcConfigurer__ interface. The __addInterceptors()__ method adds the interceptor which will switch to a new locale based on the value of the __lang_choosed__ parameter appended to a request:
```
@Override
public void addInterceptors(final InterceptorRegistry registry) {
    final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("lang_choosed");
    registry.addInterceptor(localeChangeInterceptor);
}
```
In order for our application to be able to determine which locale is currently being used, we need to add a __LocaleResolver__ bean:
```
@Bean
public LocaleResolver localeResolver() {
    final CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
    return cookieLocaleResolver;
}	
```
The __@Autowired LocaleResolver__ 
```
@Autowired
LocaleResolver localeResolver;
```
is used in __RegisterController__ class to print the Locale selected on login.html
```
@RequestMapping(value = {"/login", "/"}, method = RequestMethod.GET)
public String getLoginPage(ViewFormLogin viewFormLogin, Model model, HttpServletRequest httpServletRequest) {
    LOGGER.debug("Locale selected on login.html ==>  " + localeResolver.resolveLocale(httpServletRequest).toString());
```
and it is used in __PasswordConstraintValidatorImpl__ class which implements __ConstraintValidator<PasswordConstraintValidator, String>__, in __MessageResolver getMessageResolver()__ method to get the name of a file with password's error messages:
```
String pattern = "loc-pass-messages/password_messages_%s.properties";
HttpServletRequest httpServletRequest =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
String resourceName =
        String.format(pattern,localeResolver.resolveLocale(httpServletRequest).toString());

MessageResolver resolver = null;
InputStream resourceInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
InputStreamReader reader = new InputStreamReader(resourceInputStream, Charset.forName("UTF-8"));
try {
    Properties props = new Properties();
    props.load(reader);
    resolver = new PropertiesMessageResolver(props);
} catch (IOException e) {
    e.printStackTrace();
}
return resolver;
}
```
## --Commit-14-- ##
Some clean up was done in the messages_en.properties, messages_ru_RU.properties and in templates/login.html.

## --Commit-15-- ##
The __password confirmation validator__ (the Validation on a Class object, not on a class's field !), Thymeleaf's __global errors__ processing were added.

In order to be able to compare two fields of the same DTO class, we have to implement an annotation with __ElementType.TYPE__ argument in its __@Target__ annotation.

The __PasswordConfirmationValidator__ annotation interface and __PasswordConfirmationValidatorImpl__ class were added, they implement the validation of that the __confirmed password__ field is the same as the __password__ field.
For annotating of classes with this annotation, it has __ElementType.TYPE__ argument in its __@Target__ annotation:
```
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
```
In order to check whether the confirmed password field is the same as the password field, we have to annotate our __ViewFormUser__ DTO class with the annotation __@PasswordConfirmationValidator__:
```
@PasswordConfirmationValidator
public class ViewFormUser {
``` 
For the sake of experiment, the following code was added in the __checkPersonInfo()__ method of the __RegisterController__ class:
```
ObjectError confirmedPasswordErrMsg = bindingResult.getGlobalError();
if(confirmedPasswordErrMsg != null) {
    LOGGER.debug("Global error (@PasswordConfirmationValidator) ==>" + confirmedPasswordErrMsg.getDefaultMessage());
    model.addAttribute("confirmedPasswordError", confirmedPasswordErrMsg.getDefaultMessage());
}
```
This code is not used, it's an alternative to __Thymeleaf__'s global-errors prosessing (see [Thymeleaf: 7.3 Global errors](https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#global-errors)).

In html "form" of templates/registration.html this  __Thymeleaf__'s code checks for the global error and shows it in __<span id="confirmedPassportError" ...>__

```html
<span id="confirmedPassportError" class="alert alert-danger col-sm-4" th:if="${#fields.hasErrors('global')}" th:errors="*{global}">confirmed password error</span>

```
## --Commit-16-- ##
An intermediate, not finished transition toward Spring security. The dependency is added:
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
The main code changes are in:
- RegisterController: 
    - call UserServiceImpl.saveUserInRepository(), 
    - publish ApplicationEvent (AfterUserRegisteredEvent.class); 
- SecurityConfig adds two methods for PasswordEncoder and configure(HttpSecurity http) to allow anonymous access to login.html; 
- User class adds @Column private boolean enabled; 
- VerificationToken @Entity joined @OneToOne to User.class holds Date expirationDate field; 
- UserServiceImpl.class with saveUserInRepository(ViewFormUser dtoUser); 
- class UserAlreadyExistException extends RuntimeException.

Custom property __lizard.config.properties__ added.
__UserAlreadyExistException extends RuntimeException__ added.

On this step a development of a verification procedure with help of the link in the e-mail, which includes the verification token, started.
Please refer to [Registration – Activate a New Account by Email](https://www.baeldung.com/registration-verify-user-by-email) for additional reading. 

In the controller's __@RequestMapping(value = "/registration", method = RequestMethod.POST)__ method has the code for generating the event on basis of which an e-mail with verification token will be send:
```
User newRegisteredUser = userService.saveUserInRepository(viewFormUser);
ApplicationEvent event = new AfterUserRegisteredEvent(newRegisteredUser, request.getLocale(), getAppUri(request));
applicationEventPublisher.publishEvent(event);
```
This code includes [Spring Events](https://www.baeldung.com/spring-events) (additional reading is [Better application events in Spring Framework 4.2](https://spring.io/blog/2015/02/11/better-application-events-in-spring-framework-4-2))

The new entity which is intended to store a verification token in the database is added. The verification token is a key artifact through which a user is verified ([Registration – Activate a New Account by Email](https://www.baeldung.com/registration-verify-user-by-email))
```
@Entity
@Table(name = "verification_property")
@PropertySource("classpath:lizard.config.properties")
@ConfigurationProperties(prefix = "lizard")
public class VerificationToken {
```
There are some changes for User class. The most important is: be aware that the BCrypt algorithm generates a String of length 60, so we need to make sure that the password will be stored in a column that can accommodate it
([Registration with Spring Security – Password Encoding](https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt)).
```
@NotNull
@Column(length = 60)
private String password;
```
A simple version of __class SecurityConfig extends WebSecurityConfigurerAdapter__ is started: 
```
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
```
The WebSecurityConfig class is annotated with __@EnableWebSecurity__ to enable Spring Security’s web security support and provide the __Spring MVC integration__. It also extends __WebSecurityConfigurerAdapter__ and overrides a couple of its methods to set some specifics of the web security configuration ([Securing a Web Application]https://spring.io/guides/gs/securing-web/).

This class includes __@Bean public PasswordEncoder__ for encoding the users' passwords: 
```
@Bean
public PasswordEncoder passwordEncoder() {
  return new BCryptPasswordEncoder(17);
}
```
__BCryptPasswordEncoder__'s parameter is a strength - the log rounds to use, between 4 and 31, "strength" (a.k.a. log rounds in BCrypt) and a SecureRandom instance. The larger the strength parameter the more work will have to be done (exponentially) to hash the passwords.

[BCryptPasswordEncoder affects startup time of server](https://stackoverflow.com/questions/43259737/bcryptpasswordencoder-affects-startup-time-of-server)

## --Commit-17-- ##
The fix of small bugs.

## --Commit-18-- ##
The implementation of ApplicationListener<AfterUserRegisteredEvent> and onApplicationEvent method. Created a method in UserService which calls TokenRepository to save new token for the user. 'java.lang.StackOverflowError exception. Cannot evaluate model object toString()' issue: solved by excluding mutual reference objects.
Also I have solved an issue of '@Value from custom.property file isn't initialised'.

In __public class Role__, __public String toString()__ I ought to comment the reference to __private Set<User> users;__
```
//                ", users=" + users +

```
The same issue is for __public class User__, __public String toString()__ method because of a cyclic structure, references to __private List<Role> roles;__:
```html
//                ", roles=" + roles +
```
And also for __public class VerificationToken__:
```
//                ", user=" + user +
```

### Lombok.hashCode issue with “java.lang.StackOverflowError: null” ###
[Lombok.hashCode issue with “java.lang.StackOverflowError: null”](https://github.com/rzwitserloot/lombok/issues/1007)
```
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.2</version>
    <scope>provided</scope>
</dependency>
```

## --Commit-19-- ##
The __ERROR__ which prevented to show original messages on the html forms was fixed. Before this fix in place of the messages I got the messages like
```
{dto.user.size.firstname}
{dto.user.size.lastname}
{constraint.validator.user.registration.email}
```
It was all about the __public Validator getValidator()__ method of __public class MvcConfig implements WebMvcConfigurer__:

```
@Override
public Validator getValidator() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.setValidationMessageSource(messageSource());
    return validator;
}
```
__It should be set in order to show messages from message_en.properties/message_ru_RU.properties__ !!!
It provides a custom Validator instead of the one created by default. The default implementation, assuming JSR-303 is on the classpath, is: OptionalValidatorFactoryBean.

The 'e-mail confirmation message' which shows on the registration form is added.

## --Commit-20-- ##
The main changes are:
* The __JavaMailSender__ in __ApplicationListener<AfterUserRegisteredEvent>__ which sends an e-mail with a randomly generated confirmation token;
* __verifyConfirmationToken__ method of __UserService__. This method do search of the token in the database and qualifies it as VALID, INVALID or EXPIRED; 
* __registrationStatus__ page for showing the result of the token validation.
 
In order to send confirmation emails I use __postfix smtp server__ which is installed locally on my computer. I would recommend following articles on how to install __postfix__ on Ubuntu:
* [How To Install and Configure Postfix as a Send-Only SMTP Server on Ubuntu 14.04](https://www.digitalocean.com/community/tutorials/how-to-install-and-configure-postfix-as-a-send-only-smtp-server-on-ubuntu-14-04)
* [How To Install and Configure Postfix on Ubuntu 16.04](https://www.digitalocean.com/community/tutorials/how-to-install-and-configure-postfix-on-ubuntu-16-04)
* [Setup Postfix to Login to Your Email Account and Deliver Mail](https://www.linuxjournal.com/content/setup-postfix-login-your-email-account-and-deliver-mail)
* [16. SMTP Authentication for Mail servers](http://postfix.state-of-mind.de/patrick.koetter/smtpauth/smtp_auth_mailservers.html)
My __/etc/hosts__ file has a line that assigns my computer's IP (below it's denoted as xx.xx.xx.xx) to the name I used for the tests:
```
xx.xx.xx.xx	grigmail.lizard.com
```
The fragment of __application.properties__ file which is responsible for smtp sending in my case is shown below:
```
################### JavaMail Configuration ##########################
support.email=your.email@gmail.com
spring.mail.host=grigmail.lizard.com
spring.mail.port=25
spring.mail.protocol=smtp
#spring.mail.username=USERNAME@gmail.com
#spring.mail.password=PASSWORD
spring.mail.properties.mail.transport.protocol=smtps
spring.mail.properties.mail.smtps.auth=false
spring.mail.properties.mail.smtps.starttls.enable=false
spring.mail.properties.mail.smtps.timeout=8000
```
Main work on how to send email is concentrated in
```
@Component
public class NewUserRegisteredListener implements ApplicationListener<AfterUserRegisteredEvent> { ... }
``` 
For the details of the implementation pleas refer to [Guide to Spring Email](https://www.baeldung.com/spring-email)

## --Commit-21-- ##
In __public class VerificationToken__ I met an __"${} is not working in @Value"__ issue. I was not able to bound the value of  __lizard.verivication.token.expiration__ from __lizard.config.properties__ configuration file to __rivate Long expirationInMinutes__ field and pass it to the constructor as __this__ field.
```
@Value("${lizard.verivication.token.expiration}")
private Long expirationInMinutes;
```
Here are the articles which explain how to solve it:
* [${... } placeholders support in @Value annotations in Spring](http://blog.codeleak.pl/2015/09/placeholders-support-in-value.html);
* [Spring – ${} is not working in @Value](https://www.mkyong.com/spring/spring-is-not-working-in-value/)
* [Spring @PropertySource & @Value annotations example](http://websystique.com/spring/spring-propertysource-value-annotations-example/)
In order to fix the issue I created a configure class __public class ValuePropertiesConfig__ which configures the Property Sources to "lizard.config.properties":
```
@Bean
public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
    c.setLocation(new ClassPathResource("lizard.config.properties"));
    return c;
}
```
And then in place of
```
@Value("${lizard.verivication.token.expiration}")
private Long expirationInMinutes;
```
I did the constructor's injection of __Long expirationInMinutes__ as a parameter of VerificationToken constructor:
```
public VerificationToken(final String token, final User user, Long expirationInMinutes) {
    super();
    this.token = token;
    this.user = user;
    this.expirationDate = calculateExpiryDate(expirationInMinutes);
}
```
An object of __VerificationToken__ class is created in __UserServiceImpl__ class, where it gets the __tokenExpiration__ parameter which bounds to __@Value("${lizard.verivication.token.expiration}")__. 
```
@Value("${lizard.verivication.token.expiration}")
private Long tokenExpiration;

@Override
public void createUsersToken(User user, String token) {
    VerificationToken verificationToken = new VerificationToken(token, user, this.tokenExpiration);
    tokenRepository.save(verificationToken);
}
```
An explanation of this trick I found in
* [How to import value from properties file and use it in annotation?](https://stackoverflow.com/questions/33586968/how-to-import-value-from-properties-file-and-use-it-in-annotation)
* [Spring Property Injection in a final attribute @Value - Java](https://stackoverflow.com/questions/7130425/spring-property-injection-in-a-final-attribute-value-java)

## --Commit-22-- ##
An intermediate commit, transitional to replace the "html page with "form"'s with attributes" returned by the controller's method by response body object, which is processed by Javascript in the "form".

In this try we replace
```
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String checkPersonInfo(@Valid @ModelAttribute("viewFormUser") ViewFormUser viewFormUser,
                                  BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
```
by
```
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    public ErrorDetails checkPersonInfo(@Valid @ModelAttribute("viewFormUser") ViewFormUser viewFormUser,
                                        BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return new ErrorDetails("errors");
        }
```
An object passed to "form" is an instance of __public class ErrorDetails__ and will contain all the errors found by server validation process.

In this commit (because of the development is not complete), in place of registration.html page we get some JSON data in the browser.

## --Commit-23-- ##
On a way to replace the html-page name with Object @ResponseBody which contains Map of messages for <span id="xxxError"...> tags of html-form.
ErrorDetails class is replaced by __ResponseDetails__ class. This class contains next two fields:
``` 
private String indicator;
private Map<String, String> messages; 
```    
The first one serves for carrying over the result of server validation process, and it can be either "error" or "success".
The second one is a map of
* key - id (without suffix 'Error') of an attribute in "errors" tags like 'email' in
```
<span id="emailError" class="alert alert-danger col-sm-4" style="display:none"></span>                    
```
on the registration.html page
* value - message which should be displayed in these tags in case of error or other information event detected

The processing of __ResponseDetails__ is executed in a Javascript in
```
<script th:inline="javascript">

```
tag. This code proccesses Json representation of response body object (__ResponseDetails__ ), extracts messages and shows these messages in errors span tags on the html page.

## --Commit-24-- ##
The transition to Object @ResponseBody which contains a Map of messages for @RequestMapping("/registration") is finished. @ExceptionHandler(value = {UserAlreadyExistException.class}) returns localized message inside ResponseEntity<Object>

## --Commit-25-- ##
A customization of TomCat in order to solve __'Invalid character found in the request target'__ issue. 

The Server throws an error stack because of inappropriate characters in the request's link. 
This issue and the way of how to fix it described, for example, in
* [Invalid character found in the request target. The valid characters are defined in RFC 7230 and RFC 3986](https://github.com/bohnman/squiggly-java/issues/42)
* [Invalid character found in the request target in spring boot](http://1.yqwk.win/?questions/46251131/invalid-character-found-in-the-request-target-in-spring-boot)
* [Setting 'relaxedQueryChars' for embedded Tomcat](https://stackoverflow.com/questions/51703746/setting-relaxedquerychars-for-embedded-tomcat)

In order to fix the issue the __MyTomcatWebServerCustomizer__ class which implements __WebServerFactoryCustomizer<TomcatServletWebServerFactory>__ was developed.
```
@Component
public class MyTomcatWebServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		// customize the factory here
		factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
			@Override
			public void customize(Connector connector) {
				connector.setAttribute("relaxedQueryChars", "|{}[]");
			}
		});
	}
}
```

## --Commit-26-- ##
**_This commit starts Spring Security implementation_**. In order to implement login/authentication with Spring Security, we need to implement **_org.springframework.security.core.userdetails.UserDetailsService_** interface.

"The __UserDetailsService__ interface is used to retrieve user-related data. It has one method named __loadUserByUsername()__ which finds a user entity based on the username and can be overridden to customize the process of finding the user. It is used by the __DaoAuthenticationProvider__ to load details about the user during authentication." (the quotation from [Spring Security: Authentication with a Database-backed UserDetailsService](https://www.baeldung.com/spring-security-authentication-with-a-database)).

### UserDetailsService ###
Implemented by __UserDetailsServiceImpl__. We need to provide an implementation of the __loadUserByUsername()__ method of __UserDetailsService__. But the challenge is
that the __findByUsername()__ method of our __UserService__ returns a __User__ entity, while **_Spring Security expects_** a
__UserDetails__ object from the __loadUserByUsername()__ method. We will create a converter for this to convert __User__ to __UserDetails__ implementation. 

The user's email serves as the user name in this example:  
```
@Override
public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(()->new UsernameNotFoundException(
                    String.format("User identified by e-mail %s is not found", userEmail)));
    UserDetails userdetails = converter.convert(user);
    return userdetails;
}
```

### UserDetails implementation ###
__public class UserSecurityDetailsImpl__ implements __UserDetails__. [Interface UserDetails](https://docs.spring.io/autorepo/docs/spring-security/4.2.x-SNAPSHOT/apidocs/org/springframework/security/core/userdetails/UserDetails.html)
Provides core user information. Implementations are not used directly by Spring Security for security purposes. They simply store user information which is later encapsulated into Authentication objects. This allows non-security related user information (such as email addresses, telephone numbers etc) to be stored in a convenient location.

In my case the __username__ field of __UserSecurityDetailsImpl__ keeps user's email (and serves as user identifier field):
```
private String username; // email serves as a username
```
### Converter<User, UserDetails> ###
This class makes conversion from the __User__ object (entity) to the __UserDetails__ object:
```
@Component("userToUserDetailsConverter")
public class UserToUserDetailsConverter implements Converter<User, UserDetails>
```

### DaoAuthenticationProvider ###
__public class DaoAuthenticationProviderExtended__ extends __DaoAuthenticationProvider__.

__Spring Security__ includes a production-quality __AuthenticationProvider__ implementation called __DaoAuthenticationProvider__. 
This authentication provider is compatible with all of the authentication mechanisms that generate a __UsernamePasswordAuthenticationToken__, 
and is probably the most commonly used provider in the framework. 

Like most of the other authentication providers, the __DaoAuthenticationProvider__ leverages a __UserDetailsService__ in order to lookup the __username__, __password__ and __GrantedAuthority__. 
Unlike most of the other authentication providers that leverage __UserDetailsService__, this authentication provider actually requires the password to be presented, and the provider will actually evaluate the validity or otherwise of the password presented in an authentication request object [DAO Authentication Provider](https://docs.spring.io/spring-security/site/docs/2.0.x/reference/html/dao-provider.html).

In this commit:
* The security configuration for __/login__ page was developed; 
* @RequestMapping(value = {"/login", "/"}... for RequestMethod.GET/POST removed and replaced by __WebSecurityConfigurerAdapter__'s configuration, in particular by __protected void configure(HttpSecurity httpSecurity)__ method; 
* This changes caused __login.html__ changes and development of a whole bunch of classes which extends or implements: 
  - DaoAuthenticationProvider, 
  - UserDetailsService, 
  - UserDetails, 
  - Converter<User, UserDetails>;
* Be aware of implementing of the __httpSecurity.csrf().disable();__ configuration both in __login.html__ and in the class which extends __WebSecurityConfigurerAdapter__.

**NOTE:** Personally, I was surprised and pleased with the fact that the security configuration for __/login__ page replaces two methods (with GET and POST) for __@RequestMapping(value = {"/login", "/"})__:
```
//    @RequestMapping(value = {"/login", "/"}, method = RequestMethod.GET)
//    public String getLoginPage(ViewFormLogin viewFormLogin, Model model, HttpServletRequest httpServletRequest) {
//      ...
//    }

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public String processLoginPage(@Valid @ModelAttribute("viewFormLogin") ViewFormLogin viewFormLogin, BindingResult bindingResult) {
//      ...
//    }
```
## --Commit-27-- ##
The development of __SecurityConfig__, __MyCustomAuthenticationFailureHandler__ and __MyCustomAuthenticationSuccessHandler__ classes, "badcredentialerror" parameter of login page was done.

My implementation was in the development of __MyCustomAuthenticationFailureHandler__ @Component("authenticationFailureHandler"):
```
@Component("authenticationFailureHandler")
public class MyCustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
}
```
It uses __SimpleUrlAuthenticationFailureHandler.setDefaultFailureUrl()__ method to set up redirection page in case of an authentication error:
```
setDefaultFailureUrl("/login?badcredentialerror=true");
```     
__Note__, that __?badcredentialerror=true__ parameter for /login.html page is a flag which shows that an error occurs (th:if="${param.badcredentialerror != null}"). And errorMessage
```
String errorMessage = messages.getMessage("login.bad.credentials", null, locale);

request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
```
is processed on /login.html page as follows:
```
<div th:if="${param.badcredentialerror != null}" class="alert alert-danger" th:text="${session[SPRING_SECURITY_LAST_EXCEPTION]}">login bad credentials error</div>
```
And then we develop __MyCustomAuthenticationSuccessHandler__ class which implements __AuthenticationSuccessHandler__:
```
@Component("authenticationSuccessHandler")
public class MyCustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
```
The main purpose of the __onAuthenticationSuccess()__ method is to set up an URL where the control is passed in case of successful authentication:
```
String targetUrl = "/homepage.html?user=" + authentication.getName();
redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, targetUrl);
```
Until I haven't implemented __AuthenticationSuccessHandler__ I experienced with a __"login twice"__ issue/ The similar issue discussed on
* [Login twice ?](http://forum.spring.io/forum/spring-projects/security/90069-login-twice)
* [ProviderManager.authenticate called twice for BadCredentialsException](https://stackoverflow.com/questions/33788120/providermanager-authenticate-called-twice-for-badcredentialsexception)

## --Commit-28-- ##
The parameter __.logoutUrl("/mylogout")__ for HttpSecurity security configuration which triggers logout process set up. The message shows on top of "/login" page by analysing of __"?logoutSuccess=true"__ parameter.

## --Commit-29-- ##
Added some elements of the session management for security configuration: 
- 'invalidSession.html', 
- session.setMaxInactiveInterval() in implementation of AuthenticationSuccessHandler.

## --Commit-30-- ##
An intermediate, draft variant of the __Remember Me__ configuration.

Remember Me configuration is quite straightforward and consists of next steps:
1. first, all the time we refer to __@Bean DataSource__ which is defined in __PersistenceJPAConfig__ and should be autowired it in SecurityConfig (which )extends WebSecurityConfigurerAdapter).
2. define Token Repository based on DataSource as follows
3. in __HttpSecurity config()__ add code concerning of remember me configuration

The class __MyJdbcTokenRepositoryImpl__ which extends __JdbcTokenRepositoryImpl__ is developed.  It overrides __initDao()__ method to be able to create __persistent_logins__ table which keeps security tokens.
```
public static final String CREATE_TABLE_SQL =
        "create table persistent_logins (" +
                "username varchar(100) not null, " +
                "series varchar(64) primary key, " +
                "token varchar(100) not null, " +
                "last_used timestamp not null)";

@Override
protected void initDao() {
    try {
        super.getJdbcTemplate().execute(CREATE_TABLE_SQL);
    } catch (DataAccessException e) {
        LOGGER.info("table persistent_logins have been already created");
    }
}
```
## --Commit-31-- ##
Step by step trails in order to implement the __Remember Me__ configuration and to find the right solution. 

**_IMPORTANT NOTE:_** In order to find the right solution, I had to create a separate small project. This project was successful and after the research I implemented its code in the current project.
I put the code of this project and detailed description of it in github.com.
Here is a link to this project: [lizard-buzzard/persistent-token-rememberme-authentication](https://github.com/lizard-buzzard/persistent-token-rememberme-authentication)

## --Commit-32-- ##
This commit is a continuation of the previous commit and it finishes the sequence of commits for implementation of the __Remember Me__ configuration.

Some interesting (at least for me) remarks:

1. In case of if to set at the same time
    ```
    .rememberMeServices(rememberMeServices)
    .rememberMeCookieName("my-remember-me-cookie")
    ```
    ```
    2018-09-13 10:31:29.694 ERROR 19528 --- [  restartedMain] o.s.boot.SpringApplication               : Application run failed
    ...
    Caused by: java.lang.IllegalArgumentException: Can not set rememberMeCookieName and custom rememberMeServices.
    ```
    This IllegalArgumentException is thrown by Spring's __RememberMeConfigurer__
    ```
    RememberMeConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractHttpConfigurer<RememberMeConfigurer<H>, H>
        /**
         * Validate rememberMeServices and rememberMeCookieName have not been set at
         * the same time.
         */
        private void validateInput() {
            if (this.rememberMeServices != null && this.rememberMeCookieName != DEFAULT_REMEMBER_ME_NAME) {
                throw new IllegalArgumentException("Can not set rememberMeCookieName " +
                        "and custom rememberMeServices.");
            }
        }
        ...
    }
    ```
2. A note on the name of the checkbox in the html form:
    ```
    <div class="form-group row">
        <span class="col-sm-4 offset-4" style="text-align: right;">
            <label>
                <input class="checkbox" type="checkbox" name="my-remember-me-checkbox">
                <span class="checkbox-custom"></span>
                <span class="label" th:text="#{label.form.rememberMe}">Remember Me</span>
            </label>
        </span>
    </div>
    ```
    
    __name="my-remember-me-checkbox"__ corresponds to __String rememberMeServicesName__ in
    
    ```
    public class CustomRememberMeServices extends PersistentTokenBasedRememberMeServices {
    
        public CustomRememberMeServices(String key, String rememberMeServicesName, UserDetailsService userDetailsService, PersistentTokenRepository persistentTokenRepository) {
            super(key, userDetailsService, persistentTokenRepository);
            this.setParameter(rememberMeServicesName);
            this.tokenRepository = persistentTokenRepository;
            this.key = key;
        }
    }
    ```
    and its value is __"my-remember-me-checkbox"__ in the configuration class of __CustomRememberMeServices__ in my implementation:
    ```
    @Configuration
    public class RememberMeServiceConfig {
        @Bean
        public CustomRememberMeServices rememberMeServices(
                @Qualifier("userDetailsService") UserDetailsService userDetailsService,
                @Qualifier("persistentTokenRepository") PersistentTokenRepository persistentTokenRepository) {
            CustomRememberMeServices rememberMeServices = new CustomRememberMeServices("theKey", "my-remember-me-checkbox", userDetailsService, persistentTokenRepository);
            return rememberMeServices;
        }
    }
    ```
    So, there is no need to specify __.rememberMeParameter("my-remember-me-checkbox")__ in __HttpSecurity__ configuration in the __SecurityConfig__ class, this chank of code should be commented:
    ```
    httpSecurity.rememberMe()
            .rememberMeParameter("my-remember-me-checkbox")
    ```

## --Commit-33-- ##
In this commit a redirection depending on the role of the user who logged in is developed:
- __HttpSecurity__: __exceptionHandling().accessDeniedPage__, __.hasAuthority("ADMIN_PAGE_PRIVILEGE")__, __.hasAuthority("USER_PAGE_PRIVILEGE")__ are added; 
- __AuthenticationSuccessHandler__: redirects to /admin or /user paths depending on privileges of the user logged in; 
- __RegisterController__: @RequestMapping(value = "/homepage/{role}") displays either __user page__ or __admin console page__ depending on the role of the user who logged in; 
- __AdminService__ class is developed to prepare users' list information for administrator's page;
- __ResourceHandlerRegistry__: "classpath:/static/images/" is added. 

The redirection depending on the role of the user works according to the next code fragment. The logged users with __ADMIN_PAGE_PRIVILEGE__ are redirected to __/homepage/admin__ path and the users with __USER_PAGE_PRIVILEGE__ are redirected to __/homepage/user__ path:
```
if(sup.get().anyMatch(a->(((GrantedAuthority) a).getAuthority().equals("ADMIN_PAGE_PRIVILEGE")))) {
    targetUrl = "/homepage/admin?user=" + auth.getName();
} else if(sup.get().anyMatch(a->(((GrantedAuthority) a).getAuthority().equals("USER_PAGE_PRIVILEGE")))) {
    targetUrl = "/homepage/user?user=" + auth.getName();
} else {
    targetUrl = "/";
}
```

## --Commit-34-- ##
Most of the principal and basic features were implemented before this commit. From now there will be improvements on the html pages and some of new functions will be developed.

This commit did a minor change, homepage.html was renamed to userHomePage.htm.

## --Commit-35-- ##
Two new html pages were added, and the changes in the controller were made in order to support the new added pages:
- templates/userAccountPage.html
- templates/userServicesPage.html

An old bug for most of the html pages was fixed, the __bootstrap.min.js__ and the __jquery.min.js__ script tags should be on the bottom of the page, just before the body tag:
```html
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</body>
```
Also in this commit I started to develop the menu for User Account Page.

## --Commit-36-- ##
New menu on Bootstrap’s **navbar** was started.  

## --Commit-37-- ##
Minor bugs fixed. Two errors fixed, in registration.html (order of the __bootstrap.min.js__ and the __jquery.min.js__ script tag) and an odd import clause in AdminServiceImpl.

## --Commit-38-- ##
A minor bug fixed  in registration.html (order of the __bootstrap.min.js__ and the __jquery.min.js__ script tag)


