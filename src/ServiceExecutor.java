import java.util.*;

import static java.lang.Thread.sleep;

public class ServiceExecutor {
    public static final long MAX_WAITING_TIME = 20000;  // Максимальное время ожидания запуска сервисов до выхода по таймауту
    private HashMap<Integer, Thread> threads;

    public ServiceExecutor(ArrayList<Service> listOfServices, int tMax) {
        threads = new HashMap<>();
        ThreadGroup threadGroup = new ThreadGroup("Мои потоки");
        ArrayList<Integer> indexesOfStartedServices = new ArrayList<>();   // Список индексов сервисов, которые уже запустились
            // Запускаем потоки
        long time_start = System.currentTimeMillis();
        int i = 0;
        while (indexesOfStartedServices.size() < listOfServices.size() && (System.currentTimeMillis()-time_start) < MAX_WAITING_TIME) {
            try {   // небольшой таймаут, чтобы не засорять консоль сообщениями
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Запускаем i-й сервис, если все нужные зависимости стартовали
            if (threadGroup.activeCount() < tMax)
                if (!threads.containsKey(i)) {  // Проверяем, запущен ли уже сервис
                    //System.out.println("Запускаем сервис " + listOfServices.get(i).getType() + "...");
                    int depIndex = checkStartedDependencies(listOfServices, i, indexesOfStartedServices);
                    if (depIndex == -1) {
                        Thread thread = new Thread(threadGroup, listOfServices.get(i), "Сервис-" + (i+1));
                        thread.start();
                        threads.put(i, thread);
                    } else {
                        //System.out.println("Не могу запустить сервис " + listOfServices.get(i).getType() + "! Ожидаем запуска " + listOfServices.get(depIndex).getType());
                    }
                }
            // Если отработал запуск сервиса, но его ещё нет в списке стартанувших, то добавляем
            if (!indexesOfStartedServices.contains(i))
                if (threads.containsKey(i) && !threads.get(i).isAlive()) indexesOfStartedServices.add(i);
            i++; if (i >= listOfServices.size()) i = 0;
        }
        if (indexesOfStartedServices.size() == listOfServices.size()){
            System.out.println("Все сервисы запущены.");
        } else {
            System.out.println("! Выход по таймауту");
        }
        System.out.println("Конец работы программы");
    }

    private int checkStartedDependencies(ArrayList<Service> lstOfSrv, int idSrv, ArrayList<Integer> lstOfStarted) {
        int result = -1; // -1 означает, что всё в порядке и нет незапущенных сервисов, от которых зависим
        if (lstOfSrv.get(idSrv).getListOfDependencies().size() > 0){
            for (int j=0; j<lstOfSrv.get(idSrv).getListOfDependencies().size(); j++) {
                int indOfDep = searchKey(lstOfSrv, lstOfSrv.get(idSrv).getListOfDependencies().get(j));
                if (indOfDep >= 0)
                    if (!lstOfStarted.contains(indOfDep)) result = indOfDep;
            }
        }
        return result;
    }

    private int searchKey (ArrayList<Service> lst, String desiredType) {
        int result = -1;
        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i).getType().equals(desiredType)) {
                result = i;
            }
        }
        return result;
    }
}
