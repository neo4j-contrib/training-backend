# Neo4j Training Backend
    
Heroku App: http://neo4j-training-backend.herokuapp.com

### Usage:

````
curl -H X-Session:239739847 -XPOST http://neo4j-training-backend.herokuapp.com/backend/cypher -d'MATCH (n) RETURN n'

curl -H X-Session:239739847 -XDELETE http://neo4j-training-backend.herokuapp.com/backend

curl -H X-Session:239739847 -XPOST http://neo4j-training-backend.herokuapp.com/backend/init -d'{"init":"create (:User {name:"Andreas"}),(:User {name:"Michael"})","query":"MATCH (n:User) return n"}'

````

### Run locally:

````
mvn exec:java
````
