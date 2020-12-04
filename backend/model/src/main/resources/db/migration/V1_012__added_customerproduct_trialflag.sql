    
ALTER TABLE landlord.customerproduct
    ADD COLUMN trial boolean NOT NULL DEFAULT false;
    
ALTER TABLE propertysearcher.customerproduct
    ADD COLUMN trial boolean NOT NULL DEFAULT false;
