package com.robot.hotel.repository;

import com.robot.hotel.domain.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
//    Цей файл міститиме інтерфейс репозиторію для роботи з об'єктами відвідувачів.
//    Цей інтерфейс повинен містити методи для зберігання інформації про відвідувачів,
//    пошуку відвідувачів за прізвищем або номером паспорту, оновлення даних відвідувачів тощо.
//    Visitor save(Visitor visitor) - метод для збереження інформації про нового відвідувача.
//    void delete(Visitor visitor) - метод для видалення інформації про відвідувача.
//    Optional<Visitor> findById(Long id) - метод для отримання інформації про відвідувача за ідентифікатором.
//    Optional<Visitor> findByPassportNumber(String passportNumber) - метод для отримання інформації про відвідувача за номером паспорту.List<Visitor> findAll() - метод для отримання списку всіх відвідувачів.
    Optional<Guest> findByLastName(String lastname);
    Optional<Guest> findByPassportNumber(String passportNumber);
}
