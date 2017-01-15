-- phpMyAdmin SQL Dump
-- version 2.11.9.4
-- http://www.phpmyadmin.net
--
-- Host: mysql.receptionmapper.com
-- Generation Time: Jul 03, 2010 at 09:40 AM
-- Server version: 5.1.39
-- PHP Version: 5.2.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `receptionmapper`
--

-- --------------------------------------------------------

--
-- Table structure for table `Nodes`
--

CREATE TABLE IF NOT EXISTS `Nodes` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `latitude` float(8,5) NOT NULL,
  `longitude` float(8,5) NOT NULL,
  `network` varchar(10) NOT NULL,
  `signal-strength` tinyint(4) NOT NULL DEFAULT '-1',
  `client` varchar(20) NOT NULL,
  `phone` varchar(50) NOT NULL,
  `manufacturer` varchar(20) NOT NULL,
  `username` varchar(30) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=485 ;

--
-- Dumping data for table `Nodes`
--

INSERT INTO `Nodes` (`id`, `latitude`, `longitude`, `network`, `signal-strength`, `client`, `phone`, `manufacturer`, `username`, `time`) VALUES
(0000000049, 38.44354, -122.70706, '0', 0, '', 'PC36100', 'HTC', 'amphro', '2010-06-15 09:27:16'),
(0000000048, 38.43868, -122.70840, '0', 0, '', 'PC36100', 'HTC', 'amphro', '2010-06-15 09:26:55');

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `phoneID` varchar(50) NOT NULL,
  `password` varchar(40) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `phoneID` (`phoneID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

INSERT INTO `User` (`id`, `username`, `phoneID`, `password`, `email`) VALUES
(1, 'amphro', 'SomePhoneID', '', '');
