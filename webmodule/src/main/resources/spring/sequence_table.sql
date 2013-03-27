

CREATE TABLE IF NOT EXISTS sequences (
  name varchar(30) NOT NULL,
  value int(10) unsigned DEFAULT '0',
  PRIMARY KEY (name)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT IGNORE INTO sequences(name, value) VALUES('RESOURCE_SEQ', 0);

