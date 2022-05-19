<?php
session_start();
?>

<!DOCTYPE html>
<html lang="en-GB">

<head>
    <meta charset="UTF-8">
    <style>
        body {
            text-align: center;
            background-color: #d2d0cd;
        }

        h1 {
            margin: 0;
            padding: 10px;
            background: #14c39a;
            color: black;
        }

        h2 {
            margin: 0;
            margin-bottom: 25px;
            padding: 10px;
            background: #ebfffa;
        }

        h3 {
            padding: 10px;
            color: red;
        }

        select {
            width: 15%;
            border: 1px solid black;
            padding: 3px 5px;
            border-radius: 5px;
            margin-top: 6px;
            margin-bottom: 6px;
        }

        input {
            width: 15%;
            border: 1px solid black;
            padding: 3px 5px;
            border-radius: 5px;
            margin-top: 6px;
            margin-bottom: 6px;
        }

        input.button {
            width: 5%;
            border: 1px solid black;
            padding: 3px 5px;
            border-radius: 5px;
            margin-top: 6px;
            margin-bottom: 6px;
        }

        table {
            margin: auto;
        }

        td {
            border: 1px solid black;
            padding-left: 6px;
            padding-right: 6px;
            background-color: #ebfffa;
            color: black;
        }

        th {
            border: 1px solid black;
            background-color: #14c39a;
            color: black;
        }
    </style>

    <title>Gym Booking System</title>
</head>

<body>
    <h1>Welcome to Online Gym Booking System!</h1>

    <!-- Connect to MySQL Database -->
    <?php
    $db_hostname = "studdb.csc.liv.ac.uk";
    $db_database = "sgpqian";
    $db_username = "sgpqian";
    $db_password = "123456qpQP";
    $db_charset = "utf8mb4";
    $dsn = "mysql:host=$db_hostname;dbname=$db_database;charset=$db_charset";
    $opt = array(
        PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES => false
    );
    try {
        $pdo = new PDO($dsn, $db_username, $db_password, $opt);
        $stmt = $pdo->query("SELECT * FROM
                             Classes INNER JOIN Sessions ON
                             Classes.cName = Sessions.cName");
    } catch (PDOException $e) {
        exit("PDO Error: " . $e->getMessage() . "<br>");
    }
    ?>

    <!-- Collect Booking information -->
    <h2> Book a Class Session Here</h2>
    <?php
    // First stage: Select a Class
    function selectClass($pdo)
    {
        // Select items and order via mysql
        $stmt = $pdo->query("SELECT DISTINCT Classes.cName FROM
                                Classes INNER JOIN Sessions ON
                                Classes.cName = Sessions.cName
                                WHERE Sessions.capacity > 0
                                ORDER BY Classes.cName ASC");

        echo ' <form action="gym.php" method="post">
                   <label>Class:</label>
                   <select name="cName" onchange="submit()">';

        // If session has been set, show what session has.
        if (isset($_SESSION['cName'])) {
            echo " <option value = '" . $_SESSION['cName'] . "'>" . "Your choice: " . $_SESSION['cName'] . "</option>";
        } else {
            echo '<option value="">Select a class</option>';
        }
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            if ($row['cName'] != $_SESSION['cName']) {
                echo " <option value = '" . $row['cName'] . "'>" . $row['cName'] . "</option>";
            }
        }
        echo ' </select></form>';
    }

    // Second stage: Select a Day
    function selectDay($pdo)
    {
        // Select items and order via mysql
        $stmt = $pdo->prepare("SELECT Sessions.day 
                                   FROM Sessions
                                   WHERE cName = :cName AND capacity > 0
                                   ORDER BY FIELD(Sessions.day, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday');");
        $success = $stmt->execute(array("cName" => $_SESSION['cName']));
        echo ' <form action="gym.php" method="post">
                   <label>Day:</label>
                   <select name="day" onchange="submit()">';

        // If session has been set, show what session has.
        if (isset($_SESSION['day'])) {
            echo " <option value = '" . $_SESSION['day'] . "'>" . "Your choice: " . $_SESSION['day'] . "</option>";
        } else {
            echo '<option value="">Select a day</option>';
        }
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            if ($row['day'] != $_SESSION['day']) {
                echo " <option value = '" . $row['day'] . "'>" . $row['day'] . "</option>";
            }
        }
        echo ' </select></form>';
    }

    // Third stage: Select a period
    function selectPeriod($pdo)
    {
        // Select items and order via mysql 
        $stmt = $pdo->prepare("SELECT Sessions.cName, Sessions.day, Sessions.period 
                                   FROM Sessions
                                   WHERE cName = :cName AND day = :day
                                   ORDER BY Sessions.period ASC;");
        $success = $stmt->execute(array("cName" => $_SESSION['cName'], "day" => $_SESSION['day'],));

        echo ' <form action="gym.php" method="post">
                   <label>Period:</label>
                   <select name="period" onchange="submit()">';

        // If session has been set, show what session has.
        if (isset($_SESSION['period'])) {
            echo " <option value = '" . $_SESSION['period'] . "'>" . "Your choice: " . $_SESSION['period'] . "</option>";
        } else {
            echo '<option value="">Select a period</option>';
        }
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            if ($row['period'] != $_SESSION['period']) {
                echo " <option value = '" . $row['period'] . "'>" . $row['period'] . "</option>";
            }
        }
        echo ' </select></form>';
    }

    // Fourth stage: input name
    function inputName()
    {
        echo '<form action="gym.php" method="post">';
        if (isset($_SESSION['name'])) {
            echo ' <label>Name: <input type="text" name="name" onchange="submit()" value="' . $_SESSION['name'] . '"></label><br>';
        } else {
            echo ' <label>Name: <input type="text" name="name" onchange="submit()"></label><br>';
        }
        echo ' </form>';
    }

    // Fifth stage: input phone number
    function inputPhone()
    {
        echo '<form action="gym.php" method="post">';
        if (isset($_SESSION['phone'])) {
            echo ' <label>Phone: <input type="text" name="phone" onchange="submit()" value="' . $_SESSION['phone'] . '"></label><br>';
        } else {
            echo ' <label>Phone: <input type="text" name="phone" onchange="submit()"></label><br>';
        }
        echo ' </form>';
    }

    // Button for submit and reset
    function inputButton()
    {
        echo ' <form action="gym.php" method="post">';
        echo ' <input type="submit" class="button" name="submit" value="Submit">';
        echo ' <input type="submit" class="button" name="reset" value="Reset">';
        echo ' </form>';
    }
    ?>


    <!-- Help function -->
    <?php
    // Help function to validate select elements
    function isSelectValid($content)
    {
        if (!isset($_SESSION[$content]) || empty($_SESSION[$content])) {
            return false;
        } else {
            return true;
        }
    }

    // Help function to validate name
    function isNameValid($name)
    {
        if (preg_match('/^[a-zA-Z\'][a-zA-Z\'\-\s]*[^\-]$/', $name) && !preg_match('/[\'\-]{2}/', $name)) {
            return true;
        } else {
            return false;
        }
    }

    // Help function to validate phone
    function isPhoneValid($phone)
    {
        $processedPhone = preg_replace('/\s+/', '', $phone);
        if (!preg_match('/^0[\d]{8,9}$/', $processedPhone)) {
            return false;
        } else {
            $_SESSION['phone'] = $processedPhone;
            return true;
        }
    }

    // Help function to validate all items
    function isAllValid()
    {
        $err = "";
        if (!isSelectValid("cName")) {
            $err .= "Please select a class!<br><br>";
        }
        if (!isSelectValid("day")) {
            $err .= "Please select a day!<br><br>";
        }
        if (!isSelectValid("period")) {
            $err .= "Please select a period!<br><br>";
        }
        if (!isNameValid($_SESSION['name'])) {
            unset($_SESSION['name']);
            $err .= "Please enter a name that <br>
                                              1. consists of letters, hyphens, apostrophes and spaces<br> 
                                              2. contains no sequence of two or more of the characters hyphen and apostrophe<br> 
                                              3. starts with a letter or an apostrophe<br>
                                              4. does not end in a hyphen<br><br>";
        }
        if (!isPhoneValid($_SESSION['phone'])) {
            unset($_SESSION['phone']);
            $err .= "Please enter a phone number that<br> 
                                                      1. consists of digits and spaces<br> 
                                                      2. contains either nine or ten digits<br>
                                                      3. starts with the digit 0<br><br>";
        }
        return $err;
    }
    ?>

    <!-- Insert record and Update database -->
    <?php
    function insertBooking($pdo)
    {
        try {
            // Find sid and get current capacity for verification
            $pdo->beginTransaction();
            $stmt = $pdo->prepare("SELECT sid, capacity
                                   FROM Sessions
                                   WHERE cName = :cName AND day = :day AND period = :period;");
            $success = $stmt->execute(array(
                "cName" => $_SESSION['cName'],
                "day" => $_SESSION['day'],
                "period" => $_SESSION['period']
            ));
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            $sid = $row['sid'];
            $capacity = $row["capacity"] - 1;

            // If the chosen session is out of capacity, stop booking process
            if ($capacity < 0) {
                echo "<h3>" . $_SESSION['name'] . ", Insufficient Capacity for Your Session Choice!</h3>";
                $pdo->commit();
                return false;
            } else {
                // Insert valid booking record to Bookings
                $stmt = $pdo->prepare("INSERT INTO Bookings (cName, sid, name, phone)
        					           VALUES (:cName, :sid, :name, :phone)");
                $success = $stmt->execute(
                    array(
                        "cName" => $_SESSION['cName'],
                        "sid" => $sid,
                        "name" => $_SESSION['name'],
                        "phone" => $_SESSION['phone']
                    )
                );
                // Update capacity
                $stmt = $pdo->prepare("UPDATE Sessions SET capacity = :capacity WHERE sid = :sid");
                $success = $stmt->execute(array("capacity" => $capacity, "sid" => $sid));
                if ($success) {
                    echo "<h3>" . $_SESSION['name'] . ", your booking is confirmed!</h3>
                          Here is your booking information:<br>
                          Class Name: " . $_SESSION['cName'] . "<br>
                          Class Session: " . $_SESSION['period'] . " on " . $_SESSION['day'] . "<br>
                          Your Phone Number: " . $_SESSION['phone'] . "<br>";
                    $pdo->commit();
                    return true;
                } else {
                    echo "<h3>Something wrong with your booking.</h3>";
                    $pdo->commit();
                    return false;
                }
            }
        } catch (PDOException $e) {
            exit("PDO Error: " . $e->getMessage() . "<br>");
            $pdo->rollBack();
        }
        $pdo = NULL;
    }
    ?>


    <!-- Main Logic -->
    <?php
    // Whether legal or not, assign the value to session if form is submitted
    if (isset($_POST['cName'])) {
        if ($_SESSION['cName'] != $_POST['cName']) {
            unset($_SESSION['day']);
            unset($_SESSION['period']);
        }
        $_SESSION['cName'] = $_POST['cName'];
    }
    if (isset($_POST['day'])) {
        if ($_SESSION['day'] != $_POST['day']) {
            unset($_SESSION['period']);
        }
        $_SESSION['day'] = $_POST['day'];
    }
    if (isset($_POST['period'])) {
        $_SESSION['period'] = $_POST['period'];
    }
    if (isset($_POST['name'])) {
        $_SESSION['name'] = $_POST['name'];
    }
    if (isset($_POST['phone'])) {
        $_SESSION['phone'] = $_POST['phone'];
    }

    // If user want to reset the info they have entered
    if (isset($_POST['reset'])) {
        session_unset();
        session_destroy();
        session_start();
    }

    // Make insertion to database
    if (isset($_POST['submit'])) {
        $err = isAllValid();
        if ($err == "") {
            $_SESSION['done'] = insertBooking($pdo);
        } else {
            $_SESSION['error'] = $err;
        }
    }

    // Display page
    if ($_SESSION['done'] != true) {
        $stmt = $pdo->query("SELECT DISTINCT Classes.cName FROM
                            Classes INNER JOIN Sessions ON
                            Classes.cName = Sessions.cName
                            WHERE Sessions.capacity > 0");
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        
        // If all sessions are booked, show FULL message.
        if ($row == null) {
            echo "<h3>Sorry, all gym sessions are full!</h3><br>";
        } else {
            // Drop-down menu, input box
            selectClass($pdo);
            selectDay($pdo);
            selectPeriod($pdo);
            inputName();
            inputPhone();
            inputButton();

            // Error messages
            if (isset($_SESSION['error']) && !empty($_SESSION['error'])) {
                echo "<h3>Invalid input: </h3>" . $_SESSION['error'];
            }
        }
    } else {
        // Destroy sessions after done
        session_unset();
        session_destroy();
    }

    ?>


    <!-- Current Gym Status -->
    <h2> Current Gym Status</h2>
    <?php
    $stmt = $pdo->query("SELECT * FROM
                             Classes INNER JOIN Sessions ON
                             Classes.cName = Sessions.cName");
    echo "<table>";
    echo "<tr><th>Name</th>
                  <th>Day</th>
     		      <th>Period</th>
	   		      <th>Capacity</th></tr>";
    foreach ($stmt as $row) {
        echo "<tr><td>" . $row['cName'] . "</td>" .
            "<td>" . $row['day'] . "</td>" .
            "<td>" . $row['period'] . "</td>" .
            "<td>" . $row['capacity'] . "</td></tr>";
    }
    echo "</table><br>";
    ?>
</body>

</html>