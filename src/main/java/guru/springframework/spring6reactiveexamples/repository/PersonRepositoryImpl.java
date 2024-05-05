package guru.springframework.spring6reactiveexamples.repository;

import guru.springframework.spring6reactiveexamples.domain.Person;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

    Person ray = Person.builder().id(1).lastName("Mond").firstName("Ray").build();
    Person fiona = Person.builder().id(2).lastName("Glenanne").firstName("Fiona").build();
    Person april = Person.builder().id(3).lastName("Fooze").firstName("April").build();
    Person claire = Person.builder().id(4).lastName("Seducer").firstName("Claire").build();

    @Override
    public Mono<Person> getById(Integer id) {
        return findAll().filter(person -> person.getId().equals(id)).next();
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(ray, fiona, april, claire);
    }
}
