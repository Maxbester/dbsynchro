SELECT * FROM items i WHERE i.id=[SELECT p.id FROM products p where p.stockleve=3] LIMIT 1,100;

UPDATE items SET  stocklevel=[products.stocklevel] WHERE stockcode=[products.stockcode];
