<?php 

require_once("Includes.php");

class UserTest extends PHPUnit_Framework_TestCase {
  private $testUserName = "testInsertUserReceptionMapper";
  private $testUserPhoneID = "testInsertUserPhoneID";

  public function testUserCreation() {
    $user = new User($this->testUserName, $this->testUserPhoneID);

    $this->assertFalse($user->doesUsernameExist());
    $this->assertFalse($user->doesPhoneIDExist());
    $this->assertTrue($user->createUser());

    return $user;
  }

  /**
   * @depends testUserCreation
   */
  public function testUserExist($user) {
    $this->assertTrue($user->doesUsernameExist());
    $this->assertTrue($user->doesPhoneIDExist());
    $this->assertFalse($user->createUser());
    
    $user2 = new User($this->testUserName, $this->testUserPhoneID);
    $this->assertTrue($user2->doesUsernameExist());
    $this->assertTrue($user2->doesPhoneIDExist());
    $this->assertFalse($user2->createUser());

    return $user;
  }

  /**
   * @depends testUserCreation
   */
  public function testDeleteUser($user) {
    $this->assertTrue($user->deleteUser());
  }

  public function testUserExceptions() {
    $user1 = new User(null, null);
    $user2 = new User("test", null);

    try {
      $user1->doesUsernameExist();
      $this->assertTrue(FALSE, "Name is null, should throw an error");
    } catch (InvalidArgumentException $e) {
      $this->assertTrue(TRUE);
    }
    try {
      $user1->doesPhoneIDExist();
      $this->assertTrue(FALSE, "PhoneID is null, should throw an error");
    } catch (InvalidArgumentException $e) {
      $this->assertTrue(TRUE);
    }
    try {
      $user1->createUser();
      $this->assertTrue(FALSE, "Shouldn't create user if username or phoneid is null");
    } catch (InvalidArgumentException $e) {
      $this->assertTrue(TRUE);
    }
    try {
      $user2->createUser();
      $this->assertTrue(FALSE, "Shouldn't create user if username or phoneid is null");
    } catch (InvalidArgumentException $e) {
      $this->assertTrue(TRUE);
    }

  }
}
