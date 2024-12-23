# Task Planner App

**Task Planner App** — це Android-застосунок для планування завдань, розроблений за допомогою **Jetpack Compose**. Додаток дозволяє користувачу створювати, редагувати, сортувати та відмічати виконання завдань, а також синхронізувати їх із сервером.

## Основні функції:
- **Створення та управління завданнями**:
  - Додавання нових завдань із зазначенням назви, пріоритету та дати завершення.
  - Позначення завдань як виконаних.
  - Видалення та редагування завдань.

- **Сортування завдань**:
  - Можливість сортування завдань за пріоритетом.
  - Можливість сортування завдань за датою виконання.

- **Локальне та серверне збереження даних**:
  - Завдання зберігаються локально за допомогою SQLite.
  - Синхронізація даних із сервером через Retrofit.

- **Інтуїтивно зрозумілий інтерфейс**:
  - Простий та зручний інтерфейс із використанням Material Design.
  - Випадаюче меню для вибору фільтрації завдань.
  - Налаштування тем (світла/темна тема).

## Технології та інструменти:
- **Мова розробки**: Kotlin
- **Архітектура**: MVVM (Model-View-ViewModel)
- **Jetpack Compose**: Для побудови користувацького інтерфейсу.
- **SQLite**: Для локального збереження даних.
- **Retrofit**: Для синхронізації даних із сервером.
- **LiveData/StateFlow**: Для управління станами та оновлення UI у реальному часі.

## Знімки екрану:
### Головний екран:
![image](https://github.com/user-attachments/assets/9fa28f96-a7aa-41a9-9e02-744b46f96bde)  ![image](https://github.com/user-attachments/assets/2c5bf916-00be-4c68-bb5d-684e2f918e1f)

### Меню сортування:
![image](https://github.com/user-attachments/assets/1a5d10fb-4d36-410c-b98a-2ce81881a3fa)  ![image](https://github.com/user-attachments/assets/f238e2c5-0870-475f-8a73-6f66c5636228)

### Додаваняя завдання:
![image](https://github.com/user-attachments/assets/d3747fbb-fad6-464a-9d15-7c3d0a5df5d1)  ![image](https://github.com/user-attachments/assets/f9fd2554-88ad-4b77-834c-b05eb5f4a678)

### Перегляд інформації про завдання:
![image](https://github.com/user-attachments/assets/fb1b734d-4b71-40df-979c-7b33f5d845cf)  ![image](https://github.com/user-attachments/assets/b8253a96-8f91-4a25-915f-73bbb60bb769)

### Налаштування :
![image](https://github.com/user-attachments/assets/b14748d2-66b9-4ea5-aef7-270068831990)   ![image](https://github.com/user-attachments/assets/71e73dda-9d07-4a88-bb29-db7d687cc0cd)


