s:MS_Server: SELECT products.stockcode, products.stocklevel, products.name FROM products;
d:RedHat_Server: UPDATE items SET  stock_level=[2], item_name="[3]" WHERE stock_code=[1];
d:Debian_Server: UPDATE prod SET  stock_level=[2]+1, item_name="TEST" WHERE stock_code=[1];
d:RedHat_Server: insert into test (name) values ('[3]');
s:MS_Server: SELECT products.stockcode, products.stocklevel FROM products;
d:Debian_Server: UPDATE prod SET  stock_level=[2]+1, item_name="TEST" WHERE stock_code=[1];
