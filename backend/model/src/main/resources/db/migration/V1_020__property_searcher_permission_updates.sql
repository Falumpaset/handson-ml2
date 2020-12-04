
ALTER TABLE propertysearcher.productpermissionscheme
  ADD COLUMN id bigint NOT NULL;

ALTER TABLE propertysearcher.productpermissionscheme
  ADD CONSTRAINT uq_ps_pmgroups_01 UNIQUE (permissionscheme_id, product_id);

ALTER TABLE propertysearcher.productpermissionscheme DROP CONSTRAINT productpermissionscheme_pkey;
ALTER TABLE propertysearcher.productpermissionscheme ADD PRIMARY KEY (id);

ALTER TABLE propertysearcher.addonproductpermissionscheme
  ADD COLUMN id bigint NOT NULL;

ALTER TABLE propertysearcher.addonproductpermissionscheme
  ADD CONSTRAINT uq_ps_pmproducts UNIQUE (permissionscheme_id, addonproduct_id);

ALTER TABLE propertysearcher.addonproductpermissionscheme DROP CONSTRAINT addonproductpermissionscheme_pkey;
ALTER TABLE propertysearcher.addonproductpermissionscheme ADD PRIMARY KEY (id);   