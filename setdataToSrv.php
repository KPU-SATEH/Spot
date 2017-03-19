<?php
$servername = "ydpdp.cdv1b1jernsc.us-west-2.rds.amazonaws.com";

$username = "spot";

$password = "spot1234";

// Create connection
$conn = mysqli_connect($servername, $username, $password, "yourdrivingpattern");

//check connection

if(!$conn){
die("Connection failed : " .mysqli_connect_error());

mysqli_set_charset($conn, "utf8");

$UserNum = $_POST["UserNum"];
$DrivingDate = $_POST["DrivingDate"];
$DrivingNum = $_POST["DrivingNum"];
$DrivingTime = $_POST["DrivingTime"];
$DrivingDistance = $_POST["DrivingDistance"];
$HardAccNum = $_POST["HardAccNum"];
$HardStopNum = $_POST["HardStopNum"];
$QuickStartNum = $_POST["QuickStartNum"];
$EchoDriving = $_POST["EchoDriving"];
$RecklessDriving = $_POST["RecklessDriving"];
$DozeDriving = $_POST["DozeDriving"];

$result = mysqli_query($conn, "insert into DrivingData (UserNum, DrivingDate, DrivingNum,DrivingTime,DrivingDistance,HardAccNum,HardStopNum, QuickStartNum, EchoDriving, RecklessDriving, DozeDriving) values ('$UserNum','$DrivingDate','$DrivingNum','$DrivingTime','$DrivingDistance', '$HardAccNum', '$HardStopNum', '$QuickStartNum','$EchoDriving','$RecklessDriving','$DozeDriving')");

mysqli_close($conn);

?>
