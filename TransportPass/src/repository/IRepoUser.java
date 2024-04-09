package repository;

import domain.User;

public interface IRepoUser extends IRepository<Long, User>{
    public User findOneByUsernameAndPassword(String username, String password);
}
