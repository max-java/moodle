package by.jrr.balance.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Shelkovich Maksim on 1.3.18.
 */
@Getter
@AllArgsConstructor
public enum OperationRowDirection {
    UNKNOWN("не установлено", ""),
    INCOME("Фактически поступившие денежные средства", "Оплачено"),
    OUTCOME("Фактически совершенные расходы", "Зарплата"),
    PLAN_INCOME("Плановые значения будущих поступлений, используемые для прогнозирования", "Будущие оплаты"),
    PLAN_OUTCOME("Плановые значения будущих расходов, используемые для прогнозирования", "Будущая зарплата"),
    BUDGET("Распределение расходов по статьям", ""),
    GOAL("Постановка цели для рассчета даты достижения исходя из текущих и запланированных доходов и расходов", ""),
    INVOICE("Денежные обязательства подтвержденные условиями договора с известным сроком исполнения. График платежей клиента.", "По счету"),
    ACCEPTANCE_ACT("Сумма денежные обязательств клиента, подтвержденные актами оказанных услуг", ""),
    CONTRACT("Сумма заказанных клиентом услуг по конкретному договору. Балланс клиента = CONTRACT - INCOME", "По договору");

    final String description;
    final String userFriendlyName;

    public boolean isPlan() {
        return this.name().startsWith("PLAN_");
    }
}

