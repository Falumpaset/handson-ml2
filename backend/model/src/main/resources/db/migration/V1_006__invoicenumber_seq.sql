DROP   SEQUENCE IF EXISTS public.invoicenumber_seq CASCADE;
CREATE SEQUENCE public.invoicenumber_seq
  INCREMENT 1
  START 1000001
  MINVALUE 1
  MAXVALUE 9223372036854775807
  CACHE 1;