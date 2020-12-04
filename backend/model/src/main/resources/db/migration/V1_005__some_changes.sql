
ALTER TABLE landlord.productpermissionscheme
  ADD COLUMN id bigint NOT NULL;

ALTER TABLE landlord.productpermissionscheme
  ADD CONSTRAINT uq_pmgroups_01 UNIQUE (permissionscheme_id, product_id);

ALTER TABLE landlord.productpermissionscheme DROP CONSTRAINT productpermissionscheme_pkey;
ALTER TABLE landlord.productpermissionscheme ADD PRIMARY KEY (id);

ALTER TABLE landlord.addonproductpermissionscheme
  ADD COLUMN id bigint NOT NULL;

ALTER TABLE landlord.addonproductpermissionscheme
  ADD CONSTRAINT uq_pmproducts UNIQUE (permissionscheme_id, addonproduct_id);

ALTER TABLE landlord.addonproductpermissionscheme DROP CONSTRAINT addonproductpermissionscheme_pkey;
ALTER TABLE landlord.addonproductpermissionscheme ADD PRIMARY KEY (id);   