# Neo4j Training Backend
    
Heroku App: http://neo4j-training-backend.heroku.com

### Usage:

````
curl -H X-Session:239739847 -XPOST http://localhost:8080/backend/cypher -d'MATCH (n) RETURN n'

curl -H X-Session:239739847 -XDELETE http://localhost:8080/backend

curl -H X-Session:239739847 -XPOST http://localhost:8080/backend/init -d'{"init":"create (:User {name:"Andreas"}),(:User {name:"Michael"})","query":"MATCH (n:User) return n"}'

````