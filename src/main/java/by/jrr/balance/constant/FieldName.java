package by.jrr.balance.constant;

/**
 * Created by Shelkovich Maksim on 1.3.18.
 *
 * Class holder Request parameters, that are also name parameters in http form fields
 *
 */
public class FieldName {
    public static final String DIRECTION                = "direction";
    public static final String NAME                     = "name";

    //operation row fields
    public static final String OPERATION_DATE           = "dateOfOperation";
    public static final String ID_OPERATION             = "id";
    public static final String OPERATION_SUM            = "sumOfOperation";
    public static final String ID_CURRENCY              = "currency";
    public static final String ID_OPERATION_CATEGORY    = "IdCashFlowDirection";
    public static final String OPERATION_NOTE           = "operationNotes";
    public static final String REPEAT_N_TIMES           = "operationRepeatNTimes";
    //radio buttons for repeatable operations
    public static final String REPEAT_RADIO             = "operationRepeatRadioButton";
    public static final String REPEAT_DAILY             = "operationRepeatDaily";
    public static final String REPEAT_MONTHLY           = "operationRepeatMonthly";
    public static final String REPEAT_NONE              = "operationRepeatNone";
    public static final String END_OF_REPEATING_DATE    = "dateOfOperationRepeatableEnd";

    //goal row fields
    public static final String GOAL_DATE           = "dateOfGoal";
    public static final String ID_GOAL             = "idGoal";
    public static final String GOAL_SUM            = "sumOfGoal";
//    public static final String ID_CURRENCY              = "currency"; //TODO remove as duplicated
    public static final String ID_GOAL_CATEGORY    = "IdCashFlowDirection";
    public static final String GOAL_NOTE           = "goalNotes";
    public static final String GOAL_PRIORITY           = "goalPriority";

    public static final String Subscriber           = "Subscriber";
    public static final String PROFILE_ID           = "profileId";






}

