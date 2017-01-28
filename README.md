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
##Location
