CREATE TABLE users (
	username TEXT NOT NULL,
	userpass TEXT NOT NULL,
	userstatus TEXT NOT NULL
);


CREATE TABLE customers (
	custname TEXT NOT NULL,
	custstatus TEXT NOT NULL
);

CREATE TABLE products (
	pname TEXT NOT NULL,
	pbuy NUMERIC (5,2) NOT NULL,
	psell NUMERIC (5,2) NOT NULL,
);

CREATE TABLE supply (
	pname TEXT NOT NULL,
	pquantity NUMERIC (5,2) NOT NULL,
);

CREATE TABLE transactions (
	transdate DATE NOT NULL,
	custname TEXT NOT NULL,
	productname TEXT NOT NULL,
	buyquan NUMERIC (5,2) NOT NULL,
	productprice NUMERIC (5,2) NOT NULL
);