package service;

import repository.*;

public class Service {
    private RepoClientDB repoClientDB;
    private RepoControlorDB repoControlorDB;
    private RepoUserDB repoUserDB;
    private RepoAbonamentDB repoAbonamentDB;
    private RepoBiletDB repoBiletDB;


    public Service(RepoClientDB repoClientDB, RepoControlorDB repoControlorDB, RepoUserDB repoUserDB, RepoAbonamentDB repoAbonamentDB, RepoBiletDB repoBiletDB) {
        this.repoClientDB = repoClientDB;
        this.repoControlorDB = repoControlorDB;
        this.repoUserDB = repoUserDB;
        this.repoAbonamentDB = repoAbonamentDB;
        this.repoBiletDB = repoBiletDB;
    }
}
