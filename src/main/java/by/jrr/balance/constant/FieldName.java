package by.jrr.balance.constant;

import by.jrr.balance.bean.ContractType;
import by.jrr.balance.bean.Currency;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Lob;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Shelkovich Maksim on 1.3.18.
 * <p>
 * Class holder Request parameters, that are also name parameters in http form fields
 */
public class FieldName {
    public static final String DIRECTION = "direction";
    public static final String NAME = "name";
    public static final String FORM_COMMAND    = "formCommand";
    //operationCategory fields
    public static final String OPERATION_CATEGORY_NAME    = "nameCategory";
    public static final String ID_OPERATION_CATEGORY    = "idOperationCategory";
    public static final String OPERATION_DIRECTION    = "operationDirection";

    //operation row fields
    public static final String OPERATION_DATE           = "dateOfOperation";
    public static final String ID_OPERATION             = "id";
    public static final String OPERATION_SUM            = "sumOfOperation";
    public static final String ID_CURRENCY              = "currency";
    public static final String OPERATION_NOTE           = "operationNotes";
    public static final String REPEAT_N_TIMES           = "operationRepeatNTimes";
    //radio buttons for repeatable operations
    public static final String REPEAT_RADIO             = "operationRepeatRadioButton";
    public static final String REPEAT_DAILY             = "operationRepeatDaily";
    public static final String REPEAT_MONTHLY           = "operationRepeatMonthly";
    public static final String REPEAT_NONE              = "operationRepeatNone";
    public static final String END_OF_REPEATING_DATE    = "dateOfOperationRepeatableEnd";

    //goal row fields
    public static final String GOAL_DATE = "dateOfGoal";
    public static final String ID_GOAL = "idGoal";
    public static final String GOAL_SUM = "sumOfGoal";
    //    public static final String ID_CURRENCY              = "currency"; //TODO remove as duplicated
    public static final String ID_GOAL_CATEGORY = "IdCashFlowDirection";
    public static final String GOAL_NOTE = "goalNotes";
    public static final String GOAL_PRIORITY = "goalPriority";

    public static final String Subscriber = "Subscriber";
    public static final String PROFILE_ID = "profileId";

    //contract specific fields
    public static final String ID_CONTRACT      = "contract_id";
    public static final String CONTRACT_SUM     = "sumOfContract";
    public static final String CONTRACT_TYPE_ID = "type";
    public static final String CONTRACT_DATE    = "dateOfContract";
    public static final String CONTRACT_NUMBER  = "N";

    //contract type fields
    public static final String CONTRACT_TYPE_NAME           = "type_name";
    public static final String CONTRACT_TYPE_EFFECTIVE_DATE = "dateOfContract";
    public static final String TEXT                         = "text";

    public static final String ACCEPTANCE_ACT_ID = "acId";
    public static final String ACCEPTANCE_ACT_DATE = "acDate";
    public static final String ACCEPTANCE_ACT_NUMBER = "acNumber";
    public static final String ACCEPTANCE_ACT_SUM = "acSum";
    public static final String ACCEPTANCE_ACT_CONTRACT_ID = "acContractId";
    public static final String ACCEPTANCE_ACT_CURRENCY = "acCurrency";

}

