insert into landlord.customer (id, description, name, taxid, paymentmethods, customertype, invoiceemail,
                               preferences, files, address, location, managementunits, created, updated, paymentdetails,
                               pricemultiplier, customersize)
values (1000016, 'Beschreibung', 'cnEdited', 'DE999999999', '[
  {
    "method": "INVOICE",
    "preferred": true
  }
]', 'OTHER', 'mbaumbach@immomio.de', '{}', '[]',
        '{
          "city": "",
          "region": "",
          "street": "",
          "country": "DE",
          "zipCode": "",
          "additional": null,
          "coordinates": null,
          "houseNumber": ""
        }', 'DE', 10000, NOW(), NOW(), '{}', 2, 'MEDIUM')
on conflict do nothing;

insert into landlord.customer (id, description, name, taxid, paymentmethods, customertype, invoiceemail,
                               preferences, files, address, location, managementunits, created, updated, paymentdetails,
                               pricemultiplier, customersize)
values (1, 'Beschreibung', 'cnEdited', 'DE999999999', '[
  {
    "method": "INVOICE",
    "preferred": true
  }
]', 'OTHER', 'nlindemann@immomio.de', '{}', '[]',
        '{
          "city": "",
          "region": "",
          "street": "",
          "country": "DE",
          "zipCode": "",
          "additional": null,
          "coordinates": null,
          "houseNumber": ""
        }', 'DE', 10000, NOW(), NOW(), '{}', 2, 'MEDIUM')
on conflict do nothing;

insert into landlord.customer (id, description, name, taxid, paymentmethods, customertype, invoiceemail,
                               preferences, files, address, location, managementunits, created, updated, paymentdetails,
                               pricemultiplier, customersize)
values (2, 'Beschreibung', 'cnEdited', 'DE999999999', '[
  {
    "method": "INVOICE",
    "preferred": true
  }
]', 'OTHER', 'fsawma@immomio.de', '{}', '[]',
        '{
          "city": "",
          "region": "",
          "street": "",
          "country": "DE",
          "zipCode": "",
          "additional": null,
          "coordinates": null,
          "houseNumber": ""
        }', 'DE', 10000, NOW(), NOW(), '{}', 2, 'MEDIUM')
on conflict do nothing;

insert into landlord.customer (id, description, name, taxid, paymentmethods, customertype, invoiceemail,
                               preferences, files, address, location, managementunits, created, updated, paymentdetails,
                               pricemultiplier, customersize)
values (3, 'Beschreibung', 'cnEdited', 'DE999999999', '[
  {
    "method": "INVOICE",
    "preferred": true
  }
]', 'OTHER', 'vnavozenko@immomio.de', '{}', '[]',
        '{
          "city": "",
          "region": "",
          "street": "",
          "country": "DE",
          "zipCode": "",
          "additional": null,
          "coordinates": null,
          "houseNumber": ""
        }', 'DE', 10000, NOW(), NOW(), '{}', 2, 'MEDIUM')
on conflict do nothing;

insert into landlord.customersettings (id, subdomain, created, updated, tenant_select_type, aareon_email,
                                       branding_themes, logo, login_background, mail_config, theme_url,
                                       search_until_interval_weeks, application_archive_unit,
                                       application_archive_amount, application_archive_active,
                                       contract_customer_settings)
values (1000016, null, null, null, null, null, null, null, null, null, null, null, 'MONTH', 6, false, '{
  "contractContactInfo": {},
  "contractDefaultSignerType": "TENANT",
  "continueContractWhenNotVisitedFlat": false
}')
on conflict do nothing;
insert into landlord.productbasket (id, customer_id, status, properties, checkoutdate, created, updated, trial)
values (1, 1000016, 'PROCESSING', null, null, now(), now(), false)
on conflict do nothing;
insert into landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
values (1, 1, 200011, 1, now(), now())
on conflict do nothing;

insert into landlord."user" (id, email, password, customer_id, enabled, expired, locked,
                             lastlogin, created, updated, preferences, profile, type)
values (1000017, 'mbaumbach@immomio.de', null, 1000016, true, false, false, null, NOW(), NOW(),
        '{}', '{
    "name": "Baumbach",
    "phone": "11 11 22 2222 222",
    "title": "DR",
    "gender": "MALE",
    "portrait": null,
    "firstname": "Maik"
  }', 'COMPANYADMIN')
on conflict do nothing;


insert into landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated,
                             preferences, profile, type)
values (1003757, 'userAbacki666@employee.de', null, 1000016, false, false, false, null, NOW(), NOW(), '{}',
        '{
          "name": "UserEmployee",
          "phone": null,
          "title": "NONE",
          "gender": "MALE",
          "portrait": null,
          "firstname": "Abacki"
        }', 'EMPLOYEE')
on conflict do nothing;


insert into landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated,
                             preferences, profile, type)
values (1003754, 'userAbacki3@employee.de', null, 1000016, false, false, false, null, NOW(), NOW(), '{}',
        '{
          "name": "UserEmployee",
          "phone": null,
          "title": "NONE",
          "gender": "MALE",
          "portrait": null,
          "firstname": "Abacki"
        }', 'EMPLOYEE')
on conflict do nothing;


insert into landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated,
                             preferences, profile, type)
values (1003751, 'userAbacki2@employee.de', null, 1000016, false, false, false, null, NOW(), NOW(), '{}',
        '{
          "name": "UserEmployee",
          "phone": "01936452",
          "title": "PROF_DR",
          "gender": "MALE",
          "portrait": null,
          "firstname": "Abacki"
        }', 'EMPLOYEE')
on conflict do nothing;


insert into landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated,
                             preferences, profile, type)
values (1003747, 'userAbacki@employee.de', null, 1000016, false, false, false, null, NOW(), NOW(), '{}',
        '{
          "name": "gql-name-changed",
          "phone": "111-gql-phone",
          "title": "PROF",
          "gender": "MALE",
          "portrait": null,
          "firstname": "gql-changed"
        }', 'EMPLOYEE')
on conflict do nothing;


insert into landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated,
                             preferences, profile, type)
values (1158305, 'kacper.frysztak@gmail.com', null, 1000016, true, false, false, null, NOW(), NOW(), '{}',
        '{
          "name": "Frysztak",
          "phone": "506132129",
          "title": null,
          "gender": null,
          "portrait": null,
          "firstname": "Kacper"
        }', 'EMPLOYEE')
on conflict do nothing;

insert into landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated,
                             preferences, profile, type)
values (1, 'nlindemann@immomio.de', null, 1, true, false, false, null, NOW(), NOW(), '{}',
        '{
          "name": "Lindemann",
          "phone": "506132129",
          "title": null,
          "gender": null,
          "portrait": null,
          "firstname": "Niklas"
        }', 'COMPANYADMIN')
on conflict do nothing;

insert into landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated,
                             preferences, profile, type)
values (2, 'fsawma@immomio.de', null, 2, true, false, false, null, NOW(), NOW(), '{}',
        '{
          "name": "Sawma",
          "phone": "506132129",
          "title": null,
          "gender": null,
          "portrait": null,
          "firstname": "Freddy"
        }', 'COMPANYADMIN')
on conflict do nothing;

insert into landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated,
                             preferences, profile, type)
values (3, 'vnavozenko@immomio.de', null, 3, true, false, false, null, NOW(), NOW(), '{}',
        '{
          "name": "Navozenko",
          "phone": "506132129",
          "title": null,
          "gender": null,
          "portrait": null,
          "firstname": "Valeriy"
        }', 'COMPANYADMIN')
on conflict do nothing;

insert into landlord.customerproduct (id, product_id, customer_id, duedate, renew, created, updated, trial)
values (1000771, 100001, 1000016, NOW() + interval '1 year', true, NOW(), NOW(), false)
on conflict do nothing;


insert into landlord.customeraddonproduct (id, customerproduct_id, addonproduct_id, renew, created, updated, expirydate)
values (1255053, 1000771, 200001, true, NOW(), NOW(), null)
on conflict do nothing;
insert into landlord.customeraddonproduct (id, customerproduct_id, addonproduct_id, renew, created, updated, expirydate)
values (1003725, 1000771, 200015, true, NOW(), NOW(), null)
on conflict do nothing;
insert into landlord.customeraddonproduct (id, customerproduct_id, addonproduct_id, renew, created, updated, expirydate)
values (1004022, 1000771, 200003, true, NOW(), NOW(), null)
on conflict do nothing;
insert into landlord.customeraddonproduct (id, customerproduct_id, addonproduct_id, renew, created, updated, expirydate)
values (1004024, 1000771, 200007, true, NOW(), NOW(), null)
on conflict do nothing;
insert into landlord.customeraddonproduct (id, customerproduct_id, addonproduct_id, renew, created, updated, expirydate)
values (1004026, 1000771, 200011, true, NOW(), NOW(), null)
on conflict do nothing;
insert into landlord.customeraddonproduct (id, customerproduct_id, addonproduct_id, renew, created, updated, expirydate)
values (1133974, 1000771, 200015, true, NOW(), NOW(), null)
on conflict do nothing;
insert into landlord.customeraddonproduct (id, customerproduct_id, addonproduct_id, renew, created, updated, expirydate)
values (1133984, 1000771, 200015, true, NOW(), NOW(), null)
on conflict do nothing;
insert into landlord.customeraddonproduct (id, customerproduct_id, addonproduct_id, renew, created, updated, expirydate)
values (1133985, 1000771, 200015, true, NOW(), NOW(), null)
on conflict do nothing;



insert into landlord.credential (id, customer_id, portal, name, properties, created, updated, encrypted)
values (1310327, 1000016, 'IMMOWELT_DE', 'Immowelt ', '{
  "PASSWORD": "yQdgUBTIL6dQUZFT7CV1iA==",
  "USERNAME": "Rh+RBhIJsvp8Ju3boUx6sA=="
}', NOW(), NOW(), true)
on conflict do nothing;
insert into landlord.credential (id, customer_id, portal, name, properties, created, updated, encrypted)
values (1338446, 1000016, 'IMMOBILIENSCOUT24_DE', 'immomio demo', '{
  "TOKEN": "8woeZ/gdyDYtxhYI+UL5rM+Ebf/R1KU/GI5Qa1z9YfzDjhEn1tgVnfZWwGlYLA9m",
  "TOKEN_SECRET": "wZ9DhMvrfmUBrxktCOEhlHy4RxTtPDwj+SQnlUH/IzhycVXcQDTvmF0pJiH26dxZ5C/oz6HEUBqcVm8pHKMS1DHKgKHWyIV7OugoEltbNrdcsl6S0U/4BqKZhYSOPCdhE4H98hh9cWaoq8ZzI+gNag=="
}', NOW(), NOW(), true)
on conflict do nothing;

insert into landlord.publish_log (id, property_id, agent_info, customer_id, error, portals, publish_state,
                                  property_task, created, updated)
values (1, 1084251, null, 1, null, null, 'SUCCESS', 'ACTIVATE', NOW(), NOW())
on conflict do nothing;

insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1028759, 1000016, '{
  "householdType": {
    "value": 10,
    "choice": null
  },
  "monthlyIncome": {
    "value": 10,
    "lowerBound": 1000,
    "upperBound": 10000
  },
  "employmentType": {
    "value": 10,
    "choice": null
  },
  "personalStatus": {
    "value": 10,
    "choice": null
  }
}', NOW(), NOW(), 'Postman-Prioset', 'Created with Postman', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1640202, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 10,
  "residents": {
    "value": 10,
    "number": 2
  },
  "householdType": {
    "value": 10,
    "choice": [
      "COUPLE_WITHOUT_CHILDREN"
    ]
  },
  "monthlyIncome": {
    "value": 10,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 7,
    "choice": [
      "SELF_EMPLOYED",
      "EMPLOYED_LIMITED",
      "EMPLOYED_UNLIMITED",
      "CIVIL_SERVANT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'Paar ohne Kinder', 'Voreinstellung für "Paare ohne Kinder"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1117520, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 0
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 4,
    "lowerBound": 2,
    "upperBound": 7
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Test Prioset', 'Test Prioset', false, true)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1335155, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 5,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 5
  },
  "householdType": {
    "value": 10,
    "choice": [
      "SINGLE_WITH_CHILDREN",
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 10,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 7,
    "choice": [
      "SELF_EMPLOYED",
      "EMPLOYED_LIMITED",
      "EMPLOYED_UNLIMITED",
      "CIVIL_SERVANT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'Familie', 'Voreinstellung für "Familien"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1028237, 1000016, '{
  "householdType": {
    "value": 10,
    "choice": null
  },
  "monthlyIncome": {
    "value": 10,
    "lowerBound": 1000,
    "upperBound": 10000
  },
  "employmentType": {
    "value": 10,
    "choice": null
  },
  "personalStatus": {
    "value": 10,
    "choice": null
  }
}', NOW(), NOW(), 'Postman-Prioset', 'Created with Postman', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1335253, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1444349, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Bitte Name eingeben', 'Voreinstellung für "Name eingeben"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1335254, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1335255, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1335256, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1444931, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Bitte Name eingeben', 'Voreinstellung für "Name eingeben"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1755343, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Bitte Name eingeben', 'Voreinstellung für "Name eingeben"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1335257, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1335258, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1335259, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1335260, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1335261, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1335262, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1336607, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1338049, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (2, 1000016, '{
  "age": {
    "value": 10,
    "lowerBound": 25,
    "upperBound": 45
  },
  "wbs": false,
  "music": null,
  "animals": 10,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 8,
    "number": 4
  },
  "householdType": {
    "value": 10,
    "choice": [
      "COUPLE_WITHOUT_CHILDREN"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 4,
    "upperBound": 9
  },
  "employmentType": {
    "value": 10,
    "choice": [
      "EMPLOYED_UNLIMITED",
      "EMPLOYED_LIMITED",
      "HOUSEHOLD_MANAGER"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'Test12', 'Test', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1338240, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1335058, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1325210, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1338349, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1453461, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Bitte Name eingeben', 'Voreinstellung für "Name eingeben"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1338448, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1445518, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Bitte Name eingeben', 'Voreinstellung für "Name eingeben"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1313426, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1445029, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Bitte Name eingeben', 'Voreinstellung für "Name eingeben"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1446196, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Bitte Name eingeben', 'Voreinstellung für "Name eingeben"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1757552, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Bitte Name eingeben', 'Voreinstellung für "Name eingeben"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1341440, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1344419, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Bitte Name eingeben', 'Voreinstellung für "Name eingeben"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1346628, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Bitte Name eingeben', 'Voreinstellung für "Name eingeben"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110576, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1084334, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 10,
  "smoking": 10,
  "children": 0,
  "residents": {
    "value": 7,
    "number": 5
  },
  "householdType": {
    "value": 4,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 4,
    "lowerBound": 0,
    "upperBound": 100000
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'undefined', null, false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1346725, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Bitte Name eingeben', 'Voreinstellung für "Name eingeben"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1831478, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Bitte Name eingeben', 'Voreinstellung für "Name eingeben"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1144061, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 0
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 4,
    "lowerBound": 2,
    "upperBound": 7
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Test Prioset3', 'Test Prioset', false, true)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110530, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": true,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 4,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 3,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 4,
    "choice": [
      "LOOKING_FOR_WORK",
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'dscfs', 'dsax', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110578, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110594, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110618, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110628, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110580, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110568, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110604, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110602, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110626, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1147151, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 2
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 6,
    "lowerBound": 2,
    "upperBound": 7
  },
  "employmentType": {
    "value": 0,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'Test Prioset', 'Test Prioset', false, false)
on conflict
    do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1334476, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110630, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110650, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110638, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110640, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110648, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110676, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110666, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110658, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": true,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 4,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 3,
    "lowerBound": 2,
    "upperBound": 7
  },
  "employmentType": {
    "value": 4,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'SADF', 'asdfgh', false, false)
on conflict do
    nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1110668, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 20,
    "upperBound": 50
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 3,
    "upperBound": 8
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'MyProfile', 'My profile description - automated testing', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1318142, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1318140, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1318143, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1318144, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1318145, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1324962, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 5,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 5
  },
  "householdType": {
    "value": 10,
    "choice": [
      "SINGLE_WITH_CHILDREN",
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 10,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 7,
    "choice": [
      "SELF_EMPLOYED",
      "EMPLOYED_LIMITED",
      "EMPLOYED_UNLIMITED",
      "CIVIL_SERVANT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'Familie', 'Voreinstellung für "Familien"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1670458, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Bitte Name eingeben', 'Voreinstellung für "Name eingeben"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1325156, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1174480, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1079931, 1000016, '{
  "age": {
    "value": 4,
    "lowerBound": 9,
    "upperBound": 14
  },
  "wbs": true,
  "music": 5,
  "animals": 5,
  "smoking": 5,
  "children": 5,
  "residents": {
    "value": 7,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "COUPLE_WITHOUT_CHILDREN",
      "SINGLE_WITH_CHILDREN",
      "FAMILY",
      "FLATSHARE",
      "SINGLE"
    ]
  },
  "monthlyIncome": {
    "value": 5,
    "lowerBound": 2,
    "upperBound": 2
  },
  "employmentType": {
    "value": 5,
    "choice": [
      "SELF_EMPLOYED",
      "EMPLOYED_UNLIMITED",
      "EMPLOYED_LIMITED",
      "STUDENT",
      "CIVIL_SERVANT",
      "HOUSEHOLD_MANAGER",
      "APPRENTICE",
      "RETIRED",
      "LOOKING_FOR_WORK"
    ]
  },
  "personalStatus": {
    "value": 5,
    "choice": [
      "SINGLE",
      "MARRIED",
      "PARTNERSHIP"
    ]
  }
}', NOW(), NOW(), 'Some test with template equals true', 'Some test with template equals true',
        false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1310908, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1176406, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1325352, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1177559, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1311006, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1117513, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 10,
  "residents": {
    "value": 10,
    "number": 2
  },
  "householdType": {
    "value": 10,
    "choice": [
      "COUPLE_WITHOUT_CHILDREN"
    ]
  },
  "monthlyIncome": {
    "value": 10,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 7,
    "choice": [
      "SELF_EMPLOYED",
      "EMPLOYED_LIMITED",
      "EMPLOYED_UNLIMITED",
      "CIVIL_SERVANT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'Paar ohne Kinder', 'Test', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1165265, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1311104, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1325929, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1313227, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1147149, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 2
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 6,
    "lowerBound": 2,
    "upperBound": 7
  },
  "employmentType": {
    "value": 0,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'Test Prioset', 'Test Prioset', false, false)
on conflict
    do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1147158, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "wbs": false,
  "music": null,
  "animals": 2,
  "smoking": 3,
  "children": 2,
  "residents": {
    "value": 0,
    "number": 2
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 6,
    "lowerBound": 2,
    "upperBound": 7
  },
  "employmentType": {
    "value": 0,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'Test Prioset', 'Test Prioset', false, false)
on conflict
    do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1170622, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1147150, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 2
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 6,
    "lowerBound": 2,
    "upperBound": 7
  },
  "employmentType": {
    "value": 0,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'Test Prioset', 'Test Prioset', false, false)
on conflict
    do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1313325, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1313327, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1310329, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1313424, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1166889, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1170874, 1000016, '{
  "age": {
    "value": 10,
    "lowerBound": 18,
    "upperBound": 30
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 5,
  "residents": {
    "value": 0,
    "number": 0
  },
  "householdType": {
    "value": 10,
    "choice": [
      "COUPLE_WITHOUT_CHILDREN"
    ]
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "employmentType": {
    "value": 10,
    "choice": [
      "STUDENT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'Studenten',
        'Voreinstellung für "Studenten"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1167024, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1313523, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1313717, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1154152, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 5,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 5
  },
  "householdType": {
    "value": 10,
    "choice": [
      "SINGLE_WITH_CHILDREN",
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 10,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 7,
    "choice": [
      "SELF_EMPLOYED",
      "EMPLOYED_LIMITED",
      "EMPLOYED_UNLIMITED",
      "CIVIL_SERVANT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'Familie', 'Voreinstellung für "Familien"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1157934, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 0,
    "upperBound": 0
  },
  "wbs": false,
  "music": null,
  "animals": 5,
  "smoking": 5,
  "children": 5,
  "residents": {
    "value": 0,
    "number": 3
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 3,
    "lowerBound": 0,
    "upperBound": 0
  },
  "employmentType": {
    "value": 3,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'testtt', 'tetete', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1318041, 1000016, '{
  "age": {
    "value": 5,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 8,
  "smoking": 3,
  "children": 5,
  "residents": {
    "value": 7,
    "number": 2
  },
  "householdType": {
    "value": 5,
    "choice": [
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 6,
    "choice": [
      "SELF_EMPLOYED",
      "EMPLOYED_UNLIMITED"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil',
        'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1154159, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 5,
  "children": 0,
  "residents": {
    "value": 5,
    "number": 5
  },
  "householdType": {
    "value": 10,
    "choice": [
      "SINGLE_WITH_CHILDREN",
      "FAMILY"
    ]
  },
  "monthlyIncome": {
    "value": 10,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 7,
    "choice": [
      "SELF_EMPLOYED",
      "EMPLOYED_LIMITED",
      "EMPLOYED_UNLIMITED",
      "CIVIL_SERVANT"
    ]
  },
  "personalStatus": null
}', NOW(), NOW(), 'Familie test1', 'Voreinstellung für "Familien"', false,
        false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1157942, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 0
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 0,
    "upperBound": 0
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), '4545', '345345', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1314006, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1317944, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Kein Wunschmieterprofil', 'Voreinstellung für "Kein Wunschmieterprofil"', false, false)
on conflict do nothing;
insert into landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
values (1348166, 1000016, '{
  "age": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 99
  },
  "wbs": false,
  "music": null,
  "animals": 0,
  "smoking": 0,
  "children": 0,
  "residents": {
    "value": 0,
    "number": 1
  },
  "householdType": {
    "value": 0,
    "choice": []
  },
  "monthlyIncome": {
    "value": 0,
    "lowerBound": 1,
    "upperBound": 5
  },
  "employmentType": {
    "value": 0,
    "choice": []
  },
  "personalStatus": null
}', NOW(), NOW(), 'Bitte Name eingeben', 'Voreinstellung für "Name eingeben"', false,
        false)
on conflict do nothing;


--Properties
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084253, 1000016, '{
  "name": "Schöne 3-Zimmer-Wohnung mit Blick ins Grüne - nur mit Wohnberechtigungsschein",
  "size": 80.99,
  "floor": 0,
  "rooms": 3.0,
  "garden": false,
  "ground": null,
  "heater": "GAS",
  "address": {
    "city": "Sylt",
    "region": "Schleswig-Holstein",
    "street": "Lüng Wai",
    "country": "DE",
    "zipCode": "25996",
    "additional": "",
    "coordinates": {
      "latitude": 0.0,
      "longitude": 0.0
    },
    "houseNumber": "8"
  },
  "balcony": false,
  "contact": {
    "name": "Putzler",
    "email": "hausbetreuung-putzler@t-online.de",
    "phone": null,
    "mobile": "0171-6253514",
    "address": {
      "city": null,
      "region": null,
      "street": null,
      "country": null,
      "zipCode": null,
      "additional": null,
      "coordinates": null,
      "houseNumber": null
    },
    "firstName": null
  },
  "bailment": 1800.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 600.13,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-861a2c00a351495bac951908ac3582a9.jpg",
      "type": "IMG",
      "title": "Scannen0004.jpg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "861a2c00a351495bac951908ac3582a9"
    }
  ],
  "barrierFree": false,
  "heatingCost": 180.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": true,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "Thu Sep 01 10:16:57 CEST 2016",
  "guestToilette": false,
  "serviceCharge": 110.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": "Die hier zu vermietende drei Zimmer Wohnung liegt in einer großzügigen Anlage bestehend aus 6 Häusern welche 2001 im Friesenstil erbaut wurden. \n\nDie Häuser liegen eingebettet zwischen Friesenwällen und großzügigen Gartenanlagen. Pro Hauseingang wohnen 4 Familien.",
  "basementAvailable": false,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": "78"
    },
    "demandCertificate": null,
    "yearOfConstruction": 2001,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "Die Wohnungen wurden 2001 aufwendig und hochwertig erbaut. Das Vollbad ist weiß gefliest. Die Küche ist mit einer Einbauküche ausgestattet. \n\nZu jeder Wohnung gehört ein Dachboden.",
  "objectLocationText": "Die Wohnanlage liegt an einer Privatstraße, zentral aber ruhig belegen. Der Golf Club Sylt grenzt direkt an den rückwärtigen Grundstücksteil. Der Ortskern von Wenningstedt ist ca. 1 km entfernt.",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": "Im Mietvertrag wird eine Staffelmiete vereinbart. Für die Anmietung ist ein Wohnberechtigungsschein notwendig."
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084251, 1000016, '{
  "name": "Gemütliche 1,5 Zi-DG-Wohnung mit Vollbad & Balkon in Eilbek",
  "size": 50.0,
  "floor": 3,
  "rooms": 1.5,
  "garden": false,
  "ground": "PARQUET",
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Ritterstraße",
    "country": "DE",
    "zipCode": "22089",
    "additional": null,
    "coordinates": null,
    "houseNumber": "76"
  },
  "balcony": true,
  "contact": null,
  "bailment": 1620.0,
  "elevator": false,
  "flatType": "ROOF_STOREY",
  "basePrice": 540.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-a4b7edf5eff642b5a3c798aceb91cb7d.jpg",
      "type": "IMG",
      "title": "Gemütliche 1,5 Zi-DG-Wohnung mit Vollbad & Balkon in Eilbek Bild1.jpeg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "a4b7edf5eff642b5a3c798aceb91cb7d"
    }
  ],
  "barrierFree": false,
  "heatingCost": 123.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": 123.0,
  "parkingSpace": false,
  "availableFrom": "Thu Sep 15 09:21:06 CEST 2016",
  "guestToilette": false,
  "serviceCharge": 150.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "FIRST_TIME_USE_AFTER_REFURBISHMENT",
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": "105"
    },
    "demandCertificate": null,
    "yearOfConstruction": 1957,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "LONG_DISTANCE"
  },
  "lastRefurbishment": null,
  "objectDescription": "Der Boden in Flur, Wohn- und Schlafzimmer ist mit Stäbchenparkett versehen. Der Wohnraum ist mit einer offenen Küche ausgestattet und verfügt über hohe Decken. Im Schlafzimmer sowie im Flur sind jeweils schmale Einbauschränke vorhanden. Von der Wohnung hat man direkten Zugang zum eigenem Dachboden mit viel Stauraum.",
  "objectLocationText": "Das Objekt liegt im Stadtteil Hamm-Nord an der Grenze zum Stadtteil Eilbek. Die nächste Bahnstationen sind die S1/S11 Landwehr und U1 Ritterstraße diese ist fußläufig in wenigen Minuten erreichbar die Fahrzeit zum Hauptbahnhof beträgt hier 5-10 Minuten. Alle Einkaufsmöglichkeiten für den täglichen Bedarf sind in unmittelbarer Nähe vorhanden.",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": "Die Kosten für die Wasser- und Stromversorgung sind nicht in den Betriebskosten enthalten und müssen somit vom Mieter direkt an die jeweiligen Versorgungsunternehmen gezahlt werden.\n\nIm Keller ist eine Gemeinschaftswaschmaschine vorhanden. Sollte die Nutzung der Waschmaschine gewünscht sein, sind monatlich pauschal € 10,00 an den Vermieter zu entrichten. Im Mietvertrag wird eine Indexmiete vereinbart.\n\nDie Kosten für das Kabelfernsehen sind in der Miete enthalten."
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084259, 1000016, '{
  "name": "Helle 2-Zimmerwohnung mit schönem Holzdielen",
  "size": 45.87,
  "floor": 1,
  "rooms": 2.0,
  "garden": false,
  "ground": "OTHER",
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Kraepelinweg",
    "country": "DE",
    "zipCode": "22081",
    "additional": "",
    "coordinates": {
      "latitude": 0.0,
      "longitude": 0.0
    },
    "houseNumber": "42"
  },
  "balcony": true,
  "contact": null,
  "bailment": 1209.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 403.2,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-c6239fc0906b4567936790ac7780f624.jpg",
      "type": "IMG",
      "title": "Ansicht Haus 40.JPG",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "c6239fc0906b4567936790ac7780f624"
    }
  ],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "Tue Nov 01 15:13:53 CET 2016",
  "guestToilette": false,
  "serviceCharge": 140.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": "Die Wohnung verfügt über einen schönen Dielenboden in den Wohnräumen. Ein hell gefliestes Duschbad mit Fenster ist vorhanden.\nDie große Küche verfügt über eine Einbauküchenzeile sowie Platz für einen Essbereich.\nEs sind zwei etwa gleich große Zimmer vorhanden, von denen das Zimmer ohne Balkon ein Durchgangszimmer ist.\nDer Balkon mit Südausrichtung bietet genug Platz um gemütlich den Feierabend ausklingen zu lassen.",
  "basementAvailable": true,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": "136",
      "energyEfficiencyClass": null,
      "includesHeatConsumption": true,
      "energyConsumptionParameter": "136"
    },
    "demandCertificate": null,
    "yearOfConstruction": 1939,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "LONG_DISTANCE"
  },
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": "Das Mehrfamilienhaus liegt in einer ruhigen Einbahnstraße im Stadtteil Barmbek-Süd. Es besteht eine gute Anbindung an den öffentlichen Nahverkehr (S-Bahn Friedrichsberg/U Dehnhaide sowie Buslinien). Der Hauptbahnhof ist mit den öffentlichen Verkehrsmitteln in 10 Minuten erreichbar.\nEinkaufsmöglichkeiten sind fußläufig zu erreichen.",
  "heatingCostIncluded": true,
  "furnishingDescription": null,
  "objectMiscellaneousText": "Die Kosten für das Kabelfernsehen sind in den Nebenkosten enthalten.\nWasser und Strom werden durch den Mieter direkt mit den Anbietern abgerechnet.\nIm Mietvertrag wird eine Indexmiete vereinbart."
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084249, 1000016, '{
  "name": "Schöne 2 Zi-Whg. im Schanzenviertel - IMPORTED",
  "size": 53.53,
  "floor": 0,
  "rooms": 2.0,
  "garden": false,
  "ground": "LAMINATE",
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Amandastraße",
    "country": "DE",
    "zipCode": "20357",
    "additional": null,
    "coordinates": null,
    "houseNumber": "66 a"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1873.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 624.34,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-d0209adf5f3245709c1b27688aac8a4e.jpg",
      "type": "IMG",
      "title": "Schöne 2 Zi-Whg. im Schanzenviertel - IMPORTED Bild1.jpeg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "d0209adf5f3245709c1b27688aac8a4e"
    }
  ],
  "barrierFree": false,
  "heatingCost": 50.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "Tue Nov 01 12:27:53 CET 2016",
  "guestToilette": false,
  "serviceCharge": 63.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": "",
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": {
      "endEnergyConsumption": "159.5",
      "energyEfficiencyClass": null
    },
    "yearOfConstruction": 1956,
    "energyCertificateType": "DEMAND_IDENTIFICATION",
    "primaryEnergyProvider": "LONG_DISTANCE"
  },
  "lastRefurbishment": null,
  "objectDescription": "Wohnung mit Vollbad und Abstellkammer in der Küche.",
  "objectLocationText": "Top Lage im Schanzenviertel. Einkaufsmöglichkeiten, Cafés, die öffentlichen Verkehrsmittel etc. sind fußläufig erreichbar.",
  "heatingCostIncluded": false,
  "furnishingDescription": "t",
  "objectMiscellaneousText": "Im Mietvertrag wird eine Indexmiete vereinbart."
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084222, 1000016, '{
  "name": "Zwischen Alster und Eppendorf, 3 Zimmer in Harvestehude suchen neuen Bewohner",
  "size": 92.0,
  "floor": 2,
  "rooms": 3.0,
  "garden": false,
  "ground": "PARQUET",
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "St. Benedictstraße",
    "country": "DE",
    "zipCode": "20149",
    "additional": "",
    "coordinates": {
      "latitude": 0.0,
      "longitude": 0.0
    },
    "houseNumber": "38"
  },
  "balcony": true,
  "contact": null,
  "bailment": 3552.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 1184.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-a6d455fae55c444281e8e17c6f12991c.jpg",
      "type": "IMG",
      "title": "IMG_0996.JPG",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "a6d455fae55c444281e8e17c6f12991c"
    }
  ],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": true,
  "availableFrom": "Wed Jun 01 07:31:55 CEST 2016",
  "guestToilette": true,
  "serviceCharge": 280.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": "Die zu vermietende Wohnung befindet sich in einer 1970 erbauten, laufend gepflegten Eigentumsanlage.\n\nDie Wohnung wurde Anfang 2000 umfangreich modernisiert. Die Elektroinstallationen, sowie das Vollbad und separate WC wurden komplett modernisiert und mit hochwertigen Fliesen, Sanitärobjekten und Möbeln ausgestattet. Eine vollständig ausgestattet Einbauküche mit Elektrogeräten rundet das Bild ab. Die Wohnung wird vor Übergabe an einen neuen Mieter komplett gestrichen - der Mietbeginn kann sich aufgrund dessen etwas verschieben.",
  "basementAvailable": true,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": true,
      "energyConsumptionParameter": "187.7"
    },
    "demandCertificate": null,
    "yearOfConstruction": 1991,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "Die Wohnung verfügt über einen schönen Balkon mit Blick ins Grüne.\n\nDie Wohnanlage verfügt über eine Tiefgarage. Ein Stellplatz kann für € 52,00/Monat separat mit angemietet werden.",
  "objectLocationText": "Die Wohnung befindet sich in einer der beliebtesten Wohngegenden Harvestehudes. Die Alster, der Bolivarpark und der Eppendorfer Baum sind fußläufig schnell zu erreichen.  \nDie Anbindung an die öffentlichen Nahverkehrsmittel ist gegeben.",
  "heatingCostIncluded": true,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110651, 1000016, '{
  "name": "5 Zimmer in Hebertshausen TO Immoscout",
  "size": 70.0,
  "floor": null,
  "rooms": 5.0,
  "garden": false,
  "ground": null,
  "heater": "OVEN",
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": true,
  "contact": null,
  "bailment": 20.0,
  "elevator": false,
  "flatType": "LOFT",
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-5938013906f34a3e9d3f855523f0b6e8.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen TO Immoscout Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "5938013906f34a3e9d3f855523f0b6e8"
    }
  ],
  "barrierFree": false,
  "heatingCost": 20.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-07-26",
  "guestToilette": false,
  "serviceCharge": 20.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": "1",
  "objectLocationText": "2",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1154844, 1000016, '{
  "name": "4 Zimmer in Zawiercie",
  "size": 60.0,
  "floor": null,
  "rooms": 4.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Zawiercie",
    "region": "śląskie",
    "street": "Pomorska",
    "country": "PL",
    "zipCode": "42-400",
    "additional": null,
    "coordinates": null,
    "houseNumber": "1"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 600.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 1154159, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1157935, 1000016, '{
  "name": "12312 Zimmer in Hamburg",
  "size": 60.0,
  "floor": null,
  "rooms": 12312.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Kieler Strasse  121, undefined Hamburg, DE",
    "country": "DE",
    "zipCode": "31-232",
    "additional": null,
    "coordinates": null,
    "houseNumber": "12"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 500.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 1157934, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1156002, 1000016, '{
  "name": "4 Zimmer in Zawiercie",
  "size": 60.0,
  "floor": null,
  "rooms": 4.0,
  "garden": false,
  "ground": null,
  "heater": "FLOOR",
  "address": {
    "city": "Zawiercie",
    "region": "śląskie",
    "street": "Pomorska",
    "country": "PL",
    "zipCode": "42-400",
    "additional": null,
    "coordinates": null,
    "houseNumber": "1"
  },
  "balcony": false,
  "contact": null,
  "bailment": 200.0,
  "elevator": false,
  "flatType": "MAISONETTE",
  "basePrice": 600.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [],
  "barrierFree": false,
  "heatingCost": 200.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-08-18",
  "guestToilette": false,
  "serviceCharge": 200.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": "50",
      "energyEfficiencyClass": "D",
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "DEMAND_IDENTIFICATION",
    "primaryEnergyProvider": "ELECTRO"
  },
  "lastRefurbishment": null,
  "objectDescription": "dsddsds",
  "objectLocationText": "dsdsdssd",
  "heatingCostIncluded": false,
  "furnishingDescription": "sddssd",
  "objectMiscellaneousText": "sdsd"
}', 1174480, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1335156, 1000016, '{
  "name": "9 Zimmer in Kraków",
  "size": 50.0,
  "floor": 2,
  "rooms": 9.0,
  "garden": false,
  "ground": "CARPET",
  "heater": "OVEN",
  "address": {
    "city": "Kraków",
    "region": "małopolskie",
    "street": "Dietla",
    "country": "PL",
    "zipCode": "22-222",
    "additional": null,
    "coordinates": null,
    "houseNumber": "12"
  },
  "balcony": true,
  "contact": null,
  "bailment": 50.0,
  "elevator": false,
  "flatType": "MAISONETTE",
  "basePrice": 300.0,
  "bathRooms": 2,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-0ceb1f680fba42fb878d395346b1974d.png",
      "type": "IMG",
      "title": "9 Zimmer in Kraków Bild1.png",
      "encrypted": false,
      "extension": "png",
      "identifier": "0ceb1f680fba42fb878d395346b1974d"
    }
  ],
  "barrierFree": false,
  "heatingCost": 50.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": 20.0,
  "parkingSpace": false,
  "availableFrom": "2018-08-24",
  "guestToilette": false,
  "serviceCharge": 50.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "FIRST_TIME_USE_AFTER_REFURBISHMENT",
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": "20",
      "energyEfficiencyClass": "A_P",
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": 1200,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": "pok",
  "objectLocationText": "pok",
  "heatingCostIncluded": false,
  "furnishingDescription": "pok",
  "objectMiscellaneousText": null
}', 1444349, NOW(), NOW(), 1000017, 'IMPORT_PROTECTED_ACTIVE', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084228, 1000016, '{
  "name": "schöne 2,5 Zi-Wohnung mit Garten",
  "size": 52.33,
  "floor": 1,
  "rooms": 3.0,
  "garden": true,
  "ground": "LAMINATE",
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Begonienweg",
    "country": "DE",
    "zipCode": "22047",
    "additional": "",
    "coordinates": {
      "latitude": 0.0,
      "longitude": 0.0
    },
    "houseNumber": "6"
  },
  "balcony": false,
  "contact": {
    "name": null,
    "email": "info@gcv-gmbh.de",
    "phone": "+4940226480",
    "mobile": null,
    "address": {
      "city": null,
      "region": null,
      "street": null,
      "country": null,
      "zipCode": null,
      "additional": null,
      "coordinates": null,
      "houseNumber": null
    },
    "firstName": null
  },
  "bailment": 1374.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 465.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-b23acbaa6b1640ec8ea656961441c644.jpg",
      "type": "IMG",
      "title": "IMG_1820.JPG",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "b23acbaa6b1640ec8ea656961441c644"
    }
  ],
  "barrierFree": false,
  "heatingCost": 90.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": true,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "Thu Dec 01 18:29:37 CET 2016",
  "guestToilette": false,
  "serviceCharge": 100.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": "Es handelt sich bei dem, Objekt um ein gepflegtes Mehrfamilienhaus in einer ruhigen Wohnanlage. Zu der Wohnung gehört ein abgetrennter Teil des Gartens, der von jedem Mieter selbst zu pflegen ist.",
  "basementAvailable": true,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": null,
    "demandCertificate": {
      "endEnergyConsumption": "179",
      "energyEfficiencyClass": null
    },
    "yearOfConstruction": 1930,
    "energyCertificateType": "DEMAND_IDENTIFICATION",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "Die Wohnung  befindet sich in einem gepflegten Zustand.  Ausgestattet ist die Wohnung mit einem schönen Laminat, einem hell gefliesten Vollbad mit Fenster und einem geräumigen Wohnzimmer. Die Küche ist mit einer Einbauküche ausgestattet, welche zur Nutzung überlassen wird.",
  "objectLocationText": "Das Objekt liegt in einer wenig befahrenen Seitenstraße in einer grünen Umgebung. Die nah gelegene Bushaltstelle bietet bei Bedarf eine gute Anbindung in die Hamburger Innenstadt. Einkaufsmöglichkeiten für den täglichen Bedarf befinden sich in direkter Umgebung.",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'UPDATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084226, 1000016, '{
  "name": "sonnige 2 Zi-DG-Wohnung in Eppendorf",
  "size": 53.5,
  "floor": 4,
  "rooms": 2.0,
  "garden": false,
  "ground": null,
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Schrammsweg",
    "country": "DE",
    "zipCode": "20249",
    "additional": null,
    "coordinates": null,
    "houseNumber": "25"
  },
  "balcony": false,
  "contact": null,
  "bailment": 2097.0,
  "elevator": false,
  "flatType": "ROOF_STOREY",
  "basePrice": 699.9,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-e326a258511d4389bfa65b6465dc3cb6.jpg",
      "type": "IMG",
      "title": "sonnige 2 Zi-DG-Wohnung in Eppendorf Bild1.jpeg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "e326a258511d4389bfa65b6465dc3cb6"
    }
  ],
  "barrierFree": false,
  "heatingCost": 50.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-08-12",
  "guestToilette": false,
  "serviceCharge": 65.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": 1900,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "LONG_DISTANCE"
  },
  "lastRefurbishment": null,
  "objectDescription": "Die Wohnräume sind mit Stäbchenparkett ausgestattet. Vom Flur geht ein Abstellraum ab. Die Wohnräume sind weiß gestrichen. Im Bad ist ein Waschmaschinenanschluss vorhanden.",
  "objectLocationText": "Das Objekt liegt in Eppendorf in der Nähe der Bahnstation Kellinghusenstraße (U1 & U3). Durch die Nähe zur Eppendorfer Landstraße und dem Eppendorfer Marktplatz, sind viele Einkaufmöglichkeiten sowie Cafés und Ärzte schnell fußläufig erreichbar.",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung sind direkt an die entsprechenden Unternehmen abzurechnen. Bei Vertragsabschluss ist eine Gebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen. Im Mietvertrag wird eine Indexmiete vereinbart."
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084245, 1000016, '{
  "name": "Schöne DG-Wohnung in Barmbek-Süd",
  "size": 62.25,
  "floor": 4,
  "rooms": 3.0,
  "garden": false,
  "ground": "OTHER",
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Kraepelinweg",
    "country": "DE",
    "zipCode": "22081",
    "additional": null,
    "coordinates": null,
    "houseNumber": "42"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1710.0,
  "elevator": false,
  "flatType": "ROOF_STOREY",
  "basePrice": 570.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-67e6ba4982544897b338ad22418c6ad5.jpg",
      "type": "IMG",
      "title": "Schöne DG-Wohnung in Barmbek-Süd Bild1.jpeg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "67e6ba4982544897b338ad22418c6ad5"
    }
  ],
  "barrierFree": false,
  "heatingCost": 113.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": true,
  "availableFrom": "Sat Oct 01 16:05:26 CEST 2016",
  "guestToilette": true,
  "serviceCharge": 180.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": 1939,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "LONG_DISTANCE"
  },
  "lastRefurbishment": null,
  "objectDescription": "Objektbeschreibung:\nDie Wohnung ist in den Wohnräumen mit Dielenboden ausgestattet. Das Vollbad, das Gäste-WC sowie die Küche sind hell gefliest. Die Wohnung verfügt über einen Abstellraum mit Fenster. Das Wohn- und Esszimmer ist mit einem offenen Durchgang verbunden. Der Waschmaschinenanschluss befindet sich im Badezimmer.",
  "objectLocationText": "Lagebeschreibung:\nDas Objekt liegt in dem schönen, grünen und familienfreundlichen Stadtteil Barmbek-Süd. Mit gutem Anschluss an die Nahverkehrslinien. Fußläufig ist der nächste Bahnhof Friedrichsberg (Linie S1/S11)  innerhalb von 10 Minuten erreichbar. Die Fahrtzeit zum Hamburger Hauptbahnhof beträgt 10 Minuten. In Barmbek sind alle Einkaufsmöglichkeiten für den täglichen Bedarf sowie Ärzte und Restaurants vorhanden.",
  "heatingCostIncluded": false,
  "furnishingDescription": "Ausstattung",
  "objectMiscellaneousText": "Sonstige Anmerkungen:\nDie Kosten für das Kabelfernsehen sind in den Nebenkosten enthalten. Wasser- und Stromkosten werden direkt durch den Mieter mit den Versorgungsunternehmen abgerechnet. Im Mietvertrag wird eine Indexmiete vereinbart."
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110581, 1000016, '{
  "name": "5 Zimmer in Hebertshausen",
  "size": 70.0,
  "floor": null,
  "rooms": 5.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-9eb9df57a8694660bcc0f17138b99453.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "9eb9df57a8694660bcc0f17138b99453"
    }
  ],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084265, 1000016, '{
  "name": "Schöne 2 Zi-Single-Wohnung in der Nähe vom UKE - Immowelt Export Test",
  "size": 38.9,
  "floor": 0,
  "rooms": 2.0,
  "garden": false,
  "ground": null,
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Itzehoer Weg",
    "country": "DE",
    "zipCode": "20251",
    "additional": null,
    "coordinates": null,
    "houseNumber": "4"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1752.0,
  "elevator": false,
  "flatType": "GROUND_FLOOR",
  "basePrice": 584.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-d39542d4a4534ef2ba2f314cc7386421.jpg",
      "type": "IMG",
      "title": "Schöne 2 Zi-Single-Wohnung in der Nähe vom UKE - Immowelt Export Test Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "d39542d4a4534ef2ba2f314cc7386421"
    },
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-c807b0dbbc6744ab87833486e7fe89dc.jpg",
      "type": "IMG",
      "title": "Schöne 2 Zi-Single-Wohnung in der Nähe vom UKE - Immowelt Export Test Bild2.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "c807b0dbbc6744ab87833486e7fe89dc"
    }
  ],
  "barrierFree": false,
  "heatingCost": 100.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-07-18",
  "guestToilette": false,
  "serviceCharge": 100.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "MAY_2014",
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": {
      "endEnergyConsumption": "86",
      "energyEfficiencyClass": "C"
    },
    "yearOfConstruction": 1900,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "LONG_DISTANCE"
  },
  "lastRefurbishment": null,
  "objectDescription": "Die Wohnung wurde im Jahr 2013 komplett modernisiert. Zu der Wohnung gehört ein Abstellraum auf dem Dachboden des Hauses.",
  "objectLocationText": "Der Itzehoer Weg ist eine ruhige Seitenstraße (Einbahnstraße). Es besteht eine gute Anbindung an den öffentlichen Nahverkehr die Bushaltestelle Gärtnerstraße ist in ca. 5 Minuten fußläufig erreichbar.",
  "heatingCostIncluded": false,
  "furnishingDescription": "adsadsdsa",
  "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung müssen direkt abgerechnet werden. Im Mietvertrag wird eine Indexmiete vereinbart."
}', 1348166, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084257, 1000016, '{
  "name": "gemütliche 2,5 Zi-Wohnung in Neugraben",
  "size": 45.0,
  "floor": 2,
  "rooms": 2.5,
  "garden": false,
  "ground": null,
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Scheideholzstieg",
    "country": "DE",
    "zipCode": "21149",
    "additional": null,
    "coordinates": null,
    "houseNumber": "9"
  },
  "balcony": false,
  "contact": null,
  "bailment": 934.0,
  "elevator": false,
  "flatType": "ROOF_STOREY",
  "basePrice": 311.36,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-33fd24e1bc414fb286df3c2a8d6c7cf1.jpg",
      "type": "IMG",
      "title": "gemütliche 2,5 Zi-Wohnung in Neugraben Bild1.jpeg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "33fd24e1bc414fb286df3c2a8d6c7cf1"
    }
  ],
  "barrierFree": false,
  "heatingCost": 100.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-08-27",
  "guestToilette": false,
  "serviceCharge": 160.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": "10",
      "energyEfficiencyClass": "B",
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": 1965,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "Die Wohnung 2,5 Zimmer, ein Vollbad sowie eine kleine Küche. Zu der Wohnung gehört ein geräumiger Abstellraum im Keller.",
  "objectLocationText": "Neugraben-Fischbek bietet neben diversen fußläufig erreichbaren Einkaufsmöglichkeiten auch eine gute Anbindung an den Hamburger Verkehrsverbund (S-Bahn-Station Neugraben).",
  "heatingCostIncluded": false,
  "furnishingDescription": "Objekt",
  "objectMiscellaneousText": "Die Heizkosten sind in den Nebenkostenvorauszahlungen enthalten."
}', 1445029, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084234, 1000016, '{
  "name": "Schöne 3 Zi-Wohnung mitten in Eppendorf",
  "size": 60.9,
  "floor": 4,
  "rooms": 3.0,
  "garden": false,
  "ground": null,
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Schrammsweg",
    "country": "DE",
    "zipCode": "20249",
    "additional": null,
    "coordinates": null,
    "houseNumber": "25"
  },
  "balcony": false,
  "contact": null,
  "bailment": 2394.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 798.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-2a1f9e9c84d14e0e85ee711453cf785d.jpg",
      "type": "IMG",
      "title": "Schöne 3 Zi-Wohnung mitten in Eppendorf Bild1.jpeg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "2a1f9e9c84d14e0e85ee711453cf785d"
    }
  ],
  "barrierFree": false,
  "heatingCost": 55.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-07-26",
  "guestToilette": false,
  "serviceCharge": 84.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": 1900,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "LONG_DISTANCE"
  },
  "lastRefurbishment": null,
  "objectDescription": "1",
  "objectLocationText": "Das Objekt liegt in Eppendorf in der nähe der Bahnstation Kellinghusenstraße (U1&U3). Durch die Nähe zur Eppendorfer Landstraße und dem Eppendorfer Marktplatz, sind viele Einkaufmöglichkeiten sowie Cafés und Ärzte schnell fußläufig erreichbar.",
  "heatingCostIncluded": false,
  "furnishingDescription": "dsadsa",
  "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung sind direkt an die entsprechenden Unternehmen abzurechnen. Bei Vertragsabschluss ist eine Gebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen."
}', 1310329, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084220, 1000016, '{
  "name": "Top sanierte 2 Zi-Wohnung in Eppendorf",
  "size": 37.25,
  "floor": 5,
  "rooms": 2.0,
  "garden": false,
  "ground": "LAMINATE",
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Schrammsweg",
    "country": "DE",
    "zipCode": "20249",
    "additional": null,
    "coordinates": null,
    "houseNumber": "27e"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1452.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 484.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-4b20784d5bde46b391c2e781e02e04cd.jpg",
      "type": "IMG",
      "title": "Top sanierte 2 Zi-Wohnung in Eppendorf Bild1.jpeg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "4b20784d5bde46b391c2e781e02e04cd"
    }
  ],
  "barrierFree": false,
  "heatingCost": 500.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "Wed Jun 15 09:42:33 CEST 2016",
  "guestToilette": false,
  "serviceCharge": 130.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": "testestest",
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": 1900,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "LONG_DISTANCE"
  },
  "lastRefurbishment": null,
  "objectDescription": "Die Wohnung ist komplett weiß gestrichen, das kl. Duschbad ist weiß gefliest, in der Küche sind Spüle & Herd vorhanden. Zu der Wohnung gehört ein Abstellraum auf dem Dachboden des Hauses. Über die Küche hat man Zugang zur kl. Terrasse.",
  "objectLocationText": "Das Objekt liegt in Eppendorf in der Nähe der Bahnstation Kellinghusenstraße (U1 & U3), diese ist in ca. 5-10 Gehminuten erreichbar. Durch die Nähe zur Eppendorfer Landstraße und dem Eppendorfer Marktplatz, sind alle Einkaufmöglichkeiten schnell fußläufig erreichbar.",
  "heatingCostIncluded": false,
  "furnishingDescription": "setst",
  "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung sind direkt an die entsprechenden Unternehmen abzurechnen. Im Mietvertrag wird eine Indexmiete vereinbart. Bei Vertragsabschluss ist eine Gebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen."
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084261, 1000016, '{
  "name": "Schöne 2 Zi-Whg. mit EBK, Vollbad, Balkon und Dielen in Winterhude",
  "size": 51.73,
  "floor": 3,
  "rooms": 2.0,
  "garden": false,
  "ground": null,
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Dorotheenstr.",
    "country": "DE",
    "zipCode": "22299",
    "additional": null,
    "coordinates": null,
    "houseNumber": "133"
  },
  "balcony": true,
  "contact": null,
  "bailment": 1862.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 620.76,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-8b3ea405d3434ddc8ed5a6b4778a8a8c.jpg",
      "type": "IMG",
      "title": "Schöne 2 Zi-Whg. mit EBK, Vollbad, Balkon und Dielen in Winterhude Bild1.jpeg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "8b3ea405d3434ddc8ed5a6b4778a8a8c"
    }
  ],
  "barrierFree": false,
  "heatingCost": 100.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "Thu Dec 01 10:20:16 CET 2016",
  "guestToilette": false,
  "serviceCharge": 145.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": 1951,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "dasdsds",
  "objectLocationText": "Das Mehrfamilienhaus liegt im schönen Stadtteil Winterhude in der Dorotheenstraße. Die U-Bahnstation Sierichstraße ist fußläufig in 5 Minuten erreichbar. Die Bushaltestelle (Buslienien 25 und 109) liegt direkt vor der Haustür. Durch die Nähe zum Winterhuder Marktplatz und den Mühlenkamp sind alle Einkaufsmöglichkeiten, Cafés, Restaurants, Bäcker und Ärzte fußläufig erreichbar.",
  "heatingCostIncluded": false,
  "furnishingDescription": "daadadsads",
  "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung sind nicht in der Miete enthalten und müssen somit direkt abgerechnet werden.  Im Mietvertrag wird eine Indexmiete vereinbart. Bei Vertragsabschluss ist eine Ausfertigungsgebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen."
}', 1310908, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'UPDATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110579, 1000016, '{
  "name": "5 Zimmer in Hebertshausen TESTTTT",
  "size": 70.0,
  "floor": 1,
  "rooms": 5.0,
  "garden": false,
  "ground": "TILE",
  "heater": "FLOOR",
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1.0,
  "elevator": false,
  "flatType": "MAISONETTE",
  "basePrice": 300.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-7cc1601d8ad74cfe886057d5b835a20f.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen TESTTTT Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "7cc1601d8ad74cfe886057d5b835a20f"
    },
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-5f0e0face1f747eba30d526a1dc20852.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen TESTTTT Bild2.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "5f0e0face1f747eba30d526a1dc20852"
    }
  ],
  "barrierFree": false,
  "heatingCost": 1.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": 1000.0,
  "parkingSpace": false,
  "availableFrom": "2018-08-07",
  "guestToilette": true,
  "serviceCharge": 1.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "FIRST_TIME_USE_AFTER_REFURBISHMENT",
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "BLOCK"
  },
  "lastRefurbishment": null,
  "objectDescription": "Description",
  "objectLocationText": "adsfa",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'DEACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084275, 1000016, '{
  "name": "Schöne 3 Zi-Wohnung mitten in Eppendorf",
  "size": 82.63,
  "floor": 2,
  "rooms": 3.0,
  "garden": false,
  "ground": null,
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Schrammsweg",
    "country": "DE",
    "zipCode": "20249",
    "additional": null,
    "coordinates": null,
    "houseNumber": "27"
  },
  "balcony": true,
  "contact": null,
  "bailment": 3267.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 1089.0,
  "bathRooms": 0,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-2cc8317c01b4443e861b0522a19fa077.jpg",
      "type": "IMG",
      "title": "Schöne 3 Zi-Wohnung mitten in Eppendorf Bild1.jpeg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "2cc8317c01b4443e861b0522a19fa077"
    }
  ],
  "barrierFree": false,
  "heatingCost": 100.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "Thu Dec 01 14:33:37 CET 2016",
  "guestToilette": true,
  "serviceCharge": 213.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": 1968,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "LONG_DISTANCE"
  },
  "lastRefurbishment": null,
  "objectDescription": "Die Wohnung wurde im Jahr 2010 komplett überarbeitet.",
  "objectLocationText": "Das Mehrfamilienhaus liegt in Eppendorf in einer ruhigen Seitenstraße in der Nähe der Bahnstation Kellinghusenstraße (U1 & U3). Durch die Nähe zur Eppendorfer Landstraße und dem Eppendorfer Marktplatz, sind viele Einkaufmöglichkeiten sowie Cafés und Ärzte schnell fußläufig erreichbar.",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung sind direkt an die entsprechenden Unternehmen abzurechnen. Bei Vertragsabschluss ist eine Gebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen. Im Mietvertrag wird eine Indexmiete vereinbart. Die Vorauszahlungen für die Nutzung der SAT-Anlage sind in den Nebenkosten enthalten."
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084273, 1000016, '{
  "name": "Schöne 2-Zi-Wohnung in Eimsbüttel",
  "size": 42.6,
  "floor": 1,
  "rooms": 2.0,
  "garden": false,
  "ground": "OTHER",
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Marthastraße",
    "country": "DE",
    "zipCode": "20259",
    "additional": "",
    "coordinates": {
      "latitude": 0.0,
      "longitude": 0.0
    },
    "houseNumber": "35c"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1980.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 660.3,
  "bathRooms": 0,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-16d15cd95d6844b7b96d98bade37a2b5.jpg",
      "type": "IMG",
      "title": "IMG_4391.JPG",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "16d15cd95d6844b7b96d98bade37a2b5"
    }
  ],
  "barrierFree": false,
  "heatingCost": 50.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "Thu Dec 01 14:40:21 CET 2016",
  "guestToilette": false,
  "serviceCharge": 70.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": "Die Wohnung wird gerade grundlegend saniert und soll zum 01.12.2016 fertig gestellt werden.\nEs wird ein schöner Designbodenbelag in Holz-Optik verlegt. Die Wände und die Decken werden frisch weiß angestrichen und eine neue Einbauküche eingebaut.\nDas Objekt verfügt über eine zentrale Abluftanlage.\nDas Duschbad wurde ebenfalls bereits neu gemacht.",
  "basementAvailable": false,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "MAY_2014",
    "usageCertificate": null,
    "demandCertificate": {
      "endEnergyConsumption": "155.9",
      "energyEfficiencyClass": "E"
    },
    "yearOfConstruction": 1900,
    "energyCertificateType": "DEMAND_IDENTIFICATION",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "Die Wohnung verfügt über ein neues Duschbad und ein separates  WC.",
  "objectLocationText": "Tolle Lage mitten in Eimsbüttel zwischen Belliancestraße und Eppendorfer Weg. Alle Einkaufsmöglichkeiten für den täglichen Bedarf sowie Cafés sind fußläufig erreichbar. Die nächsten U-Bahnstationen sind Emilienstraße und Christuskirche.",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": "Die Kosten für den Wasser- und Stromverbrauch sind nicht in den Nebenkosten enthalten. Im Mietvertrag wird eine Indexmiete sowie eine Mindestlaufzeit von 2 Jahren vereinbart."
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110631, 1000016, '{
  "name": "5 Zimmer in Hebertshausen",
  "size": 70.0,
  "floor": null,
  "rooms": 5.0,
  "garden": false,
  "ground": null,
  "heater": "OVEN",
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": false,
  "contact": null,
  "bailment": 100.0,
  "elevator": false,
  "flatType": "MAISONETTE",
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-b034ee6943934acf902f6be6f013d841.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "b034ee6943934acf902f6be6f013d841"
    }
  ],
  "barrierFree": false,
  "heatingCost": 4.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-07-25",
  "guestToilette": false,
  "serviceCharge": 4.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": "adsadsads",
  "objectLocationText": "asdasdads",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": "sadasddas"
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110577, 1000016, '{
  "name": "5 Zimmer in Hebertshausen",
  "size": 70.0,
  "floor": null,
  "rooms": 5.0,
  "garden": false,
  "ground": null,
  "heater": "OVEN",
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": true,
  "contact": null,
  "bailment": 0.0,
  "elevator": false,
  "flatType": "LOFT",
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-1509cf96c21049e099d7ac1247c8186d.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "1509cf96c21049e099d7ac1247c8186d"
    }
  ],
  "barrierFree": false,
  "heatingCost": 10.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-07-25",
  "guestToilette": false,
  "serviceCharge": 20.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "jkhnkjnk",
  "objectLocationText": "njknjk",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084267, 1000016, '{
  "name": "modernisierte 3 Zi-Whg. mit Balkon in ruhiger Lage",
  "size": 73.06,
  "floor": 1,
  "rooms": 3.0,
  "garden": false,
  "ground": null,
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Rudolf-Klug-Weg",
    "country": "DE",
    "zipCode": "22455",
    "additional": null,
    "coordinates": null,
    "houseNumber": "13"
  },
  "balcony": true,
  "contact": null,
  "bailment": 2070.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 690.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-171dda7e11ee45df8856e1fc448ec15d.jpg",
      "type": "IMG",
      "title": "modernisierte 3 Zi-Whg. mit Balkon in ruhiger Lage Bild1.jpeg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "171dda7e11ee45df8856e1fc448ec15d"
    }
  ],
  "barrierFree": false,
  "heatingCost": 123.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "Tue Nov 01 11:56:13 CET 2016",
  "guestToilette": false,
  "serviceCharge": 180.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": true,
      "energyConsumptionParameter": "108"
    },
    "demandCertificate": null,
    "yearOfConstruction": 1986,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "ersbesrtb",
  "objectLocationText": "Der Rudolf-Klug-Weg ist eine Seitenstraße in der Nähe der U-Bahn Niendorf Nord. Einkaufmöglichkeiten, Schulen, Kindergärten und Ärzte sind fußläufig erreichbar.",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": "Die Vorauszahlungen für das Kabelfernsehen sind in den Nebenkosten enthalten. Die Vorauszahlungen für den Wasser- und Stromverbrauch müssen direkt abgerechnet werden."
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110619, 1000016, '{
  "name": "5 Zimmer in Hebertshausen",
  "size": 5.0,
  "floor": 5,
  "rooms": 55.0,
  "garden": false,
  "ground": "PARQUET",
  "heater": "OVEN",
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": false,
  "contact": null,
  "bailment": 5.0,
  "elevator": false,
  "flatType": "ROOF_STOREY",
  "basePrice": 5.0,
  "bathRooms": 5,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-e93bbf1f0a1a45fdaa6350b691e1dc0f.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "e93bbf1f0a1a45fdaa6350b691e1dc0f"
    }
  ],
  "barrierFree": false,
  "heatingCost": 5.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": 5.0,
  "parkingSpace": false,
  "availableFrom": "2018-08-05",
  "guestToilette": false,
  "serviceCharge": 5.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "FIRST_TIME_USE",
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": "5",
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": 1111,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "LOREM IPSUM",
  "objectLocationText": "LOREM IPSUM",
  "heatingCostIncluded": false,
  "furnishingDescription": "LOREM IPSUM",
  "objectMiscellaneousText": ""
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110667, 1000016, '{
  "name": "5 Zimmer in Hebertshausen",
  "size": 70.0,
  "floor": null,
  "rooms": 5.0,
  "garden": false,
  "ground": null,
  "heater": "CENTRAL",
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": false,
  "contact": null,
  "bailment": 10.0,
  "elevator": false,
  "flatType": "TERRACED_FLAT",
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-02080ca8045d4a5692a7a45f8fdce5bb.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "02080ca8045d4a5692a7a45f8fdce5bb"
    }
  ],
  "barrierFree": false,
  "heatingCost": 30.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2019-07-05",
  "guestToilette": false,
  "serviceCharge": 5.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": "Object description",
  "objectLocationText": "locationDescription",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110605, 1000016, '{
  "name": "5 Zimmer in Hebertshausen TO Upload",
  "size": 70.0,
  "floor": null,
  "rooms": 5.0,
  "garden": false,
  "ground": null,
  "heater": "OVEN",
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": true,
  "contact": null,
  "bailment": 10.0,
  "elevator": false,
  "flatType": "LOFT",
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-237a8c4cf7084efda64c6a72f58e7047.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen TO Upload Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "237a8c4cf7084efda64c6a72f58e7047"
    }
  ],
  "barrierFree": false,
  "heatingCost": 10.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-07-26",
  "guestToilette": false,
  "serviceCharge": 10.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": "5",
  "objectLocationText": "4",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1003754, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084224, 1000016, '{
  "name": "Schöne 3 Zimmer im Herzen von Eppendorf",
  "size": 82.53,
  "floor": 2,
  "rooms": 3.0,
  "garden": false,
  "ground": "PARQUET",
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Schrammsweg",
    "country": "DE",
    "zipCode": "20249",
    "additional": null,
    "coordinates": null,
    "houseNumber": "27"
  },
  "balcony": true,
  "contact": null,
  "bailment": 2574.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 858.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-f7fe177855ec4e5d83ed4bd4d32401ab.jpg",
      "type": "IMG",
      "title": "Schöne 3 Zimmer im Herzen von Eppendorf Bild1.jpeg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "f7fe177855ec4e5d83ed4bd4d32401ab"
    }
  ],
  "barrierFree": false,
  "heatingCost": 80.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "Wed Jun 01 09:14:43 CEST 2016",
  "guestToilette": true,
  "serviceCharge": 129.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": "testtestestest",
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": 1968,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "OIL"
  },
  "lastRefurbishment": null,
  "objectDescription": "Die Immobilie befindet sich im Herzen von Eppendorf, die U-Bahn Station Kellinghusen ist fußläufig zu erreichen.  Durch die Nähe zur Eppendorfer Landstaße und dem Eppendorfer Marktplatz, sind viele Einkaufsmöglichkeiten sowie Cafés und Ärzte schnell fußläufig erreichbar.",
  "objectLocationText": "Die Immobilie befindet sich im Herzen von Eppendorf, die U-Bahn Station Kellinghusen ist fußläufig zu erreichen.  Durch die Nähe zur Eppendorfer Landstaße und dem Eppendorfer Marktplatz, sind viele Einkaufsmöglichkeiten sowie Cafés und Ärzte schnell fußläufig erreichbar.",
  "heatingCostIncluded": false,
  "furnishingDescription": "test",
  "objectMiscellaneousText": "Die Vorauszahlungen für Wasser- und Strom werden direkt an die Versorger geleistet.\n\nBei Vertragsabschluss ist eine Gebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen.\n\nIm Mietvertrag wird eine Anpassung der Miete über Lebenshaltungskostenindex vereinbart."
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'UPDATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110659, 1000016, '{
  "name": "3 Zimmer in Hebertshausen",
  "size": 70.0,
  "floor": null,
  "rooms": 3.0,
  "garden": false,
  "ground": null,
  "heater": "GROUND_FLOOR",
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "4"
  },
  "balcony": false,
  "contact": null,
  "bailment": 3.0,
  "elevator": false,
  "flatType": "PENTHOUSE",
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [],
  "barrierFree": false,
  "heatingCost": 3.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-07-21",
  "guestToilette": false,
  "serviceCharge": 3.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "BLOCK"
  },
  "lastRefurbishment": null,
  "objectDescription": "ASDFGH",
  "objectLocationText": "asdfg",
  "heatingCostIncluded": false,
  "furnishingDescription": "asdfg",
  "objectMiscellaneousText": "asdfg"
}', 1334476, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110569, 1000016, '{
  "name": "5 Zimmer in Hebertshausen",
  "size": 70.0,
  "floor": null,
  "rooms": 5.0,
  "garden": false,
  "ground": null,
  "heater": "FLOOR",
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": false,
  "contact": null,
  "bailment": 100.0,
  "elevator": false,
  "flatType": "GROUND_FLOOR",
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-21b4f2c8858543c0984be7161d731b27.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "21b4f2c8858543c0984be7161d731b27"
    }
  ],
  "barrierFree": false,
  "heatingCost": 100.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-08-27",
  "guestToilette": false,
  "serviceCharge": 100.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": "10",
      "energyEfficiencyClass": "C",
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "BLOCK"
  },
  "lastRefurbishment": null,
  "objectDescription": "Objekt",
  "objectLocationText": "Objekt",
  "heatingCostIncluded": false,
  "furnishingDescription": "Objekt",
  "objectMiscellaneousText": null
}', 1445518, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110629, 1000016, '{
  "name": "5 Zimmer in Hebertshausen",
  "size": 70.0,
  "floor": null,
  "rooms": 5.0,
  "garden": false,
  "ground": "LAMINATE",
  "heater": "OVEN",
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": false,
  "contact": null,
  "bailment": 100.0,
  "elevator": false,
  "flatType": "LOFT",
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-e2e5663f529946eca4a151a27b410598.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "e2e5663f529946eca4a151a27b410598"
    }
  ],
  "barrierFree": false,
  "heatingCost": 100.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-08-27",
  "guestToilette": false,
  "serviceCharge": 100.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "MINT_CONDITION",
  "energyCertificate": {
    "creationDate": "MAY_2014",
    "usageCertificate": {
      "energyConsumption": "10",
      "energyEfficiencyClass": "C",
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "Objekt",
  "objectLocationText": "Objekt",
  "heatingCostIncluded": false,
  "furnishingDescription": "Objekt",
  "objectMiscellaneousText": null
}', 1444931, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084255, 1000016, '{
  "name": "Schöne 2 Zi-Wohnung nähe Wandsbek-Markt",
  "size": 65.0,
  "floor": 2,
  "rooms": 2.0,
  "garden": false,
  "ground": "OTHER",
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Wandsbeker Zollstraße",
    "country": "DE",
    "zipCode": "22041",
    "additional": null,
    "coordinates": null,
    "houseNumber": "3"
  },
  "balcony": true,
  "contact": null,
  "bailment": 2004.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 668.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-f8317fbc6ab646018e2c31b311239034.jpg",
      "type": "IMG",
      "title": "Schöne 2 Zi-Wohnung nähe Wandsbek-Markt Bild1.jpeg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "f8317fbc6ab646018e2c31b311239034"
    }
  ],
  "barrierFree": false,
  "heatingCost": 65.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "Sat Oct 15 14:40:02 CEST 2016",
  "guestToilette": false,
  "serviceCharge": 95.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": null,
    "demandCertificate": {
      "endEnergyConsumption": "323",
      "energyEfficiencyClass": null
    },
    "yearOfConstruction": 1951,
    "energyCertificateType": "DEMAND_IDENTIFICATION",
    "primaryEnergyProvider": "LONG_DISTANCE"
  },
  "lastRefurbishment": null,
  "objectDescription": "Die Wohnung ist in den Wohnräumen mit Dielenboden ausgestattet.",
  "objectLocationText": "Das Objekt liegt an der Ecke Wandsbeker Zollstraße / Kattunbleiche gegenüber der Wandsbeker Sporthalle. Der Bahnhof Wandsbek sowie zahlreiche Einkaufsmöglichkeiten sind fußläufig erreichbar. Das Objekt liegt zurückversetzt und liegt nicht direkt an der Straße.",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": "Die Kosten für Wasser- Stromversorgung sind nicht in den Nebenkostenvorauszahlungen enthalten."
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1157909, 1000016, '{
  "name": "4 Zimmer in Zawiercie",
  "size": 60.0,
  "floor": null,
  "rooms": 4.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Zawiercie",
    "region": "śląskie",
    "street": "Pomorska",
    "country": "PL",
    "zipCode": "42-400",
    "additional": null,
    "coordinates": null,
    "houseNumber": "1"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 600.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 1154159, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1145442, 1000016, '{
  "name": "Gut gelegene Wohnung Hamburg Altona",
  "size": 52.0,
  "floor": null,
  "rooms": 3.0,
  "garden": false,
  "ground": "OTHER",
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "",
    "street": null,
    "country": "DE",
    "zipCode": "22765",
    "additional": null,
    "coordinates": null,
    "houseNumber": null
  },
  "balcony": false,
  "contact": null,
  "bailment": 2500.0,
  "elevator": false,
  "flatType": "APARTMENT",
  "basePrice": 690.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": "4",
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-a077f47ef1fd46028c771f30dd76fe6a.jpg",
      "type": "IMG",
      "title": null,
      "encrypted": false,
      "extension": "jpg",
      "identifier": "a077f47ef1fd46028c771f30dd76fe6a"
    }
  ],
  "barrierFree": false,
  "heatingCost": 70.0,
  "kitchenette": false,
  "referenceId": "4567",
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "15.08.2018",
  "guestToilette": false,
  "serviceCharge": 70.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "NO_INFORMATION",
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": null,
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 1154159, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 8, 'DELETE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1105131, 1000016, '{
  "name": "3 Zimmer in Kraków - Immowelt Export Test",
  "size": 80.0,
  "floor": null,
  "rooms": 3.0,
  "garden": false,
  "ground": null,
  "heater": "GAS",
  "address": {
    "city": "Kraków",
    "region": "małopolskie",
    "street": "Floriańska",
    "country": "PL",
    "zipCode": "30-348",
    "additional": null,
    "coordinates": null,
    "houseNumber": "1"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1.0,
  "elevator": false,
  "flatType": "LOFT",
  "basePrice": 2500.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-cf01960595d140c3900ec782da556b56.jpg",
      "type": "IMG",
      "title": "3 Zimmer in Kraków - Immowelt Export Test Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "cf01960595d140c3900ec782da556b56"
    }
  ],
  "barrierFree": false,
  "heatingCost": 1.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-09",
  "guestToilette": false,
  "serviceCharge": 1.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": 1111,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "Objektbeschreibung",
  "objectLocationText": "Lagebeschreibung",
  "heatingCostIncluded": false,
  "furnishingDescription": "Ausstattung",
  "objectMiscellaneousText": "Sonstige Anmerkungen"
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1085059, 1000016, '{
  "name": "4 Zimmer in Berlin",
  "size": 50.0,
  "floor": null,
  "rooms": 4.0,
  "garden": false,
  "ground": null,
  "heater": "GAS",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Chausseestr",
    "country": "DE",
    "zipCode": "10115",
    "additional": null,
    "coordinates": null,
    "houseNumber": "100"
  },
  "balcony": true,
  "contact": null,
  "bailment": 2000.0,
  "elevator": false,
  "flatType": "TERRACED_FLAT",
  "basePrice": 750.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-672378c19daf4461b04e911e86874589.png",
      "type": "IMG",
      "title": "4 Zimmer in Berlin Bild1.png",
      "encrypted": false,
      "extension": "png",
      "identifier": "672378c19daf4461b04e911e86874589"
    },
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-eec102ca96de41d0912a48e4e5a9caf1.jpg",
      "type": "IMG",
      "title": "4 Zimmer in Berlin Bild2.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "eec102ca96de41d0912a48e4e5a9caf1"
    }
  ],
  "barrierFree": false,
  "heatingCost": 100.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-07-03",
  "guestToilette": false,
  "serviceCharge": 100.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": "10",
      "energyEfficiencyClass": "A",
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "LONG_DISTANCE"
  },
  "lastRefurbishment": null,
  "objectDescription": "Super Wohnung",
  "objectLocationText": "1A Lage",
  "heatingCostIncluded": false,
  "furnishingDescription": "Objekte",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110649, 1000016, '{
  "name": "5 Zimmer in Hebertshausen",
  "size": 70.0,
  "floor": null,
  "rooms": 5.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-eb7f1d2fe50542899f4c65634dd36fe7.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "eb7f1d2fe50542899f4c65634dd36fe7"
    }
  ],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1143531, 1000016, '{
  "name": "Schöne Wohnung Kaiserslautern",
  "size": 45.0,
  "floor": null,
  "rooms": 2.0,
  "garden": false,
  "ground": "OTHER",
  "heater": "CENTRAL",
  "address": {
    "city": "Kaiserslautern",
    "region": "",
    "street": null,
    "country": "DE",
    "zipCode": "67659",
    "additional": null,
    "coordinates": null,
    "houseNumber": null
  },
  "balcony": true,
  "contact": null,
  "bailment": 600.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 250.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": "7",
  "attachments": [],
  "barrierFree": false,
  "heatingCost": 40.0,
  "kitchenette": false,
  "referenceId": "7890",
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "30.09.2018",
  "guestToilette": false,
  "serviceCharge": 40.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "NO_INFORMATION",
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": null,
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 1117520, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1145434, 1000016, '{
  "name": null,
  "size": null,
  "floor": null,
  "rooms": null,
  "garden": false,
  "ground": "OTHER",
  "heater": "CENTRAL",
  "address": {
    "city": null,
    "region": null,
    "street": null,
    "country": null,
    "zipCode": "13125",
    "additional": null,
    "coordinates": null,
    "houseNumber": null
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": "OTHER",
  "basePrice": null,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": "3",
  "attachments": [],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": "3456",
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "NO_INFORMATION",
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": null,
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 1144061, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1145430, 1000016, '{
  "name": null,
  "size": null,
  "floor": null,
  "rooms": null,
  "garden": false,
  "ground": "OTHER",
  "heater": "CENTRAL",
  "address": {
    "city": null,
    "region": null,
    "street": null,
    "country": null,
    "zipCode": "13125",
    "additional": null,
    "coordinates": null,
    "houseNumber": null
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": "OTHER",
  "basePrice": null,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": "8",
  "attachments": [],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": "8912",
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "NO_INFORMATION",
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": null,
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 1144061, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1085067, 1000016, '{
  "name": "4 Zimmer in Berlin",
  "size": 98.0,
  "floor": null,
  "rooms": 4.0,
  "garden": false,
  "ground": null,
  "heater": "OVEN",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Chausseestr. ",
    "country": "DE",
    "zipCode": "10115",
    "additional": null,
    "coordinates": null,
    "houseNumber": "100"
  },
  "balcony": true,
  "contact": null,
  "bailment": 400.0,
  "elevator": false,
  "flatType": "MAISONETTE",
  "basePrice": 750.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-a5eba80e90f746aa883af245c1d6f9a9.jpg",
      "type": "IMG",
      "title": "4 Zimmer in Berlin Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "a5eba80e90f746aa883af245c1d6f9a9"
    }
  ],
  "barrierFree": false,
  "heatingCost": 300.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-08",
  "guestToilette": false,
  "serviceCharge": 500.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": "test",
  "objectLocationText": "test",
  "heatingCostIncluded": false,
  "furnishingDescription": "test",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110641, 1000016, '{
  "name": "5 Zimmer in Hebertshausen",
  "size": 70.0,
  "floor": null,
  "rooms": 5.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-16e28534262d4465ad505fa1f8e0d611.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "16e28534262d4465ad505fa1f8e0d611"
    }
  ],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110639, 1000016, '{
  "name": "5 Zimmer in Hebertshausen",
  "size": 70.0,
  "floor": 123,
  "rooms": 5.0,
  "garden": false,
  "ground": null,
  "heater": "ELECTRIC_HEATING",
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": true,
  "contact": null,
  "bailment": 123.0,
  "elevator": false,
  "flatType": "TERRACED_FLAT",
  "basePrice": 300.0,
  "bathRooms": 123,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-74aade6b301d4e9c8354557a800ba756.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "74aade6b301d4e9c8354557a800ba756"
    }
  ],
  "barrierFree": false,
  "heatingCost": 123.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": 123.0,
  "parkingSpace": false,
  "availableFrom": "2018-07-05",
  "guestToilette": true,
  "serviceCharge": 123.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "MINT_CONDITION",
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "DEMAND_IDENTIFICATION",
    "primaryEnergyProvider": "BLOCK"
  },
  "lastRefurbishment": null,
  "objectDescription": "dasfadsf",
  "objectLocationText": "asdfasf",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1318042, 1000016, '{
  "name": "3 Zimmer in Mainz",
  "size": 60.0,
  "floor": null,
  "rooms": 3.0,
  "garden": false,
  "ground": null,
  "heater": "GAS",
  "address": {
    "city": "Mainz",
    "region": "Rheinland-Pfalz",
    "street": "Forsterstraße",
    "country": "DE",
    "zipCode": "55118",
    "additional": null,
    "coordinates": null,
    "houseNumber": "13"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1100.0,
  "elevator": false,
  "flatType": "MAISONETTE",
  "basePrice": 1100.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-54bf1b3d9fff445b949d256642961497.jpg",
      "type": "IMG",
      "title": "3 Zimmer in Mainz Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "54bf1b3d9fff445b949d256642961497"
    }
  ],
  "barrierFree": false,
  "heatingCost": 1100.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-07",
  "guestToilette": false,
  "serviceCharge": 1100.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": "111",
  "objectLocationText": "222",
  "heatingCostIncluded": false,
  "furnishingDescription": "222",
  "objectMiscellaneousText": "333"
}', 1318041, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'UPDATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110669, 1000016, '{
  "name": "5 Zimmer in Hebertshausen",
  "size": 70.0,
  "floor": null,
  "rooms": 5.0,
  "garden": false,
  "ground": null,
  "heater": "CENTRAL",
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": false,
  "contact": null,
  "bailment": 10.0,
  "elevator": false,
  "flatType": "TERRACED_FLAT",
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-d5abfab3c41f4df3920b80d7391379e6.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "d5abfab3c41f4df3920b80d7391379e6"
    }
  ],
  "barrierFree": false,
  "heatingCost": 30.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2019-07-05",
  "guestToilette": false,
  "serviceCharge": 5.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": "Object description",
  "objectLocationText": "locationDescription",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1154819, 1000016, '{
  "name": "4 Zimmer in Zawiercie",
  "size": 60.0,
  "floor": null,
  "rooms": 4.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Zawiercie",
    "region": "śląskie",
    "street": "Pomorska",
    "country": "PL",
    "zipCode": "42-400",
    "additional": null,
    "coordinates": null,
    "houseNumber": "1"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 600.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 1154159, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1324963, 1000016, '{
  "name": "8 Zimmer in Kraków",
  "size": 80.0,
  "floor": null,
  "rooms": 8.0,
  "garden": false,
  "ground": null,
  "heater": "OVEN",
  "address": {
    "city": "Kraków",
    "region": "małopolskie",
    "street": "Dietla",
    "country": "PL",
    "zipCode": "22-222",
    "additional": null,
    "coordinates": null,
    "houseNumber": "10"
  },
  "balcony": false,
  "contact": null,
  "bailment": 200.0,
  "elevator": false,
  "flatType": "MAISONETTE",
  "basePrice": 800.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [],
  "barrierFree": false,
  "heatingCost": 200.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-08-24",
  "guestToilette": false,
  "serviceCharge": 200.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": "200",
      "energyEfficiencyClass": "A_P",
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": "Objekt",
  "objectLocationText": "Objekt",
  "heatingCostIncluded": false,
  "furnishingDescription": "Objekt",
  "objectMiscellaneousText": null
}', 1336607, NOW(), NOW(), 1000017, 'IMPORT_PROTECTED', null, 0, 'UPDATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1170875, 1000016, '{
  "name": "1 zimmer im Hamburg",
  "size": 20.0,
  "floor": null,
  "rooms": 1.0,
  "garden": false,
  "ground": null,
  "heater": "OVEN",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Osterstrasse",
    "country": "DE",
    "zipCode": "20257",
    "additional": null,
    "coordinates": null,
    "houseNumber": "12"
  },
  "balcony": true,
  "contact": null,
  "bailment": 100.0,
  "elevator": false,
  "flatType": "LOFT",
  "basePrice": 400.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-bfe08df72dc1459cbb9d68783c051349.jpg",
      "type": "IMG",
      "title": "1 zimmer im Hamburg Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "bfe08df72dc1459cbb9d68783c051349"
    }
  ],
  "barrierFree": false,
  "heatingCost": 10.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-01",
  "guestToilette": false,
  "serviceCharge": 10.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": "50",
      "energyEfficiencyClass": "C",
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": "abv",
  "objectLocationText": "ASCADF",
  "heatingCostIncluded": false,
  "furnishingDescription": "Objekt",
  "objectMiscellaneousText": null
}', 1325210, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1640203, 1000016, '{
  "name": "2 Zimmer in Hamburg",
  "size": 50.0,
  "floor": null,
  "rooms": 2.0,
  "garden": false,
  "ground": null,
  "heater": "OVEN",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Jungfernstieg",
    "country": "DE",
    "zipCode": "20354",
    "additional": null,
    "coordinates": null,
    "houseNumber": "12"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1234.0,
  "elevator": false,
  "flatType": "PENTHOUSE",
  "basePrice": 600.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-e62edafb45f84eb89028b02327d2c179.jpg",
      "type": "IMG",
      "title": "2 Zimmer in Hamburg Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "e62edafb45f84eb89028b02327d2c179"
    }
  ],
  "barrierFree": false,
  "heatingCost": 123.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-10-01",
  "guestToilette": false,
  "serviceCharge": 123.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "BLOCK"
  },
  "lastRefurbishment": null,
  "objectDescription": "asd",
  "objectLocationText": "asd",
  "heatingCostIncluded": false,
  "furnishingDescription": "asdfadf",
  "objectMiscellaneousText": null
}', 1640202, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084269, 1000016, '{
  "name": "Gemütliche 2 Zi-Dachgeschosswohnung sucht nette Mieter",
  "size": 59.5,
  "floor": 1,
  "rooms": 2.0,
  "garden": false,
  "ground": null,
  "heater": "CENTRAL",
  "address": {
    "city": "Morsum/Sylt",
    "region": "Schleswig-Holstein",
    "street": "Gurtmuasem",
    "country": "DE",
    "zipCode": "25980",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2 d"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1261.0,
  "elevator": false,
  "flatType": "PENTHOUSE",
  "basePrice": 420.66,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-679b060ff0bd4c2b90b202134dc8476c.jpg",
      "type": "IMG",
      "title": "Gemütliche 2 Zi-Dachgeschosswohnung sucht nette Mieter Bild1.jpeg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "679b060ff0bd4c2b90b202134dc8476c"
    }
  ],
  "barrierFree": false,
  "heatingCost": 190.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-07-18",
  "guestToilette": false,
  "serviceCharge": 130.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": true,
      "energyConsumptionParameter": "92"
    },
    "demandCertificate": null,
    "yearOfConstruction": 2004,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "Ruhige Lage in Sylt/Morsum.",
  "objectLocationText": "Ruhige Lage in Sylt/Morsum.",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": "Die Mieterhöhung erfolgt über eine Staffel. die Nettokaltmiete erhöht sich alle 3 Jahre um 9%. Die Wohnung wird nur an Interessenten mit Wohnberechtigungsschein vermietet"
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110603, 1000016, '{
  "name": "5 Zimmer in Hebertshausen",
  "size": 70.0,
  "floor": null,
  "rooms": 5.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-fb1b247933c54670a5d8aa3c7f79a11c.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "fb1b247933c54670a5d8aa3c7f79a11c"
    }
  ],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1170623, 1000016, '{
  "name": "100 Zimmer in Zawiercie",
  "size": 100.0,
  "floor": null,
  "rooms": 100.0,
  "garden": false,
  "ground": "PARQUET",
  "heater": "FLOOR",
  "address": {
    "city": "Zawiercie",
    "region": "śląskie",
    "street": "Pomorska",
    "country": "PL",
    "zipCode": "42-400",
    "additional": null,
    "coordinates": {
      "latitude": 50.4805704,
      "longitude": 19.4290893
    },
    "houseNumber": "1"
  },
  "balcony": false,
  "contact": null,
  "bailment": 100.0,
  "elevator": false,
  "flatType": "PENTHOUSE",
  "basePrice": 100.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [],
  "barrierFree": false,
  "heatingCost": 100.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-08-19",
  "guestToilette": false,
  "serviceCharge": 100.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": "10",
      "energyEfficiencyClass": "F",
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": 1968,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "COAL"
  },
  "lastRefurbishment": null,
  "objectDescription": "jkjkjk",
  "objectLocationText": "jkjkjk",
  "heatingCostIncluded": false,
  "furnishingDescription": "kllk",
  "objectMiscellaneousText": "klklk"
}', 1313426, NOW(), NOW(), 1000017, 'IMPORT_PROTECTED', null, 0, 'UPDATE',
        '0101000020E61000004490DECBD86D33407BB6B354833D4940'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084277, 1000016, '{
  "name": "kleine 2,5 Zi-Wohnung in netter Wohnanlage",
  "size": 43.5,
  "floor": 0,
  "rooms": 2.5,
  "garden": false,
  "ground": null,
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Schrammsweg",
    "country": "DE",
    "zipCode": "20249",
    "additional": null,
    "coordinates": null,
    "houseNumber": "25b"
  },
  "balcony": true,
  "contact": null,
  "bailment": 1698.0,
  "elevator": false,
  "flatType": "GROUND_FLOOR",
  "basePrice": 580.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-17a8619930a94b77b003ac8d1f8606d3.png",
      "type": "IMG",
      "title": "kleine 2,5 Zi-Wohnung in netter Wohnanlage Bild1.png",
      "encrypted": false,
      "extension": "png",
      "identifier": "17a8619930a94b77b003ac8d1f8606d3"
    },
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-6763136646fe4e04aca45457c3a88d26.png",
      "type": "IMG",
      "title": "kleine 2,5 Zi-Wohnung in netter Wohnanlage Bild2.png",
      "encrypted": false,
      "extension": "png",
      "identifier": "6763136646fe4e04aca45457c3a88d26"
    }
  ],
  "barrierFree": false,
  "heatingCost": 100.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-07-08",
  "guestToilette": false,
  "serviceCharge": 110.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": "151",
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": 1900,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "LONG_DISTANCE"
  },
  "lastRefurbishment": null,
  "objectDescription": "Die Wohnung wird auf Wunsch mit Spüle & Herd ausgestattet.",
  "objectLocationText": "Das Objekt liegt in Eppendorf in der Nähe der Bahnstation Kellinghusenstraße (U1 & U3). Durch die Nähe zur Eppendorfer Landstraße und dem Eppendorfer Marktplatz, sind alle Einkaufmöglichkeiten schnell fußläufig erreichbar.",
  "heatingCostIncluded": false,
  "furnishingDescription": "dsfdssfd",
  "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung sind direkt an die entsprechenden Unternehmen abzurechnen. Im Mietvertrag wird eine Indexmiete vereinbart. Bei Vertragsabschluss ist eine Gebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen. Die Vorauszahlungen für die Nutzung der SAT-Anlage sind bereits in den Nebenkosten enthalten."
}', 1314006, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 13, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1157943, 1000016, '{
  "name": "123 Zimmer in Hamburg",
  "size": 45.0,
  "floor": null,
  "rooms": 123.0,
  "garden": false,
  "ground": null,
  "heater": "GROUND_FLOOR",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Kieler Strasse  121, undefined Hamburg, DE",
    "country": "DE",
    "zipCode": "31-232",
    "additional": null,
    "coordinates": null,
    "houseNumber": "12"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1000.0,
  "elevator": false,
  "flatType": "MAISONETTE",
  "basePrice": 500.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [],
  "barrierFree": false,
  "heatingCost": 100.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-08-09",
  "guestToilette": false,
  "serviceCharge": 100.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "COAL"
  },
  "lastRefurbishment": null,
  "objectDescription": "test",
  "objectLocationText": "test",
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 1157942, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110531, 1000016, '{
  "name": "3 Zimmer in Hebertshausen",
  "size": 50.0,
  "floor": null,
  "rooms": 3.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "3"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'ACTIVATE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110627, 1000016, '{
  "name": "5 Zimmer in Hebertshausen",
  "size": 70.0,
  "floor": null,
  "rooms": 5.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-4ab1e1d3afd24378823b589dafd41123.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "4ab1e1d3afd24378823b589dafd41123"
    }
  ],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1145365, 1000016, '{
  "name": "Helle 3-Zimmer Dachgeschosswohnung",
  "size": 65.0,
  "floor": null,
  "rooms": 3.0,
  "garden": false,
  "ground": "TILE",
  "heater": "FLOOR",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Schrammsweg",
    "country": "DE",
    "zipCode": "20099",
    "additional": null,
    "coordinates": null,
    "houseNumber": "20"
  },
  "balcony": false,
  "contact": null,
  "bailment": 3000.0,
  "elevator": false,
  "flatType": "ROOF_STOREY",
  "basePrice": 870.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-30f6eb2a6bf04890977a81ea5210cc20.jpg",
      "type": "IMG",
      "title": "Helle 3-Zimmer Dachgeschosswohnung Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "30f6eb2a6bf04890977a81ea5210cc20"
    }
  ],
  "barrierFree": false,
  "heatingCost": 50.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "01.10.2018",
  "guestToilette": false,
  "serviceCharge": 90.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "NO_INFORMATION",
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": "10",
      "energyEfficiencyClass": "A",
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": {
      "endEnergyConsumption": null,
      "energyEfficiencyClass": null
    },
    "yearOfConstruction": null,
    "energyCertificateType": "DEMAND_IDENTIFICATION",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "Text Text Text Text Text Text Text",
  "objectLocationText": "Lage Lage Lage Lage Lage Lage Lage",
  "heatingCostIncluded": false,
  "furnishingDescription": "Objekt",
  "objectMiscellaneousText": null
}', 1446196, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1165266, 1000016, '{
  "name": "4 Zimmer in Hamburg",
  "size": 70.0,
  "floor": null,
  "rooms": 4.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Mettlerkampsweg",
    "country": "DE",
    "zipCode": "20535",
    "additional": null,
    "coordinates": null,
    "houseNumber": "21"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 700.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 1165265, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1110595, 1000016, '{
  "name": "5 Zimmer in Hebertshausen",
  "size": 70.0,
  "floor": null,
  "rooms": 5.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Hebertshausen",
    "region": "Bayern",
    "street": "Gemeinde Hebertshausen",
    "country": "DE",
    "zipCode": "85716",
    "additional": null,
    "coordinates": null,
    "houseNumber": "2"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 300.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-d35c5d235d7846969615f7355ff5794b.jpg",
      "type": "IMG",
      "title": "5 Zimmer in Hebertshausen Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "d35c5d235d7846969615f7355ff5794b"
    }
  ],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": true,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1453462, 1000016, '{
  "name": "100 Zimmer in Zawiercie",
  "size": 100.0,
  "floor": null,
  "rooms": 100.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Zawiercie",
    "region": "śląskie",
    "street": "Pomorska",
    "country": "PL",
    "zipCode": "42-400",
    "additional": null,
    "coordinates": null,
    "houseNumber": "12"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 100.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 1453461, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1143532, 1000016, '{
  "name": "2 Zimmer Wohnung Hamburg",
  "size": 36.0,
  "floor": null,
  "rooms": 2.0,
  "garden": false,
  "ground": "OTHER",
  "heater": "CENTRAL",
  "address": {
    "city": "Hamburg",
    "region": "",
    "street": null,
    "country": "DE",
    "zipCode": "20257",
    "additional": null,
    "coordinates": {
      "latitude": 0.0001,
      "longitude": 0.0001
    },
    "houseNumber": null
  },
  "balcony": true,
  "contact": null,
  "bailment": 2000.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 490.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": "2",
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-68309b23feb54757a18e4bbe9c0e8f9e.jpg",
      "type": "IMG",
      "title": null,
      "encrypted": false,
      "extension": "jpg",
      "identifier": "68309b23feb54757a18e4bbe9c0e8f9e"
    },
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-stg-img-store/IMG-922c8d67cf39477884608d97c589bf32.jpg",
      "type": "IMG",
      "title": null,
      "encrypted": false,
      "extension": "jpg",
      "identifier": "922c8d67cf39477884608d97c589bf32"
    }
  ],
  "barrierFree": false,
  "heatingCost": 50.0,
  "kitchenette": false,
  "referenceId": "2345",
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "01.08.2018",
  "guestToilette": false,
  "serviceCharge": 50.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "NO_INFORMATION",
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": null,
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 1117520, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E61000002D431CEBE2361A3F2D431CEBE2361A3F'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1154806, 1000016, '{
  "name": "4 Zimmer in Zawiercie",
  "size": 60.0,
  "floor": null,
  "rooms": 4.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Zawiercie",
    "region": "śląskie",
    "street": "Pomorska",
    "country": "PL",
    "zipCode": "42-400",
    "additional": null,
    "coordinates": null,
    "houseNumber": "1"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 600.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 1154159, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1084263, 1000016, '{
  "name": "Schöne Altbauwohnung in Hoheluft",
  "size": 62.3,
  "floor": 1,
  "rooms": 3.0,
  "garden": false,
  "ground": "OTHER",
  "heater": "GAS",
  "address": {
    "city": "Hamburg",
    "region": "Hamburg",
    "street": "Itzehoer Weg",
    "country": "DE",
    "zipCode": "20251",
    "additional": null,
    "coordinates": null,
    "houseNumber": "4"
  },
  "balcony": true,
  "contact": null,
  "bailment": 2803.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 934.5,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-cf34aa484c944fb49773eade9a89bfbe.jpg",
      "type": "IMG",
      "title": "Schöne Altbauwohnung in Hoheluft Bild1.jpeg",
      "encrypted": false,
      "extension": "jpeg",
      "identifier": "cf34aa484c944fb49773eade9a89bfbe"
    }
  ],
  "barrierFree": false,
  "heatingCost": 60.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "Fri Dec 01 12:52:12 CET 2017",
  "guestToilette": false,
  "serviceCharge": 80.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": 1900,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "LONG_DISTANCE"
  },
  "lastRefurbishment": null,
  "objectDescription": "Die Wohnung verfügt über eine geräumige Einbauküche, 3 Zimmer, davon sind zwei Zimmer durch eine Flügeltür miteinander verbunden. Alle drei Zimmer sind vom Flur aus zu begehen. \nDie Wohnung wurde vor ca. 3 Jahren grundlegend saniert.",
  "objectLocationText": "Das Objekt liegt im schönen Stadtteil Hoheluft. Alle Einkaufsmöglichkeiten sind in unmittelbarer Nähe, auf der Hoheluftchaussee vorhanden. Es bestehen gute Anbindungen an den öffentlichen Nahverkehr. Die nächst gelegene Bushaltestelle ist Gärtnerstraße mit Anschluss an die Metrobusse 5, 20, 25.",
  "heatingCostIncluded": false,
  "furnishingDescription": "dasdsa",
  "objectMiscellaneousText": "Es soll eine Mindestvertragslaufzeit von 2 Jahren sowie eine Indexmieterhöhung vereinbart werden."
}', 1317944, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;
insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1000290, 1000016, '{
  "name": "2 Zimmer in Berlin",
  "size": 51.0,
  "floor": null,
  "rooms": 2.0,
  "garden": false,
  "ground": null,
  "heater": "GAS",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Kirschnwerweg",
    "country": "DE",
    "zipCode": "12353",
    "additional": null,
    "coordinates": {
      "latitude": 52.42718,
      "longitude": 13.45408
    },
    "houseNumber": "13"
  },
  "balcony": false,
  "contact": null,
  "bailment": 900.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 370.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-15036e3750594d35a3f9cf8f83a91239.jpg",
      "type": "IMG",
      "title": "2 Zimmer in Berlin Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "15036e3750594d35a3f9cf8f83a91239"
    }
  ],
  "barrierFree": false,
  "heatingCost": 50.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-01",
  "guestToilette": false,
  "serviceCharge": 90.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": " ",
  "objectLocationText": " ",
  "heatingCostIncluded": false,
  "furnishingDescription": " ",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E61000000D897B2C7DE82A40ADC090D5AD364A40'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1000215, 1000016, '{
  "name": "Zentral in Neukölln",
  "size": 65.0,
  "floor": null,
  "rooms": 3.0,
  "garden": false,
  "ground": null,
  "heater": "GAS",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Kirschnerweg",
    "country": "DE",
    "zipCode": "12353",
    "additional": null,
    "coordinates": {
      "latitude": 52.42718,
      "longitude": 13.45408
    },
    "houseNumber": "13"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1300.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 458.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-af594db4fad045fbbd51d95cd472d73a.jpg",
      "type": "IMG",
      "title": "Zentral in Neukölln Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "af594db4fad045fbbd51d95cd472d73a"
    }
  ],
  "barrierFree": false,
  "heatingCost": 50.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-10-01",
  "guestToilette": false,
  "serviceCharge": 100.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": " Ruhig gelegen und nette Nachbarschaft",
  "objectLocationText": "Bei Interesse, bitte bewerben Sie sich ausschließlich über unser digital Bewerbungsportal: \nhttps://tenant.immomio.com/apply/1000215\n\nZentral in Neukölln. Nur wenige Gehminuten zu U-Bahn und Bus.",
  "heatingCostIncluded": false,
  "furnishingDescription": " Neue Einbauküche",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'UPDATE',
        '0101000020E61000000D897B2C7DE82A40ADC090D5AD364A40'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1000218, 1000016, '{
  "name": "Gut geschnittene Wohnung in Neukölln",
  "size": 55.0,
  "floor": null,
  "rooms": 2.0,
  "garden": false,
  "ground": null,
  "heater": "GAS",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Kirschnerweg",
    "country": "DE",
    "zipCode": "12353",
    "additional": null,
    "coordinates": {
      "latitude": 52.42718,
      "longitude": 13.45408
    },
    "houseNumber": "13"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1100.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 396.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-77fb0ee4f2f5417e98afcd5a7610fa93.jpg",
      "type": "IMG",
      "title": "Gut geschnittene Wohnung in Neukölln Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "77fb0ee4f2f5417e98afcd5a7610fa93"
    }
  ],
  "barrierFree": false,
  "heatingCost": 40.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-28",
  "guestToilette": false,
  "serviceCharge": 80.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": " Ruhig gelegen und nette Nachbarschaft",
  "objectLocationText": "Bei Interesse, bitte bewerben Sie sich ausschließlich über unser digitales Bewerbungsportal: \nhttps://tenant.immomio.com/apply/1000218\n\nZentral in Neukölln. Nur wenige Gehminuten zu U-Bahn und Bus.",
  "heatingCostIncluded": false,
  "furnishingDescription": " Neue Einbauküche",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E61000000D897B2C7DE82A40ADC090D5AD364A40'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1000173, 1000016, '{
  "name": "3 Zimmer in Berlin",
  "size": 70.0,
  "floor": 3,
  "rooms": 3.0,
  "garden": false,
  "ground": "PARQUET",
  "heater": "CENTRAL",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Springbornstraße",
    "country": "DE",
    "zipCode": "12487",
    "additional": null,
    "coordinates": {
      "latitude": 52.43584999999999,
      "longitude": 13.49928
    },
    "houseNumber": "7"
  },
  "balcony": false,
  "contact": null,
  "bailment": 2400.0,
  "elevator": false,
  "flatType": "ROOF_STOREY",
  "basePrice": 818.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-cca82df6b9dd49d995b1233a0fcfc4da.jpg",
      "type": "IMG",
      "title": "3 Zimmer in Berlin Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "cca82df6b9dd49d995b1233a0fcfc4da"
    }
  ],
  "barrierFree": false,
  "heatingCost": 50.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-01",
  "guestToilette": false,
  "serviceCharge": 100.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "REFURBISHED",
  "energyCertificate": {
    "creationDate": "APRIL_2014",
    "usageCertificate": {
      "energyConsumption": "393",
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": 1974,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "Zu dem Objekt zählen drei einladende Zimmer. Ein aktueller Energieausweis liegt vor. Des Weiteren überzeugt der gute Schnitt, durch den sie auch für WGs geeignet ist. Eine Besonderheit ist auch der Balkon, der nach Süden ausgerichtet ist. Zur Verfügung stehen Ihnen außerdem eine Einbauküche sowie ein Kellerraum.",
  "objectLocationText": "- Autobahnzufahrt bzw. ausfahrt in unmittelbahrer Nähe. Direkte Anbindung in die City. \n- 5min entfernt liegt eine Grundschule und eine Kita \n- Geschäfte für den täglichen Bedarf und eine Apotheke findet man im Umkreis von 450m. \n- zwei Buslinie in direkter Nähe. Beide fahren zur Sbahn bzw. U-Bahn. - Mit der S-Bahn und verschiedenen Buslinien, vor allem die Linien M11 und 160, gelangen Sie innerhalb kürzester Zeit in die Innenstadt. \n- In nur ca. 25 Minuten sind Sie am Alexanderplatz – dem Herzen Berlins. - Der U-Bahnhof Zwickauer Damm liegt ca. 2,4 km entfernt und der S-Bahnhof Schöneweide ist nach ca. 1,8 km erreicht. Die A 113 verbindet den Berliner Stadtring (A 100) mit dem Berliner Ring (A 10) und liegt in unmittelbarer Nähe",
  "heatingCostIncluded": false,
  "furnishingDescription": "Wohnzimmer, Schlafzimmer und Flur sind mit Parkett ausgelegt. \nDie große, vom Wohnzimmer zugängliche Dachloggia mit Markise lädt zum Verweilen an.\nDas Tageslichtbad wurde renoviert und ist mit einer Dusche ausgestattet. \nDie Einbauküche verfügt über einen Geschirrspüler, ein Ceran-Kochfeld sowie einen Backofen.",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E61000000118CFA0A1FF2A4014FBCBEEC9374A40'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1000221, 1000016, '{
  "name": "Schicke Singlewohnung",
  "size": 50.0,
  "floor": null,
  "rooms": 2.0,
  "garden": false,
  "ground": null,
  "heater": "GAS",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Kirschnerweg",
    "country": "DE",
    "zipCode": "12353",
    "additional": null,
    "coordinates": {
      "latitude": 52.42718,
      "longitude": 13.45408
    },
    "houseNumber": "13"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1000.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 368.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-696e6a3fe0904b49b707b62c4b0fb079.jpg",
      "type": "IMG",
      "title": "Schicke Singlewohnung Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "696e6a3fe0904b49b707b62c4b0fb079"
    }
  ],
  "barrierFree": false,
  "heatingCost": 35.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-01",
  "guestToilette": false,
  "serviceCharge": 80.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": " Ruhig gelegen und nette Nachbarschaft",
  "objectLocationText": "Bei Interesse, bitte bewerben Sie sich ausschließlich über unser digitales Bewerbungsportal: \nhttps://tenant.immomio.com/apply/1000221\n\nZentral in Neukölln. Nur wenige Gehminuten zu U-Bahn und Bus.",
  "heatingCostIncluded": false,
  "furnishingDescription": " Neue Einbauküche",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'UPDATE',
        '0101000020E61000000D897B2C7DE82A40ADC090D5AD364A40'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1001013, 1000016, '{
  "name": "1 zimmer im Berlin",
  "size": 63.0,
  "floor": null,
  "rooms": 1.0,
  "garden": false,
  "ground": null,
  "heater": "OVEN",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Kirschnerweg",
    "country": "DE",
    "zipCode": "12353",
    "additional": null,
    "coordinates": {
      "latitude": 52.42718,
      "longitude": 13.45408
    },
    "houseNumber": "13"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1000.0,
  "elevator": false,
  "flatType": "ROOF_STOREY",
  "basePrice": 425.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-36fc4ed8fdd14a1092e99bccfaa9071b.jpg",
      "type": "IMG",
      "title": "1 zimmer im Berlin Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "36fc4ed8fdd14a1092e99bccfaa9071b"
    }
  ],
  "barrierFree": false,
  "heatingCost": 60.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-01",
  "guestToilette": false,
  "serviceCharge": 100.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": " ",
  "objectLocationText": " ",
  "heatingCostIncluded": false,
  "furnishingDescription": " ",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E61000000D897B2C7DE82A40ADC090D5AD364A40'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1001918, 1000016, '{
  "name": "2 Zimmer in Berlin",
  "size": 55.0,
  "floor": null,
  "rooms": 2.0,
  "garden": false,
  "ground": "PARQUET",
  "heater": "GAS",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Ortolanweg",
    "country": "DE",
    "zipCode": "12359",
    "additional": null,
    "coordinates": {
      "latitude": 52.44407,
      "longitude": 13.45949
    },
    "houseNumber": "12"
  },
  "balcony": false,
  "contact": null,
  "bailment": 900.0,
  "elevator": false,
  "flatType": "PENTHOUSE",
  "basePrice": 386.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-1824ab5383174d228909a7d5ce024650.jpg",
      "type": "IMG",
      "title": "2 Zimmer in Berlin Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "1824ab5383174d228909a7d5ce024650"
    }
  ],
  "barrierFree": false,
  "heatingCost": 50.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-10-20",
  "guestToilette": false,
  "serviceCharge": 100.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "dq",
  "objectLocationText": "dq",
  "heatingCostIncluded": false,
  "furnishingDescription": "dq",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000097ADF54542EB2A403F912749D7384A40'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1001405, 1000016, '{
  "name": "2 Zimmer in Mainz",
  "size": 60.0,
  "floor": null,
  "rooms": 2.0,
  "garden": false,
  "ground": null,
  "heater": "OVEN",
  "address": {
    "city": "Mainzai",
    "region": "Rheinland-Pfalz",
    "street": "Ritterstrasse",
    "country": "DE",
    "zipCode": "55131",
    "additional": null,
    "coordinates": {
      "latitude": 49.9893554,
      "longitude": 8.275506
    },
    "houseNumber": "6"
  },
  "balcony": false,
  "contact": null,
  "bailment": 2700.0,
  "elevator": false,
  "flatType": "GROUND_FLOOR",
  "basePrice": 900.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-450cbe418f6a4b5eb707d76783403f59.jpg",
      "type": "IMG",
      "title": "2 Zimmer in Mainz Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "450cbe418f6a4b5eb707d76783403f59"
    }
  ],
  "barrierFree": false,
  "heatingCost": 100.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-10-31",
  "guestToilette": false,
  "serviceCharge": 50.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "ALTERNATIVE"
  },
  "lastRefurbishment": null,
  "objectDescription": " ",
  "objectLocationText": " ",
  "heatingCostIncluded": false,
  "furnishingDescription": " ",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E61000001CB4571F0F8D20407D8F9F32A3FE4840'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1001067, 1000016, '{
  "name": "4 Zimmer in Berlin",
  "size": 80.0,
  "floor": null,
  "rooms": 4.0,
  "garden": false,
  "ground": null,
  "heater": "GAS",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Kirschnerweg",
    "country": "DE",
    "zipCode": "12353",
    "additional": null,
    "coordinates": {
      "latitude": 52.42718,
      "longitude": 13.45408
    },
    "houseNumber": "13"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1670.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 557.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [],
  "barrierFree": false,
  "heatingCost": 61.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-01",
  "guestToilette": false,
  "serviceCharge": 117.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": " \"Bei Interesse, bitte bewerben Sie sich ausschließlich über unser digitales Bewerbungsportal: \n\n\nZentral in Neukölln. Nur wenige Gehminuten zu U-Bahn und Bus.\"",
  "objectLocationText": " Ruhig gelegen und nette Nachbarschaft",
  "heatingCostIncluded": false,
  "furnishingDescription": " Neue Einbauküche",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E61000000D897B2C7DE82A40ADC090D5AD364A40'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1001022, 1000016, '{
  "name": "2 Zimmer in Berlin",
  "size": 52.0,
  "floor": null,
  "rooms": 2.0,
  "garden": false,
  "ground": null,
  "heater": "OVEN",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Kirschnerweg",
    "country": "DE",
    "zipCode": "12353",
    "additional": null,
    "coordinates": {
      "latitude": 52.42718,
      "longitude": 13.45408
    },
    "houseNumber": "13"
  },
  "balcony": false,
  "contact": null,
  "bailment": 900.0,
  "elevator": false,
  "flatType": "PENTHOUSE",
  "basePrice": 360.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-552480e8a9a14634852a27c5814a4d2d.jpg",
      "type": "IMG",
      "title": "2 Zimmer in Berlin Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "552480e8a9a14634852a27c5814a4d2d"
    }
  ],
  "barrierFree": false,
  "heatingCost": 50.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-01",
  "guestToilette": false,
  "serviceCharge": 90.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "DEMAND_IDENTIFICATION",
    "primaryEnergyProvider": "BLOCK"
  },
  "lastRefurbishment": null,
  "objectDescription": " ",
  "objectLocationText": " ",
  "heatingCostIncluded": false,
  "furnishingDescription": " ",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E61000000D897B2C7DE82A40ADC090D5AD364A40'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1001011, 1000016, '{
  "name": "2 Zimmer in Berlin",
  "size": 51.0,
  "floor": null,
  "rooms": 2.0,
  "garden": false,
  "ground": null,
  "heater": "OVEN",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Kirschnwerweg",
    "country": "DE",
    "zipCode": "12353",
    "additional": null,
    "coordinates": {
      "latitude": 52.42718,
      "longitude": 13.45408
    },
    "houseNumber": "13"
  },
  "balcony": false,
  "contact": null,
  "bailment": 900.0,
  "elevator": false,
  "flatType": "LOFT",
  "basePrice": 370.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-47a86d4bdc1e407891664ab13e830328.jpg",
      "type": "IMG",
      "title": "2 Zimmer in Berlin Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "47a86d4bdc1e407891664ab13e830328"
    }
  ],
  "barrierFree": false,
  "heatingCost": 50.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-01",
  "guestToilette": false,
  "serviceCharge": 90.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": " ",
  "objectLocationText": " ",
  "heatingCostIncluded": false,
  "furnishingDescription": " ",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E61000000D897B2C7DE82A40ADC090D5AD364A40'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1000181, 1000016, '{
  "name": "Wohnung in ruhiger Lage",
  "size": 53.0,
  "floor": 1,
  "rooms": 2.0,
  "garden": false,
  "ground": "PARQUET",
  "heater": "GAS",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Britzer Damm ",
    "country": "DE",
    "zipCode": "12347",
    "additional": null,
    "coordinates": {
      "latitude": 52.45018349999999,
      "longitude": 13.4367852
    },
    "houseNumber": "146"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1480.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 515.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-5b432fa77c9440beb026d57a141074ee.jpg",
      "type": "IMG",
      "title": "Wohnung in ruhiger Lage Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "5b432fa77c9440beb026d57a141074ee"
    }
  ],
  "barrierFree": false,
  "heatingCost": 65.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-01",
  "guestToilette": false,
  "serviceCharge": 100.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "REFURBISHED",
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "Parken ist auf der Straße kostenlos möglich und Parkplätze leicht zu finden...",
  "objectLocationText": "Zentrale Lage",
  "heatingCostIncluded": false,
  "furnishingDescription": "Das Bad hat Fußbodenheizung und hochwertige Fließen, sowie eine begehbare Dusche und Fenster.\n\nZu der Wohnung gehört ein abschließbarer Keller und ein Waschmaschinen Anschluss.",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E6100000EEC04A4FA2DF2A4071D9E89C9F394A40'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1000162, 1000016, '{
  "name": "Charmante 2 Zimmer Wohnung",
  "size": 52.0,
  "floor": 4,
  "rooms": 2.0,
  "garden": false,
  "ground": "PARQUET",
  "heater": "CENTRAL",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Pätzer Straße",
    "country": "DE",
    "zipCode": "12359",
    "additional": null,
    "coordinates": {
      "latitude": 52.4520887,
      "longitude": 13.4390932
    },
    "houseNumber": "15"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1500.0,
  "elevator": false,
  "flatType": "ROOF_STOREY",
  "basePrice": 500.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-a0f13df26cc04e5883f0cb36d1eded43.jpg",
      "type": "IMG",
      "title": "Charmante 2 Zimmer Wohnung Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "a0f13df26cc04e5883f0cb36d1eded43"
    }
  ],
  "barrierFree": false,
  "heatingCost": 65.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": -1.0,
  "parkingSpace": false,
  "availableFrom": "2018-09-01",
  "guestToilette": false,
  "serviceCharge": 100.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "REFURBISHED",
  "energyCertificate": {
    "creationDate": "MAY_2014",
    "usageCertificate": {
      "energyConsumption": "180",
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": 1900,
    "energyCertificateType": "USAGE_IDENTIFICATION",
    "primaryEnergyProvider": "OIL"
  },
  "lastRefurbishment": null,
  "objectDescription": "Gartenbenutzung möglich\n",
  "objectLocationText": "In Neukölln lebt man am Puls der Hauptstadt, viele Künstler und Studenten ziehen aufgrund der weitaus günstigeren Mieten von Prenzlauerberg nach Neukölln. Kreuzberg und Friedrichhain sind die Nachbarbezirke von Neukölln und in nur 15min ist man mit den öffentlichen Verkehrsmitteln im Prenzlauer Berg, Mitte oder in Charlottenburg.\n\nBus:         170\nMetrobus: M44\nU-Bahn:    U7 - U-Bhf. Blaschkoallee",
  "heatingCostIncluded": false,
  "furnishingDescription": "Fachwerk, Kunststoffenster\n",
  "objectMiscellaneousText": "Stromkosten sind in den Nebenkosten enthalten.\n"
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'UPDATE',
        '0101000020E6100000BFCAEBD2D0E02A4011B2E20ADE394A40'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1000209, 1000016, '{
  "name": "4 Zimmer in Berlin",
  "size": 80.0,
  "floor": null,
  "rooms": 4.0,
  "garden": false,
  "ground": null,
  "heater": "GAS",
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Kirschnerweg",
    "country": "DE",
    "zipCode": "12353",
    "additional": null,
    "coordinates": {
      "latitude": 52.42718,
      "longitude": 13.45408
    },
    "houseNumber": "13"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1500.0,
  "elevator": false,
  "flatType": "FLOOR",
  "basePrice": 556.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-7e44313f58a44d3e8d2389a5b72c35bf.jpg",
      "type": "IMG",
      "title": "4 Zimmer in Berlin Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "7e44313f58a44d3e8d2389a5b72c35bf"
    }
  ],
  "barrierFree": false,
  "heatingCost": 100.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-01",
  "guestToilette": false,
  "serviceCharge": 50.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": "",
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "Bei Interesse, bitte bewerben Sie sich ausschließlich über unser digital Bewerbungsportal: https://tenant.immomio.com/apply/1000209\n\nRuhig gelegen und nette Nachbarschaft",
  "objectLocationText": "Zentral in Neukölln. Nur wenige Gehminuten zu U-Bahn und Bus.",
  "heatingCostIncluded": false,
  "furnishingDescription": " Neue Einbauküche",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E61000000D897B2C7DE82A40ADC090D5AD364A40'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1001065, 1000016, '{
  "name": "4 Zimmer in Berlin",
  "size": 80.0,
  "floor": null,
  "rooms": 4.0,
  "garden": false,
  "ground": null,
  "heater": null,
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Kirschnerweg",
    "country": "DE",
    "zipCode": "12353",
    "additional": null,
    "coordinates": {
      "latitude": 52.42718,
      "longitude": 13.45408
    },
    "houseNumber": "13"
  },
  "balcony": false,
  "contact": null,
  "bailment": null,
  "elevator": false,
  "flatType": null,
  "basePrice": 556.0,
  "bathRooms": null,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-03fe275dc48f411f82685073e9f02d8a.jpg",
      "type": "IMG",
      "title": "4 Zimmer in Berlin Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "03fe275dc48f411f82685073e9f02d8a"
    }
  ],
  "barrierFree": false,
  "heatingCost": null,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": null,
  "guestToilette": false,
  "serviceCharge": null,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": null,
  "lastRefurbishment": null,
  "objectDescription": null,
  "objectLocationText": null,
  "heatingCostIncluded": false,
  "furnishingDescription": null,
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E61000000D897B2C7DE82A40ADC090D5AD364A40'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1000179, 1000016, '{
  "name": "Erdgeschoss Wohnung in zentraler Lage",
  "size": 55.0,
  "floor": 1,
  "rooms": 2.0,
  "garden": false,
  "ground": "PARQUET",
  "heater": "GAS",
  "address": {
    "city": "Mainz",
    "region": "Rheinland-Pfalz",
    "street": "Ritterstrasse",
    "country": "DE",
    "zipCode": "55131",
    "additional": null,
    "coordinates": {
      "latitude": 49.9893554,
      "longitude": 8.275506
    },
    "houseNumber": "6"
  },
  "balcony": true,
  "contact": null,
  "bailment": 1500.0,
  "elevator": false,
  "flatType": "GROUND_FLOOR",
  "basePrice": 10000.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-9a4142c0a3b24b3098858c5c7c9cbd00.jpg",
      "type": "IMG",
      "title": "Erdgeschoss Wohnung in zentraler Lage Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "9a4142c0a3b24b3098858c5c7c9cbd00"
    }
  ],
  "barrierFree": false,
  "heatingCost": 50.0,
  "kitchenette": true,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-01",
  "guestToilette": false,
  "serviceCharge": 90.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": "REFURBISHED",
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": null,
    "demandCertificate": null,
    "yearOfConstruction": 1950,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": "\nDie Wohnung verfügt über ein helles Wohnzimmer mit Zugang zur eigenen Terrasse, eine Küche mit vorhandener Einbauküche,  ein Schlaf- und ein Arbeitszimmer sowie ein separates Bad und WC. Ein Waschmaschinenstellplatz in der Waschküche sowie ein Kellerraum gehören ebenfalls zur Wohnung, zudem ein PKW-Stellplatz. Beheizt mit Zentralheizung. ",
  "objectLocationText": "Einkaufsmöglichkeiten, Ärzte, Apotheke, Kindergarten, Schwimmbad und Schule in fußläufiger Nähe. \n\nVerkehrsverbindungen:\n\nU6 Alt-Mariendorf\nU7 Johannisthaler Chaussee\n\nBus M76, X76, 179    bis Tauernallee\nBus M11, X11 bis Quarzweg",
  "heatingCostIncluded": false,
  "furnishingDescription": "Parkettboden, Terrasse, PKW-Stellplatz, begehbarer Wandschrank im Flur, Einbauküche, Kellerraum, Waschmaschinenstellplatz in Waschküche",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E61000001CB4571F0F8D20407D8F9F32A3FE4840'::geometry)
on conflict do nothing;

insert into landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection,
                               validuntil, runtimeindays, property_task, location)
values (1001009, 1000016, '{
  "name": "2 Zimmer in Mainz",
  "size": 55.0,
  "floor": 1,
  "rooms": 2.0,
  "garden": false,
  "ground": null,
  "heater": "GAS",
  "address": {
    "city": "Mainz",
    "region": "Rheinland-Pfalz",
    "street": "Ritterstrasse ",
    "country": "DE",
    "zipCode": "55131",
    "additional": null,
    "coordinates": null,
    "houseNumber": "6"
  },
  "balcony": false,
  "contact": null,
  "bailment": 1500.0,
  "elevator": false,
  "flatType": "GROUND_FLOOR",
  "basePrice": 100.0,
  "bathRooms": 1,
  "documents": [],
  "flatShare": false,
  "externalId": null,
  "attachments": [
    {
      "url": "https://s3.eu-central-1.amazonaws.com/immomio-prod-img-store/IMG-1fc492cd818b4835a50cacb9fef8a269.jpg",
      "type": "IMG",
      "title": "2 Zimmer in Mainz Bild1.jpg",
      "encrypted": false,
      "extension": "jpg",
      "identifier": "1fc492cd818b4835a50cacb9fef8a269"
    }
  ],
  "barrierFree": false,
  "heatingCost": 50.0,
  "kitchenette": false,
  "referenceId": null,
  "showAddress": false,
  "showContact": false,
  "basementSize": null,
  "parkingSpace": false,
  "availableFrom": "2018-09-01",
  "guestToilette": false,
  "serviceCharge": 90.0,
  "constructionYear": null,
  "historicBuilding": false,
  "shortDescription": null,
  "basementAvailable": null,
  "buildingCondition": null,
  "energyCertificate": {
    "creationDate": null,
    "usageCertificate": {
      "energyConsumption": null,
      "energyEfficiencyClass": null,
      "includesHeatConsumption": false,
      "energyConsumptionParameter": null
    },
    "demandCertificate": null,
    "yearOfConstruction": null,
    "energyCertificateType": "NO_AVAILABLE",
    "primaryEnergyProvider": "GAS"
  },
  "lastRefurbishment": null,
  "objectDescription": " ",
  "objectLocationText": " ",
  "heatingCostIncluded": false,
  "furnishingDescription": " ",
  "objectMiscellaneousText": null
}', 2, NOW(), NOW(), 1000017, 'UNPROTECTED', null, 0, 'IDLE',
        '0101000020E610000000000000000000000000000000000000'::geometry)
on conflict do nothing;

insert into constants.state (id, name)
values (1217633, 'Bayern')
on conflict do nothing;
insert into constants.state (id, name)
values (1217659, 'Berlin')
on conflict do nothing;

insert into constants.city (id, state_id, name)
values (1217634, 1217633, 'Pfaffenhofen an der Glonn')
on conflict do nothing;
insert into constants.city (id, state_id, name)
values (1217647, 1217633, 'Sulzemoos')
on conflict do nothing;
insert into constants.city (id, state_id, name)
values (1217660, 1217659, 'Berlin')
on conflict do nothing;

insert into constants.district (id, city_id, name, latitude, longitude)
values (1217635, 1217634, 'Bayerzell', 48.280675, 11.130375)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217637, 1217634, 'Ebersried', 48.282375, 11.145963)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217638, 1217634, 'Egenburg', 48.292576, 11.152676)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217639, 1217634, 'Kaltenbach', 48.288498, 11.136034)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217640, 1217634, 'Miesberg', 48.31025, 11.128491)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217641, 1217634, 'Oberumbach', 48.311553, 11.148289)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217642, 1217634, 'Pfaffenhofen a d Glonn', 48.295689, 11.163319)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217643, 1217634, 'Stockach', 48.304813, 11.135655)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217644, 1217634, 'Unterumbach', 48.31402, 11.164479)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217645, 1217634, 'Wagenhofen', 48.301765, 11.173116)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217646, 1217634, 'Weitenried', 48.292633, 11.126498)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217648, 1217647, 'Eichenhof', 48.286829, 11.269799)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217650, 1217647, 'Haidhof', 48.280158, 11.263356)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217651, 1217647, 'Hilpertsried', 48.275662, 11.240886)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217652, 1217647, 'Lederhof', 48.280978, 11.260536)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217653, 1217647, 'Lindenhof', 48.310108, 11.242592)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217654, 1217647, 'Oberwinden', 48.279118, 11.236258)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217655, 1217647, 'Orthofen', 48.309029, 11.234782)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217656, 1217647, 'Sulzemoos', 48.290101, 11.262828)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217657, 1217647, 'Unterwinden', 48.281591, 11.237921)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217658, 1217647, 'Ziegelstadel', 48.287982, 11.256479)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217661, 1217660, 'Adlershof', 52.427559, 13.522499)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217664, 1217660, 'Altglienicke', 52.426278, 13.515199)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217666, 1217660, 'Alt-Hohenschönhausen', 52.561918, 13.493258)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217670, 1217660, 'Alt-Treptow', 52.489965, 13.457668)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217672, 1217660, 'Baumschulenweg', 52.469563, 13.472531)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217674, 1217660, 'Biesdorf', 52.505205, 13.550876)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217676, 1217660, 'Blankenburg', 52.583833, 13.469032)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217678, 1217660, 'Blankenfelde', 52.633856, 13.420757)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217681, 1217660, 'Bohnsdorf', 52.422798, 13.532762)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217683, 1217660, 'Borsigwalde', 52.585011, 13.301963)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217685, 1217660, 'Britz', 52.464305, 13.399226)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217692, 1217660, 'Buch', 52.630354, 13.497826)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217694, 1217660, 'Buchholz', 52.6018, 13.420471)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217695, 1217660, 'Buckow', 52.41328, 13.406308)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217699, 1217660, 'Charlottenburg', 52.512368, 13.324708)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217727, 1217660, 'Dahlem', 52.452543, 13.263103)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217731, 1217660, 'Falkenberg', 52.567416, 13.551416)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217733, 1217660, 'Französisch Buchholz', 52.594157, 13.43016)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217735, 1217660, 'Friedenau', 52.496302, 13.49201)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217741, 1217660, 'Friedrichsfelde', 52.506214, 13.49741)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217746, 1217660, 'Friedrichshagen', 52.462834, 13.595961)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217748, 1217660, 'Friedrichshain', 52.51183, 13.439777)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217753, 1217660, 'Frohnau', 52.62779, 13.292088)
on conflict do nothing;
insert into constants.district (id, city_id, name, latitude, longitude)
values (1217755, 1217660, 'Gatow', 52.479134, 13.182692)
on conflict do nothing;

insert into constants.zipcode (id, city_id, zipcode)
values (1217636, 1217634, '85235')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217649, 1217647, '85254')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217662, 1217660, '12487')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217663, 1217660, '12489')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217665, 1217660, '12524')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217667, 1217660, '13051')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217668, 1217660, '13053')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217669, 1217660, '13055')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217671, 1217660, '12435')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217673, 1217660, '12437')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217675, 1217660, '12683')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217677, 1217660, '13129')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217679, 1217660, '13127')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217680, 1217660, '13159')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217682, 1217660, '12526')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217684, 1217660, '13509')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217686, 1217660, '12099')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217687, 1217660, '12347')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217688, 1217660, '12349')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217689, 1217660, '12351')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217690, 1217660, '12359')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217691, 1217660, '13587')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217693, 1217660, '13125')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217696, 1217660, '12305')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217697, 1217660, '12353')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217698, 1217660, '12357')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217700, 1217660, '10557')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217701, 1217660, '10585')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217702, 1217660, '10587')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217703, 1217660, '10589')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217704, 1217660, '10623')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217705, 1217660, '10625')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217706, 1217660, '10627')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217707, 1217660, '10629')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217708, 1217660, '10707')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217709, 1217660, '10709')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217710, 1217660, '10711')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217711, 1217660, '10719')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217712, 1217660, '10785')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217713, 1217660, '10787')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217714, 1217660, '10789')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217715, 1217660, '12589')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217716, 1217660, '12623')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217717, 1217660, '13088')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217718, 1217660, '13347')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217719, 1217660, '13353')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217720, 1217660, '13467')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217721, 1217660, '13597')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217722, 1217660, '13627')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217723, 1217660, '14050')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217724, 1217660, '14055')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217725, 1217660, '14057')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217726, 1217660, '14059')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217728, 1217660, '14169')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217729, 1217660, '14193')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217730, 1217660, '14195')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217732, 1217660, '13057')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217734, 1217660, '13158')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217736, 1217660, '12157')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217737, 1217660, '12159')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217738, 1217660, '12161')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217739, 1217660, '12163')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217740, 1217660, '14197')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217742, 1217660, '10315')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217743, 1217660, '10317')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217744, 1217660, '10318')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217745, 1217660, '10319')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217747, 1217660, '12587')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217749, 1217660, '10243')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217750, 1217660, '10245')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217751, 1217660, '10247')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217752, 1217660, '10249')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217754, 1217660, '13465')
on conflict do nothing;
insert into constants.zipcode (id, city_id, zipcode)
values (1217756, 1217660, '14089')
on conflict do nothing;

insert into constants.district_zipcode (district_id, zipcode_id)
values (1217635, 1217636)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217637, 1217636)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217638, 1217636)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217639, 1217636)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217640, 1217636)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217641, 1217636)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217642, 1217636)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217643, 1217636)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217644, 1217636)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217645, 1217636)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217646, 1217636)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217648, 1217649)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217650, 1217649)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217651, 1217649)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217652, 1217649)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217653, 1217649)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217654, 1217649)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217655, 1217649)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217656, 1217649)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217657, 1217649)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217658, 1217649)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217661, 1217662)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217661, 1217663)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217664, 1217665)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217666, 1217667)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217666, 1217668)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217666, 1217669)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217670, 1217671)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217672, 1217673)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217672, 1217662)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217674, 1217675)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217676, 1217667)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217676, 1217677)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217678, 1217679)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217678, 1217680)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217681, 1217682)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217683, 1217684)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217685, 1217686)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217685, 1217687)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217685, 1217688)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217685, 1217689)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217685, 1217690)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217685, 1217691)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217692, 1217693)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217694, 1217679)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217695, 1217696)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217695, 1217688)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217695, 1217689)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217695, 1217697)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217695, 1217698)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217695, 1217690)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217700)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217701)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217702)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217703)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217704)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217705)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217706)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217707)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217708)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217709)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217710)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217711)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217712)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217713)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217714)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217715)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217716)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217717)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217718)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217719)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217720)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217721)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217722)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217723)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217724)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217725)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217699, 1217726)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217727, 1217728)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217727, 1217729)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217727, 1217730)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217731, 1217732)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217733, 1217679)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217733, 1217734)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217735, 1217736)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217735, 1217737)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217735, 1217738)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217735, 1217739)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217735, 1217740)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217741, 1217742)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217741, 1217743)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217741, 1217744)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217741, 1217745)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217746, 1217747)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217748, 1217749)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217748, 1217750)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217748, 1217751)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217748, 1217752)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217753, 1217754)
on conflict do nothing;
insert into constants.district_zipcode (district_id, zipcode_id)
values (1217755, 1217756)
on conflict do nothing;

insert into landlord.credential (id, customer_id, portal, name, properties, created, updated, encrypted)
values (5000001, 1000016, 'IMMOBILIENSCOUT24_DE', 'immomio demo', '{
  "TOKEN": "8woeZ/gdyDYtxhYI+UL5rM+Ebf/R1KU/GI5Qa1z9YfzDjhEn1tgVnfZWwGlYLA9m",
  "TOKEN_SECRET": "wZ9DhMvrfmUBrxktCOEhlHy4RxTtPDwj+SQnlUH/IzhycVXcQDTvmF0pJiH26dxZ5C/oz6HEUBqcVm8pHKMS1DHKgKHWyIV7OugoEltbNrdcsl6S0U/4BqKZhYSOPCdhE4H98hh9cWaoq8ZzI+gNag=="
}', NOW(), NOW(), true)
on conflict do nothing;
insert into landlord.credential (id, customer_id, portal, name, properties, created, updated, encrypted)
values (5000002, 1000016, 'IMMOWELT_DE', 'Immowelt ', '{
  "PASSWORD": "yQdgUBTIL6dQUZFT7CV1iA==",
  "USERNAME": "Rh+RBhIJsvp8Ju3boUx6sA=="
}', NOW(), NOW(), true)
on conflict do nothing;


insert into landlord.propertyportal (id, property_id, state, activated, deactivated, created, updated, credential_id,
                                     portal)
values (4000001, 1084251, 'DEACTIVATED', null, null, NOW(), NOW(), 5000002, 'IMMOWELT_DE')
on conflict do nothing;
insert into landlord.propertyportal (id, property_id, state, activated, deactivated, created, updated, credential_id,
                                     portal)
values (4000002, 1084251, 'DEACTIVATED', null, null, NOW(), NOW(), 5000001, 'IMMOBILIENSCOUT24_DE')
on conflict do nothing;



insert into propertysearcher.customer(id, paymentmethods, files, location, created, updated, paymentdetails)
values (2000001, '[
  {
    "method": "INVOICE",
    "preferred": true
  }
]', '[]', 'DE', NOW(), NOW(), '{}')
on conflict do nothing;

insert into propertysearcher.customer(id, paymentmethods, files, location, created, updated, paymentdetails)
values (1, '[
  {
    "method": "INVOICE",
    "preferred": true
  }
]', '[]', 'DE', NOW(), NOW(), '{}')
on conflict do nothing;

insert into propertysearcher.customer(id, paymentmethods, files, location, created, updated, paymentdetails)
values (2, '[
  {
    "method": "INVOICE",
    "preferred": true
  }
]', '[]', 'DE', NOW(), NOW(), '{}')
on conflict do nothing;

insert into propertysearcher.customer(id, paymentmethods, files, location, created, updated, paymentdetails)
values (3, '[
  {
    "method": "INVOICE",
    "preferred": true
  }
]', '[]', 'DE', NOW(), NOW(), '{}')
on conflict do nothing;

insert into propertysearcher."user" (id, email, customer_id, enabled, type, lastlogin, created, updated, email_verified,
                                     rented_applications_fetched)
values (2000002, 'mbaumbach@immomio.de', 2000001, true, 'REGISTERED', NOW(), NOW(), NOW(), NOW(), NOW())
on conflict do nothing;

insert into propertysearcher.user_profile (id, user_id, email, type, created, updated, address, data, search_until,
                                           customer_tenant_pool_id)
values (2000002, 2000002, 'mbaumbach@immomio.de', 'MAIN', NOW(), NOW(), '{}', '{
  "householdType": "COUPLE_WITHOUT_CHILDREN",
  "personalStatus": "MARRIED",
  "residents": 1,
  "dateOfBirth": null,
  "moveInDate": null,
  "profession": {
    "type": "EMPLOYED_UNLIMITED",
    "subType": "Programmer",
    "income": 3000.0,
    "employmentDate": null
  },
  "creditScreening": {
    "available": false,
    "value": null
  },
  "law": {
    "noRentArrears": true,
    "noPoliceRecord": true,
    "noTenancyLawConflicts": true,
    "informationTrueAndComplete": true,
    "allowSchufa": true
  },
  "smoker": {
    "smoker": false,
    "inhouse": false
  },
  "additionalInformation": {
    "animals": false,
    "bailment": false,
    "music": false,
    "wbs": false
  },
  "firstname": "Niklas",
  "name": "Lindemann",
  "phone": "17612345678",
  "gender": "FEMALE",
  "title": null,
  "portrait": null
}', NOW() + interval '8 weeks', null) on conflict do nothing;

insert into propertysearcher."user" (id, email, customer_id, enabled, type, lastlogin, created, updated, email_verified,
                                     rented_applications_fetched)
values (1, 'nlindemann@immomio.de', 1, true, 'REGISTERED', NOW(), NOW(), NOW(), NOW(), NOW())
on conflict do nothing;

insert into propertysearcher.user_profile (id, user_id, email, type, created, updated, address, data, search_until,
                                           customer_tenant_pool_id)
values (1, 1, 'nlindemann@immomio.de', 'MAIN', NOW(), NOW(), '{}', '{
  "householdType": "COUPLE_WITHOUT_CHILDREN",
  "personalStatus": "MARRIED",
  "residents": 1,
  "dateOfBirth": null,
  "moveInDate": null,
  "profession": {
    "type": "EMPLOYED_UNLIMITED",
    "subType": "Programmer",
    "income": 3000.0,
    "employmentDate": null
  },
  "creditScreening": {
    "available": false,
    "value": null
  },
  "law": {
    "noRentArrears": true,
    "noPoliceRecord": true,
    "noTenancyLawConflicts": true,
    "informationTrueAndComplete": true,
    "allowSchufa": true
  },
  "smoker": {
    "smoker": false,
    "inhouse": false
  },
  "additionalInformation": {
    "animals": false,
    "bailment": false,
    "music": false,
    "wbs": false
  },
  "firstname": "Niklas",
  "name": "Lindemann",
  "phone": "17612345678",
  "gender": "FEMALE",
  "title": null,
  "portrait": null
}', NOW() + interval '8 weeks', null) on conflict do nothing;

insert into propertysearcher."user" (id, email, customer_id, enabled, type, lastlogin, created, updated, email_verified,
                                     rented_applications_fetched)
values (2, 'fsawma@immomio.de', 2, true, 'REGISTERED', NOW(), NOW(), NOW(), NOW(), NOW())
on conflict do nothing;

insert into propertysearcher.user_profile (id, user_id, email, type, created, updated, address, data, search_until,
                                           customer_tenant_pool_id)
values (2, 2, 'fsawma@immomio.de', 'MAIN', NOW(), NOW(), '{}', '{
  "householdType": "COUPLE_WITHOUT_CHILDREN",
  "personalStatus": "MARRIED",
  "residents": 1,
  "dateOfBirth": null,
  "moveInDate": null,
  "profession": {
    "type": "EMPLOYED_UNLIMITED",
    "subType": "Programmer",
    "income": 3000.0,
    "employmentDate": null
  },
  "creditScreening": {
    "available": false,
    "value": null
  },
  "law": {
    "noRentArrears": true,
    "noPoliceRecord": true,
    "noTenancyLawConflicts": true,
    "informationTrueAndComplete": true,
    "allowSchufa": true
  },
  "smoker": {
    "smoker": false,
    "inhouse": false
  },
  "additionalInformation": {
    "animals": false,
    "bailment": false,
    "music": false,
    "wbs": false
  },
  "firstname": "Freddy",
  "name": "Sawma",
  "phone": "17612345678",
  "gender": "FEMALE",
  "title": null,
  "portrait": null
}', NOW() + interval '8 weeks', null) on conflict do nothing;

insert into propertysearcher."user" (id, email, customer_id, enabled, type, lastlogin, created, updated, email_verified,
                                     rented_applications_fetched)
values (3, 'vnavozenko@immomio.de', 3, true, 'REGISTERED', NOW(), NOW(), NOW(), NOW(), NOW())
on conflict do nothing;

insert into propertysearcher.user_profile (id, user_id, email, type, created, updated, address, data, search_until,
                                           customer_tenant_pool_id)
values (3, 3, 'vnavozenko@immomio.de', 'MAIN', NOW(), NOW(), '{}', '{
  "householdType": "COUPLE_WITHOUT_CHILDREN",
  "personalStatus": "MARRIED",
  "residents": 1,
  "dateOfBirth": null,
  "moveInDate": null,
  "profession": {
    "type": "EMPLOYED_UNLIMITED",
    "subType": "Programmer",
    "income": 3000.0,
    "employmentDate": null
  },
  "creditScreening": {
    "available": false,
    "value": null
  },
  "law": {
    "noRentArrears": true,
    "noPoliceRecord": true,
    "noTenancyLawConflicts": true,
    "informationTrueAndComplete": true,
    "allowSchufa": true
  },
  "smoker": {
    "smoker": false,
    "inhouse": false
  },
  "additionalInformation": {
    "animals": false,
    "bailment": false,
    "music": false,
    "wbs": false
  },
  "firstname": "Valeriy",
  "name": "Navozenko",
  "phone": 17612345678,
  "gender": "FEMALE",
  "title": null,
  "portrait": null
}', NOW() + interval '8 weeks', null) on conflict do nothing;

insert into propertysearcher.prospectoptin (id, user_id, opt_in_for_prospect, created, updated)
values (2000000, 2000002, true, NOW(), null)
on conflict do nothing;

insert into propertysearcher.prospectoptin (id, user_id, opt_in_for_prospect, created, updated)
values (1, 1, true, NOW(), null)
on conflict do nothing;

insert into propertysearcher.prospectoptin (id, user_id, opt_in_for_prospect, created, updated)
values (2, 2, true, NOW(), null)
on conflict do nothing;

insert into propertysearcher.prospectoptin (id, user_id, opt_in_for_prospect, created, updated)
values (3, 3, true, NOW(), null)
on conflict do nothing;


insert into landlord.credential (id, customer_id, portal, name, properties, created, updated, encrypted)
values (1310327, 1000016, 'IMMOWELT_DE', 'Immowelt ', '{
  "PASSWORD": "yQdgUBTIL6dQUZFT7CV1iA==",
  "USERNAME": "Rh+RBhIJsvp8Ju3boUx6sA=="
}', NOW(), NOW(), true)
on conflict do nothing;

insert into landlord.credential (id, customer_id, portal, name, properties, created, updated, encrypted)
values (1338446, 1000016, 'IMMOBILIENSCOUT24_DE', 'immomio demo', '{
  "TOKEN": "8woeZ/gdyDYtxhYI+UL5rM+Ebf/R1KU/GI5Qa1z9YfzDjhEn1tgVnfZWwGlYLA9m",
  "TOKEN_SECRET": "wZ9DhMvrfmUBrxktCOEhlHy4RxTtPDwj+SQnlUH/IzhycVXcQDTvmF0pJiH26dxZ5Cmmz/oz6HEUBqcVm8pHKMS1DHKgKHWyIV7OugoEltbNrdcsl6S0U/4BqKZhYSOPCdhE4H98hh9cWaoq8ZzI+gNag=="
}', NOW(), NOW(), true)
on conflict do nothing;

insert into landlord.credential (id, customer_id, portal, name, properties, created, updated, encrypted)
values (5000001, 1000016, 'IMMOBILIENSCOUT24_DE', 'immomio demo', '{
  "TOKEN": "8woeZ/gdyDYtxhYI+UL5rM+Ebf/R1KU/GI5Qa1z9YfzDjhEn1tgVnfZWwGlYLA9m",
  "TOKEN_SECRET": "wZ9DhMvrfmUBrxktCOEhlHy4RxTtPDwj+SQnlUH/IzhycVXcQDTvmF0pJiH26dxZ5C/oz6HEUBqcVm8pHKMS1DHKgKHWyIV7OugoEltbNrdcsl6S0U/4BqKZhYSOPCdhE4H98hh9cWaoq8ZzI+gNag=="
}', NOW(), NOW(), true)
on conflict do nothing;

insert into landlord.credential (id, customer_id, portal, name, properties, created, updated, encrypted)
values (5000002, 1000016, 'IMMOWELT_DE', 'Immowelt ', '{
  "PASSWORD": "yQdgUBTIL6dQUZFT7CV1iA==",
  "USERNAME": "Rh+RBhIJsvp8Ju3boUx6sA=="
}', NOW(), NOW(), true)
on conflict do nothing;


insert into landlord.propertyportal (id, property_id, state, activated, deactivated, created, updated, credential_id,
                                     portal)
values (4000001, 1084251, 'DEACTIVATED', null, null, NOW(), NOW(), 5000002, 'IMMOWELT_DE')
on conflict do nothing;

insert into landlord.propertyportal (id, property_id, state, activated, deactivated, created, updated, credential_id,
                                     portal)
values (4000002, 1084251, 'ACTIVE', null, null, NOW(), NOW(), 5000001, 'IMMOBILIENSCOUT24_DE')
on conflict do nothing;


insert into administration."user" (id, email, password, enabled, expired, locked, lastlogin, created, updated, type)
values (2, 'importer@immomio.de', '$2a$10$BFdOu1/.asZ8CV0Gug6RueN.KDy.2X3iDU.bay2FImUKRLvYtw0n6', true, false, false,
        NOW(), NOW(), NOW(), 'SERVICE')
on conflict do nothing;

insert into administration."user" (id, email, password, enabled, expired, locked, lastlogin, created, updated, type)
values (1, 'admin@immomio.de', '$2a$10$BFdOu1/.asZ8CV0Gug6RueN.KDy.2X3iDU.bay2FImUKRLvYtw0n6', true, false, false,
        NOW(), NOW(), NOW(), 'ROOT')
on conflict do nothing;

insert into propertysearcher.searchprofile (id, user_profile_id, data, created, updated, location, property_id, manuallycreated,
                                            deleted)
values (2147132, 2000002, '{
  "name": "Auto-Generated: 12045, Hüttenroder Weg",
  "rent": {
    "lowerBound": 0.0,
    "upperBound": 873.9999999999999
  },
  "size": {
    "lowerBound": 48.0,
    "upperBound": "Infinity"
  },
  "rooms": {
    "lowerBound": 1.5,
    "upperBound": "Infinity"
  },
  "radius": 5000,
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Hüttenroder Weg",
    "country": "DE",
    "zipCode": "12045",
    "additional": null,
    "coordinates": {
      "latitude": 52.48539,
      "longitude": 13.4460801
    },
    "houseNumber": "6"
  }
}', NOW(), NOW(), '0101000020E6100000F6CA619C64E42A4019E76F42213E4A40', 1084251, false, false)
on conflict do nothing;

insert into propertysearcher.searchprofile (id, user_profile_id, data, created, updated, location, property_id, manuallycreated,
                                            deleted)
values (1, 2, '{
  "name": "Auto-Generated: 12045, Hüttenroder Weg",
  "rent": {
    "lowerBound": 0.0,
    "upperBound": 873.9999999999999
  },
  "size": {
    "lowerBound": 48.0,
    "upperBound": "Infinity"
  },
  "rooms": {
    "lowerBound": 1.5,
    "upperBound": "Infinity"
  },
  "radius": 5000,
  "address": {
    "city": "Berlin",
    "region": "Berlin",
    "street": "Hüttenroder Weg",
    "country": "DE",
    "zipCode": "12045",
    "additional": null,
    "coordinates": {
      "latitude": 52.48539,
      "longitude": 13.4460801
    },
    "houseNumber": "6"
  }
}', NOW(), NOW(), '0101000020E6100000F6CA619C64E42A4019E76F42213E4A40', 1084251, false, false)
on conflict do nothing;

insert into shared.propertyproposal (id, user_profile_id, property_id, score, state, created, updated, searchprofile_id)
values (1, 2000002, 1084251, 7, 'PROSPECT', NOW(), NOW(), 2147132)
on conflict do nothing;

insert into shared.propertyproposal (id, user_profile_id, property_id, score, state, created, updated, searchprofile_id,
                                     offered)
values (2, 1, 1084251, 7, 'OFFERED', NOW(), NOW(), 2147132, NOW())
on conflict do nothing;

insert into shared.propertyproposal (id, user_profile_id, property_id, score, state, created, updated, searchprofile_id,
                                     offered)
values (3, 2, 1084251, 7, 'OFFERED', NOW(), NOW(), 2147132, NOW())
on conflict do nothing;

insert into shared.discount (id, name, startdate, enddate, created, updated, value)
values (1, 'Baba Discount', '2019-01-10 11:47:28.299000', '2020-01-10 11:47:30.022000', '2018-12-10 11:47:39.840000',
        '2019-01-10 11:47:51.881000', 0.5)
on conflict do nothing;

insert into shared.appointment (id, date, state, maxinviteecount, showcontactinformation, property_id, created, updated,
                                contact)
values (1, NOW() + interval '1 year', 'ACTIVE', 10, false, 1084251, NOW(), NOW(), null)
on conflict do nothing;

insert into landlord.discount_customer (discount_id, customer_id)
values (1, 1000016)
on conflict do nothing;

insert into landlord.messagesource (id, customer_id, messagekey, locale, value)
values (1008179, 1000016, 'application.rejected.head', 'de_DE', 'A')
on conflict do nothing;
insert into landlord.messagesource (id, customer_id, messagekey, locale, value)
values (1008177, 1000016, 'application.rejected.text', 'de_DE', 'B')
on conflict do nothing;
insert into landlord.messagesource (id, customer_id, messagekey, locale, value)
values (1008178, 1000016, 'application.rejected.text1', 'de_DE', 'C')
on conflict do nothing;
insert into landlord.messagesource (id, customer_id, messagekey, locale, value)
values (1008174, 1000016, 'general.footer.address', 'de_DE', 'D')
on conflict do nothing;
insert into landlord.messagesource (id, customer_id, messagekey, locale, value)
values (1008171, 1000016, 'general.footer.company', 'de_DE', 'E')
on conflict do nothing;
insert into landlord.messagesource (id, customer_id, messagekey, locale, value)
values (1008176, 1000016, 'general.footer.disclaimer', 'de_DE', 'F')
on conflict do nothing;
insert into landlord.messagesource (id, customer_id, messagekey, locale, value)
values (1008173, 1000016, 'general.footer.thanks', 'de_DE', 'G')
on conflict do nothing;
insert into landlord.messagesource (id, customer_id, messagekey, locale, value)
values (1008172, 1000016, 'general.salutation', 'de_DE', 'H')
on conflict do nothing;
insert into landlord.messagesource (id, customer_id, messagekey, locale, value)
values (1008169, 1000016, 'invitation.decline.notification.button', 'de_DE', 'I')
on conflict do nothing;
insert into landlord.messagesource (id, customer_id, messagekey, locale, value)
values (1008175, 1000016, 'invitation.decline.notification.footer', 'de_DE', 'Maik sind, die auch zugesagt haben.')
on conflict do nothing;
insert into landlord.messagesource (id, customer_id, messagekey, locale, value)
values (1008166, 1000016, 'invitation.decline.notification.head', 'de_DE', 'J')
on conflict do nothing;
insert into landlord.messagesource (id, customer_id, messagekey, locale, value)
values (1008167, 1000016, 'invitation.decline.notification.intro', 'de_DE', 'K')
on conflict do nothing;
insert into landlord.messagesource (id, customer_id, messagekey, locale, value)
values (1008168, 1000016, 'invitation.decline.notification.intro2', 'de_DE', 'L')
on conflict do nothing;
insert into landlord.messagesource (id, customer_id, messagekey, locale, value)
values (1008170, 1000016, 'invitation.decline.notification.main', 'de_DE', 'M')
on conflict do nothing;

insert into shared.application (id, score, status, property_id, user_profile_id, created, updated, text, portal)
values (1000002, 7.12, 'UNANSWERED', 1084251, 2000002, NOW(), NOW(), null, null)
on conflict do nothing;
insert into shared.application (id, score, status, property_id, user_profile_id, created, updated, text, portal)
values (1000007, 7.37, 'UNANSWERED', 1084228, 2000002, NOW(), NOW(), null, null)
on conflict do nothing;
insert into shared.application (id, score, status, property_id, user_profile_id, created, updated, text, portal)
values (1000010, 7.35, 'UNANSWERED', 1000162, 2000002, NOW(), NOW(), null, null)
on conflict do nothing;
insert into shared.application (id, score, status, property_id, user_profile_id, created, updated, text, portal)
values (1000014, 7.67, 'UNANSWERED', 1110605, 2000002, NOW(), NOW(), null, null)
on conflict do nothing;
insert into shared.application (id, score, status, property_id, user_profile_id, created, updated, text, portal)
values (1000017, 7.05, 'UNANSWERED', 1084245, 2000002, NOW(), NOW(), null, null)
on conflict do nothing;
insert into shared.application (id, score, status, property_id, user_profile_id, created, updated, text, portal)
values (1000020, 6.51, 'UNANSWERED', 1105131, 2000002, NOW(), NOW(), null, null)
on conflict do nothing;
insert into shared.application (id, score, status, property_id, user_profile_id, created, updated, text, portal)
values (1000023, 0, 'UNANSWERED', 1110629, 2000002, NOW(), NOW(), null, null)
on conflict do nothing;

insert into shared.appointment_acceptance (id, state, appointment_id, created, updated, application_id, email_sent)
values (1, 'ACTIVE', 1, NOW(), NOW(), 1000002, false)
on conflict do nothing;

insert into landlord.ftp_access (id, userpassword, homedirectory, enabled, writepermission, idletime, uploadrate,
                                 downloadrate, maxloginnumber, maxloginperip, customer_id)
values (1, 'r48lda', '/1000016', true, true, 100, -1, 0, 1, 1, 1000016)
on conflict do nothing;

insert into landlord.schufa_account (id, customer_id, username, password, created, updated)
values (1000001, 1000016, '6FzojYq6tPV/bx52rCfMkg==', '1lVKod6KAisfdnzJ695uIA==', '2019-07-05 15:06:20.125000',
        '2019-07-05 15:06:20.125000')
on conflict do nothing;

insert into landlord.digital_contract_api_user (id, customer_id, docusign_api_user_id, created, updated)
values (1000003, 1000016, '94a6a175-3442-4418-bd8f-9b993cb4969f', '2020-03-30 16:41:54.872000',
        '2020-03-30 16:41:54.872000')
on conflict do nothing;