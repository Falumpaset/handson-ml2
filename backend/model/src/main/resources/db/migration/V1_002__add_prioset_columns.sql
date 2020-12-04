ALTER TABLE landlord.prioset ADD COLUMN name varchar(255);
ALTER TABLE landlord.prioset ADD COLUMN description text;
ALTER TABLE landlord.prioset ADD COLUMN locked boolean;
ALTER TABLE landlord.prioset ADD COLUMN template boolean;