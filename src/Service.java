import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Service implements Runnable{
    private static final long PAUSE_CONST = 5000;
    private long pause_random;
    private String serviceType = "Какой-то сервис"; // Тип сервиса. Нужно для зависимости.
    private ArrayList<String> dependencies;                      // Список зависимостей

    public Service() {
        dependencies = new ArrayList<>();
        Random r = new Random();
        pause_random = (r.nextInt(1000) + 1000) * 2;    // Возвращает случайное число от 2 до 4 секунд
    }

    @Override
    public void run() {
        Thread t = Thread.currentThread();
        String threadName = t.getName();
        System.out.println("Поток " + threadName + " начал запускаться. Тип сервиса: " + serviceType );
        try {
            sleep(PAUSE_CONST);   // имитация запуска потока
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Поток " + threadName + " запустился. Тип сервиса: " + serviceType );
    }

    public ArrayList<String> getListOfDependencies() {
        return this.dependencies;
    }

    public String getType() {
        return serviceType;
    }

    public void addDependsOn(String dependsOn) {
        this.dependencies.add(dependsOn);
    }

    public void setType(String Type) {
        this.serviceType = Type;
    }
}
