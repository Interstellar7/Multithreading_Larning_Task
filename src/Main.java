import java.util.ArrayList;

public class Main {
    static final int MAX_THREADS = 2;

    public static void main(String[] args) {
        System.out.println("Старт программы");
        System.out.println("Максимальное число одновременно запускаемых потоков: " + MAX_THREADS);
            // Создаём сервисы
        ArrayList<Service> services = new ArrayList<>();
        Service s1 = new Service(); s1.setType("Сервер печати");        services.add(s1);
        Service s2 = new Service(); s2.setType("Печать документа");     services.add(s2);
        Service s3 = new Service(); s3.setType("Ютубчик");              services.add(s3);
        Service s4 = new Service(); s4.setType("Драйвер звука");        services.add(s4);
        //Service s5 = new Service(); s5.setType("Драйвер видеокарты");   services.add(s5);
            // Устанавливаем зависимости:
        s1.addDependsOn(s2.getType());
        s1.addDependsOn(s3.getType());
        s3.addDependsOn(s4.getType());
        s2.addDependsOn(s4.getType());
        s4.addDependsOn(s1.getType());
            // Запускаем менеджер
        ServiceExecutor serviceExecutor = new ServiceExecutor(services, MAX_THREADS);
    }
}
