#
# TABLE STRUCTURE FOR: sb_admin
#

DROP TABLE IF EXISTS `sb_admin`;

CREATE TABLE `sb_admin` (
  `AdminID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `Username` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `CreatedAt` datetime NOT NULL,
  `UpdatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `IsDeleted` enum('1','0') NOT NULL DEFAULT '0' COMMENT '1-Deleted,0-Active',
  PRIMARY KEY (`AdminID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `sb_admin` (`AdminID`, `Username`, `Email`, `Password`, `CreatedAt`, `UpdatedAt`, `IsDeleted`) VALUES ('1', 'Admin', 'admin@ilaxo.com', 'd033e22ae348aeb5660fc2140aec35850c4da997', '2016-02-20 00:00:00', '2016-04-14 09:16:42', '0');


#
# TABLE STRUCTURE FOR: sb_messages
#

DROP TABLE IF EXISTS `sb_messages`;

CREATE TABLE `sb_messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `message` blob NOT NULL,
  `add_date` datetime NOT NULL,
  `is_read` int(11) NOT NULL,
  `IsDelete` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

INSERT INTO `sb_messages` (`id`, `sender_id`, `receiver_id`, `message`, `add_date`, `is_read`, `IsDelete`) VALUES ('12', '1', '8', 'Hi this is 1st testing message', '2016-02-27 06:39:08', '0', '0');
INSERT INTO `sb_messages` (`id`, `sender_id`, `receiver_id`, `message`, `add_date`, `is_read`, `IsDelete`) VALUES ('11', '1', '10', 'Hi this is 1st testing message', '2016-02-27 06:39:05', '0', '0');
INSERT INTO `sb_messages` (`id`, `sender_id`, `receiver_id`, `message`, `add_date`, `is_read`, `IsDelete`) VALUES ('10', '1', '10', 'Hi this is 1st testing message', '2016-02-27 06:33:24', '0', '0');


#
# TABLE STRUCTURE FOR: sb_users
#

DROP TABLE IF EXISTS `sb_users`;

CREATE TABLE `sb_users` (
  `UserID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `SessionID` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Username` varchar(100) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `Birthdate` date NOT NULL,
  `Gender` enum('Male','Female') NOT NULL DEFAULT 'Male' COMMENT 'Male,Female',
  `Latitude` decimal(10,8) NOT NULL,
  `Longitude` decimal(11,8) NOT NULL,
  `CreatedAt` datetime NOT NULL,
  `UpdatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `IsDeleted` enum('1','0') NOT NULL DEFAULT '0' COMMENT '1-Deleted,0-Active',
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

INSERT INTO `sb_users` (`UserID`, `SessionID`, `Email`, `Username`, `Password`, `Birthdate`, `Gender`, `Latitude`, `Longitude`, `CreatedAt`, `UpdatedAt`, `IsDeleted`) VALUES ('1', 'fj3zas7uptc6gib1dnv9y', '', 'hardik patel', 'ebc4706e4b03a7b90127a364ee9de4ba730a406d', '1991-05-30', 'Male', '10.56700000', '20.56700000', '2016-02-19 11:46:45', '2016-02-27 11:23:28', '1');
INSERT INTO `sb_users` (`UserID`, `SessionID`, `Email`, `Username`, `Password`, `Birthdate`, `Gender`, `Latitude`, `Longitude`, `CreatedAt`, `UpdatedAt`, `IsDeleted`) VALUES ('2', '', '', 'hardik4', '123456', '0000-00-00', 'Female', '10.56700000', '20.56700000', '2016-02-19 11:48:17', '2016-02-23 11:13:05', '0');
INSERT INTO `sb_users` (`UserID`, `SessionID`, `Email`, `Username`, `Password`, `Birthdate`, `Gender`, `Latitude`, `Longitude`, `CreatedAt`, `UpdatedAt`, `IsDeleted`) VALUES ('3', '', '', 'hardik2', '7c4a8d09ca3762af61e59520943dc26494f8941b', '1991-05-18', 'Female', '10.10000000', '20.20000000', '2016-02-19 11:52:18', '2016-02-23 11:13:09', '0');
INSERT INTO `sb_users` (`UserID`, `SessionID`, `Email`, `Username`, `Password`, `Birthdate`, `Gender`, `Latitude`, `Longitude`, `CreatedAt`, `UpdatedAt`, `IsDeleted`) VALUES ('4', '', 'hardik3@gmail.com', 'hardik3', '7c4a8d09ca3762af61e59520943dc26494f8941b', '1991-05-18', 'Male', '10.10000000', '20.20000000', '2016-02-19 12:28:15', '2016-02-25 08:12:08', '0');
INSERT INTO `sb_users` (`UserID`, `SessionID`, `Email`, `Username`, `Password`, `Birthdate`, `Gender`, `Latitude`, `Longitude`, `CreatedAt`, `UpdatedAt`, `IsDeleted`) VALUES ('5', '', 'hadik1@gmail.com', 'hardik1', 'ebc4706e4b03a7b90127a364ee9de4ba730a406d', '2016-02-20', 'Male', '0.00000000', '0.00000000', '2016-02-22 07:55:08', '2016-02-25 08:11:54', '0');
INSERT INTO `sb_users` (`UserID`, `SessionID`, `Email`, `Username`, `Password`, `Birthdate`, `Gender`, `Latitude`, `Longitude`, `CreatedAt`, `UpdatedAt`, `IsDeleted`) VALUES ('6', '', 'hardik5@gmail.com', 'hardik5', 'ebc4706e4b03a7b90127a364ee9de4ba730a406d', '2016-03-01', 'Male', '0.00000000', '0.00000000', '2016-02-22 07:59:45', '2016-02-25 08:11:45', '0');
INSERT INTO `sb_users` (`UserID`, `SessionID`, `Email`, `Username`, `Password`, `Birthdate`, `Gender`, `Latitude`, `Longitude`, `CreatedAt`, `UpdatedAt`, `IsDeleted`) VALUES ('7', '', 'hardik6@gmail.com', 'hardik6', 'da39a3ee5e6b4b0d3255bfef95601890afd80709', '2016-02-25', 'Female', '0.00000000', '0.00000000', '2016-02-22 08:06:30', '2016-02-25 12:41:33', '1');
INSERT INTO `sb_users` (`UserID`, `SessionID`, `Email`, `Username`, `Password`, `Birthdate`, `Gender`, `Latitude`, `Longitude`, `CreatedAt`, `UpdatedAt`, `IsDeleted`) VALUES ('8', '', '', 'hardik', '7c4a8d09ca3762af61e59520943dc26494f8941b', '1991-05-18', 'Male', '10.10000000', '20.20000000', '2016-02-22 10:39:10', '2016-02-27 11:21:53', '1');
INSERT INTO `sb_users` (`UserID`, `SessionID`, `Email`, `Username`, `Password`, `Birthdate`, `Gender`, `Latitude`, `Longitude`, `CreatedAt`, `UpdatedAt`, `IsDeleted`) VALUES ('9', '3snvy8jbgexqkm69h2zwu', 'ilaxo.hardik@gmail.com', 'hardik', '7c4a8d09ca3762af61e59520943dc26494f8941b', '1991-05-18', 'Male', '10.10000000', '20.10000000', '2016-02-22 11:41:39', '2016-02-23 09:11:42', '0');
INSERT INTO `sb_users` (`UserID`, `SessionID`, `Email`, `Username`, `Password`, `Birthdate`, `Gender`, `Latitude`, `Longitude`, `CreatedAt`, `UpdatedAt`, `IsDeleted`) VALUES ('10', '', 'hardik7@gmail.com', 'hardik7', 'ebc4706e4b03a7b90127a364ee9de4ba730a406d', '2016-02-25', 'Male', '0.00000000', '0.00000000', '2016-02-22 11:48:13', '2016-02-27 11:03:21', '0');
INSERT INTO `sb_users` (`UserID`, `SessionID`, `Email`, `Username`, `Password`, `Birthdate`, `Gender`, `Latitude`, `Longitude`, `CreatedAt`, `UpdatedAt`, `IsDeleted`) VALUES ('11', '', 'hardik11@gmail.com', 'hardik11', 'ebc4706e4b03a7b90127a364ee9de4ba730a406d', '0000-00-00', 'Male', '0.00000000', '0.00000000', '2016-02-25 08:33:13', '2016-02-25 13:39:24', '0');


