package ru.t1.java.service_2.model;

import lombok.*;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

@RequiredArgsConstructor
public class RecordsParameters {
    @Getter
    private final LinkedList<ObjectFromRecord> recordList = new LinkedList<>();
    @Getter
    private long oldestMessageTime;
    private long lastMessageTime;

    // Число элементов
    public int getSize() {
        return this.recordList.size();
    }

    private void initTimes() {
        if (getSize() > 0) {
            this.oldestMessageTime = this.recordList.getFirst().getMessageTimestamp();
            this.lastMessageTime = this.recordList.getLast().getMessageTimestamp();
        }
    }

    //метод добавления ObjectFromRecord
    public boolean addObjectAndGetResult(ObjectFromRecord o, int maxNumberOfTransactions, long period) {
        if (o != null) {
            this.recordList.add(o);
            // сортируем список после добавления записи по messageTimestamp
            // (т.к. топик в случаем нескольких партиций не гарантируют порядок сообщений)
            this.recordList.sort(Comparator.comparing(ObjectFromRecord::getMessageTimestamp));
            initTimes();
        }
        if (getSize() > maxNumberOfTransactions) {
            // проверка на period по messageTimestamp
            if (!checkByMessageTimestamp(lastMessageTime, period)) {
                //проверка на period по transactionTimestamp
                //если false = сигнал соответствия превышению кол-ва транзакций в заданный период времени
                return checkByTransactionTimestamp(period);
            } else {
                // удаляем записи сначала списка, которые не попадают в период
                removeOldRecords(period);
                return true;
            }
        } else {
            return true;
        }
    }

    // удаление записей сначала списка, которые не попадают в период
    private void removeOldRecords(long period) {
        ListIterator<ObjectFromRecord> iterator = recordList.listIterator();
        while (iterator.hasNext() && checkByMessageTimestamp(lastMessageTime, period)) {
            this.recordList.removeFirst();
            initTimes();
        }
    }

    // удаление записей сначала списка (для периодической очистки устаревших записей)
    public void removeOldRecords(long maxTime, long period) {
        ListIterator<ObjectFromRecord> iterator = recordList.listIterator();
        while (iterator.hasNext() && checkByMessageTimestamp(maxTime, period)) {
            this.recordList.removeFirst();
            initTimes();
        }
    }

    //  проверка на period по massageTimestamp
    public boolean checkByMessageTimestamp(long latestTime, long period) {
        return latestTime - oldestMessageTime > period;
    }

    //  проверка на period по transactionTimestamp
    public boolean checkByTransactionTimestamp(long period) {
        //копируем список и сортируем по transactionTimestamp
        LinkedList<ObjectFromRecord> newList = new LinkedList<>(recordList);
        long maxTime = newList.getLast().getTransactionTimestamp();
        long minTime = newList.getFirst().getTransactionTimestamp();
        return maxTime - minTime > period;
    }

    // Очистка списка
    public void clear() {
        this.recordList.clear();
    }

}