update landlord.customersettings
set display_itp_flags = jsonb_build_object('dmvCard', true, 'shopCard', true, 'schufaCard', true, 'whitelabel', true,
                                           'sendRegisterMail', true)
where display_itp_flags is null;