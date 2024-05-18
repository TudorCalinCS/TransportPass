package repository;

import domain.Bilet;

public interface IRepoBilet extends IRepository<Integer, Bilet>{
    public void deleteBilete();
}
