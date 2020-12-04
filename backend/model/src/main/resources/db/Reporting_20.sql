Select count(*) from landlord."user" where created < '2019-04-01 00:00:00.00000' AND email not like '%@immomio.de';
Select count(*) from landlord."user" where created < '2019-04-01 00:00:00.00000' AND type = 'COMPANYADMIN' AND email not like '%@immomio.de';
Select count(*) from landlord."user" where created < '2019-04-01 00:00:00.00000' AND type = 'EMPLOYEE' AND email not like '%@immomio.de';
Select count(*) from landlord."user" where created < '2019-04-01 00:00:00.00000' AND type = 'HOTLINE' AND email not like '%@immomio.de';

SELECT count(*) from landlord.customer c where id in (SELECT customer_id from landlord."user" where created < '2019-04-01 00:00:00.00000' AND email not like '%@immomio.de' group by customer_id);


Select count(*) from propertysearcher."user" where created < '2019-04-01 00:00:00.00000' AND email not like '%@immomio.de';
Select count(*) from propertysearcher."user" where created < '2019-02-01 00:00:00.00000' AND email not like '%@immomio.de' AND lastlogin is not null AND status != 'ANONYMOUS';


Select count(*) from propertysearcher.searchprofile
where created < '2019-04-01 00:00:00.000000'
  and user_id in (Select id from propertysearcher."user" where created < '2019-04-01 00:00:00.00000' AND email not like '%@immomio.de');


Select sum(post_discount_price) from landlord.invoice
where customer_id in (
  SELECT id from landlord.customer c
  where id in (
             SELECT customer_id from landlord."user"
             where email not like '%@immomio.de' group by customer_id
             )
  ) AND invoice_date >= '2019-03-01 00:00:00.00000' AND invoice_date < '2019-04-01 00:00:00.00000';