-- Q1
CREATE TABLE Customers(
	birth_day DATE,
	first_name VARCHAR(20),
	last_name VARCHAR(20),
	c_id INT,
	CONSTRAINT PK_Customers PRIMARY KEY (c_id)
);

CREATE TABLE Employees(
	birth_day DATE,
	first_name VARCHAR(20),
	last_name VARCHAR(20),
	e_id INT,
	CONSTRAINT PK_Employees PRIMARY KEY(e_id)
);

CREATE TABLE Locations(
	address VARCHAR(20),
	l_id INT,
	CONSTRAINT PK_Locations PRIMARY KEY(l_id)
);

CREATE TABLE Transactions(
	e_id INT,
	c_id INT,
	l_id INT,
	date DATE,
	t_id INT,
	CONSTRAINT PK_Transactions PRIMARY KEY(t_id),
	CONSTRAINT FK_Employees FOREIGN KEY (e_id) REFERENCES Employees(e_id),
	CONSTRAINT FK_Customers FOREIGN KEY (c_id) REFERENCES Customers(c_id),
	CONSTRAINT FK_Locations FOREIGN KEY (l_id) REFERENCES Locations(l_id)
);

CREATE TABLE Items(
	price_for_each INT,
	name VARCHAR(20),
	CONSTRAINT PK_Items PRIMARY KEY(name)
);

CREATE TABLE ItemsBroughtIntoShop(
	name VARCHAR(20),
	l_id INT,
	amount INT,
	date DATE,
	CONSTRAINT FK_Items FOREIGN KEY (name) REFERENCES Items(name),
	CONSTRAINT FK_Locations2 FOREIGN KEY (l_id) REFERENCES Locations(l_id)
);

CREATE TABLE MovementOfItems(
	name VARCHAR(20),
	from_l_id INT,
	to_l_id INT,
	amount INT,
	date DATE,
	CONSTRAINT FK_Items2 FOREIGN KEY (name) REFERENCES Items(name),
	CONSTRAINT FK_Locations3 FOREIGN KEY (from_l_id) REFERENCES Locations(l_id),
	CONSTRAINT FK_Locations4 FOREIGN KEY (to_l_id) REFERENCES Locations(l_id)
);

CREATE TABLE ItemsInTransactions(
	name VARCHAR(20),
	t_id INT,
	amount INT,
	CONSTRAINT FK_Items3 FOREIGN KEY (name) REFERENCES Items(name),
	CONSTRAINT FK_Transactions FOREIGN KEY (t_id) REFERENCES Transactions(t_id)
);

-- Q2
CREATE VIEW DeniseTransactions AS(
	SELECT COUNT(Employees.e_id) AS number_of_transactions
	FROM Transactions
	INNER JOIN Employees ON Transactions.e_id = Employees.e_id
	WHERE Transactions.date LIKE '2021-09-%' AND Employees.first_name = 'Denise' AND Employees.last_name = 'Davies'
);
SELECT * FROM DeniseTransactions;


-- Q3
CREATE VIEW CustomerInShop AS (
	SELECT DISTINCT Customers.birth_day, Customers.first_name, Customers.last_name
	FROM Transactions
	INNER JOIN Customers on Transactions.c_id = Customers.c_id
	WHERE Transactions.date = '2021-9-07' AND Transactions.l_id = 1
);
CREATE VIEW EmployeeInShop AS (
	SELECT DISTINCT Employees.birth_day, Employees.first_name, Employees.last_name
	FROM Transactions
	INNER JOIN Employees ON Transactions.e_id = Employees.e_id
	WHERE Transactions.date = '2021-9-07' AND Transactions.l_id = 1
);
CREATE VIEW PeopleInShop AS 
	SELECT * FROM CustomerInShop
	UNION
	SELECT * FROM EmployeeInShop
	ORDER BY birth_day ASC;
    
SELECT * FROM PeopleInShop;

-- Q4
CREATE VIEW TransactionValue AS (
	SELECT ItemsInTransactions.t_id, SUM(Items.price_for_each * ItemsInTransactions.amount) AS value
	FROM ItemsInTransactions
	INNER JOIN Items ON ItemsInTransactions.name = Items.name
	GROUP BY ItemsInTransactions.t_id
	ORDER BY ItemsInTransactions.t_id
);
SELECT * FROM TransactionValue;
    
-- Q5
CREATE VIEW ItemsAmountOut1 AS (
	SELECT ItemsInTransactions.name, Transactions.l_id, Transactions.date, -(ItemsInTransactions.amount) AS 'amount'
	FROM Transactions 
    INNER JOIN ItemsInTransactions ON Transactions.t_id = ItemsInTransactions.t_id
);

CREATE VIEW ItemsAmountIn1 AS(
	SELECT ItemsBroughtIntoShop.name, ItemsBroughtIntoShop.l_id, ItemsBroughtIntoShop.date, ItemsBroughtIntoShop.amount
    FROM ItemsBroughtIntoShop
);

CREATE VIEW ItemsAmountOut2 AS(
	SELECT MovementOfItems.name, MovementOfItems.from_l_id, MovementOfItems.date, -(MovementOfItems.amount) AS 'amount'
    FROM MovementOfItems
);

CREATE VIEW ItemsAmountIn2 AS(
	SELECT MovementOfItems.name, MovementOfItems.to_l_id, MovementOfItems.date, MovementOfItems.amount
    FROM MovementOfItems
);

CREATE VIEW ItemsChangeTable AS
	SELECT * FROM ItemsAmountOut1
	UNION 
	SELECT * FROM ItemsAmountIn1
	UNION 
	SELECT * FROM ItemsAmountIn2
    UNION 
	SELECT * FROM ItemsAmountOut2;

CREATE VIEW KeyDays AS(
	SELECT TmpTable.name, TmpTable.l_id, TmpTable.date, SUM(TmpTable.amount) AS 'amount'
	FROM( SELECT * FROM ItemsChangeTable ) AS TmpTable
	GROUP BY TmpTable.name, TmpTable.l_id, TmpTable.date
);

CREATE VIEW DistinctDays AS(
	SELECT DISTINCT Items.name, Locations.l_id, KeyDays.date, 0 as 'amount'
    FROM Items, Locations, KeyDays
);

CREATE VIEW AllDays AS(
	SELECT TmpTable.name, TmpTable.l_id, TmpTable.date, SUM(TmpTable.amount) AS 'amount'
    FROM (SELECT * FROM KeyDays
		  UNION 
          SELECT * FROM DistinctDays) AS TmpTable
	GROUP BY TmpTable.name, TmpTable.l_id, TmpTable.date
	ORDER BY TmpTable.name, TmpTable.l_id, TmpTable.date
);

CREATE VIEW AllDaysWithAmount AS(
	SELECT A.name, A.l_id, A.date, SUM(B.amount) AS 'amount'
	FROM AllDays AS A, KeyDays AS B
	WHERE A.name = B.name AND A.l_id = B.l_id AND B.date <= A.date
	GROUP BY A.name, A.l_id, A.date
);

CREATE VIEW ItemsOnDateAndLocation AS(
	SELECT * FROM AllDaysWithAmount
    WHERE AllDaysWithAmount.amount != 0
    ORDER BY AllDaysWithAmount.name, AllDaysWithAmount.l_id, AllDaysWithAmount.date
);
SELECT * FROM ItemsOnDateAndLocation;

-- Q6
CREATE VIEW NotFeasibleLocations AS(
	SELECT DISTINCT ItemsOnDateAndLocation.l_id, 0 AS 'feasible'
    FROM ItemsOnDateAndLocation WHERE ItemsOnDateAndLocation.amount < 0
);

CREATE VIEW FeasibleOrNot AS
	SELECT DISTINCT Locations.l_id, 1 AS 'feasible'
    FROM Locations
    WHERE Locations.l_id NOT IN (SELECT NotFeasibleLocations.l_id FROM NotFeasibleLocations)
    UNION
    SELECT * FROM NotFeasibleLocations;

CREATE VIEW FeasibleLocations AS(
	SELECT * FROM FeasibleOrNot
    ORDER BY FeasibleOrNot.l_id
);
SELECT * FROM FeasibleLocations;