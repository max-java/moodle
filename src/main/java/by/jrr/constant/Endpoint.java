package by.jrr.constant;

import by.jrr.common.annotations.ToDeprecated;

public class Endpoint {
    public static final String HOME = "/";

    public static final String REDIRECT = "/redirect";

    public static final String TOPIC = "/topic";
    public static final String TOPIC_LIST = "/topics";
    public static final String LECTURE = "/lecture";
    public static final String LECTURE_LIST = "/lectures";
    public static final String COURSE = "/course";
    public static final String COURSE_LIST = "/courses";
    public static final String PRACTICE = "/practice";
    public static final String PRACTICE_LIST = "/practices";


    public static final String PROJECT = "/project";
    public static final String PROJECT_LIST = "/projects";
    public static final String ISSUE = "/issue";
    public static final String ISSUE_LIST = "/issues";
    public static final String DOMAIN = "/domain";
    public static final String DOMAIN_LIST = "/domains";
    public static final String SUBJECT = "/subject";
    public static final String SUBJECT_LIST = "/subjects";

    public static final String Q_AND_A = "/question";
    public static final String Q_AND_A_LIST = "/questions";

    public static final String PROFILE_CARD = "/profile";
    public static final String PROFILE_CARD_ADMIN_VIEW = "/crm/profile";
    public static final String PROFILE_LIST = "/profiles";
    public static final String PROFILE_TIMELINE = "/timeline";

    //manage subscriptions
    public static final String SUBSCRIPTIONS_REQUEST = "/subscriptions/request";
    public static final String SUBSCRIPTIONS_APPROVE = "/subscriptions/approve";
    public static final String SUBSCRIPTIONS_REJECT = "/subscriptions/reject";
    public static final String SUBSCRIPTIONS_UNSUBSCRIBE = "/subscriptions/unsubscribe";


    @ToDeprecated("use request for review forms")
    public static final String REVIEW_REQUEST_FORM = "/reviewRequest/newRequest"; // TODO: 27/05/20 change mapping names, maybe add ?newRequest=true for this?
    public static final String REVIEW_REQUEST_CARD = "/reviewRequest"; // TODO: 27/05/20 change mapping names
    public static final String REVIEW_REQUEST_LIST = "/reviewRequests"; // TODO: 27/05/20 change mapping names
    public static final String REQUEST_FOR_REVIEW = "/requestForReview"; // TODO: 27/05/20 change mapping names


    public static final String REVIEW_FORM = "/review/new"; // TODO: 28/05/20 tbd optimize mapping and views
    public static final String REVIEW_CARD = "/review";
    public static final String REVIEW_LIST = "/reviews";

    public static final String IMAGE = "/img";
    public static final String PDF = "/pdf";
    public static final String VIDEO = "/video";
    public static final String FILES = "/files";


    public static final String REGISTER_STREAM = "/stream/register";
    public static final String REGISTER_TEAM = "/team/register";

    public static final String REGISTER_USER_AND_ENROLL_TO_STREAM = "/registerAndSubscribe";
    public static final String REGISTER_USER = "/registration";
    public static final String REGISTER_USER_ADMIN_REST = "/admin/api/users/";
    public static final String REGISTER_USER_ADMIN_REST_ERRORS = "errors";
    public static final String URCHIN_TRACKING_REST = "utms";

    public static final String BOOK_LIST = "/books";
    public static final String BOOK = "/book";

    public static final String CRM = "/crm";
    public static final String CRM_NEW_HISTORY_ITEM = "/crm/historyItem";
    public static final String CRM_DASHBOARD = "/crm/dashboard";
    public static final String CRM_TIMELINE = "/crm/timeline";
    public static final String CRM_STREAM = "/crm/stream";
    public static final String CRM_DEBTOR = "/crm/debtor";
    public static final String CRM_CASHFLOW = "/crm/cashflow";

    public static final String CRM_CONTRACTS = "/crm/contracts";
    public static final String CRM_OPERATION_CATEGORIES = "/crm/operationCategories";



    //RestApi Endpoints
    public static final String CRM_MESSAGES = "/crm/messages";
    public static final String API_CREATE_REDIRECTION_LINK = "/api/createRedirectionLink";
    public static final String API_REDIRECTION_LINKS = "/api/redirectionLinksForProfile/";

}
