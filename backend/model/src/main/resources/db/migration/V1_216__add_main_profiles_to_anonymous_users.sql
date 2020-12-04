do
$$
    declare
        temprow record;

    begin
        FOR temprow IN
            SELECT * FROM propertysearcher.user_profile u where type = 'ANONYMOUS'
            LOOP
            if((select count(*) from propertysearcher.user_profile up where up.user_id = temprow.user_id and up.type = 'MAIN') = 0) THEN
                INSERT INTO propertysearcher.user_profile
                VALUES ((Select nextval('dictionary_seq') from public.dictionary_seq), temprow.user_id,
                        temprow.email,
                        'MAIN',
                        temprow.created,
                        temprow.updated,
                        temprow.address,
                        temprow.data,
                        null,
                        null);
                END IF;
            END LOOP;
    end
$$