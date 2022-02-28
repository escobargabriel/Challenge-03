CREATE TABLE IF NOT EXISTS products (
  id SERIAL,
  name varchar(255) NOT NULL,
  stock INT NOT NULL,
  price FLOAT NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS shoppingCart(
    idShoppingCart SERIAL,
    clientName varchar(255) NOT NULL,
    primary key (idShoppingCart)
);
CREATE TABLE IF NOT EXISTS shoppingList(
  idShopping SERIAL,
  IdCart INTEGER NOT NULL,
  idProduct INTEGER NOT NULL,
  quantity INTEGER NOT NULL,
  primary key (idShopping),
  foreign key (idProduct) REFERENCES products (id),
  foreign key (idCart) REFERENCES shoppingcart(idShoppingCart)
);

