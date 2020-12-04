--
-- CREATE SCHEMA 			    public;


CREATE SEQUENCE  public.dictionary_seq
  INCREMENT 1
  START 1000001
  MINVALUE 1
  MAXVALUE 9223372036854775807
  CACHE 1;


CREATE SCHEMA 	if not exists		    constants;

CREATE TABLE constants.country
(
  id      bigint                    NOT NULL,
  name    character varying(255)    NOT NULL,
  iso_a2  character varying(2)      NOT NULL,
  locale  character varying(10)     NOT NULL,
  CONSTRAINT country_pkey PRIMARY KEY (id)
);

CREATE TABLE constants.region
(
  id            bigint                    NOT NULL,
  country_id    bigint                    NOT NULL,
  name          character varying(255)    NOT NULL,
  shortcode     character varying(10)     NOT NULL,
  CONSTRAINT pk_region PRIMARY KEY (id),
  CONSTRAINT fk_region_01 FOREIGN KEY (country_id)
  REFERENCES constants.country (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE constants.zipcode
(
  id              bigint                  NOT NULL,
  code            character varying(20),
  stateshortcode  character varying(10),
  region_id       bigint                  NOT NULL,
  CONSTRAINT zipcode_pkey PRIMARY KEY (id),
  CONSTRAINT fk_zipcode_01 FOREIGN KEY (region_id)
  REFERENCES constants.region (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);


CREATE TYPE           constants.currencytype AS ENUM ('EUR', 'USD', 'GBP');


CREATE SCHEMA 	if not exists    administration;


CREATE TYPE           administration.usertype AS ENUM ('DEVELOPEMENT', 'SALES');

CREATE TABLE administration."user"
(
  id        bigint NOT NULL,
  email     character varying(255) NOT NULL,
  password  character varying(255) NOT NULL,

  enabled   boolean NOT NULL DEFAULT false,
  expired   boolean NOT NULL DEFAULT false,
  locked    boolean NOT NULL DEFAULT false,

  lastlogin timestamp without time zone,
  created   timestamp without time zone,
  updated   timestamp without time zone,

  type      administration.usertype NOT NULL,

  CONSTRAINT user_pkey PRIMARY KEY (id),
  CONSTRAINT uq_user_email UNIQUE (email)
);


CREATE SCHEMA 	if not exists		    shared;


CREATE SCHEMA if not exists  landlord;


CREATE TYPE           shared.customerlocation AS ENUM (
  'DE', 'AT');


CREATE TYPE           shared.paymentmethod AS ENUM (
  'PAYPAL', 'STRIPE', 'INVOICE', 'DEFAULT');


CREATE TYPE           landlord.customertype AS ENUM (
  'PROPERTYMANAGEMENT', 'ESTATEAGENT', 'PRIVATE', 'OTHER');

CREATE TABLE landlord.customer
(
  id                    bigint                        NOT NULL,
  description           character varying(255),
  name                  character varying(255),
  taxid                 character varying(255),
  paymentmethods        shared.paymentmethod[]        NOT NULL,
  customertype          landlord.customertype         NOT NULL DEFAULT 'OTHER',
  invoiceemail          character varying(255)        NOT NULL,

  preferences           jsonb                         NOT NULL, -- [color,favicon,flat_bookings,FLAT_AFTERDEACTIVATE]
  files                 jsonb                         NOT NULL,
  address               jsonb,

  location              shared.customerlocation       NOT NULL,

  managementunits       integer,

  created               timestamp without time zone,
  updated               timestamp without time zone,
  CONSTRAINT customer_pkey PRIMARY KEY (id)
);


CREATE TYPE           landlord.usertype AS ENUM (
  'COMPANYADMIN', 'EMPLOYEE', 'HOTLINE');

CREATE TABLE landlord."user"
(
  id              bigint                 NOT NULL,
  email           character varying(255) NOT NULL,
  password        character varying(255),

  customer_id     bigint                 NOT NULL,

  enabled         boolean                NOT NULL DEFAULT false,
  expired         boolean                NOT NULL DEFAULT false,
  locked          boolean                NOT NULL DEFAULT false,

  lastlogin       timestamp without time zone,
  created         timestamp without time zone,
  updated         timestamp without time zone,

  preferences     jsonb                  NOT NULL, -- [IMMOCARDID]
  profile         jsonb                  NOT NULL,

  type            landlord. usertype     NOT NULL,

  CONSTRAINT user_pkey PRIMARY KEY (id),
  CONSTRAINT uq_user_email UNIQUE (email),
  CONSTRAINT fk_user_01 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.user_change_email
(
  id          bigint                      NOT NULL,
  token       character varying(255)      NOT NULL,
  user_id     integer                     NOT NULL,
  email       character varying(255)      NOT NULL,
  validuntil  timestamp without time zone,
  created     timestamp without time zone NOT NULL,
  updated     timestamp without time zone NOT NULL,
  CONSTRAINT user_change_email_pkey PRIMARY KEY (id),
  CONSTRAINT user_change_email_email_key UNIQUE (email),
  CONSTRAINT user_change_email_token_key UNIQUE (token),
  CONSTRAINT user_change_email_user_id_fkey FOREIGN KEY (user_id)
  REFERENCES landlord."user" (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.ftp_access
(
  id              bigint                 NOT NULL,
  userpassword    character varying(64)  NOT NULL,
  homedirectory   character varying(255) NOT NULL,
  enabled         boolean                NOT NULL DEFAULT true,
  writepermission boolean                NOT NULL DEFAULT false,
  idletime        bigint                 NOT NULL DEFAULT 100,
  uploadrate      bigint                 NOT NULL DEFAULT -1,
  downloadrate    bigint                 NOT NULL DEFAULT 0,
  maxloginnumber  bigint                 NOT NULL DEFAULT 1,
  maxloginperip   bigint                 NOT NULL DEFAULT 1,
  customer_id     bigint                 NOT NULL,
  CONSTRAINT ftp_access_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ftp_access_01 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.import_log
(
  id            bigint                        NOT NULL,
  created       timestamp without time zone,
  updated       timestamp without time zone,
  status        bigint                        NOT NULL DEFAULT 0,
  statusmessage character varying(255),
  total         bigint                        NOT NULL DEFAULT 0,
  current       bigint                        NOT NULL DEFAULT 0,
  customer_id   bigint                        NOT NULL,
  CONSTRAINT ftp_import_log_pkey PRIMARY KEY (id),
  CONSTRAINT chk_ftp_import_01 CHECK (total >= 0),
  CONSTRAINT chk_ftp_import_02 CHECK (current >= 0),
  CONSTRAINT chk_ftp_import_03 CHECK (current <= total),
  CONSTRAINT fk_ftp_import_log_01 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.credential
(
  id          bigint                  NOT NULL,
  customer_id bigint                  NOT NULL,
  portal      character varying(255),
  name        character varying(255),
  properties  jsonb                   NOT NULL,
  created     timestamp without time zone,
  updated     timestamp without time zone,
  CONSTRAINT credential_pkey PRIMARY KEY (id),
  CONSTRAINT fk_credential_01 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.prioset
(
  id          bigint  NOT NULL,
  customer_id bigint  NOT NULL,

  data        jsonb   NOT NULL,

  created     timestamp without time zone,
  updated     timestamp without time zone,

  CONSTRAINT prioset_pkey PRIMARY KEY (id),
  CONSTRAINT fk_prioset_01 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.property
(
  id          bigint  NOT NULL,
  customer_id bigint  NOT NULL,

  data        jsonb   NOT NULL,

  prioset_id  bigint  NOT NULL,

  created     timestamp without time zone,
  updated     timestamp without time zone,

  CONSTRAINT property_pkey PRIMARY KEY (id),
  CONSTRAINT fk_property_01 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_property_02 FOREIGN KEY (prioset_id)
  REFERENCES landlord.prioset (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE shared.discount
(
  id          bigint                        NOT NULL,
  name        character varying(255)        NOT NULL,
  startdate   timestamp without time zone   NOT NULL,
  enddate     timestamp without time zone   NOT NULL,
  created     timestamp without time zone,
  updated     timestamp without time zone,
  value       float                         NOT NULL,
  CONSTRAINT discount_pkey   PRIMARY KEY (id),
  CONSTRAINT chk_discount_01 CHECK (value >= 0 AND value <= 1)
);

CREATE TABLE landlord.coupon
(
  id          bigint                  NOT NULL,
  discount_id bigint                  NOT NULL,
  couponcode  character varying(25)   NOT NULL,
  occurence   integer,
  created     timestamp without time zone,
  updated     timestamp without time zone,
  CONSTRAINT coupon_pkey   PRIMARY KEY (id),
  CONSTRAINT fk_coupon_01  FOREIGN KEY (discount_id)
  REFERENCES shared.discount (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.couponusage
(
  id            bigint                      NOT NULL,
  coupon_id     bigint                      NOT NULL,
  user_id       bigint                      NOT NULL,
  description   character varying(255)      NOT NULL,
  created       timestamp without time zone,
  CONSTRAINT couponusage_pkey   PRIMARY KEY (id),
  CONSTRAINT fk_couponusage_01  FOREIGN KEY (coupon_id)
  REFERENCES landlord.coupon (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_couponusage_02  FOREIGN KEY (user_id)
  REFERENCES landlord.user (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE shared."right"
(
  id          bigint                  NOT NULL,
  description character varying(255)  NOT NULL,
  name        character varying(255)  NOT NULL,
  shortcode   character varying(255)  NOT NULL,
  "group"     character varying(255)  NOT NULL,
  created     timestamp without time zone,
  updated     timestamp without time zone,
  CONSTRAINT right_pkey PRIMARY KEY (id)
);

CREATE TABLE landlord."right"
(
  id          bigint                  NOT NULL,
  right_id    bigint                  NOT NULL,
  created       timestamp without time zone,
  CONSTRAINT right_pkey PRIMARY KEY (right_id),
  CONSTRAINT fk_right_01 FOREIGN KEY (right_id)
  REFERENCES shared.right (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.usertype_rights
(
  id          bigint                          NOT NULL,
  usertype    landlord.usertype               NOT NULL,
  right_id    bigint                          NOT NULL,
  created     timestamp without time zone,
  CONSTRAINT groups_rights_pkey PRIMARY KEY (usertype, right_id),
  CONSTRAINT fk_usertype_rights_01 FOREIGN KEY (right_id)
  REFERENCES landlord."right" (right_id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.permission_scheme
(
  id          bigint                  NOT NULL,
  description character varying(255)  NOT NULL,
  name        character varying(255)  NOT NULL,
  created timestamp without time zone,
  updated timestamp without time zone,
  CONSTRAINT permission_scheme_pkey PRIMARY KEY (id)
);

CREATE TABLE landlord.permissionscheme_rights
(
  id                    bigint            NOT NULL,
  permission_scheme_id  bigint            NOT NULL,
  right_id              bigint            NOT NULL,
  usertype              landlord.usertype NOT NULL,
  CONSTRAINT permissionscheme_rights_pkey PRIMARY KEY (permission_scheme_id, right_id, usertype),
  CONSTRAINT fk_permissionscheme_rights_01 FOREIGN KEY (right_id)
  REFERENCES landlord."right" (right_id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_permissionscheme_rights_02 FOREIGN KEY (permission_scheme_id)
  REFERENCES landlord.permission_scheme (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);


CREATE TYPE           shared.producttype AS ENUM ('SUBSCRIPTION', 'SINGLE_PURCHASE');

CREATE TABLE landlord.price
(
  id            bigint                  NOT NULL,

  fixedpart     double precision        NOT NULL,
  variablepart  double precision        NOT NULL,

  currency      constants.currencytype  NOT NULL,

  CONSTRAINT price_pkey PRIMARY KEY (id)
);


CREATE TYPE           shared.subscriptionperiod AS ENUM ('ONCE', 'WEEKLY', 'MONTHLY', 'YEARLY');

CREATE TABLE landlord.product
(
  id                 bigint                    NOT NULL,
  name               character varying(255)    NOT NULL,
  description        character varying(255)    NOT NULL,
  subscriptionperiod shared.subscriptionperiod NOT NULL,

  created timestamp without time zone,
  updated timestamp without time zone,
  CONSTRAINT product_pkey PRIMARY KEY (id)
);

CREATE TABLE landlord.addonproduct
(
  id          bigint                  NOT NULL,
  name        character varying(255)  NOT NULL,
  description character varying(255)  NOT NULL,
  producttype shared.producttype      NOT NULL,

  created timestamp without time zone,
  updated timestamp without time zone,
  CONSTRAINT addonproduct_pkey PRIMARY KEY (id)
);

CREATE TABLE landlord.productaddon
(
  id              bigint NOT NULL,
  product_id      bigint NOT NULL,
  addonproduct_id bigint NOT NULL,
  CONSTRAINT productaddon_pkey PRIMARY KEY (id),
  CONSTRAINT uq_productaddon_01 UNIQUE (product_id, addonproduct_id),
  CONSTRAINT fk_productaddon_01 FOREIGN KEY (product_id)
  REFERENCES landlord.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_productaddon_02 FOREIGN KEY (addonproduct_id)
  REFERENCES landlord.addonproduct (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.productprice
(
  id              bigint                  NOT NULL,
  product_id      bigint                  NOT NULL,
  price_id        bigint                  NOT NULL,
  location        shared.customerlocation NOT NULL,
  paymentmethods  shared.paymentmethod[]  NOT NULL,

  CONSTRAINT productprice_pkey PRIMARY KEY (id),
  CONSTRAINT uq_productprice_01 UNIQUE (product_id, location),
  CONSTRAINT fk_productprice_02 FOREIGN KEY (product_id)
  REFERENCES landlord.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_productprice_03 FOREIGN KEY (price_id)
  REFERENCES landlord.price (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.productaddonprice
(
  id                bigint                  NOT NULL,
  productaddon_id   bigint                  NOT NULL,
  price_id          bigint                  NOT NULL,
  location          shared.customerlocation NOT NULL,
  paymentmethods    shared.paymentmethod[]  NOT NULL,

  CONSTRAINT productaddonprice_pkey PRIMARY KEY (id),
  CONSTRAINT uq_productaddonprice_01 UNIQUE (productaddon_id, location),
  CONSTRAINT fk_productaddonprice_02 FOREIGN KEY (productaddon_id)
  REFERENCES landlord.productaddon (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_productaddonprice_03 FOREIGN KEY (price_id)
  REFERENCES landlord.price (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.customerproduct
(
  id          bigint NOT NULL,
  product_id  bigint NOT NULL,
  customer_id  bigint NOT NULL,

  duedate     timestamp without time zone,

  renew       boolean NOT NULL,

  created timestamp without time zone,
  updated timestamp without time zone,
  CONSTRAINT customerproduct_pkey PRIMARY KEY (id),
  CONSTRAINT fk_customerproduct_01 FOREIGN KEY (product_id)
  REFERENCES landlord.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_customerproduct_02 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.customeraddonproduct
(
  id                  bigint NOT NULL,

  customerproduct_id  bigint NOT NULL,
  addonproduct_id     bigint NOT NULL,

  renew               boolean NOT NULL,

  created timestamp without time zone,
  updated timestamp without time zone,
  CONSTRAINT customeraddonproduct_pkey PRIMARY KEY (id),
  CONSTRAINT fk_customeraddonproduct_01 FOREIGN KEY (customerproduct_id)
  REFERENCES landlord.customerproduct (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_customeraddonproduct_02 FOREIGN KEY (addonproduct_id)
  REFERENCES landlord.addonproduct (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.productpermissionscheme
(
  product_id            bigint NOT NULL,
  permissionscheme_id   bigint NOT NULL,

  CONSTRAINT productpermissionscheme_pkey PRIMARY KEY (product_id, permissionscheme_id),
  CONSTRAINT fk_productpermissionscheme_01 FOREIGN KEY (product_id)
  REFERENCES landlord.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_productpermissionscheme_02 FOREIGN KEY (permissionscheme_id)
  REFERENCES landlord.permission_scheme (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.addonproductpermissionscheme
(
  addonproduct_id            bigint NOT NULL,
  permissionscheme_id   bigint NOT NULL,

  CONSTRAINT addonproductpermissionscheme_pkey PRIMARY KEY (addonproduct_id, permissionscheme_id),
  CONSTRAINT fk_addonproductpermissionscheme_01 FOREIGN KEY (addonproduct_id)
  REFERENCES landlord.addonproduct (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_productpermissionscheme_02 FOREIGN KEY (permissionscheme_id)
  REFERENCES landlord.permission_scheme (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.discount_product
(
  discount_id   bigint NOT NULL,
  product_id    bigint NOT NULL,

  CONSTRAINT discount_product_pkey PRIMARY KEY (discount_id, product_id),
  CONSTRAINT fk_discount_product_01 FOREIGN KEY (discount_id)
  REFERENCES shared.discount (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_discount_product_02 FOREIGN KEY (product_id)
  REFERENCES landlord.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.discount_productaddon
(
  discount_id     bigint NOT NULL,
  productaddon_id bigint NOT NULL,

  CONSTRAINT discount_productaddon_pkey PRIMARY KEY (discount_id, productaddon_id),
  CONSTRAINT fk_discount_productaddon_01 FOREIGN KEY (discount_id)
  REFERENCES shared.discount (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_discount_productaddon_02 FOREIGN KEY (productaddon_id)
  REFERENCES landlord.productaddon (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.discount_customer
(
  discount_id bigint NOT NULL,
  customer_id bigint NOT NULL,

  CONSTRAINT discount_customer_pkey PRIMARY KEY (discount_id, customer_id),
  CONSTRAINT fk_discount_customer_01 FOREIGN KEY (discount_id)
  REFERENCES shared.discount (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_discount_customer_02 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);


CREATE TYPE           landlord.productlimitation AS ENUM (
  'FLAT', 'SOLVENCY', 'SUBDOMAIN', 'USER', 'BRANDING', 'EMAIL_EDITOR', 'IMPORT',
  'PORTALS', 'HINTS', 'UPDATES', 'NO_ADS', 'FLAT_RUNTIME', 'FLOORPLAN_SERVICE',
  'PHOTO_CONSULTING', 'D360_TOUR', 'HANDOVER_SERVICE', 'EXPOSE_SERVICE');

CREATE TABLE landlord.product_limitations
(
  id                  bigint                      NOT NULL,
  product_id          bigint                      NOT NULL,
  limitation          landlord.productlimitation  NOT NULL,
  limitation_value    character varying(255)      NOT NULL,
  CONSTRAINT product_limitations_pkey  PRIMARY KEY (id),
  CONSTRAINT fk_product_limitations_01 FOREIGN KEY (product_id)
  REFERENCES landlord.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.addonproduct_limitations
(
  id                  bigint                      NOT NULL,
  addonproduct_id     bigint                      NOT NULL,
  limitation          landlord.productlimitation  NOT NULL,
  limitation_value    character varying(255)      NOT NULL,
  CONSTRAINT addonproduct_limitations_pkey  PRIMARY KEY (id),
  CONSTRAINT fk_addonproduct_limitations_01 FOREIGN KEY (addonproduct_id)
  REFERENCES landlord.addonproduct (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

-- RECHTE
-- SUBDOMAIN, BRANDING, EMAIL_EDITOR, IMPORT, HINTS, NO_ADS

-- ANDERE
-- USER, UPDATES // PORTALS


CREATE TYPE           landlord.quotatype AS ENUM (
  'FLAT', 'SOLVENCY', 'FLAT_RUNTIME', 'FLOORPLAN_SERVICE', 'PHOTO_CONSULTING',
  'D360_TOUR', 'HANDOVER_SERVICE', 'EXPOSE_SERVICE');

CREATE TABLE landlord.quota
(
  id                  bigint                        NOT NULL,
  quantity            integer                       NOT NULL,
  duedate             timestamp without time zone   NOT NULL,
  customer_id         bigint                        NOT NULL,
  product_id          bigint                        NOT NULL,
  type                landlord.quotatype            NOT NULL,
  created             timestamp without time zone,
  updated             timestamp without time zone,
  CONSTRAINT quota_pkey  PRIMARY KEY (id),
  CONSTRAINT fk_quota_01 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_quota_02 FOREIGN KEY (product_id)
  REFERENCES landlord.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);


CREATE TYPE           shared.usagestatus AS ENUM ('PENDING', 'BOOKED');

CREATE TABLE landlord.usage
(
  id          bigint                      NOT NULL,
  quota_id    bigint                      NOT NULL,
  user_id     bigint                      NOT NULL,
  status      shared.usagestatus          NOT NULL,
  comment     text,
  created     timestamp without time zone,
  updated     timestamp without time zone,
  CONSTRAINT usage_pkey  PRIMARY KEY (id),
  CONSTRAINT fk_usage_01 FOREIGN KEY (user_id)
  REFERENCES landlord."user" (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_usage_02 FOREIGN KEY (quota_id)
  REFERENCES landlord.quota (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);


CREATE TYPE           shared.productbasketstatus AS ENUM (
  'PENDING', 'PROCESSING', 'ERROR', 'CANCELED', 'CHARGED');

CREATE TABLE landlord.productbasket
(
  id            bigint                        NOT NULL,
  customer_id   bigint                        NOT NULL,
  status        shared.productbasketstatus    NOT NULL,

  properties    jsonb,

  checkoutdate  timestamp without time zone,

  created     timestamp without time zone NOT NULL,
  updated     timestamp without time zone NOT NULL,

  CONSTRAINT productbasket_pkey  PRIMARY KEY (id),
  CONSTRAINT fk_productbasket_01 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.productbasket_product
(
  id                bigint  NOT NULL,
  productbasket_id  bigint  NOT NULL,
  product_id        bigint  NOT NULL,
  quantity          integer NOT NULL,

  created     timestamp without time zone NOT NULL,
  updated     timestamp without time zone NOT NULL,

  CONSTRAINT productbasket_product_pkey PRIMARY KEY (productbasket_id, product_id),
  CONSTRAINT fk_productbasket_product_01 FOREIGN KEY (productbasket_id)
  REFERENCES landlord.productbasket (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_productbasket_product_02 FOREIGN KEY (product_id)
  REFERENCES landlord.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE landlord.productbasket_productaddon
(
  id                bigint  NOT NULL,
  productbasket_id  bigint  NOT NULL,
  productaddon_id   bigint  NOT NULL,
  quantity          integer NOT NULL,

  created     timestamp without time zone NOT NULL,
  updated     timestamp without time zone NOT NULL,

  CONSTRAINT productbasket_productaddon_pkey PRIMARY KEY (productbasket_id, productaddon_id),
  CONSTRAINT fk_productbasket_producaddont_01 FOREIGN KEY (productbasket_id)
  REFERENCES landlord.productbasket (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_productbasket_productaddon_02 FOREIGN KEY (productaddon_id)
  REFERENCES landlord.productaddon (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE if not exists landlord.messagesource
(
  id          bigint                  NOT NULL,
  customer_id bigint                  NOT NULL,
  messagekey  character varying(255)  NOT NULL,
  locale      character varying(10)   NOT NULL,
  value       text                    NOT NULL,
  CONSTRAINT messagesource_pkey PRIMARY KEY (id),
  CONSTRAINT fk_messagesource_01 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);


CREATE TYPE           landlord.propertystate AS ENUM (
  'PENDING', 'ACTIVE', 'ERROR', 'DEACTIVATED');

CREATE TABLE landlord.propertyportal
(
  id          bigint                  NOT NULL,
  property_id bigint                  NOT NULL,
  portal      character varying(255)  NOT NULL,

  state       landlord.propertystate  NOT NULL,

  activated   timestamp without time zone,
  deactivated timestamp without time zone,

  created     timestamp without time zone NOT NULL,
  updated     timestamp without time zone NOT NULL,

  CONSTRAINT propertyportal_pkey PRIMARY KEY (id),
  CONSTRAINT fk_propertyportal_01 FOREIGN KEY (property_id)
  REFERENCES landlord.property (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);


CREATE SCHEMA 			    propertysearcher;

CREATE TABLE propertysearcher.customer
(
  id                  bigint                  NOT NULL,
  paymentmethods      shared.paymentmethod[]  NOT NULL,
  files               jsonb                   NOT NULL,
  location            shared.customerlocation NOT NULL,

  created timestamp without time zone,
  updated timestamp without time zone,
  CONSTRAINT customer_pkey PRIMARY KEY (id)
);


CREATE TYPE           propertysearcher.userstatus AS ENUM (
  'ANONYMOUS', 'REGISTERED');

CREATE TABLE propertysearcher."user"
(
  id          bigint                      NOT NULL,
  email       character varying(255)      NOT NULL,
  password    character varying(255),

  customer_id bigint NOT NULL,

  enabled     boolean                     NOT NULL DEFAULT false,
  expired     boolean                     NOT NULL DEFAULT false,
  locked      boolean                     NOT NULL DEFAULT false,

  status      propertysearcher.userstatus NOT NULL DEFAULT 'ANONYMOUS',

  lastlogin   timestamp without time zone,
  created     timestamp without time zone,
  updated     timestamp without time zone,

  profile jsonb                       NOT NULL,
  address     jsonb,

  CONSTRAINT user_pkey PRIMARY KEY (id),
  CONSTRAINT uq_user_email UNIQUE (email),
  CONSTRAINT fk_user_01 FOREIGN KEY (customer_id)
  REFERENCES propertysearcher.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.user_change_email
(
  id          bigint NOT NULL,
  token       character varying(255) NOT NULL,
  user_id     integer NOT NULL,
  email       character varying(255) NOT NULL,
  validuntil  timestamp without time zone,
  created     timestamp without time zone NOT NULL,
  updated    timestamp without time zone NOT NULL,
  CONSTRAINT user_change_email_pkey PRIMARY KEY (id),
  CONSTRAINT user_change_email_email_key UNIQUE (email),
  CONSTRAINT user_change_email_token_key UNIQUE (token),
  CONSTRAINT user_change_email_user_id_fkey FOREIGN KEY (user_id)
  REFERENCES propertysearcher."user" (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher."right"
(
  id      bigint                  NOT NULL,
  created timestamp without time zone,
  CONSTRAINT right_pkey PRIMARY KEY (id),
  CONSTRAINT fk_right_01 FOREIGN KEY (id)
  REFERENCES shared.right (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.permission_scheme
(
  id          bigint                  NOT NULL,
  description character varying(255)  NOT NULL,
  name        character varying(255)  NOT NULL,
  created timestamp without time zone,
  updated timestamp without time zone,
  CONSTRAINT permission_scheme_pkey PRIMARY KEY (id)
);

CREATE TABLE propertysearcher.permissionscheme_rights
(
  id                   bigint NOT NULL,
  permission_scheme_id bigint NOT NULL,
  right_id             bigint NOT NULL,
  CONSTRAINT permissionscheme_rights_pkey PRIMARY KEY (permission_scheme_id, right_id),
  CONSTRAINT fk_permissionscheme_rights_01 FOREIGN KEY (right_id)
  REFERENCES propertysearcher."right" (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_permissionscheme_rights_02 FOREIGN KEY (permission_scheme_id)
  REFERENCES propertysearcher.permission_scheme (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.searchprofile
(
  id          bigint NOT NULL,
  user_id     bigint NOT NULL,

  data        jsonb NOT NULL,

  created     timestamp without time zone,
  updated     timestamp without time zone,

  CONSTRAINT searchprofile_pkey PRIMARY KEY (id),
  CONSTRAINT fk_searchprofile_01 FOREIGN KEY (user_id)
  REFERENCES propertysearcher.user (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

-- product

CREATE TABLE propertysearcher.price
(
  id            bigint NOT NULL,

  fixedpart     double precision NOT NULL,
  variablepart  double precision NOT NULL,

  currency      constants.currencytype NOT NULL,
  CONSTRAINT price_pkey PRIMARY KEY (id)
);

CREATE TABLE propertysearcher.product
(
  id bigint NOT NULL,
  name character varying(255) NOT NULL,
  description character varying(255) NOT NULL,

  subscriptionperiod shared.subscriptionperiod NOT NULL,

  created timestamp without time zone,
  updated timestamp without time zone,
  CONSTRAINT product_pkey PRIMARY KEY (id)
);

CREATE TABLE propertysearcher.addonproduct
(
  id          bigint                  NOT NULL,
  name        character varying(255)  NOT NULL,
  description character varying(255)  NOT NULL,
  producttype shared.producttype      NOT NULL,

  created timestamp without time zone,
  updated timestamp without time zone,
  CONSTRAINT addonproduct_pkey PRIMARY KEY (id)
);

CREATE TABLE propertysearcher.productaddon
(
  id              bigint NOT NULL,
  product_id      bigint NOT NULL,
  productaddon_id bigint NOT NULL,
  CONSTRAINT productaddon_pkey PRIMARY KEY (id),
  CONSTRAINT uq_productaddon_01 UNIQUE (product_id, productaddon_id),
  CONSTRAINT fk_productaddon_01 FOREIGN KEY (product_id)
  REFERENCES propertysearcher.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_productaddon_02 FOREIGN KEY (productaddon_id)
  REFERENCES propertysearcher.addonproduct (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.productprice
(
  product_id      bigint                  NOT NULL,
  price_id        bigint                  NOT NULL,
  location        shared.customerlocation NOT NULL,
  paymentmethods  shared.paymentmethod[]  NOT NULL,

  CONSTRAINT productprice_pkey PRIMARY KEY (product_id, location),
  CONSTRAINT fk_productprice_01 FOREIGN KEY (product_id)
  REFERENCES propertysearcher.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_productprice_02 FOREIGN KEY (price_id)
  REFERENCES propertysearcher.price (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.productaddonprice
(
  productaddon_id   bigint                  NOT NULL,
  price_id          bigint                  NOT NULL,
  location          shared.customerlocation NOT NULL,
  paymentmethods    shared.paymentmethod[]  NOT NULL,

  CONSTRAINT productaddonprice_pkey PRIMARY KEY (productaddon_id, location),
  CONSTRAINT fk_productaddonprice_01 FOREIGN KEY (productaddon_id)
  REFERENCES propertysearcher.productaddon (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_productaddonprice_02 FOREIGN KEY (price_id)
  REFERENCES propertysearcher.price (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.customerproduct
(
  id          bigint NOT NULL,
  product_id  bigint NOT NULL,
  customer_id  bigint NOT NULL,

  duedate     timestamp without time zone,

  renew       boolean NOT NULL,

  created timestamp without time zone,
  updated timestamp without time zone,
  CONSTRAINT customerproduct_pkey PRIMARY KEY (id),
  CONSTRAINT fk_customerproduct_01 FOREIGN KEY (product_id)
  REFERENCES propertysearcher.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_customerproduct_02 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.customeraddonproduct
(
  id                  bigint NOT NULL,

  customerproduct_id  bigint NOT NULL,
  addonproduct_id     bigint NOT NULL,

  renew               boolean NOT NULL,

  created timestamp without time zone,
  updated timestamp without time zone,
  CONSTRAINT customeraddonproduct_pkey PRIMARY KEY (id),
  CONSTRAINT fk_customeraddonproduct_01 FOREIGN KEY (customerproduct_id)
  REFERENCES propertysearcher.customerproduct (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_customeraddonproduct_02 FOREIGN KEY (addonproduct_id)
  REFERENCES propertysearcher.addonproduct (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.productpermissionscheme
(
  product_id            bigint NOT NULL,
  permissionscheme_id   bigint NOT NULL,

  CONSTRAINT productpermissionscheme_pkey PRIMARY KEY (product_id, permissionscheme_id),
  CONSTRAINT fk_productpermissionscheme_01 FOREIGN KEY (product_id)
  REFERENCES propertysearcher.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_productpermissionscheme_02 FOREIGN KEY (permissionscheme_id)
  REFERENCES propertysearcher.permission_scheme (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.addonproductpermissionscheme
(
  addonproduct_id       bigint NOT NULL,
  permissionscheme_id   bigint NOT NULL,

  CONSTRAINT addonproductpermissionscheme_pkey PRIMARY KEY (addonproduct_id, permissionscheme_id),
  CONSTRAINT fk_addonproductpermissionscheme_01 FOREIGN KEY (addonproduct_id)
  REFERENCES propertysearcher.addonproduct (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_productpermissionscheme_02 FOREIGN KEY (permissionscheme_id)
  REFERENCES propertysearcher.permission_scheme (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);


CREATE TABLE propertysearcher.coupon
(
  id          bigint                  NOT NULL,
  discount_id bigint                  NOT NULL,
  couponcode  character varying(25)   NOT NULL,
  occurence   integer,
  created     timestamp without time zone,
  updated     timestamp without time zone,
  CONSTRAINT coupon_pkey   PRIMARY KEY (id),
  CONSTRAINT fk_coupon_01  FOREIGN KEY (discount_id)
  REFERENCES shared.discount (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.couponusage
(
  id            bigint                      NOT NULL,
  coupon_id     bigint                      NOT NULL,
  user_id       bigint                      NOT NULL,
  description   character varying(255)      NOT NULL,
  created       timestamp without time zone,
  CONSTRAINT couponusage_pkey   PRIMARY KEY (id),
  CONSTRAINT fk_couponusage_01  FOREIGN KEY (coupon_id)
  REFERENCES propertysearcher.coupon (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_couponusage_02  FOREIGN KEY (user_id)
  REFERENCES propertysearcher.user (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.discount_product
(
  discount_id   bigint NOT NULL,
  product_id    bigint NOT NULL,

  CONSTRAINT discount_product_pkey PRIMARY KEY (discount_id, product_id),
  CONSTRAINT fk_discount_product_01 FOREIGN KEY (discount_id)
  REFERENCES shared.discount (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_discount_product_02 FOREIGN KEY (product_id)
  REFERENCES propertysearcher.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.discount_productaddon
(
  discount_id     bigint NOT NULL,
  productaddon_id bigint NOT NULL,

  CONSTRAINT discount_productaddon_pkey PRIMARY KEY (discount_id, productaddon_id),
  CONSTRAINT fk_discount_productaddon_01 FOREIGN KEY (discount_id)
  REFERENCES shared.discount (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_discount_productaddon_02 FOREIGN KEY (productaddon_id)
  REFERENCES propertysearcher.productaddon (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.discount_customer
(
  discount_id bigint NOT NULL,
  customer_id bigint NOT NULL,

  CONSTRAINT discount_customer_pkey PRIMARY KEY (discount_id, customer_id),
  CONSTRAINT fk_discount_customer_01 FOREIGN KEY (discount_id)
  REFERENCES shared.discount (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_discount_customer_02 FOREIGN KEY (customer_id)
  REFERENCES propertysearcher.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);


CREATE TYPE           propertysearcher.productlimitation AS ENUM ('SOLVENCY', 'NO_ADS');

CREATE TABLE propertysearcher.product_limitations
(
  id                  bigint                              NOT NULL,
  product_id          bigint                              NOT NULL,
  limitation          propertysearcher.productlimitation  NOT NULL,
  limitation_value    character varying(255)              NOT NULL,
  CONSTRAINT product_limitations_pkey  PRIMARY KEY (id),
  CONSTRAINT fk_product_limitations_01 FOREIGN KEY (product_id)
  REFERENCES propertysearcher.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.addonproduct_limitations
(
  id                  bigint                              NOT NULL,
  addonproduct_id     bigint                              NOT NULL,
  limitation          propertysearcher.productlimitation  NOT NULL,
  limitation_value    character varying(255)              NOT NULL,
  CONSTRAINT addonproduct_limitations_pkey  PRIMARY KEY (id),
  CONSTRAINT fk_addonproduct_limitations_01 FOREIGN KEY (addonproduct_id)
  REFERENCES propertysearcher.addonproduct (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

-- RECHTE
-- NO_ADS

-- ANDERE
-- not avaiable at the moment


CREATE TYPE           propertysearcher.quotatype AS ENUM (
  'SOLVENCY');

CREATE TABLE propertysearcher.quota
(
  id                  bigint                        NOT NULL,
  quantity            integer                       NOT NULL,
  duedate             timestamp without time zone   NOT NULL,
  customer_id         bigint                        NOT NULL,
  product_id          bigint                        NOT NULL,
  type                propertysearcher.quotatype    NOT NULL,
  CONSTRAINT quota_pkey  PRIMARY KEY (id),
  CONSTRAINT fk_quota_01 FOREIGN KEY (customer_id)
  REFERENCES propertysearcher.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_quota_02 FOREIGN KEY (product_id)
  REFERENCES propertysearcher.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.usage
(
  id          bigint                      NOT NULL,
  date        timestamp without time zone NOT NULL DEFAULT CURRENT_DATE,
  quota_id    bigint                      NOT NULL,
  user_id     bigint                      NOT NULL,
  status      shared.usagestatus          NOT NULL DEFAULT 'BOOKED',
  comment     text,
  CONSTRAINT usage_pkey  PRIMARY KEY (id),
  CONSTRAINT fk_usage_01 FOREIGN KEY (user_id)
  REFERENCES propertysearcher."user" (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_usage_02 FOREIGN KEY (quota_id)
  REFERENCES propertysearcher.quota (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.productbasket
(
  id            bigint                        NOT NULL,
  customer_id   bigint                        NOT NULL,
  status        shared.productbasketstatus    NOT NULL,

  properties    jsonb,

  checkoutdate  timestamp without time zone,

  created     timestamp without time zone NOT NULL,
  updated    timestamp without time zone NOT NULL,

  CONSTRAINT productbasket_pkey  PRIMARY KEY (id),
  CONSTRAINT fk_productbasket_01 FOREIGN KEY (customer_id)
  REFERENCES propertysearcher.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.productbasket_product
(
  id                bigint  NOT NULL,
  productbasket_id  bigint  NOT NULL,
  product_id        bigint  NOT NULL,
  quantity          integer NOT NULL,

  created     timestamp without time zone NOT NULL,
  updated     timestamp without time zone NOT NULL,

  CONSTRAINT productbasket_product_pkey PRIMARY KEY (productbasket_id, product_id),
  CONSTRAINT fk_productbasket_product_01 FOREIGN KEY (productbasket_id)
  REFERENCES propertysearcher.productbasket (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_productbasket_product_02 FOREIGN KEY (product_id)
  REFERENCES propertysearcher.product (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.productbasket_productaddon
(
  id                bigint  NOT NULL,
  productbasket_id  bigint  NOT NULL,
  productaddon_id   bigint  NOT NULL,
  quantity          integer NOT NULL,

  created     timestamp without time zone NOT NULL,
  updated     timestamp without time zone NOT NULL,

  CONSTRAINT productbasket_productaddon_pkey PRIMARY KEY (productbasket_id, productaddon_id),
  CONSTRAINT fk_productbasket_producaddont_01 FOREIGN KEY (productbasket_id)
  REFERENCES propertysearcher.productbasket (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_productbasket_productaddon_02 FOREIGN KEY (productaddon_id)
  REFERENCES propertysearcher.productaddon (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE SCHEMA IF NOT EXISTS shared;


CREATE TYPE           shared.applicationstate AS ENUM ('UNANSWERED', 'ACCEPTED', 'REJECTED');

CREATE TABLE shared.application
(
  id          bigint                  NOT NULL,
  score       float                   NOT NULL DEFAULT 0,
  state       shared.applicationstate NOT NULL DEFAULT 'UNANSWERED',
  marked      boolean                 NOT NULL DEFAULT false,

  property_id bigint                  NOT NULL,
  user_id     bigint                  NOT NULL,

  created     timestamp without time zone,
  updated     timestamp without time zone,

  CONSTRAINT application_pkey   PRIMARY KEY (id),
  CONSTRAINT uq_application_01  UNIQUE (property_id, user_id),
  CONSTRAINT fk_application_01 FOREIGN KEY (property_id)
  REFERENCES landlord.property (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_application_02 FOREIGN KEY (user_id)
  REFERENCES propertysearcher.user (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);


CREATE TYPE           shared.appointmentstate AS ENUM ('ACTIVE', 'CANCELED');

CREATE TABLE shared.appointment
(
  id                      bigint                        NOT NULL,
  date                    timestamp without time zone   NOT NULL,
  state                   shared.appointmentstate       NOT NULL DEFAULT 'ACTIVE',
  maxInviteeCount         int                           NOT NULL DEFAULT 0,
  showContactInformation  boolean                       NOT NULL DEFAULT false,

  property_id             bigint                        NOT NULL,

  created                 timestamp without time zone,
  updated                 timestamp without time zone,

  CONSTRAINT appointment_pkey  PRIMARY KEY (id),
  CONSTRAINT fk_appointment_01 FOREIGN KEY (property_id)
  REFERENCES landlord.property (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);


CREATE TYPE           shared.appointmentacceptancestate AS ENUM ('ACTIVE', 'CANCELED');

CREATE TABLE shared.appointment_acceptance
(
  id              bigint                             NOT NULL,
  state           shared.appointmentacceptancestate  NOT NULL DEFAULT 'ACTIVE',

  appointment_id  bigint					                   NOT NULL,
  user_id         bigint                             NOT NULL,

  created       timestamp without time zone,
  updated       timestamp without time zone,

  CONSTRAINT appointment_acceptance_pkey  PRIMARY KEY (id),
  CONSTRAINT uq_appointment_acceptance_01 UNIQUE (appointment_id, user_id),
  CONSTRAINT fk_appointment_acceptance_01 FOREIGN KEY (appointment_id)
  REFERENCES shared.appointment (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_appointment_acceptance_02 FOREIGN KEY (user_id)
  REFERENCES propertysearcher.user (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE shared.propertytenant
(
  id            bigint                  NOT NULL,
  property_id   bigint                  NOT NULL,
  user_id       bigint                  NOT NULL,

  contractstart timestamp without time zone,

  created       timestamp without time zone,

  CONSTRAINT propertytenant_pkey  PRIMARY KEY (id),
  CONSTRAINT uq_propertytenant_01 UNIQUE (property_id, user_id),
  CONSTRAINT fk_propertytenant_01 FOREIGN KEY (property_id)
  REFERENCES landlord.property (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_propertytenant_02 FOREIGN KEY (user_id)
  REFERENCES propertysearcher.user (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE shared.comments
(
  id            bigint NOT NULL,
  ps_user_id    bigint NOT NULL,
  ll_user_id    bigint NOT NULL,

  comment       text   NOT NULL,

  created       timestamp without time zone,
  updated       timestamp without time zone,

  CONSTRAINT comments_pkey  PRIMARY KEY (id),
  CONSTRAINT fk_comments_01 FOREIGN KEY (ps_user_id)
  REFERENCES propertysearcher.user (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_comments_02 FOREIGN KEY (ll_user_id)
  REFERENCES landlord.user (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);
