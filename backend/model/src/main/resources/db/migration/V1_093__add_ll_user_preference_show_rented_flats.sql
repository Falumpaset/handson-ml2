UPDATE landlord.user
SET preferences = jsonb_set(preferences, '{showRentedFlats}', to_jsonb(true), TRUE);