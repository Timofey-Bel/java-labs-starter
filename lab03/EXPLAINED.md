# ЛР 3 — подробный разбор кода

Файл объясняет каждую строчку кода лабораторной 3. Сначала идёт база Java, без которой код не понять, потом разбор каждого файла по порядку.

---

## Часть 0. База

### Что такое класс и объект

**Класс** — это чертёж. Он описывает, какие данные есть у объекта (поля) и что объект умеет делать (методы).

**Объект** — конкретная вещь, построенная по чертежу. Создаётся словом `new`:

```java
ProjectTask task = new ProjectTask("T-1", "Задача", 8);
```

- `ProjectTask` слева — **тип переменной** (что в ней может лежать).
- `task` — **имя переменной**.
- `new ProjectTask(...)` — создать новый объект и вызвать его **конструктор** с этими аргументами.
- В переменной `task` лежит не сам объект, а **ссылка** на него (как адрес дома). Две переменные могут ссылаться на один и тот же объект.

### Поле, метод, конструктор

```java
public class PriceCalculator {
    private final DiscountPolicy policy;          // поле — данные объекта

    public PriceCalculator(DiscountPolicy policy) { // конструктор — вызывается при new
        this.policy = policy;
    }

    public double calculate(double price) {        // метод — действие объекта
        return policy.apply(price);
    }
}
```

- **Поле** живёт столько же, сколько объект. У каждого объекта свои значения полей.
- **Конструктор** имеет то же имя, что и класс, и не имеет типа возврата. Его задача — правильно заполнить поля.
- **Метод** — функция внутри класса. Перед именем пишется тип результата (`double`, `int`, `boolean`, `void` — «ничего не возвращает»).

### Модификаторы

| Слово | Что значит |
|---|---|
| `public` | доступно из любого класса |
| `private` | доступно только внутри этого же класса |
| без модификатора | доступно только внутри того же пакета (в тестах `class DiscountPolicyTest` так и написано) |
| `final` у поля | значение присваивается один раз (в конструкторе) и больше не меняется |
| `static` | принадлежит классу, а не объекту (в ЛР 3 в основном коде не используется) |

### `this`

`this` — ссылка на текущий объект. Нужна, когда имя параметра совпадает с именем поля:

```java
this.policy = policy;
// this.policy — поле объекта
// policy      — параметр конструктора
```

### Типы, которые встречаются

| Тип | Что хранит | Пример |
|---|---|---|
| `int` | целое число | `8`, `-3` |
| `double` | дробное число | `100.0`, `0.2` |
| `boolean` | `true` или `false` | |
| `String` | текст | `"T-1"` |
| `null` | «ссылка ни на что» — объекта нет | |

`int`, `double`, `boolean` — **примитивы**, они не могут быть `null`. `String`, `ProjectTask` и любые классы — **ссылочные типы**, переменная такого типа может быть `null`.

### Исключения

```java
throw new IllegalArgumentException("Цена не может быть отрицательной");
```

- `throw` — немедленно прервать метод и «выбросить» ошибку наверх тому, кто его вызвал.
- `IllegalArgumentException` — стандартный класс Java: «тебе передали неправильный аргумент».
- Текст в скобках — сообщение, его видно в отчёте об ошибке.
- После `throw` код ниже в методе не выполняется. Поэтому объект с неправильными данными просто не будет создан.

### `package` и `import`

```java
package edu.course.lab03;
```

Первая строка каждого файла. Пакет — это «папка» для классов. Он обязан совпадать с путём `src/main/java/edu/course/lab03/`. Классы одного пакета видят друг друга без `import`.

```java
import org.junit.jupiter.api.Test;
```

`import` подключает класс из другого пакета, чтобы писать короткое имя `Test`, а не `org.junit.jupiter.api.Test`.

### Комментарии

```java
// это комментарий до конца строки, компилятор его игнорирует
```

---

## Часть 1. Главная идея ЛР 3: интерфейс и полиморфизм

### Интерфейс

**Интерфейс** — это контракт: список методов, которые класс **обещает** иметь. Сам интерфейс ничего не делает, в нём нет кода методов.

```java
public interface DiscountPolicy {
    double apply(double price);
}
```

Читается так: «любая политика скидки умеет принять цену и вернуть новую цену». Как именно — решает конкретный класс.

### `implements`

```java
public class NoDiscount implements DiscountPolicy { ... }
```

«Класс `NoDiscount` выполняет контракт `DiscountPolicy`». Компилятор проверит, что в классе есть метод `double apply(double price)`. Если его нет — ошибка компиляции.

### `@Override`

```java
@Override
public double apply(double price) { ... }
```

Аннотация-подсказка компилятору: «этот метод реализует метод из интерфейса». Если ошибиться в имени или параметрах (например, `aply`), компилятор сразу скажет об ошибке. Без `@Override` код тоже работает, но ошибка в имени прошла бы незамеченной.

### Полиморфизм — самое важное

```java
DiscountPolicy policy = new PercentDiscount(20);
policy.apply(100.0); // вернёт 80.0
```

Здесь два разных понятия:

- **Тип ссылки** — `DiscountPolicy` (слева). Он определяет, **какие методы можно вызвать** — компилятор разрешит только `apply`.
- **Фактический тип объекта** — `PercentDiscount` (справа, после `new`). Он определяет, **чей код выполнится** при вызове.

Вызов `policy.apply(...)` один и тот же, но выполнится код того класса, объект которого реально лежит в переменной. Это и есть полиморфизм («много форм»): одна строка кода — разное поведение.

### Композиция и передача через конструктор

`PriceCalculator` не наследует скидку и не создаёт её сам — он **хранит её в поле** (композиция) и **получает снаружи** через конструктор:

```java
PriceCalculator calc = new PriceCalculator(new PercentDiscount(20));
```

Зачем: чтобы поменять поведение, достаточно передать другой объект. Сам `PriceCalculator` при этом не меняется ни на строчку. Это требование условия: «реализация передаётся через конструктор и не создаётся внутри обработчика».

### Почему нельзя `instanceof` и `if` по типу

Плохо:

```java
if (policy instanceof PercentDiscount) { ... } else if (policy instanceof NoDiscount) { ... }
```

При каждой новой скидке пришлось бы дописывать новый `if` в калькулятор. С интерфейсом калькулятор просто вызывает `policy.apply(price)` и не знает, какой там класс.

### Перегрузка vs переопределение (спросят на защите)

- **Переопределение (override)** — класс даёт свою реализацию метода из интерфейса/родителя. Та же сигнатура. Пример: `apply` в `NoDiscount` и `PercentDiscount`.
- **Перегрузка (overload)** — в одном классе несколько методов с одним именем, но разными параметрами: `print(int x)` и `print(String s)`. Выбор делает компилятор по типам аргументов. В ЛР 3 перегрузки нет.

---

## Часть 2. Подготовительная часть (скидки)

### `DiscountPolicy.java`

```java
package edu.course.lab03;
```
Пакет, совпадает с папкой.

```java
// Общий контракт для любой скидки.
public interface DiscountPolicy {
```
Объявляем публичный интерфейс. `public` — чтобы им можно было пользоваться из любого места.

```java
    double apply(double price);
```
Метод без тела (нет `{ }`, сразу `;`). Принимает цену `double`, возвращает `double`. В интерфейсе методы автоматически `public`, поэтому слово не пишем.

```java
}
```
Конец интерфейса.

---

### `NoDiscount.java`

```java
public class NoDiscount implements DiscountPolicy {
```
Класс «без скидки», выполняет контракт `DiscountPolicy`.

```java
    @Override
    public double apply(double price) {
```
Реализация метода интерфейса. `public` обязателен: в интерфейсе метод публичный, а реализация не может быть «закрытее».

```java
        if (price < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
```
Проверка входа: условие задания «отрицательная цена запрещена». Если цена меньше нуля — выбрасываем ошибку, дальше не идём.

```java
        return price;
```
`return` — вернуть результат вызывающему. Без скидки цена та же.

Полей у класса нет — ему не нужно ничего помнить. Конструктор не написан, поэтому Java сама добавляет пустой конструктор по умолчанию, и `new NoDiscount()` работает.

---

### `PercentDiscount.java`

```java
public class PercentDiscount implements DiscountPolicy {

    private final double percent;
```
Поле: размер скидки в процентах.
- `private` — снаружи его не прочитать и не поменять.
- `final` — после конструктора значение не меняется. Скидка 20% остаётся 20% навсегда.

```java
    public PercentDiscount(double percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Процент должен быть от 0 до 100");
        }
        this.percent = percent;
    }
```
Конструктор.
- `||` — логическое «ИЛИ»: условие истинно, если хотя бы одна часть истинна. Ошибка, если процент меньше 0 **или** больше 100.
- Границы 0 и 100 разрешены (0 — скидки нет, 100 — бесплатно).
- Проверка стоит **до** присваивания: если процент неверный, объект не создастся вообще. Это и есть **инвариант** — у любого существующего `PercentDiscount` процент всегда от 0 до 100.
- `this.percent = percent;` — записать параметр в поле.

```java
    @Override
    public double apply(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        return price - price * percent / 100;
    }
```
Сначала проверка цены, потом формула. Пример для `price = 100`, `percent = 20`:
- `price * percent` = 2000
- `/ 100` = 20 — это размер скидки
- `price - 20` = 80

Порядок действий как в математике: сначала `*` и `/` слева направо, потом `-`.

---

### `PriceCalculator.java`

```java
// Калькулятор не знает, какая именно скидка ему передана.
// Он зависит только от интерфейса DiscountPolicy.
public class PriceCalculator {

    private final DiscountPolicy policy;
```
Поле **интерфейсного типа** — главное требование ЛР. Здесь может лежать `NoDiscount`, `PercentDiscount` или любая будущая скидка.

```java
    public PriceCalculator(DiscountPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Политика скидки не может быть null");
        }
        this.policy = policy;
    }
```
Политика приходит снаружи. Проверка на `null`: без неё при вызове `calculate` упало бы `NullPointerException` — ошибка случилась бы позже и было бы непонятно, откуда она. Лучше сразу отказать при создании.

`==` для ссылок сравнивает, указывают ли они на один и тот же объект. `policy == null` — «объекта нет».

```java
    public double calculate(double price) {
        return policy.apply(price);
    }
}
```
Калькулятор просто передаёт работу политике. Какой `apply` выполнится — решается при запуске по фактическому объекту в поле `policy`.

---

## Часть 3. Обязательная часть (приоритет задач)

### `TaskStatus.java`

```java
public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE
}
```
**enum** (перечисление) — тип с фиксированным набором значений. Переменная `TaskStatus` может быть только `TODO`, `IN_PROGRESS`, `DONE` (или `null`). Опечатку вроде `"DOEN"` компилятор не пропустит — в отличие от строки.

Обращение: `TaskStatus.DONE`. Сравнение: `status == TaskStatus.DONE` — для enum `==` безопасно, каждое значение существует в единственном экземпляре.

---

### `ProjectTask.java`

Минимальная копия задачи из ЛР 2. Модули независимы, поэтому класс скопирован в `lab03`, а не подключён из `lab02`.

```java
public class ProjectTask {

    private final String id;
    private final String title;
    private TaskStatus status;
    private int estimatedHours;
```
- `id` и `title` — `final`: задаются один раз.
- `status` и `estimatedHours` без `final`: меняются, но только через методы ниже.
- Все поля `private`: напрямую `task.status = ...` снаружи не написать.

```java
    public ProjectTask(String id, String title, int estimatedHours) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id не может быть пустым");
        }
```
- `id == null` проверяется **первым**. `||` вычисляется слева направо и останавливается на первом `true` (короткое замыкание). Если `id` равен `null`, `id.isBlank()` не вызовется — иначе был бы `NullPointerException`.
- `isBlank()` — метод `String`: `true`, если строка пустая или только из пробелов (`""`, `"   "`).

```java
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title не может быть пустым");
        }
        if (estimatedHours <= 0) {
            throw new IllegalArgumentException("Оценка в часах должна быть больше нуля");
        }
```
То же для названия; часы должны быть больше нуля (`<=` — «меньше или равно»).

```java
        this.id = id;
        this.title = title;
        this.estimatedHours = estimatedHours;
        this.status = TaskStatus.TODO;
    }
```
Все проверки пройдены — заполняем поля. Статус не передаётся параметром: новая задача всегда начинается с `TODO`.

```java
    public void changeStatus(TaskStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Статус не может быть null");
        }
        this.status = newStatus;
    }
```
Единственный способ поменять статус. `void` — ничего не возвращает. Защищает от `null`.

```java
    public boolean isDone() {
        return status == TaskStatus.DONE;
    }
```
Выражение `status == TaskStatus.DONE` само по себе даёт `true` или `false`, его и возвращаем.

```java
    public void increaseEstimate(int hours) {
        if (hours <= 0) {
            throw new IllegalArgumentException("Можно добавить только положительное число часов");
        }
        estimatedHours = estimatedHours + hours;
    }
```
Увеличить оценку можно только на положительное число. Здесь `this.` не написан — оно не обязательно, когда нет конфликта имён.

```java
    public String getId() { return id; }
    public String getTitle() { return title; }
    public TaskStatus getStatus() { return status; }
    public int getEstimatedHours() { return estimatedHours; }
```
**Геттеры** — методы только для чтения. Сеттеров (`setStatus`, `setEstimatedHours`) нет специально.

---

### `PriorityPolicy.java`

```java
// Общий контракт: по задаче посчитать приоритет.
// Чем больше число, тем выше приоритет.
public interface PriorityPolicy {
    int calculatePriority(ProjectTask task);
}
```
Интерфейс из условия. Принимает задачу, возвращает целое число. Договорённость (записана в комментарии): больше число — важнее задача. Все реализации и `TaskPrioritizer` опираются на это правило.

---

### `EffortPriorityPolicy.java`

```java
public class EffortPriorityPolicy implements PriorityPolicy {

    @Override
    public int calculatePriority(ProjectTask task) {
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть null");
        }
        return task.getEstimatedHours();
    }
}
```
Политика «по трудоёмкости»: приоритет = количество часов. Идея: большую задачу надо начинать раньше. Задачу только читаем через геттер, ничего в ней не меняем (требование «входные объекты не изменяются»).

---

### `StatusPriorityPolicy.java`

```java
public class StatusPriorityPolicy implements PriorityPolicy {

    @Override
    public int calculatePriority(ProjectTask task) {
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть null");
        }
        switch (task.getStatus()) {
            case IN_PROGRESS:
                return 2;
            case TODO:
                return 1;
            default: // DONE
                return 0;
        }
    }
}
```
Политика «по статусу»: начатую задачу важнее доделать (2), новая — потом (1), готовая не важна (0).

Как работает `switch`:
- Берёт значение в скобках (`task.getStatus()`) и прыгает на подходящий `case`.
- Внутри `switch` по enum пишут просто `IN_PROGRESS`, без `TaskStatus.`.
- `return` сразу выходит из метода, поэтому `break` не нужен.
- `default` — «во всех остальных случаях». Здесь это только `DONE`.

Это **не** запрещённый «`if` по типу»: мы выбираем по значению поля задачи, а не по классу объекта политики.

---

### `TaskPrioritizer.java`

```java
public class TaskPrioritizer {

    private final PriorityPolicy policy;

    public TaskPrioritizer(PriorityPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Политика приоритета не может быть null");
        }
        this.policy = policy;
    }
```
То же устройство, что у `PriceCalculator`: поле интерфейсного типа, реализация приходит через конструктор.

```java
    // При равном приоритете возвращается первая задача.
    public ProjectTask choose(ProjectTask first, ProjectTask second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Задачи не могут быть null");
        }
```
Метод возвращает одну из двух переданных задач (тип результата — `ProjectTask`).

```java
        int firstPriority = policy.calculatePriority(first);
        int secondPriority = policy.calculatePriority(second);
```
Считаем приоритет каждой задачи. Какая формула применится — зависит от объекта в `policy`. `TaskPrioritizer` этого не знает и знать не должен.

```java
        if (secondPriority > firstPriority) {
            return second;
        }
        return first;
    }
}
```
Если вторая строго важнее — возвращаем её. Иначе (первая важнее **или равны**) — первую. Поэтому при равенстве выигрывает первая; это правило зафиксировано тестом.

Чтобы добавить новую политику (например, по сроку), достаточно создать новый класс `implements PriorityPolicy`. `TaskPrioritizer` не меняется — это критерий приёмки.

---

## Часть 4. Тесты

### Основы JUnit

- **JUnit** — библиотека для автотестов. Подключена в `lab03/pom.xml` с `scope test`: нужна только для тестов, в основной код не попадает.
- Тесты лежат в `src/test/java/...` в том же пакете `edu.course.lab03`, поэтому видят классы основного кода без `import`.
- Maven при `.\mvnw.cmd -pl lab03 test` компилирует код, находит классы с `@Test` и запускает каждый такой метод.

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
```
- Первая строка подключает аннотацию `@Test`.
- `import static` подключает **статический метод** класса `Assertions`, чтобы писать `assertEquals(...)`, а не `Assertions.assertEquals(...)`.

```java
class DiscountPolicyTest {
    @Test
    void noDiscountReturnsSamePrice() { ... }
}
```
- Класс без `public` — JUnit это не мешает.
- `@Test` помечает метод как тест. Метод `void`, без параметров.
- Имя метода описывает, что проверяется.

Каждый тест устроен по схеме **подготовка → действие → проверка**.

### Проверки (assert)

| Метод | Что проверяет |
|---|---|
| `assertEquals(ожидаемое, фактическое)` | значения равны |
| `assertEquals(80.0, x, 0.0001)` | дробные числа равны с точностью 0.0001 |
| `assertTrue(условие)` | условие истинно |
| `assertSame(a, b)` | `a` и `b` — **один и тот же объект** (как `==`) |
| `assertThrows(Класс.class, () -> код)` | код выбросил исключение этого класса |

Если проверка не прошла — тест «падает», Maven пишет `FAILURE` и что ожидалось.

**Почему точность у `double`.** Дробные числа в компьютере хранятся приближённо: `0.1 + 0.2` даёт `0.30000000000000004`. Поэтому дробные сравнивают «с допуском» — третий аргумент.

**Что такое `() -> код`.** Это **лямбда** — кусок кода, который передаётся в метод, но не выполняется сразу. `assertThrows` сам запускает его внутри себя и ловит ожидаемую ошибку. Если написать просто `policy.apply(-1.0)` без лямбды, исключение вылетело бы до вызова `assertThrows`, и тест упал бы.

**`Класс.class`** — объект, описывающий класс. Так `assertThrows` узнаёт, какой тип ошибки ждать.

---

### `DiscountPolicyTest.java`

```java
    @Test
    void noDiscountReturnsSamePrice() {
        DiscountPolicy policy = new NoDiscount();
        assertEquals(100.0, policy.apply(100.0), 0.0001);
    }
```
Тип переменной — интерфейс, объект — `NoDiscount`. Без скидки 100 остаётся 100.

```java
    @Test
    void noDiscountNegativePriceThrows() {
        DiscountPolicy policy = new NoDiscount();
        assertThrows(IllegalArgumentException.class, () -> policy.apply(-1.0));
    }
```
Отрицательная цена → ошибка.

```java
    @Test
    void twentyPercentOfHundredIsEighty() {
        DiscountPolicy policy = new PercentDiscount(20);
        assertEquals(80.0, policy.apply(100.0), 0.0001);
    }
```
Ровно пример из условия: скидка 20% для цены 100 даёт 80.

```java
    @Test
    void percentDiscountNegativePriceThrows() {
        DiscountPolicy policy = new PercentDiscount(20);
        assertThrows(IllegalArgumentException.class, () -> policy.apply(-5.0));
    }
```
Отрицательная цена запрещена и для процентной скидки.

```java
    @Test
    void percentOutOfRangeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(-1));
        assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(101));
    }
```
Граничные случаи: сразу за пределами диапазона с обеих сторон. Объект создать нельзя.

```java
    @Test
    void calculatorWorksWithDifferentPolicies() {
        PriceCalculator withoutDiscount = new PriceCalculator(new NoDiscount());
        PriceCalculator withDiscount = new PriceCalculator(new PercentDiscount(20));

        assertEquals(100.0, withoutDiscount.calculate(100.0), 0.0001);
        assertEquals(80.0, withDiscount.calculate(100.0), 0.0001);
    }
```
**Тест замены политики.** Класс калькулятора один, вызов `calculate(100.0)` одинаковый, а результат разный — потому что внутри разные политики. Это наглядная демонстрация полиморфизма.

```java
    @Test
    void calculatorWithNullPolicyThrows() {
        assertThrows(IllegalArgumentException.class, () -> new PriceCalculator(null));
    }
```
Калькулятор без политики создать нельзя.

---

### `PriorityPolicyTest.java`

```java
    @Test
    void effortPolicyReturnsHours() {
        PriorityPolicy policy = new EffortPriorityPolicy();
        ProjectTask task = new ProjectTask("T-1", "Задача", 8);
        assertEquals(8, policy.calculatePriority(task));
    }
```
Задача на 8 часов → приоритет 8.

```java
    @Test
    void effortPolicyBiggerTaskHasHigherPriority() {
        PriorityPolicy policy = new EffortPriorityPolicy();
        ProjectTask small = new ProjectTask("T-1", "Маленькая", 2);
        ProjectTask big = new ProjectTask("T-2", "Большая", 10);
        assertTrue(policy.calculatePriority(big) > policy.calculatePriority(small));
    }
```
Проверяем смысл, а не конкретное число: большая задача важнее маленькой.

```java
    @Test
    void effortPolicyNullTaskThrows() {
        PriorityPolicy policy = new EffortPriorityPolicy();
        assertThrows(IllegalArgumentException.class, () -> policy.calculatePriority(null));
    }
```
`null` вместо задачи → ошибка. Итого у `EffortPriorityPolicy` три теста (условие требует минимум два).

```java
    @Test
    void statusPolicyInProgressIsHigherThanTodo() {
        PriorityPolicy policy = new StatusPriorityPolicy();
        ProjectTask todo = new ProjectTask("T-1", "Новая", 1);
        ProjectTask inProgress = new ProjectTask("T-2", "Начатая", 1);
        inProgress.changeStatus(TaskStatus.IN_PROGRESS);

        assertEquals(1, policy.calculatePriority(todo));
        assertEquals(2, policy.calculatePriority(inProgress));
    }
```
Обе задачи новые (`TODO`), вторую переводим в `IN_PROGRESS`. Проверяем числа 1 и 2.

```java
    @Test
    void statusPolicyDoneHasZeroPriority() {
        PriorityPolicy policy = new StatusPriorityPolicy();
        ProjectTask done = new ProjectTask("T-1", "Готовая", 1);
        done.changeStatus(TaskStatus.DONE);
        assertEquals(0, policy.calculatePriority(done));
    }
```
Готовая задача → 0. Это проверяет ветку `default` в `switch`. У `StatusPriorityPolicy` два теста.

```java
    @Test
    void prioritizerResultDependsOnPolicy() {
        ProjectTask bigTodo = new ProjectTask("T-1", "Большая", 10);
        ProjectTask smallInProgress = new ProjectTask("T-2", "Маленькая", 1);
        smallInProgress.changeStatus(TaskStatus.IN_PROGRESS);

        TaskPrioritizer byEffort = new TaskPrioritizer(new EffortPriorityPolicy());
        TaskPrioritizer byStatus = new TaskPrioritizer(new StatusPriorityPolicy());

        assertSame(bigTodo, byEffort.choose(bigTodo, smallInProgress));
        assertSame(smallInProgress, byStatus.choose(bigTodo, smallInProgress));
    }
```
**Главный тест заменяемости.** Две задачи подобраны так, чтобы политики давали разный ответ:
- по часам: 10 > 1 → побеждает `bigTodo`;
- по статусу: `IN_PROGRESS` (2) > `TODO` (1) → побеждает `smallInProgress`.

Один и тот же класс `TaskPrioritizer` с одинаковым вызовом `choose` выбирает разное. `assertSame` проверяет, что вернулся именно тот объект, который мы передали, а не просто похожий.

```java
    @Test
    void prioritizerReturnsFirstWhenEqual() {
        ProjectTask first = new ProjectTask("T-1", "Первая", 3);
        ProjectTask second = new ProjectTask("T-2", "Вторая", 3);
        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());
        assertSame(first, prioritizer.choose(first, second));
    }
```
Граничный случай: приоритеты равны (3 и 3) → возвращается первая. Закрепляет правило из комментария к `choose`.

```java
    @Test
    void prioritizerDoesNotChangeTasks() {
        ProjectTask first = new ProjectTask("T-1", "Первая", 3);
        ProjectTask second = new ProjectTask("T-2", "Вторая", 5);
        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());

        prioritizer.choose(first, second);

        assertEquals(3, first.getEstimatedHours());
        assertEquals(TaskStatus.TODO, first.getStatus());
        assertEquals(5, second.getEstimatedHours());
        assertEquals(TaskStatus.TODO, second.getStatus());
    }
```
Требование «входные объекты не изменяются»: после `choose` часы и статусы обеих задач остались прежними. Результат `choose` здесь специально не сохраняется — нас интересует только то, что задачи не тронуты.

```java
    @Test
    void prioritizerNullArgumentsThrow() {
        assertThrows(IllegalArgumentException.class, () -> new TaskPrioritizer(null));
        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());
        ProjectTask task = new ProjectTask("T-1", "Задача", 1);
        assertThrows(IllegalArgumentException.class, () -> prioritizer.choose(null, task));
        assertThrows(IllegalArgumentException.class, () -> prioritizer.choose(task, null));
    }
```
Три проверки на `null`: без политики, без первой задачи, без второй.

---

## Часть 5. Как это всё запускается

1. `.\mvnw.cmd -pl lab03 test` — запуск Maven Wrapper (скачивает нужный Maven сам). `-pl lab03` — собрать только модуль `lab03`.
2. Maven читает `lab03/pom.xml`, берёт версию Java (25) и JUnit из родительского `pom.xml`.
3. Компилирует `src/main/java` → `.class` файлы в `target/classes`.
4. Компилирует `src/test/java` → `target/test-classes`.
5. Плагин Surefire находит классы `*Test` и запускает все методы с `@Test`.
6. Итог: `Tests run: 16, Failures: 0` и `BUILD SUCCESS`. Если хоть один тест упал — `BUILD FAILURE`.

Папка `target/` — результат сборки, в Git её не добавляют.

---

## Часть 6. Вопросы на защите — короткие ответы

- **Зачем интерфейс?** Чтобы клиент (`TaskPrioritizer`) зависел от контракта, а не от конкретного класса. Тогда реализации можно менять, не трогая клиента.
- **Тип ссылки vs тип объекта.** Ссылка решает, какие методы можно вызвать (проверяет компилятор). Объект решает, чей код выполнится (решается при запуске).
- **Перегрузка vs переопределение.** Перегрузка — одно имя, разные параметры, выбирает компилятор. Переопределение — своя реализация метода интерфейса/родителя, выбирается по объекту при запуске.
- **Зачем `@Override`?** Компилятор проверит, что метод действительно реализует метод интерфейса.
- **Зачем `final` у поля `policy`?** Политику нельзя подменить после создания, объект всегда в корректном состоянии.
- **Зачем проверка на `null` в конструкторе?** Чтобы ошибка случилась сразу и была понятной, а не позже как `NullPointerException`.
- **Почему без `instanceof`?** Иначе каждая новая политика требовала бы правки клиента.

### Как добавить новую политику (частое задание на защите)

Например, «короткие задачи первыми»: чем меньше часов, тем выше приоритет.

1. Создать файл `ShortFirstPriorityPolicy.java`:
   ```java
   package edu.course.lab03;

   public class ShortFirstPriorityPolicy implements PriorityPolicy {

       @Override
       public int calculatePriority(ProjectTask task) {
           if (task == null) {
               throw new IllegalArgumentException("Задача не может быть null");
           }
           return -task.getEstimatedHours(); // меньше часов -> больше число
       }
   }
   ```
2. Добавить два теста по образцу `EffortPriorityPolicy`.
3. `TaskPrioritizer` не трогать — он уже работает с любой `PriorityPolicy`.
