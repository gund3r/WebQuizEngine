# WebQuizEngine #

This project was developed with "JetBrains Academy" https://hyperskill.org/. 
In this project  only backend for the service. There is no frontend.

There are some capabilities of the service:

* register a user request `POST api/register`
 
You must send a json with email and password like this:
````json
{
  "email": "test@gmail.com",
  "password": "secret"
}
````
* create a quiz request `POST api/quizzes`

You must send a json with some information:
````json
{
  "title": "The Java Logo",
  "text": "What is depicted on the Java logo?",
  "options": ["Robot","Tea leaf","Cup of coffee","Bug"],
  "answer": 2
}
````
* solve a quiz `POST api/quizzes/{id}/solve`

To solve a quiz you must send a json with your variant of answer, for example:
````json
{
  "answer": [2]
}
````
When you will see at list with all quizzes with 
feature "get all quizzes" you do not may see at the answer

And other capabilities of service:

* delete a quiz (you must be author of the quiz) `DELETE api/quizzes/{id}`;
* get all quizzes (with pagination and sort support) `GET api/quizzes`;
* get a quiz by id `GET api/quizzes/{id}`;
* get all solved quizzes by user (with pagination and sort support) `GET api/quizzes/completed`;
* get all users `GET api/users`;
* get a user by id `GET api/users/{id}`;

All the operations, except register a user, are protected by basic authentication.
Users and quizzes are stored in H2 database.