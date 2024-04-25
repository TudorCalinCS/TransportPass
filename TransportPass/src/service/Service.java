package service;

import repository.RepoClientDB;
import repository.RepoControlorDB;
import repository.RepoUserDB;

public class Service {
    private RepoClientDB repoClientDB;
    private RepoControlorDB repoControlorDB;
    private RepoUserDB repoUserDB;

    public Service(RepoClientDB repoClientDB, RepoControlorDB repoControlorDB, RepoUserDB repoUserDB) {
        this.repoClientDB = repoClientDB;
        this.repoControlorDB = repoControlorDB;
        this.repoUserDB = repoUserDB;
    }

}
