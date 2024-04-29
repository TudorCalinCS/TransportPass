CREATE TABLE User(
                     id INTEGER PRIMARY KEY,
                     email VARCHAR(50),
                     parola VARCHAR(50),
                     nume VARCHAR(50),
                     prenume VARCHAR(50),
                     CNP VARCHAR(20)
);
CREATE TABLE Client(
                       statut VARCHAR(20),
                       userId INTEGER,
                       FOREIGN KEY (userId) REFERENCES User(id)
);
CREATE TABLE Controlor(
                          legitimatie VARCHAR(50),
                          userId INTEGER,
                          FOREIGN KEY (userId) REFERENCES User(id)
);
CREATE TABLE Bilet(
                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                      dataIncepere DATETIME,
                      dataExpirare DATETIME,
                      pret DOUBLE,
                      tip VARCHAR(20),
                      idClient INT, FOREIGN KEY (idClient) REFERENCES Client(userId)
);
CREATE TABLE Abonament(
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          dataIncepere DATETIME,
                          dataExpirare DATETIME,
                          pret DOUBLE,
                          tip VARCHAR(20),
                          idClient INT,
                          FOREIGN KEY (idClient) REFERENCES Client(userId)
);
CREATE TABLE imagini (
                         id INTEGER PRIMARY KEY,
                         nume TEXT,
                         continut_imagine BLOB
);