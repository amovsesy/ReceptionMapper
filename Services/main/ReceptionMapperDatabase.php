<?php 

class ReceptionMapperDatabase {
  //Fill in this information
  private $server = 'localhost';
  private $user = 'root';
  private $password = '';
  
  const database = 'receptionmapper';
  const testDatabase = 'receptionmappertest';
  
  private $conn;

  function __construct($debug = false) {
    $this->connect($debug);
  }

  private function connect($debug) {
    $this->conn = mysql_connect(
        $this->server,
        $this->user,
        $this->password);
    
    if (!$this->conn) {
      throw new DatabaseException('Could not connect: ' . mysql_error());
    }

    $database=$debug?self::testDatabase:self::database;

    if (!mysql_select_db($database)) {
      throw new DatabaseException('Unable to select ' . $database . ': '
          . mysql_error());
    }
  }

  public function selectUsers(array $param) {
    return $this->getRows($this->buildSelectQuery('User', $param));
  }

  public function selectNodes(array $param) {
    return $this->getRows($this->buildSelectQuery('Nodes', $param));
  }

  public function selectCarriers(array $param) {
    return $this->getRows($this->buildSelectQuery('Carrier', $param));
  }

  public function selectUser(array $param) {
    return mysql_fetch_array($this->query($this->buildSelectQuery('User', $param)));
  }

  public function selectGSS($level, array $param) {
    return $this->getRows($this->buildSelectQuery('GridLevel'.$level, $param));
  }
  
  public function insertGSS($level, array $param) {
    return $this->query($this->buildInsertQuery('GridLevel'.$level, $param));
  }

  public function updateGSS($level, array $param, array $values) {
    return $this->query($this->buildUpdateQuery('GridLevel'.$level, $param, $values));
  }

  public function insertCarrier(array $param) {
    if (strlen($param['name']) > 20) {
      throw new DatabaseException("Name can't be longer than 20 characters");
    }
    if (strlen($param['name']) <= 0) {
      throw new DatabaseException("Name has to be longer than 0 characters");
    }

    return $this->query($this->buildInsertQuery('Carrier', $param));
  }

  public function getNodeCount(array $param) {
    return $this->query($this->buildCountQuery('Nodes', $param));
  }

  public function insertUser(array $param) {
    if (strlen($param['username']) > 32) {
      throw new DatabaseException("Username can't be longer than 32 characters");
    }
    if (strlen($param['phoneID']) > 52) {
      throw new DatabaseException("Phone ID can't be longer than 52 characters");
    }

    return $this->query($this->buildInsertQuery('User', $param));
  }

  public function deleteUser(array $param) {
    return $this->query($this->buildDeleteQuery('User', $param));
  }

  private function getConnection() {
    return $this->conn;
  }

  private function query($query) {
    $res = mysql_query($query);
    if (!$res) {
      throw new DatabaseException(mysql_error());
    }
    return $res;
  }

  private function getRows($query) {
    $rows = array();
    $result = $this->query($query);

    while ($row = mysql_fetch_array($result)) {
      $rows[] = $row;
    }
    return $rows;
  }
  private function buildCountQuery($table, array $param) {
    $isFirst = TRUE;
    $query = "SELECT COUNT(*) FROM {$table}";
    
    foreach ($param as $key => $value) {
      if (!$isFirst) {
        $query .= " AND ";
      } else {
        $query .= " WHERE ";
      }
      
      $query .= $key . " = " . $value;
      $isFirst = FALSE;
    }
    return $query;
  }

  private function buildInsertQuery($table, array $param) {
    if (empty($param)) {
      throw new InvalidArgumentException("Need to insert something");
    }

    $isFirst = TRUE;
    $query = "INSERT INTO {$table} (";

    $names = '';
    $values = '';

    foreach ($param as $key => $value) {
      if (!$isFirst) {
        $names .= ",";
        $values .= ",";
      }
      $names .= '`' . $key . '`'; 
      $values .= $value;
      $isFirst = FALSE;
    }

    $query .= $names . ") VALUES (" . $values .")";
    
    return $query;
  }

  private function buildUpdateQuery($table, array $param, array $values) {
    $isFirst = TRUE;
    $query = "UPDATE {$table} SET ";

    foreach ($values as $key => $value) {
      if (!$isFirst) {
        $query .= ", ";
      }
      
      $query .= $key . " = " . $value;
      $isFirst = FALSE; 
    }
    $isFirst = TRUE;

    foreach ($param as $key => $value) {
      if (!$isFirst) {
        $query .= " AND ";
      } else {
        $query .= " WHERE ";
      }
      
      $query .= $key . " = " . $value;
      $isFirst = FALSE;
    }
    return $query;
  }

  private function buildSelectQuery($table, array $param) {
    $isFirst = TRUE;
    $query = "SELECT * FROM {$table}";

    foreach ($param as $key => $value) {
      if (!$isFirst) {
        $query .= " AND ";
      } else {
        $query .= " WHERE ";
      }
      
      $query .= $key . " = " . $value;
      $isFirst = FALSE;
    }
    return $query;
  }  
  
  private function buildDeleteQuery($table, array $param) {
    if (empty($param)) {
      throw new InvalidArgumentException("Param cannot be empty. Cannot delete a whole table");
    }

    $isFirst = TRUE;
    $query = "DELETE FROM {$table}";

    foreach ($param as $key => $value) {
      if (!$isFirst) {
        $query .= " AND ";
      } else {
        $query .= " WHERE ";
      }
      
      $query .= $key . " = " . $value;
      $isFirst = FALSE;
    }
    return $query;
  }
}

class DatabaseException extends Exception {
  private $myMessage;
  
  public function __construct($message, $code = 0, Exception $previous = null) {
    $this->myMessage = $message;

    parent::__construct($message, $code, $previous);
  }

  public function __toString() {
    return __CLASS__ . ": [{$this->code}]:{$this->message}\n";
  }
}


?>
