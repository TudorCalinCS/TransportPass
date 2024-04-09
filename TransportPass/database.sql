CREATE TABLE User(
                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                     email VARCHAR(50),
                     parola VARCHAR(50),
                     nume VARCHAR(50),
                     prenume VARCHAR(50),
                     cnp VARCHAR(20)
);
CREATE TABLE Client(
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       statut VARCHAR(20),
                       userId INTEGER,
                       FOREIGN KEY (userId) REFERENCES User(id)
);
CREATE TABLE CONTROLOR(
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          legitimatie VARCHAR(50),
                          userId INTEGER,
                          FOREIGN KEY (userId) REFERENCES User(id)
);
CREATE TABLE BILET(
                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                      dataIncepere DATETIME,
                      dataExpirare DATETIME,
                      pret DOUBLE,
                      tip VARCHAR(20),
                      idClient INT, FOREIGN KEY (idClient) REFERENCES Client(id)
);
CREATE TABLE ABONAMENT(
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          dataIncepere DATETIME,
                          dataExpirare DATETIME,
                          pret DOUBLE,
                          idClient INT,
                          FOREIGN KEY (idClient) REFERENCES Client(id)
);
