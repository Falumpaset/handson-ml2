ALTER TABLE shared.propertytenant
ADD CONSTRAINT UC_property_tenant_1_2_1 UNIQUE (property_id);
