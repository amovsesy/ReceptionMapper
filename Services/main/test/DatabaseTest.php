<?php 

require_once("Includes.php");

class DatabaseTest extends PHPUnit_Framework_TestCase {
  private $testUserName = "testInsertUserReceptionMapper";
  private $testUserPhoneID = "testInsertUserPhoneID";

  public function testDatabaseCreation() {
    try {
      $db = new ReceptionMapperDatabase();
    } catch(DatabaseException $e) {
      $this->assertTrue(FALSE, $e);
    }
    $this->assertTrue(TRUE);

    return $db;
  }

  /**
   * @depends testDatabaseCreation
   */
  public function testDatabaseException($db) {
    try {
      $db->selectUser(array('NotARealColumn' => "'SomeValue'"));
      $this->assertTrue(FALSE);
    } catch (DatabaseException $e) {
      $this->assertTrue(TRUE);
    }

    return $db;
  }

  /**
   * @depends testDatabaseCreation
   */
  public function testInsertUser($db) {
    $testUserInsert = array(
        'username' => "'{$this->testUserName}'", 
        'phoneID' => "'{$this->testUserPhoneID}'",
        'password' => "''",
        'email' => "''");
  
    try {
      $db->insertUser($testUserInsert);
    } catch (DatabaseException $e) {
      $this->assertTrue(FALSE, $e);
    }
    $this->assertTrue(TRUE);

    return $db;
  }

  /**
   * @depends testDatabaseCreation
   */
  public function testInsertInvalidUser($db) {
    $testInvalidName = array(
        'username' => "'0123456789012345678901234567891'", 
        'phoneID' => "'someTestInsertInvalidUser'",
        'password' => "''",
        'email' => "''");
    $testInvalidPhone = array(
        'username' => "'someTestInsertInvalidUser'", 
        'phoneID' => "'012345678901234567890123456789012345678901234567891'",
        'password' => "''",
        'email' => "''");
  
    try {
      $db->insertUser($testInvalidName);
      $db->deleteUser($testInvalidName);
      $this->assertTrue(FALSE, "Shouldn't insert: username is over 30 char: ");
    } catch (DatabaseException $e) {
      $this->assertTrue(TRUE);
    }

    try {
      $db->insertUser($testInvalidPhone);
      $db->deleteUser($testInvalidPhone);
      $this->assertTrue(FALSE, "Shouldn't insert: phoneID is over 50 char");
    } catch (DatabaseException $e) {
      $this->assertTrue(TRUE);
    }

    return $db;
  }

  /**
   * @depends testDatabaseCreation
   */
  public function testSelectInvalidUser($db) {
    $user1;
    $user2;
    try {
      $user1 = $db->selectUser(array('username' => "'RMTestSelectInvalidUser'"));
    } catch (DatabaseException $e) {
      $this->assertTrue(FALSE, $e);
    }
    try {
      $user2 = $db->selectUsers(array('username' => "'RMTestSelectInvalidUser'"));
    } catch (DatabaseException $e) {
      $this->assertTrue(FALSE, $e);
    }

    $this->assertFalse($user1);
    $this->assertTrue(empty($user2));
    
    return $db;
  }


  /**
   * @depends testInsertUser
   */
  public function testDuplicateUser($db) {
    $testDupName =  array(
        'username' => "'{$this->testUserName}'", 
        'phoneID' => "'Doesn't Matter'",
        'password' => "''",
        'email' => "''");

    $testDupPhone =  array(
        'username' => "'Doesn't Matter'", 
        'phoneID' => "'{$this->testUserPhoneID}'",
        'password' => "''",
        'email' => "''");
  
    try {
      $db->insertUser($testDupName);
      $this->assertTrue(FALSE, "Shouldn't allow dupplicate name in database");
    } catch (DatabaseException $e) {
      $this->assertTrue(TRUE);
    }
    try {
      $db->insertUser($testDupPhone);
      $this->assertTrue(FALSE, "Shouldn't allow dupplicate phone in database");
    } catch (DatabaseException $e) {
      $this->assertTrue(TRUE);
    }

    return $db;
  }


  /**
   * @depends testInsertUser
   */
  public function testSelectUsers($db) {
    $user1;
    $user2;

    try {
      $user1 = $db->selectUser(array('username' => "'{$this->testUserName}'"));
      $user2 = $db->selectUsers(array('username' => "'{$this->testUserName}'", 'phoneID' => "'{$this->testUserPhoneID}'"));
    } catch (DatabaseException $e) {
      $this->assertTrue(FALSE, $e);
    }
    $this->assertFalse(empty($user1));
    $this->assertFalse(empty($user2));
    $this->assertEquals($this->testUserPhoneID, $user1['phoneID']);
    $this->assertEquals($this->testUserPhoneID, $user2[0]['phoneID']);
    return $db;
  }

  /**
   * @depends testInsertUser
   */
  public function testDeleteUser($db) {
    $testUserDelete = array(
        'username' => "'{$this->testUserName}'",
        'phoneID' => "'{$this->testUserPhoneID}'");
    
    try {
      $db->deleteUser($testUserDelete);
    } catch (DatabaseException $e) {
      $this->assertTrue(FALSE, $e);
    }
    $this->assertTrue(TRUE);

    return $db;
  }

  /**
   * @depends testInsertUser
   */
  public function testDeleteAllUser($db) {
    try {
      $db->deleteUser(array());
      $this->assertTrue(FALSE, "Can't delete the whole user table");
    } catch (InvalidArgumentException $e) {
    $this->assertTrue(TRUE);
    }

    return $db;
  }

}
