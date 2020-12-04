ALTER TABLE landlord.customersettings
ADD COLUMN branding_themes jsonb;

ALTER TABLE landlord.customersettings
    ADD COLUMN logo jsonb;

ALTER TABLE landlord.customersettings
    ADD COLUMN login_background jsonb;
