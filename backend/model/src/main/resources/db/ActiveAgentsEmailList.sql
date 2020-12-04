SELECT email, profile -> 'name' as Nachname, profile -> 'firstname' as name, customer_id ,type from landlord."user"
where customer_id in (SELECT customer_id from landlord.customerproduct cp WHERE cp.duedate > now() AND enabled = true)
  AND email NOT LIKE '%immomio.de' AND email not like '%employee.de'
ORDER BY customer_id asc;