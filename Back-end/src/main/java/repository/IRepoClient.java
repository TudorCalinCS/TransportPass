package repository;

import domain.Client;

public interface IRepoClient extends IRepository<Integer, Client>{
    public Client findOneByEmailAndCNP(String email, String CNP);
}
