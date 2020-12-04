UPDATE propertysearcher."user"
SET profile = jsonb_set(profile, '{furtherInformation}', '""', TRUE);