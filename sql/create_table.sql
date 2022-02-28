CREATE TABLE IF NOT EXISTS products (
  id SERIAL,
  name varchar(255) NOT NULL,
  stock INT NOT NULL,
  price FLOAT NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS shoppingCart(
    idShoppingCart SERIAL,
    idProduct INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    primary key (idShoppingCart),
    foreign key (idProduct) REFERENCES products (id)
);
