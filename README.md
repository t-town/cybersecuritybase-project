# cybersecuritybase-project
Writeup of obvious vulnerabilities in this project

##General
This is a general Java Spring - Hibernate application, and can be accessed through localhost:8080 when using a default installation. The main class is CyberSecurityBaseProjectApplication.
The application consists of the following pages
- /form : the signup form, use this as the beginning page
- /done : the page that is displayed after signup or login
- /login : use this to login after you have signed up (you can access this page through a private browsing window ;)

This writeup will always explain the location of the vulnerability first. I advise you to first search for it yourself, the vulnerbilities do not require special tools in general.

##Flaw 1: A3 - Cross Site Scripting (XSS)
###Location:
- /form & /done


###Reproduction steps
- Go to the /form page
- Establish that both the name and pledge field are displayed on the /done page
- Insert javascript code into the name field: eg <script>alert(1)</script>
- Click through to the /done page
- Anyone landing on the /done page will have the javascript you just inserted executed by their browser

###Possible consequence
An attacker can execute random javascript code on your machine. In this webapplication the attacker could send the sessionId contained in the session cookie to a server he controls.


###Remediation
When printing user provided data on a webpage, always escape it.
When using cookies to store session information, use the httpOnly flag.


##Flaw 2: A8 - Cross Site Request Forgery (CSRF)
###Location:
- /form & /done



###Reproduction steps
This flaw is closely related to the previous XSS flaw
- Go to the /form page
- Insert Javascript code into the name field, which submits a request to the /changePledge page
- Everyone visiting the /done page will now have their pledge changed!

###Possible consequence
An attacker can force the victim to change their pledge without having to know their sessionId.
More advanced attacks could, for example, delete your account or post something to your prefered social network (if that website would be vulnerable ofc).

###Remediation
Normally a same-origin policy would prevent a CSRF, however since we are using an XSS vulnerability this does not prevent the CSRF!
Another valid approach is the CSRF token, which is a secret in the html page that is sent with the request, this also does not prevent the XSS kind of CSRF!
Here the only defence is requiring user interaction, by having them re-enter their password. This would of course mean that the password of the user is properly protected ;)

##Flaw 3: A1 - Injection
###Location
/login

###Reproduction steps
- go to the /done page
- identify a username we want to login as
- This is a Java application, try a jpa injection: name: USERNAME'--
- Login as USERNAME

###Possible consequence
The attacker can now log in as any user.

###Remediation
Do not use string concatenation when making database queries!!!!!!!
For each language there are dedicated frameworks that help you execute queries on your database. For example you can use parameterized or named queries, effectevely disabling any attempt to alter the syntax tree of the query.
Do input sanitation

##Flaw 4: Missing function level access control

###Location
/done

Try using the hidden admin functionality ;)


###reproduction steps
- go the the /done page, adding the admin=1 url parameter
- remove a user
- capture this request
- This request can be resubmitted without being an admin, or having ever visited the admin page

###Possible consequence
Admin functions are not checked server side, and can be submitted by anyone

###Remediation
Always protect functions at the server side (business logic).

##Flaw 5: A2-Broken Authentication and Session Management

###Location
/done mostly

###Reproduction steps
- Go the the /done page
- Establish that session id is just an integer with a very predictable pattern
- Change your sessionId cookie (use any modern browser)
- Change the pledge of someone else

###Possible consequence
SessionIds are very valuable, since stealing one enables the attacker to impersonate the user.

###Remediation
Use an unpredictable, very long session id. The framework you use will be able to do so (like spring sessions)



