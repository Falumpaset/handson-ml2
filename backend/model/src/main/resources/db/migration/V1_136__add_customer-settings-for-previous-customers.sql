INSERT INTO landlord.customersettings ("id", "customer_id", "created", "updated")
    SELECT nextval('dictionary_seq'), id, NOW(), NOW()
    FROM landlord.customer ON CONFLICT DO NOTHING;
