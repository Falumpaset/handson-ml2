DROP   TYPE IF EXISTS landlord.addontype CASCADE;
CREATE TYPE           landlord.addontype AS ENUM ('EMAILEDITOR', 'DATAINSIGHTS',
    'PORTALPUBLISH', 'SHORTLIST', 'BRANDING', 'IMPORT', 'HPMODULE', 'AGENT1');

ALTER TABLE landlord.addonproduct
    ADD COLUMN addontype landlord.addontype;