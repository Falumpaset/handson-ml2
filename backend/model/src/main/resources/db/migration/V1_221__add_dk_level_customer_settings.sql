alter table landlord.customersettings
    add column dk_level_customer_settings jsonb;

update landlord.customersettings
set dk_level_customer_settings = '{
    "nameLevel": "DK1",
    "scoringLevel": "DK1",
    "portraitLevel": "DK1"
}';