package org.example.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceTemp {

    private final RepositoryTemp repositoryTemp;

    @Autowired
    public ServiceTemp(RepositoryTemp repositoryTemp) {
        this.repositoryTemp = repositoryTemp;
    }

    public String findId(String itemId) {
        return repositoryTemp.findById(itemId);
    }
}
