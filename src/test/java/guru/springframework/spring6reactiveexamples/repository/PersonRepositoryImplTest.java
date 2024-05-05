package guru.springframework.spring6reactiveexamples.repository;

import guru.springframework.spring6reactiveexamples.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void testMonoByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();
        System.out.println(person);
    }

    @Test
    void testGetByIdSubscriber() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.subscribe(System.out::println);
    }

    @Test
    void testMapOperation() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.map(Person::getFirstName).subscribe(System.out::println);
    }

    @Test
    void testFluxBlockFirst() {
        Flux<Person> personFlux = personRepository.findAll();

        Person person = personFlux.blockFirst();
        System.out.println(person);
    }

    @Test
    void testFluxSubscribe() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.subscribe(System.out::println);
    }

    @Test
    void testMapOperationFlux() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.map(Person::getFirstName).subscribe(System.out::println);
    }

    @Test
    void testFluxToList() {
        Flux<Person> personFlux = personRepository.findAll();

        Mono<List<Person>> personList = personFlux.collectList();
        personList.subscribe(people -> people.forEach(System.out::println));
    }

    @Test
    void filterOnName() {
        Flux<Person> personFlux = personRepository.findAll();
        personFlux.filter(person -> person.getFirstName().equals("Fiona")).subscribe(System.out::println);
    }

    @Test
    void testGetById() {
        Mono<Person> fionaMono = personRepository.findAll().filter(person -> person.getFirstName().equals("Fiona")).next();

        fionaMono.subscribe(System.out::println);
    }

    @Test
    void testByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 8;

        Mono<Person> personMono = personFlux.filter(person -> person.getId().equals(id))
                .single()
                .doOnError(throwable -> {
                    System.out.println("Error occurred in flux");
                    System.out.println(throwable.toString());
                });

        personMono.subscribe(System.out::println, throwable -> {
            System.out.println("Error occurred in mono");
            System.out.println(throwable.toString());
        });
    }

    @Test
    void testGetByIdFound() {
        Mono<Person> personMono = personRepository.getById(3);
        assertTrue(personMono.hasElement().block());
    }

    @Test
    void testGetByIdFoundStepVerifier() {
        Mono<Person> personMono = personRepository.getById(3);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();
        personMono.subscribe(System.out::println);
    }

    @Test
    void testGetByIdNotFound() {
        Mono<Person> personMono = personRepository.getById(6);
        assertFalse(personMono.hasElement().block());
    }

    @Test
    void testGetByIdNotFoundStepVerifier() {
        Mono<Person> personMono = personRepository.getById(6);

        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();
        personMono.subscribe(System.out::println);
    }

}