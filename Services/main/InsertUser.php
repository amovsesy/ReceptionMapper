<?php 

require_once "User.php";

$username=$_GET['username'];
$phoneID=$_GET['phoneid'];

if (empty($username)) {
  $username = null;
} else {
  $username = rawurlencode($username);
}

if (empty($phoneID)) {
  $phoneID = null;
} else {
  $phoneID = rawurlencode($phoneID);
}

$user = new User($username, $phoneID);
$nameExist = false;
$idExist = false;

if ($username != null && $user->doesUsernameExist()) {
  echo "<username exist=\"true\" />";
  $nameExist = true;
} else {
  echo "<username exist=\"false\" />";
}

if ($phoneID != null && $user->doesPhoneIDExist()) {
  echo "<phoneid exist=\"true\" />";
  $idExist = true;
} else {
  echo "<phoneid exist=\"false\" />";
}

if ($username != null && $phoneID != null && !$nameExist && !$idExist) {
  if ($user->createUser()) {
    echo "<user created=\"true\" />";
  } else {
    echo "<user created=\"false\" />";
  }
} else {
  echo "<user created=\"false\" />";
}

?>

