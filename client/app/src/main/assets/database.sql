DROP TABLE IF EXISTS contacts;
CREATE TABLE contacts(
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	android_contact_id INTEGER NOT NULL,
	name VARCHAR(30),
	is_synced INTEGER CHECK(is_synced IN (0,1)) NOT NULL DEFAULT 0
);

DROP TABLE IF EXISTS phone_numbers;
CREATE TABLE phone_numbers(
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	contact_id INTEGER NOT NULL,
	phone_number VARCHAR(20) NOT NULL,
	phone_type VARCHAR(10) NOT NULL,
	is_synced INTEGER CHECK(is_synced IN (0,1)) NOT NULL DEFAULT 0,
	FOREIGN KEY (contact_id) REFERENCES contacts (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TRIGGER after_contacts_update
AFTER UPDATE ON contacts
FOR EACH ROW BEGIN
UPDATE contacts SET is_synced = 0 WHERE id = OLD.id;
END;

CREATE TRIGGER after_phone_numbers_update
AFTER UPDATE ON phone_numbers
FOR EACH ROW BEGIN
UPDATE phone_numbers SET is_synced = 0 WHERE id = OLD.id;
END;