myRetail
Pre-req to run the app: Cassandra database on port 9042 on localhost
-----------------------------------------------------------------------------------
Run the below cql against the cassandra database:
~~~
CREATE KEYSPACE productpricing
   WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
   
CREATE TABLE myretail_product (
	id int,
	price decimal,
	CURRENCY_CODE text,
	PRIMARY KEY (id)
);
~~~
--------------------------------------------------------------------------
Launch the app 

Item price details can be saved in cassandra through the api 
Request header client-id:myretail needs to passed to the put request below
PUT http://localhost:8080/products/5678902
json structure example
~~~
{
  "price": 399.99,
  "currency_code": "USD"
}
~~~

Product pricing details can be accessed 
Get http://localhost:8080/products/5678902




