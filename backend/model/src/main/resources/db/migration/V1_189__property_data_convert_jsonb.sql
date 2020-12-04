update landlord.property set data =
    jsonb_build_object(
        'flatData', property.data
    );

