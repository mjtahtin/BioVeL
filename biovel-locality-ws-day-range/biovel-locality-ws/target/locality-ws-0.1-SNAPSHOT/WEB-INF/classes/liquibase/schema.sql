-- This is nonsense of course... Use good types!
CREATE TABLE location
(
  occurrenceID varchar(255),
  continent varchar(32),
  locality varchar(255),
  country varchar(64),
  stateProvince varchar(255),
  county varchar(255),
  decimalLatitude varchar(32),
  decimalLongitude varchar(32),
  recordedBy varchar(255),
  year int(11),
  month int(11),
  day int(11)
);