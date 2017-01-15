<?php

require_once 'PHPUnit/Framework.php';
$oldPath = set_include_path(get_include_path() . PATH_SEPARATOR . str_replace('test', '', getcwd()));
require_once 'ReceptionMapperDatabase.php';
//require_once 'Node.php';
require_once 'User.php';
set_include_path($oldPath);
?>
