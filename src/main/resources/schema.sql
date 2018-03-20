CREATE DATABASE IF NOT EXISTS `dms` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `dms`;

--
-- Table structure for table `admin`
--

CREATE TABLE IF NOT EXISTS `admin` (
  `adminId` int(11) NOT NULL AUTO_INCREMENT,
  `adminName` varchar(100) NOT NULL,
  `archive` tinyint(1) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(20) NOT NULL,
  `registerDate` date NOT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `username` varchar(40) NOT NULL,
  `address` longtext NOT NULL,
  `mobile` varchar(11) NOT NULL,
  PRIMARY KEY (`adminId`),
  UNIQUE KEY `UK_c0r9atamxvbhjjvy5j8da1kam` (`email`),
  UNIQUE KEY `UK_gfn44sntic2k93auag97juyij` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `authorities`
--

CREATE TABLE IF NOT EXISTS `authorities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `archive` tinyint(1) DEFAULT NULL,
  `authority` varchar(45) NOT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `archive` tinyint(1) DEFAULT NULL,
  `authority` varchar(45) NOT NULL,
  `email` varchar(100) NOT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  `password` varchar(20) NOT NULL,
  `photo` longtext,
  `username` varchar(45) NOT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;