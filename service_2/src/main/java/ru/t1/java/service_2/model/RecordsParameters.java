package ru.t1.java.service_2.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

@RequiredArgsConstructor
@Builder
public class RecordsParameters {
    private final Long period;
    private final Integer maxNumberOfTransactions;
    @Getter
    private final LinkedList<ObjectFromRecord> recordList;

    // Число элементов
    public int getSize() {
        return this.recordList.size();
    }

    //метод добавления ObjectFromRecord
    public boolean addObjectAndGetResult(ObjectFromRecord o) {
        if (o != null) {
            this.recordList.add(o);
            // сортируем список после добавления записи по messageTimestamp
            // (т.к. топик в случаем нескольких партиций не гарантируют порядок сообщений)
            this.recordList.sort(Comparator.comparing(ObjectFromRecord::getMessageTimestamp));
        }
        if (getSize() > maxNumberOfTransactions) {
            // проверка на period по messageTimestamp
            long firstMaxTime = this.recordList.getLast().getMessageTimestamp();
            if (!checkByMessageTimestamp(firstMaxTime)) {
                //проверка на period по transactionTimestamp
                //если false = сигнал соответствия превышению кол-ва транзакций в заданный период времени
                return checkByTransactionTimestamp();
            } else {
                // удаляем записи сначала списка, которые не попадают в период
                removeOldRecords();
                return true;
            }
        } else {
            return true;
        }
    }

    // удаление записей сначала списка, которые не попадают в период
    public void removeOldRecords() {
        ListIterator<ObjectFromRecord> iterator = recordList.listIterator();
        while (iterator.hasNext() && checkByMessageTimestamp(this.recordList.getLast().getMessageTimestamp())) {
            this.recordList.removeFirst();
        }
    }

    // удаление записей сначала списка (для периодической очистки устаревших записей)
    public void removeOldRecords(long maxTime) {
        ListIterator<ObjectFromRecord> iterator = recordList.listIterator();
        while (iterator.hasNext() && checkByMessageTimestamp(maxTime)) {
            this.recordList.removeFirst();
        }
    }

    //  проверка на period по massageTimestamp
    public boolean checkByMessageTimestamp(long maxTime ) {
        if (getSize() > 1) {
            long minTime = this.recordList.getFirst().getMessageTimestamp();
            return maxTime - minTime > period;
        }
        return false;
    }

    //  проверка на period по transactionTimestamp
    public boolean checkByTransactionTimestamp() {
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