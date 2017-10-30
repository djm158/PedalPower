
from flask import Flask, request, abort, url_for, redirect, session
#from test.test_typechecks import Integer
#from unittest.test.test_result import __init__
from flask_sqlalchemy import SQLAlchemy

### init

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///test.db'
db = SQLAlchemy(app)

loginPage = """<!DOCTYPE html>
<html>
    <head>
        <title>Welcome to Catering peeps!</title>
    </head>
    <body>
        <form action="" method="post">
            Username:  <input type="text" name="user" />
            <br />
            Password:  <input type="text" name="pass" />
            <br />
            <button type="button" onclick="addUser">NewUser!</button>
            <br />
            <button type="button" onclick="logout">Logout!</button>
            <br />
            <input type="submit" value="submit" />
        </form>
    </body>
</html>
"""

userProfile = """<!DOCTYPE html>
<html>
    <head>
        <title>Your Events!</title>
    </head>
    <body name="userProfileBody">
        Welcome back!
        <br />
        <br />
        <br />
    </body>
</html>
"""

logoutPage = """<!DOCTYPE html>
<html>
    <head>
        <title>Logged out</title>
    </head>
    <body>
        You have successfully been logged out!
    </body>
</html>
"""

# by default, direct to login
@app.route("/")
def default():
    return redirect(url_for("logger"))

@app.route("/login/", methods=["GET", "POST"])
def logger():
    name=0
    pw=0
    # first check if the user is logged in
    if "username" in session:
        return redirect(url_for("profile", username=session["username"]))

    # if not, and the incoming request is via POST try to log them in
    elif request.method == "POST":
        for k, v in vars(Employees).items():
            if k is "username":
                if v is request.form["user"]:
                    name=1
            if k is"password":
                if v is request.form["pass"]:
                    if name==1:
                        return displayResult(9, Employees.query.filter(Employees.username is k).all())
    # if all else fails, offer to log them in
    return loginPage


@app.route("/profile/")
@app.route("/profile/<username>")
def profile():
    
    return loginPage

@app.route("/logout/")
def unlogger():
    if "username" in session:
        session.clear()
        return loginPage

# needed to use sessions
# note that this is a terrible secret key
app.secret_key = "secret key"

if __name__ == "__main__":
    app.run()


### models

class Employees(db.Model):      
    #Managers & Staff
    id = db.Column(db.Integer, primary_key=True)
    #admin Privledges:: 2=admin 1=worker 0=customer
    adminPrivledges=db.Column(db.Integer)
    username = db.Column(db.String(80), unique=True)
    password = db.Column(db.String(120))
    def __init__(self, username, password,adminPrivledges):
        self.username = username
        self.password = password
        self.adminPrivledges = adminPrivledges
    def __repr__(self):
       return "\n<User(id='%r', name='%s', privledges='%r')>" % (self.id, self.username, self.adminPrivledges)

class Events(db.Model):
    #Events - one per day - up to 3 staff per event
    id = db.Column(db.Integer,primary_key=True)
    customer=db.Column(db.String(80), unique=True)
    eventName = db.Column(db.String(100), unique=True)
    date = db.Column(db.String(8))
    #only 3 staff members per event
    Staff1=db.ForeignKey(Employees.username)
    Staff2=db.ForeignKey(Employees.username)
    Staff3=db.ForeignKey(Employees.username)
    def __init__(self, name, date):
        self.eventName = name
        self.date=date
    def __repr__(self):
       return "\n<Events(id='%d', event='%s', customer='%s', date: '%s')>" % (
                            self.id, self.eventName, self.customer, self.date)

class Customers(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True)
    password = db.Column(db.String(120))
    #Max 5 events per customer
    Event1=db.ForeignKey(Events.eventName)
    Event2=db.ForeignKey(Events.eventName)
    Event3=db.ForeignKey(Events.eventName)
    Event4=db.ForeignKey(Events.eventName)
    Event5=db.ForeignKey(Events.eventName)
    def __init__(self, username, password):
        self.username = username
        self.password = password
    def __repr__(self):
       return "\n<Customers(id='%d', name='%s')>" % (
                            self.id, self.username)


### controllers

def displayResult(num, res):
    print("\nQ{}:\n".format(num), res, "\n\n")


@app.cli.command('initdb')
def initdb_command():
    """Reinitializes the database"""
    db.drop_all()
    db.create_all()
    #hardcode owner and password :: add others for displaying
    db.session.add(Employees("owner", "pass","2"))
    db.session.add(Employees("K.J.", "blahblah","1"))
    db.session.add(Employees("Wyatt", "password2","1"))
    db.session.add(Customers("Hannah", "blahblah"))
    db.session.add(Customers("FatHead", "password2"))
    db.session.add(Customers("Lafonda", "blahblah"))
    db.session.add(Customers("Jacoby", "password2"))
    db.session.add(Customers("Nancy Wheeler", "blahblah"))
    db.session.add(Customers("DemiGorgon", "password2"))
    db.session.add(Customers("Alec Trebeck", "blahblah"))
    db.session.add(Customers("Cat Orlando", "password2"))
    db.session.add(Events("Cthulu's Birthday Party","6/25/2018"))
    db.session.add(Events("National Taco day Party","5/15/2018"))
    db.session.add(Events("Demigorgon society's social hour","6/15/2018"))
    db.session.add(Events("National demidog day","6/30/2018"))
    db.session.add(Events("RIP Samwise Gamgee","10/31/2017"))


    # commit
    db.session.commit()
    print('Initialized the database.')

    #taken from class example
@app.cli.command('check')
def default():
    """demonstrates model queries and relationships --- tookk for testing """
    # queries
    displayResult(1, Employees.query.order_by(Employees.username))
    displayResult(2, Employees.query.order_by(Employees.username).all())
    displayResult(3, Employees.query.all())
    displayResult(4, Customers.query.order_by(Customers.username))
    displayResult(5, Customers.query.order_by(Customers.username).all())
    displayResult(6, Employees.query.limit(1).all())
    displayResult(7, Customers.query.order_by(Customers.username))
    displayResult(8,Events.query.order_by(Events.date).all())
    displayResult(9, Employees.query.limit(1).all())
    displayResult(9, Employees.query.filter(Employees.id < 3).all())
    displayResult(10, Employees.query)


