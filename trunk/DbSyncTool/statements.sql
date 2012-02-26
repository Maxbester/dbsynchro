s:bdd1:SELECT o.num, o.date FROM orders o WHERE o.state = 'sent';
d:bdd2:INSERT INTO oldOrders (id,date) VALUES ('[1]','[2]');
s:bdd1:SELECT o.num, o.date FROM orders o WHERE o.state = 'received';
d:bdd2:INSERT INTO newOrders (id) VALUES ('[1]');