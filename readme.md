# WebQuizEngine #

This project was developed with "JetBrains Academy" https://hyperskill.org/. 
In this project  only backend for the service. There is no frontend.

There are some capabilities of the service:

* register a user
 
You must send a json with email and password like this:
````json
{
  "email": "test@gmail.com",
  "password": "secret"
}
````
* create a quiz

You must send a json with some information:
````json
{
  "title": "The Java Logo",
  "text": "What is depicted on the Java logo?",
  "options": ["Robot","Tea leaf","Cup of coffee","Bug"],
  "answer": 2
}
````
* solve a quiz 

To solve a quiz you must send a json with your variant of answer, for example:
````json
{
  "answer": [2]
}
````
When you will see at list with all quizzes with 
feature "get all quizzes" you do not may see at the answer

And all other capabilities of service:

* delete a quiz (you must be author of the quiz)
* get all quizzes (with pagination and sort support)
* get a quiz by id
* get all solved quizzes by user (with pagination and sort support)

All the operations, except register a user, are protected by basic authentication.
Users and quizzes are stored in H2 database.