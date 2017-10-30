Project3 : So you think you can cater
Name: Angela Hoeltje
PittID: amh227
File: catering.py

Pre installation:
Install pip
Install python 3.6.3
Create virtual environment "catering"

On the command line type:

workon catering 			//to open created virtual environment 
set FLASK_APP=catering.py
flask initdb				//initializes the database with premade users,including admin
flask check				//prints out different premade users and events in different orders
flask run				//will show http address that can be loaded into google chrome for viewing


I could not get the program to run correctly, but if you run these commands you can see that I did get some aspects to run.
It correcctly adds Users in 2 catagories, Customers and Employees. The employees have a catagory with admin privledges.

Thereis also a class for events. 