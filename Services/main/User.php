<?php 

require_once("ReceptionMapperDatabase.php");

class User {
  private $database;

  private $username;
  private $phoneID;
  private $password;
  private $email;

  public function __construct($username, $phoneID, $password = '', $email = '', $db = null) {
    if ($db == null) {
      $this->database = new ReceptionMapperDatabase();
    } else {
      $this->database = $db;
    }

    $this->username = $username;
    $this->phoneID = $phoneID;
    $this->password = $password;
    $this->email = $email;
  }

  public function getUsername() {
    return $this->username;
  }

  public function setUsername($username) {
    $this->username = $username;
  }

  public function doesUsernameExist() {
    if ($this->username == null) {
      throw new InvalidArgumentException("Username is NULL");
    } 
    $user = $this->database->selectUser(array('username' => "'{$this->username}'"));
    
    if ($user) {
      return true;
    } 
    return false;
  }

  public function getPhoneID() {
    return $this->phoneID;
  }

  public function setPhoneID($phoneID) {
    $this->phoneID = $phoneID;
  }

  public function doesPhoneIDExist() {
    if ($this->phoneID == null) {
      throw new InvalidArgumentException("PhoneID is NULL");
    }
    $user = $this->database->selectUser(array('phoneID' => "'{$this->phoneID}'"));
    if ($user) {
      return true;
    } 
    return false;
  }

  public function createUser() {
    if ($this->username == null || $this->phoneID == null) {
      throw new InvalidArgumentException("Username or phoneID can't be null to create a user");
    }
    try {
      return $this->database->insertUser(array(
          'username' => "'{$this->username}'",
          'phoneID' => "'{$this->phoneID}'",
          'password' => "'{$this->password}'",
          'email' => "'{$this->email}'"));
    } catch (Exception $e) {
      return false;
    }
  }

  public function deleteUser() {
    if ($this->username == NULL || $this->phoneID == NULL) {
      throw new InvalidArgumentException("Username or phoneID can't be null to delete a user");
    }
    try {
      $this->database->deleteUser(array(
          'username' => "'{$this->username}'",
          'phoneID' => "'{$this->phoneID}'"));
      return true;
    } catch (Exception $e) {
      return false;
    }
  }
}

?>
