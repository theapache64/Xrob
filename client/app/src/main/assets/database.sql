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

DROP TABLE IF EXISTS command_statuses;
CREATE TABLE command_statuses(
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    command_id INTEGER NOT NULL,
    status VARCHAR(10) CHECK (status IN ('DELIVERED','FINISHED','FAILED')) NOT NULL,
    status_message TEXT NOT NULL,
    created_at TEXT NOT NULL
);

DROP TABLE IF EXISTS pending_deliveries;
CREATE TABLE pending_deliveries(
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    is_error INTEGER CHECK(is_error IN (0,1)) NOT NULL DEFAULT 0,
    data_type TEXT NOT NULL,
    data TEXT NOT NULL,
    message VARCHAR(100) NOT NULL,
    created_at TEXT NOT NULL
);

DROP TABLE IF EXISTS messages;
CREATE TABLE messages(
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    from_phone VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    _type VARCHAR(3) CHECK (_type IN ('in','out')) NOT NULL,
    created_at TEXT NOT NULL
);