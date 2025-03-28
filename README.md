## Training Diary 
Разработайте приложение для ведения дневника тренировок, которое позволит пользователям записывать свои тренировки, просматривать их и анализировать свой прогресс в тренировках.

## Описание 
Тренировку одного типа можно заносить раз в день.
Пользователь может видеть только свои тренировки, администратор может видеть тренировки всех пользователей.
Создайте реализацию, которая соответствует описанным ниже требованиям и ограничениям.

## Требования 
- предусмотреть расширение перечня типов тренировок (решение через enum не принимается)
- регистрация пользователя
- авторизация пользователя
- реализовать возможность добавлять новые тренировки, включая дату, тип тренировки (например, кардио, силовая тренировка, йога), длительность тренировки и количество потраченных калорий. Также должна быть реализована возможность добавления дополнительной информации такой, как количество выполненных упражнений, пройденное расстояние и тд.
- реализовать возможность просматривать свои предыдущие тренировки, отсортированные по дате.
- реализовать возможность редактировать или удалять свои тренировки, если это необходимо.
- реализовать контроль прав пользователя
- реализовать возможность получения статистики по тренировкам (например количество потраченных калорий в разрезе времени)
- Аудит действий пользователя (авторизация, завершение работы, добавление, редактирование и удаление тренировок, получение статистики тренировок и тд)

## Нефункциональные требования 
Unit-тестирование
