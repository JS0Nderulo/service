package ro.unibuc.hello.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * No need to implement this interface.
 * Spring Data MongoDB automatically creates a class it implementing the interface when you run the application.
 */
@Repository
public interface AvionRepository extends MongoRepository<Avion, String>, AvionCustomRepository {

    Avion findByNumber(String number);
    void deleteByNumber(String number);

}