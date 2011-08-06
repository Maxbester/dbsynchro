-- we select information about the products in the source database
s: SELECT products.stockcode, products.stocklevel, products.name FROM products;
-- we update the stock level on the distant database
d: UPDATE items SET  stock_level=[2], name="[3]" WHERE stock_code=[1];
-- we insert product names into the table test2
d: INSERT INTO test2 (name) value ("[3]");
